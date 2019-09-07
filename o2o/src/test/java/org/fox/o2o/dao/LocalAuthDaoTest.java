package org.fox.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.fox.o2o.BaseTest;
import org.fox.o2o.entity.LocalAuth;
import org.fox.o2o.entity.PersonInfo;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LocalAuthDaoTest extends BaseTest{
	@Autowired
	private LocalAuthDao localAuthDao;
	private static final String username = "test";
	private static final String password = "password";
	private static final String newPassword = "newPassword";
	
	
	@Test
	@Ignore
	public void testInsertLocalAuth() throws Exception{
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		
		personInfo.setUserId(1L);
		localAuth.setPersonInfo(personInfo);
		
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}
	
	@Test
	@Ignore
	public void testQueryLocalByUserNameAndPwd() throws Exception{
		LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
		System.out.println(localAuth.getUsername());
//		assertEquals("陈平安", localAuth.getPersonInfo().getName());
	}
	
	@Test
	@Ignore
	public void testQueryLocalByUserid() throws Exception{
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		System.out.println(localAuth.getUsername());
		assertEquals("陈平安", localAuth.getPersonInfo().getName());
	}
	
	@Test
	public void testUpdateLocalAuth() throws Exception{
		Date now = new Date();
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, newPassword, now);
		assertEquals(1, effectedNum);
	}
}
