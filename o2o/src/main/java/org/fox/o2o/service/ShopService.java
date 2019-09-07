package org.fox.o2o.service;

import org.fox.o2o.dto.ImageHolder;
import org.fox.o2o.dto.ShopExecution;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 根据shopId查询店铺信息
	 * @param shopId
	 * @return
	 */
	public Shop getByShopId(long shopId);
	
	/**
	 * 添加店铺
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
	
	/**
	 * 更新店铺信息，包括对图片的处理
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 * @throws ShopOperationException
	 */
	public ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
	
	/**
	 * 根据shopCondition分页返回相应的店铺列表
	 * @param shopCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList( Shop shopCondition, int pageIndex, int pageSize);

}


















