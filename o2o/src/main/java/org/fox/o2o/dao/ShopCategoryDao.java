package org.fox.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fox.o2o.entity.ShopCategory;

public interface ShopCategoryDao {
	/**
	 * 查询商铺所有类别
	 * @param shopCategoryCondition
	 * @return
	 */
	public List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
