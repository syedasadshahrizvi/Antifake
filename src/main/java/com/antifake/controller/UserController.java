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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.antifake.VO.ResultVO;
import com.antifake.VO.UserVO;
import com.antifake.converter.UserForm2UserModelConverter;
import com.antifake.converter.UserInfoForm2UserModelConverter;
import com.antifake.converter.UserRepetition2UserModelConverter;
import com.antifake.enums.CodeEnum;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.form.UserChpwdByTelForm;
import com.antifake.form.UserChpwdForm;
import com.antifake.form.UserInfoForm;
import com.antifake.form.UserLoginForm;
import com.antifake.form.UserRegistForm;
import com.antifake.form.UserRepetition;
import com.antifake.form.UserTelphoneForm;
import com.antifake.model.User;
import com.antifake.service.CodeService;
import com.antifake.service.UserService;
import com.antifake.utils.ResultVOUtil;
import com.antifake.utils.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin
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
	public ResultVO<Map<String,Object>> registUser(@Valid @RequestBody UserRegistForm userForm, BindingResult bindingResult,HttpServletRequest request) throws Exception {
		if (bindingResult.hasErrors()) {
			log.error("【用户注册】参数不正确,UserForm={}", userForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(),
					bindingResult.getFieldError().getDefaultMessage());
		}
		String codeId = request.getHeader(CodeEnum.CODE_ID.getCode());
		if(codeId==null) {
			log.error("【校验验证码】验证码ID不存在！");
			throw new AntiFakeException(ResultEnum.CODEID_ERROR);
		}
		if(!codeService.checkImgCode(codeId,userForm.getImgCode())) {
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage(),false);
		}
		Boolean checkCode = codeService.checkCode(userForm.getTelphone(), userForm.getCode());
		if (!checkCode) {
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage(),false);
		}
		User userconvert = UserForm2UserModelConverter.convert(userForm);
		User repetitionUser = userService.repetitionByUser(userconvert);
		if (repetitionUser != null) {
			return ResultVOUtil.success(ResultEnum.USERNAME_IS_EXIST.getCode(),
					ResultEnum.USERNAME_IS_EXIST.getMessage(),false);
		}

		Map<String, Object> resultMap = userService.registUser(userconvert);
		return ResultVOUtil.success(resultMap);
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
	public ResultVO repetition(@Valid @RequestBody UserRepetition userRepetition, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("【用户信息重复判断】参数不正确,UserRepetition={}", userRepetition);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(),
					bindingResult.getFieldError().getDefaultMessage());
		}
		User userconvert = UserRepetition2UserModelConverter.convert(userRepetition);
		User userResult = userService.repetitionByUser(userconvert);

		if (userResult != null) {
			if (userconvert.getUsername() != null) {
				return ResultVOUtil.success(ResultEnum.USERNAME_IS_EXIST.getCode(),
						ResultEnum.USERNAME_IS_EXIST.getMessage(),false);
			}
			if (userconvert.getTelphone() != null) {
				log.info("【用户信息重复判断】手机号已被占用，User={}", userconvert);
				return ResultVOUtil.success(ResultEnum.TELPHONE_IS_EXIST.getCode(),
						ResultEnum.TELPHONE_IS_EXIST.getMessage(),false);
			}
		}
		return ResultVOUtil.success(true);
	}

	/**
	 * <p>
	 * Description: 密码登陆
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月10日
	 */
	/*@PostMapping("/pwd/login")
	public ResultVO<UserVO> pwdLogin(@RequestBody UserLoginForm userLoginForm,
			HttpServletRequest request, HttpServletResponse response) {
		User user = new User();
		user.setUsername(userLoginForm.getUsername());
		user.setPassword(userLoginForm.getPassword());
		UserVO userVO = userService.loginByUsernameOrtelPhone(user);
		// 添加token
		String get32uuid = UUIDUtil.get32UUID();
			response.setHeader(r_token, get32uuid);
			Cookie cookie = new Cookie(u_token, get32uuid);
			cookie.setMaxAge(30 * 24 * 60 * 60);// 设置为30天
			cookie.setPath("/");
			response.addCookie(cookie);
		redisTemplate.opsForValue().set(get32uuid, userVO.getUserId(), 30, TimeUnit.DAYS);
		if (userVO == null) {
			return ResultVOUtil.success(ResultEnum.LOGIN_ERROE.getCode(), ResultEnum.LOGIN_ERROE.getMessage());
		}
		return ResultVOUtil.success(userVO);
	}
*?/
/**
	 * <p>
	 * Description: 密码登陆
	 * </p >
	 * 
	 * @author JZR
	 * @date 2018年4月10日
	 */
	@PostMapping("/pwd/login")
	public ResultVO<UserVO> pwdLogin(@RequestBody UserLoginForm userLoginForm,
			HttpServletRequest request, HttpServletResponse response) {
		log.error("【登陆】用户名："+userLoginForm.getUsername()+"密码："+userLoginForm.getPassword());
		//System.out.println(userLoginForm.getUsername()+"  "+userLoginForm.getPassword());
		User user = new User();
		user.setUsername(userLoginForm.getUsername());
		user.setPassword(userLoginForm.getPassword());
		UserVO userVO = userService.loginByUsernameOrtelPhone(user);
		// 添加token
		String get32uuid = UUIDUtil.get32UUID();
			response.setHeader(r_token, get32uuid);
			Cookie cookie = new Cookie(u_token, get32uuid);
			cookie.setMaxAge(30 * 24 * 60 * 60);// 设置为30天
			cookie.setPath("/");
			response.addCookie(cookie);
		redisTemplate.opsForValue().set(get32uuid, userVO.getUserId(), 30, TimeUnit.DAYS);
		return ResultVOUtil.success(userVO);
	}
	/**
	 * <p>
	 * Description: 验证码登陆
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月10日
	 */
	@PostMapping("/code/login")
	public ResultVO<UserVO> codeLogin(@RequestBody UserTelphoneForm userTelphoneForm,
			HttpServletRequest request, HttpServletResponse response) {
		if (!codeService.checkCode(userTelphoneForm.getTelphone(), userTelphoneForm.getCode()))
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
		UserVO userVO = userService.findByTelphone(userTelphoneForm.getTelphone());
		// 添加token
		String get32uuid = UUIDUtil.get32UUID();
			response.setHeader(r_token, get32uuid);
		/*		
			Cookie cookie = new Cookie(u_token, get32uuid);
			cookie.setMaxAge(30 * 24 * 60 * 60);// 设置为30min
			cookie.setPath("/");
			response.addCookie(cookie);
		*/
		redisTemplate.opsForValue().set(get32uuid, userVO.getUserId(), 30, TimeUnit.DAYS);
		return ResultVOUtil.success(userVO);
	}

	/**
	 * <p>
	 * Description: 编辑用户昵称
	 * </p>
	 * 
	 * @author JZR
	 * @date 2018年4月11日
	 */
	@PostMapping("/chinfo")
	public ResultVO<Integer> editUser(@Valid UserInfoForm userInfoForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("【用户资料编辑】参数格式不正确，userInfoForm = {}", userInfoForm);
			throw new AntiFakeException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
		}
		User userconvert = UserInfoForm2UserModelConverter.convert(userInfoForm);
		Integer userResult = userService.updateByUsernameOrTelphone(userconvert);

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
	@PostMapping("/chpwd")
	public ResultVO editPassword(@RequestBody UserChpwdForm chpwdForm) {
		Boolean flag = userService.updatePwdByPwd(chpwdForm.getUserId(), chpwdForm.getOldpwd(), chpwdForm.getNewpwd());
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
	@PostMapping("/chpwdbycode")
	public ResultVO editPwdBycode(@RequestBody UserChpwdByTelForm userChpwdByTelForm) {
		// 判断验证码
		if (!codeService.checkCode(userChpwdByTelForm.getTelphone(), userChpwdByTelForm.getCode()))
			return ResultVOUtil.success(ResultEnum.CODE_ERROR.getCode(), ResultEnum.CODE_ERROR.getMessage());
		userService.updatePwdByCode(userChpwdByTelForm.getUserId(), userChpwdByTelForm.getNewpwd());
		return ResultVOUtil.success();
	}

}
