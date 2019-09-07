package org.fox.o2o.service;

import java.util.Date;

import org.fox.o2o.dto.LocalAuthExecution;
import org.fox.o2o.entity.LocalAuth;
import org.fox.o2o.entity.PersonInfo;
import org.fox.o2o.exceptions.LocalAuthOperationException;
import org.fox.o2o.exceptions.PersonInfoOperationException;

public interface LocalAuthService {
	
	/**
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	public LocalAuthExecution registerLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException,PersonInfoOperationException;
	
	/**
	 * 通过账号和密码获取平台账号信息
	 * @param userName
	 * @param password
	 * @return
	 */
	public LocalAuth getLocalAuthByUsernameAndPwd(String userName,String password);
	
	/**
	 * 通过userId获取平台账号信息
	 * @param userId
	 * @return
	 */
	public LocalAuth getLocalAuthByUserId(Long userId);
	
	/**
	 * 修改平台账号登录密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPssword
	 * @param lastEditTime
	 * @return
	 * @throws LocalAuthOperationException
	 */
	public LocalAuthExecution modifyLocalAuth(Long userId,String userName,String password,String newPassword) throws LocalAuthOperationException;
}
