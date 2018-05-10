package com.antifake.controller;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.VO.UserVO;
import com.antifake.converter.UserForm2UserModelConverter;
import com.antifake.converter.UserInfoForm2UserModelConverter;
import com.antifake.converter.UserRepetition2UserModelConverter;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.UserRegistForm;
import com.antifake.form.UserInfoForm;
import com.antifake.form.UserRepetition;
import com.antifake.model.User;
import com.antifake.service.CodeService;
import com.antifake.service.KeyService;
import com.antifake.service.UserService;
import com.antifake.utils.ResultVOUtil;
import com.antifake.utils.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	public static final String u_token = "u_token";
	public static final String r_token = "r_token";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private UserService userService;

	@Autowired
	private CodeService codeService;

	/**
	 * <p>
	 * Description: 账户注册
	 * </p>
	 * 
	 * @author JZR
	 * @throws Exception
	 * @date 2018年4月10日
	 */
	@PostMapping("/regist")
	public ResultVO registUser(@Valid UserRegistForm userForm, BindingResult bindingResult) throws Exception {

		if (bindingResult.hasErrors()) {
			log.error("【用户注册】参数不正确,UserForm={}", userForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(),
					bindingResult.getFieldError().getDefaultMessage());
		}
		Boolean checkCode = codeService.checkCode(userForm.getTelphone(), userForm.getCode());
		if (!checkCode) {
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
		}
		User userconvert = UserForm2UserModelConverter.convert(userForm);
		User repetitionUser = userService.repetitionByUser(userconvert);
		if (repetitionUser != null) {
			return ResultVOUtil.success(ResultEnum.USERNAME_IS_EXIST.getCode(),
					ResultEnum.USERNAME_IS_EXIST.getMessage());
		}

		String privateKey = userService.registUser(userconvert);
		return ResultVOUtil.success(privateKey);
	}

	/**
	 * <p>
	 * Description: 判断用户名或者手机号是否重复
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月10日
	 */
	@PostMapping("/repetition")
	public ResultVO repetition(@Valid UserRepetition userRepetition, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("【用户信息重复判断】参数不正确,UserRepetition={}", userRepetition);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(),
					bindingResult.getFieldError().getDefaultMessage());
		}
		User userconvert = UserRepetition2UserModelConverter.convert(userRepetition);
		User userResult = userService.repetitionByUser(userconvert);

		if (userResult != null) {
			if (userconvert.getUsername() != null) {
				log.info("【用户信息重复判断】用户名重复，User={}", userconvert);
				return ResultVOUtil.success(ResultEnum.USERNAME_IS_EXIST.getCode(),
						ResultEnum.USERNAME_IS_EXIST.getMessage());
			}
			if (userconvert.getTelphone() != null) {
				log.info("【用户信息重复判断】手机号已被占用，User={}", userconvert);
				return ResultVOUtil.success(ResultEnum.TELPHONE_IS_EXIST.getCode(),
						ResultEnum.TELPHONE_IS_EXIST.getMessage());
			}
		}
		return ResultVOUtil.success();
	}

	/**
	 * <p>
	 * Description: 密码登陆
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月10日
	 */
	@PostMapping("/pwdlogin")
	public ResultVO<UserVO> pwdLogin(@RequestParam(required = true) String username,
			@RequestParam(required = true) String password,
			@RequestParam(name = "app", defaultValue = "false", required = false) Boolean app,
			HttpServletRequest request, HttpServletResponse response) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		UserVO userResult = userService.loginByUsernameOrtelPhone(user);
		// 添加token
		String get32uuid = UUIDUtil.get32UUID();
		if (app) {
			response.setHeader(r_token, get32uuid);
		} else {
			Cookie cookie = new Cookie(u_token, get32uuid);
			cookie.setMaxAge(30 * 24 * 60 * 60);// 设置为30天
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		redisTemplate.opsForValue().set(get32uuid, userResult.getUserId(), 30, TimeUnit.DAYS);
		if (userResult == null) {
			return ResultVOUtil.success(ResultEnum.LOGIN_ERROE.getCode(), ResultEnum.LOGIN_ERROE.getMessage());
		}
		return ResultVOUtil.success(userResult);
	}

	/**
	 * <p>
	 * Description: 验证码登陆(注册)
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月10日
	 */
	@PostMapping("/codelogin")
	public ResultVO<UserVO> codeLogin(@RequestParam(required = true) String telphone,
			@RequestParam(required = true) String code, @RequestParam(name = "app", defaultValue = "false") Boolean app,
			HttpServletRequest request, HttpServletResponse response) {
		if (!codeService.checkCode(telphone, code))
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
		UserVO userVO = userService.findByTelphone(telphone);
		// 添加token
		String get32uuid = UUIDUtil.get32UUID();
		if (app) {
			response.setHeader(r_token, get32uuid);
		} else {
			Cookie cookie = new Cookie(u_token, get32uuid);
			cookie.setMaxAge(30 * 24 * 60 * 60);// 设置为30min
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		redisTemplate.opsForValue().set(get32uuid, userVO.getUserId(), 30, TimeUnit.DAYS);
		return ResultVOUtil.success(userVO);
	}

	/**
	 * <p>
	 * Description: 编辑用户资料
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月11日
	 */
	@PostMapping("/editinfo")
	public ResultVO<UserVO> editUser(@Valid UserInfoForm userInfoForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("【用户资料编辑】参数格式不正确，userInfoForm = {}", userInfoForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR);
		}
		User userconvert = UserInfoForm2UserModelConverter.convert(userInfoForm);
		UserVO userResult = userService.updateByUsernameOrTelphone(userconvert);

		return ResultVOUtil.success(userResult);
	}

	/**
	 * <p>
	 * Description: 通过密码修改密码
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月11日
	 */
	@PostMapping("/editpwd")
	public ResultVO editPassword(@RequestParam(required = true) String userId,
			@RequestParam(required = true) String oldpwd, @RequestParam(required = true) String newpwd) {
		Boolean flag = userService.updatePwdByPwd(userId, oldpwd, newpwd);
		if (flag) {
			return ResultVOUtil.success();
		}
		return ResultVOUtil.error(ResultEnum.OLDPWD_ERROR.getCode(), ResultEnum.OLDPWD_ERROR.getMessage());
	}

	/**
	 * <p>
	 * Description: 验证码修改密码
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月11日
	 */
	@PostMapping("/editpwd2")
	public ResultVO editPwdBycode(String userId, String telphone, String code, String newpwd) {
		// 判断验证码
		if (!codeService.checkCode(telphone, code))
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
		userService.updatePwdByCode(userId, newpwd);
		return ResultVOUtil.success();
	}

}
