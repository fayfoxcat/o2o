package org.fox.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fox.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 通过shop id查询店铺
	 * @param shopId
	 * @return
	 */
	public Shop queryByShopId(Long shopId);
	
	/**
	 * 新增店铺
	 * @param shop
	 * @return
	 */
	public int insertShop(Shop shop);
	
	/**
	 * 更新店铺信息
	 * @param shop
	 * @return
	 */
	public int updateShop(Shop shop);
	
	/**
	 * 分页查询店铺，可输入条件有：店铺名（模糊），店铺状态，店铺类别，区域Id，owner
	 * @param shopCondition：条件
	 * @param rowIndex：起始行
	 * @param pageSize：返回行数
	 * @return
	 */
	public List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,@Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);
	
	/**
	 * 返回queryShopList总数
	 * @param shopCondition
	 * @return
	 */
	public int queryShopCount(@Param("shopCondition") Shop shopCondition);
}
