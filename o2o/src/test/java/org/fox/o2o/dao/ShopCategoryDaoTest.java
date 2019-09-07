package org.fox.o2o.dao;



import static org.junit.Assert.assertEquals;

import java.util.List;

import org.fox.o2o.BaseTest;
import org.fox.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopCategoryDaoTest extends BaseTest{
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void testShopCategoryDao() {
		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
		assertEquals(3, shopCategoryList.size());
		if(shopCategoryList.get(0)!=null) {
			System.out.println(shopCategoryList.get(0).getShopCategoryName());
		}else {
			System.out.println("空值");
		}
		
		ShopCategory shopCategory = new ShopCategory();
		ShopCategory parentShopCategory = new ShopCategory();
		parentShopCategory.setShopCategoryId(1L);
		shopCategory.setParent(parentShopCategory);
		shopCategoryList = shopCategoryDao.queryShopCategory(shopCategory);
		assertEquals(1, shopCategoryList.size());
		if(shopCategoryList.get(0)!=null) {
			System.out.println(shopCategoryList.get(0).getShopCategoryName());
		}else {
			System.out.println("空值");
		}
//		List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
//		System.out.println(shopCategoryList.get(0).getShopCategoryImg());
		
	}
}
