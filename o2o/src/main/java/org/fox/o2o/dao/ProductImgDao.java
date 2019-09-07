package org.fox.o2o.dao;

import java.util.List;

import org.fox.o2o.entity.ProductImg;

public interface ProductImgDao{
	/**
	 * 批量添加图片
	 * @param productImgList
	 * @return
	 */
	public int batchInsertProductImg(List<ProductImg> productImgList);
	
	/**
	 * 删除指定商品下的所有详情图
	 * @param productId
	 * @return
	 */
	public int deleteProductImgByProductId(long productId);
	
	/**
	 * 根据productId获取图片列表
	 * @param productId
	 * @return
	 */
	public List<ProductImg> queryProductImgList(long productId);
}
