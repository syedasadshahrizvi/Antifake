package com.antifake.interceptor;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.antifake.model.Role;
import com.antifake.service.RoleService;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private final StringRedisTemplate redisTemplate;

	private final RoleService roleSerivce;

	public LoginInterceptor(StringRedisTemplate redisTemplate, RoleService roleSerivce) {
		this.redisTemplate = redisTemplate;
		this.roleSerivce = roleSerivce;
	}

	public static final String u_token = "u_token";
	public static final String r_token = "r_token";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			LoginRequired loginRequired = findAnnotation((HandlerMethod) handler, LoginRequired.class);
			if (loginRequired == null) {
				return true;
			} else {
				boolean checkRole = loginRequired.checkRole();
				String roleString = loginRequired.role();
				// 认证
				String userId = getToken(request);
				if(userId==null) {
					response.getWriter().write("a请先登陆！");
					return false;
				}
				if (checkRole) {// 做授权操作
					List<Role> roles = roleSerivce.queryByUId(userId);
					for (Role role : roles) {
						if (roleString.indexOf(role.getRoleName()) != -1) {
							return true;
						}
					}
				} else if (!checkRole) {
					return true;
				}
				response.getWriter().write("a没有权限访问！");
				return false;
			}
		} else {
			return true;
		}
	}

	private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
		T annotation = handler.getBeanType().getAnnotation(annotationType);
		if (annotation != null)
			return annotation;
		return handler.getMethodAnnotation(annotationType);
	}

	private String getToken(HttpServletRequest request) {
		if (request.getHeader(r_token) != null) {
			// 重置token生存时间
			if (redisTemplate.opsForValue().get(request.getHeader(u_token)) != null) {
				redisTemplate.opsForValue().set(request.getHeader(r_token),
						redisTemplate.opsForValue().get(request.getHeader(r_token)), 60, TimeUnit.DAYS);
				return redisTemplate.opsForValue().get(request.getHeader(u_token));
			}
		} else {
			// 获取token
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(u_token)) {
					if (redisTemplate.opsForValue().get(cookie.getValue()) != null)
						// 重置token生存时间
						redisTemplate.opsForValue().set(cookie.getValue(),
								redisTemplate.opsForValue().get(cookie.getValue()), 30, TimeUnit.MINUTES);
					return redisTemplate.opsForValue().get(cookie.getValue());
				}
			}
		}
		return null;
	}

}
