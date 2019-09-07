package org.fox.o2o.service;

import java.util.List;

import org.fox.o2o.dto.ImageHolder;
import org.fox.o2o.dto.ProductExecution;
import org.fox.o2o.entity.Product;
import org.fox.o2o.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 添加商品信息以及图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws ProductOperationException
	 */
	public ProductExecution addProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws ProductOperationException;
	
	/**
	 * 通过商品Id查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	public Product getProductById(long productId);
	
	/**
	 * 修改商品信息以及图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 * @throws ProductOperationException
	 */
	public ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws ProductOperationException;
	
	/**
	 * 查询商品列表并分页，输入条件：商品名（模糊）、商品状态、商铺Id、商品类别
	 * @param prodcutCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
}













