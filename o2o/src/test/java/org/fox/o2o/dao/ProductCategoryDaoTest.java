package org.fox.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fox.o2o.BaseTest;
import org.fox.o2o.entity.ProductCategory;
import org.fox.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

//设置test方法执行顺序
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	ProductCategoryDao productCategoryDao;
	
	@Test
	@Ignore
	public void testQueryByShopId() {
		Shop shop = new Shop();
		shop.setShopId(2L);
		List<ProductCategory> queryProductCategoyList = productCategoryDao.queryProductCategoyList(shop.getShopId());
		System.out.println("该店铺自定义类别数："+queryProductCategoyList.size());
	}
	
	@Test
	public void testAInsertProductCategory() throws Exception {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("商品类别1");
		productCategory.setPriority(1);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(1L);
		
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品类别2");
		productCategory2.setPriority(2);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory);
		productCategoryList.add(productCategory2);
		
		int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectedNum);
	}
}
//insert into tb_product_category values(1,"数码产品",1,NULL,81);
//insert into tb_shop values(1,2,1,1,"星空数码旗舰店","正品行货，欲购从速","合肥市蜀山区","10789933321",null,1,null,null,1,null);
//insert into tb_area values(1,"安徽财经大学",3,null,null);
//insert into tb_person_info values(3,"崔东山",NULL,null,"1",1,2,null,null);