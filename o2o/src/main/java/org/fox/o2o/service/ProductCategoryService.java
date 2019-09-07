package org.fox.o2o.service;

import java.util.List;

import org.fox.o2o.dto.ProductCategoryExecution;
import org.fox.o2o.entity.ProductCategory;
import org.fox.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 查询指定店铺下的所有商品类别信息
	 * @param shopId
	 * @return
	 */
	public List<ProductCategory> getProductCategoryList(long shopId);
	
	/**
	 * 批量添加商铺类别
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;
	
	/**
	 * 将类别下的商品里的类别id置空，再删除该商品的类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	public ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) throws ProductCategoryOperationException;
}
