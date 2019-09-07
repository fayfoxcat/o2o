package org.fox.o2o.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fox.o2o.dto.LocalAuthExecution;
import org.fox.o2o.entity.LocalAuth;
import org.fox.o2o.entity.PersonInfo;
import org.fox.o2o.enums.LocalAuthStateEnum;
import org.fox.o2o.service.LocalAuthService;
import org.fox.o2o.util.Codeutil;
import org.fox.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="local",method = {RequestMethod.GET,RequestMethod.POST})
public class LocalAuthController {
	
	@Autowired
	private LocalAuthService localAuthService;
	
	/**
	 * 创建账号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="registerlocalauth",method=RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> registerLocalAuth(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//验证码
		if(!Codeutil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errorMessage","输入了错误的验证码！");
			return modelMap;
		}
		//获取输入的账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		//获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		//非空判断，要求账号密码
		if(userName != null && password !=null ) {
			//构建LocalAuth对象
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(userName);
			localAuth.setPassword(password);
			//调用Service创建平台账号
			LocalAuthExecution re = localAuthService.registerLocalAuth(localAuth);
			if(re.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			}else {
				modelMap.put("success", false);
				modelMap.put("errorMessage",re.getStateInfo());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errorMessage", "用户名和密码均不能为空");
		}
		return modelMap;
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/changelocalpwd",method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> changeLocalPwd(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//验证码
		if(!Codeutil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errorMessage","输入了错误的验证码！");
			return modelMap;
		}
		//获取账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		//获取原密码
		String password = HttpServletRequestUtil.getString(request, "password");
		//获取新密码
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		//获取Session中当前用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		//非空判断
		if(userName != null && password != null && newPassword != null && user !=null && user.getUserId() != null && !password.equals(newPassword)) {
			try {
				//查看原先账号与输入的账号是否一致，不一致则认为是非法操作
				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
				if(localAuth == null || !localAuth.getUsername().equals(userName)) {
					//信息不一致，退出
					modelMap.put("success",false);
					modelMap.put("errorMessage","输入的账号非本次登录的账号");
					return modelMap;
				}
				//修改账号
				LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password, newPassword);
				if(le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);
				}else {
					modelMap.put("success",false);
					modelMap.put("errorMessage",le.getStateInfo());
				}
			}catch(Exception e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage","请输入密码");
			}
		}
		return modelMap;
	}
	
	/**
	 * 校验登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logincheck",method =RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> loginCheck(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//获取是否需要进行验证码校验的标识符
//		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if(!Codeutil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errorMessage","输入了错误的验证码！");
			return modelMap;
		}
		//获取账号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		//获取密码
		String password = HttpServletRequestUtil.getString(request, "password");
		//非空校验
		if(userName != null && password != null) {
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			if(localAuth != null) {
				//若获取到账号的信息则登录成功
				modelMap.put("success",true);
				//向session里设置用户信息
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
			}else {
				modelMap.put("success",false);
				modelMap.put("errorMessage","用户名或密码错误");
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errorMessage","用户名和密码不能为空");
		}
		return modelMap;
	}
	
	
	/**
	 * 当用户点击登出的时候销毁session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout",method =RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> logOut(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//将用户session置为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success",true);
		return modelMap;
	}
}
















