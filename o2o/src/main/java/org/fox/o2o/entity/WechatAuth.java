package org.fox.o2o.entity;

import java.util.Date;

//微信账号
public class WechatAuth {
	//ID
	private Long wechatAuthId;
	//微信账号与公众号绑定标识
	private String openId;
	//创建时间
	private Date createTime;
	//用户实体类
	private PersonInfo personInfo;
	
	public Long getWechatAuthId() {
		return wechatAuthId;
	}
	public void setWechatAuthId(Long wechatAuthId) {
		this.wechatAuthId = wechatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	
}
