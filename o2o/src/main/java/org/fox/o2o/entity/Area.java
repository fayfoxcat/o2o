package org.fox.o2o.entity;

import java.util.Date;

public class Area {
	//属性采用引用类型,让其默认值为空
	//ID
	private long areaId;
	//地域名称
	private String areaName;
	//权重（排名）
	private Integer priority;
	//创建时间
	private Date createTime;
	//更新时间
	private Date lastEditTime;
	
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	
}
