package org.fox.o2o.entity;

import java.util.Date;

//商品类别
public class ProductCategory {
	//商品类别id
	private Long productCategoryId;
	//商品类别 所属 店铺id
	private Long shopId;
	//商品类别名
	private String productCategoryName;
	//权重
	private Integer priority;
	//创建时间
	private Date createTime;
	//最后修改时间
//	private Date lastEditTime;
	public Long getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getProductCategoryName() {
		return productCategoryName;
	}
	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
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
	
}
