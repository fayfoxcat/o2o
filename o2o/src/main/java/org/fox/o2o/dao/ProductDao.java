package org.fox.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fox.o2o.entity.Product;

public interface ProductDao {

	/**
	 * 添加商品
	 * @param product
	 * @return
	 */
	public int insertProduct(Product product);
	
	/**
	 * 通过productId查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	public Product queryProductByProductId(long productId);
	
	/**
	 * 查询对应商品总数
	 * @param productCondition
	 * @return
	 */
	public int queryProductCount(@Param("productCondition") Product productCondition);
	
	
	/**
	 * 查询商品列表并分页，输入的条件有：商品名（模糊），商品状态，商铺Id，商品类别
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	public List<Product> queryProductList(@Param("productCondition") Product productCondition,@Param("rowIndex") int rowIndex ,@Param("pageSize") int pageSize);
	
	/**
	 * 更新商品信息
	 * @param product
	 * @return
	 */
	public int updateProduct(Product product);
	
	/**
	 * 删除指定商品类别之前，将商品类别ID置空
	 * @param productCategoryId
	 * @return
	 */
	public int updateProductCategoryToNull(long productCategoryId);
}
