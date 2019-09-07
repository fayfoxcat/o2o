 package org.fox.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fox.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过shop id查询店铺商品类别
	 * @param shopId
	 * @return
	 */
	public List<ProductCategory> queryProductCategoyList(long shopId);
	
	/**
	 * 批量新增商店类别
	 * @param productCategoryList
	 * @return
	 */
	public int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	/**
	 * 删除指定商品类别
	 * @param procuctCategoryId
	 * @param shopId
	 * @return
	 */
	public int deleteProductCategory(@Param("productCategoryId") long productCategoryId,@Param("shopId") long shopId);
}
