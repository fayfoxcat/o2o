package org.fox.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.fox.o2o.BaseTest;
import org.fox.o2o.entity.Area;
import org.fox.o2o.entity.PersonInfo;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopDaoTest extends BaseTest{
	@Autowired
	private ShopDao shopDao;
	
 	@Test
 	public void testQueryShopList() {
 		Shop shop = new Shop();
 		PersonInfo owner =  new PersonInfo();
 		owner.setUserId(1L);
 		shop.setOwner(owner);
 		//1、测试一
 		List<Shop> shoplist = shopDao.queryShopList(shop, 0, 2);
 		System.out.println("店铺列表大小"+shoplist.size());
 		//2、测试二
 		ShopCategory sc = new ShopCategory();
 		sc.setShopCategoryId(3L);
 		shop.setShopCategory(sc);
 		List<Shop> shoplist2 = shopDao.queryShopList(shop, 0, 1);
 		System.out.println("店铺列表大小"+shoplist2.size());
 		//3、测试三
 		
 		//4、测试四
 		
 		//5、测试五
 		
 		int count = shopDao.queryShopCount(shop);
 		System.out.println("店铺总数："+count);
 	}
	
	
	
	
	@Test
	@Ignore
	public void testqueryById() {
		Shop shop = shopDao.queryByShopId(79L);
		System.out.println(shop.getArea().getAreaName());
		System.out.println(shop.getShopName());
	}
	
	@Test
	@Ignore
	public void testInsertShop() {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试店铺");
		shop.setShopDesc("test");
		shop.setShopAddr("test");
		shop.setPhone("test");
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int effectedNum = shopDao.insertShop(shop);
		assertEquals(1,effectedNum);
	}
	
	
	@Test
	@Ignore
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(1L);
		
		shop.setShopDesc("测试描述");
		shop.setShopAddr("测试地址");
		shop.setLastEditTime(new Date());
		
		int effectedNum = shopDao.updateShop(shop);
		assertEquals(1,effectedNum);
	}

}
//SELECT
//s.shop_id,
//s.shop_name,
//s.shop_desc,
//s.shop_addr,
//s.phone,
//s.shop_img,
//s.priority,
//s.create_time,
//s.last_edit_time,
//s.enable_status,
//s.advice,
//a.area_id,
//a.area_name,
//sc.shop_category_id,
//sc.shop_category_name
//FROM
//tb_shop s,
//tb_area a,
//tb_shop_category sc
//WHERE
//s.area_id = a.area_id
//AND
//s.shop_category_id = sc.shop_category_id
//AND
//
//s.owner_id= 0
//
//ORDER BY
//s.priority DESC
//LIMIT 0,6;