package org.fox.o2o.service;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.fox.o2o.BaseTest;
import org.fox.o2o.dto.ImageHolder;
import org.fox.o2o.dto.ShopExecution;
import org.fox.o2o.entity.Area;
import org.fox.o2o.entity.PersonInfo;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.entity.ShopCategory;
import org.fox.o2o.enums.ShopStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopServiceTest extends BaseTest{
	@Autowired
	private ShopService shopService;
	
	@Test
 	public void testQueryShopList() {
 		Shop shop = new Shop();
 		ShopCategory sc = new ShopCategory();
 		sc.setShopCategoryId(3L);
 		shop.setShopCategory(sc);
 		ShopExecution se = shopService.getShopList(shop, 0, 2);
 		System.out.println("店铺列表数"+se.getCount());
 	}
	
	@Test
	@Ignore
	public void testModifyShop() throws FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopId(79L);
		shop.setShopName("邂逅面包店");
		File shopImg = new File("D:/Users/o2o_image/matebook_pro_2019.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(),is);
		ShopExecution se = shopService.modifyShop(shop,imageHolder);
		System.out.println("更新后图片地址为："+se.getShop().getShopImg());
	}
	
	@Test
	@Ignore
	public void test() throws FileNotFoundException {
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
		shop.setShopName("测试店铺4");
		shop.setShopDesc("test4");
		shop.setShopAddr("test4");
		shop.setPhone("test4");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		
		File shopImg = new File("D:/Users/o2o_image/matebook_pro_2019.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(),is);
		ShopExecution se = shopService.addShop(shop,imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
	}

}
