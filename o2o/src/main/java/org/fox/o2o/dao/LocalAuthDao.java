package org.fox.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.fox.o2o.entity.LocalAuth;

public interface LocalAuthDao {
	
	/**
	 * 通过账号密码查询对应信息，登录用
	 * @param username
	 * @param password
	 * @return
	 */
	public LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username,@Param("password") String password);
	
	/**
	 * 通过对应的Id查询对应的localauth
	 * @param userId
	 * @return
	 */
	public LocalAuth queryLocalByUserId(@Param("userId") Long userId);
	
	/**
	 * 注册账号
	 * @param localAuth
	 * @return
	 */
	public int insertLocalAuth(LocalAuth localAuth);
	
	/**
	 * 通过userId，username，password更新密码
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	public int updateLocalAuth(@Param("userId") Long userId,@Param("username") String username,
			@Param("password") String password,@Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
