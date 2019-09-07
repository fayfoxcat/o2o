package org.fox.o2o.service.impl;

import java.util.Date;

import org.fox.o2o.dao.LocalAuthDao;
import org.fox.o2o.dao.PersonInfoDao;
import org.fox.o2o.dto.LocalAuthExecution;
import org.fox.o2o.entity.LocalAuth;
import org.fox.o2o.entity.PersonInfo;
import org.fox.o2o.enums.LocalAuthStateEnum;
import org.fox.o2o.exceptions.LocalAuthOperationException;
import org.fox.o2o.exceptions.PersonInfoOperationException;
import org.fox.o2o.service.LocalAuthService;
import org.fox.o2o.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service	
public class LocalAuthServiceImpl implements LocalAuthService {
	
	@Autowired
	private LocalAuthDao localAuthDao;
	
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(userName, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(Long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}
	
	@Override
	@Transactional
	public LocalAuthExecution registerLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException,PersonInfoOperationException{
		//空值判断，传入的localAuth账号密码，userId不能为空 
		if(localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			//添加用户个人信息
			PersonInfo personInfo = new PersonInfo();
			personInfo.setName(localAuth.getUsername());
			//创建用户默认为1：顾客
			personInfo.setUserType(1);
			//用户初始装态,1:可用
			personInfo.setEnableStatus(1);
			personInfo.setCreateTime(new Date());
			personInfo.setLastEditTime(new Date());
			int effectedNum1 = personInfoDao.insertPersonInfo(personInfo);
			//判断是否添加成功
			if(effectedNum1 <= 0) {
				throw new PersonInfoOperationException("信息添加失败！");
			}else {
				//创建平台账号
				localAuth.setPersonInfo(personInfo);;
				localAuth.setCreateTime(new Date());
				localAuth.setLastEditTime(new Date());
				//对密码进行MD5加密
				localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
				int effectedNum2 = localAuthDao.insertLocalAuth(localAuth);
				//判断是否创建成功
				if(effectedNum2 <= 0) {
					throw new LocalAuthOperationException("账号创建失败！");
				}else {
					return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
				}
			}
		}catch(Exception e){
			throw new LocalAuthOperationException("创建平台账号错误！"+e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword) throws LocalAuthOperationException {
		//非空判断，判断传入的用户Id、账号、新旧密码是否为空，新旧密码是否相同，若不满足条件则返回错误信息
		if(userId != null && userName != null && password != null && newPassword != null && !password.equals(newPassword)) {
			try {
				//更新密码，并对新密码进行MD5加密
				int effectedNum = localAuthDao.updateLocalAuth(userId, userName, MD5.getMd5(password),MD5.getMd5(newPassword), new Date());
				//判断更新是否成功
				if(effectedNum <=0 ) {
					throw new LocalAuthOperationException("更新密码失败！");
				}
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			}catch(Exception e) {
				throw new LocalAuthOperationException("更新密码失败"+e.toString());
			}
		}else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

	

}
