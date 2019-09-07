package org.fox.o2o.service.impl;

import java.util.List;

import org.fox.o2o.dao.ProductCategoryDao;
import org.fox.o2o.dao.ProductDao;
import org.fox.o2o.dto.ProductCategoryExecution;
import org.fox.o2o.entity.ProductCategory;
import org.fox.o2o.enums.ProductCategoryStateEnum;
import org.fox.o2o.exceptions.ProductCategoryOperationException;
import org.fox.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	
	@Autowired
	ProductCategoryDao productCategoryDao;
	
	@Autowired
	ProductDao productDao;
	
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoyList(shopId);
	}

	@Override
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList !=null && productCategoryList.size()>0) {
			try {
				int batchInsertProductCategory = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if(batchInsertProductCategory <= 0) {
					throw new ProductCategoryOperationException("店铺类别创建失败");
				}else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			}catch(Exception e){
				throw new ProductCategoryOperationException("批量添加店铺失败："+e.getMessage());
			}
		}else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//1、解除tb_product里的商品与productcategoryId的关联
		try {
			
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if(effectedNum < 0) {
				throw new ProductCategoryOperationException("商品类别更新失败");
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
			throw new ProductCategoryOperationException("删除产品类别失败："+e.getMessage());
		}
		//2、删除该productCategory
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum <=0 ) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			}else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("删除类别错误"+e.getMessage());
		}
	}
}
