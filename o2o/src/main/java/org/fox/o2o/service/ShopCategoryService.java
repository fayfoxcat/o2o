package org.fox.o2o.service;

import java.util.List;

import org.fox.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	/**
	 * 根据查询条件获取ShopCategory所有类别
	 * @param shopCategoryCondition
	 * @return
	 */
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
