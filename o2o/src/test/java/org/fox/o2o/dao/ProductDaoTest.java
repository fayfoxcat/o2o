package org.fox.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.fox.o2o.BaseTest;
import org.fox.o2o.entity.Product;
import org.fox.o2o.entity.ProductCategory;
import org.fox.o2o.entity.ProductImg;
import org.fox.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Test
	@Ignore
	public void testAInsertProduct() throws Exception {
		Shop shop1 = new Shop();
		shop1.setShopId(1L);
		Shop shop2 = new Shop();
		shop2.setShopId(2L);
		ProductCategory pc1 = new ProductCategory();
		pc1.setProductCategoryId(2L);
		ProductCategory pc2 = new ProductCategory();
		pc2.setProductCategoryId(3L);
		ProductCategory pc3 = new ProductCategory();
		pc3.setProductCategoryId(4L);
		Product product1 = new Product();
		product1.setProductName("测试1");
		product1.setProductDesc("测试Desc1");
		product1.setImgAddr("test1");
		product1.setPriority(0);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEditTime(new Date());
		product1.setShop(shop1);
		product1.setProductCategory(pc1);
		Product product2 = new Product();
		product2.setProductName("测试2");
		product2.setProductDesc("测试Desc2");
		product2.setImgAddr("test2");
		product2.setPriority(0);
		product2.setEnableStatus(0);
		product2.setCreateTime(new Date());
		product2.setLastEditTime(new Date());
		product2.setShop(shop1);
		product2.setProductCategory(pc2);
		Product product3 = new Product();
		product3.setProductName("测试3");
		product3.setProductDesc("测试Desc3");
		product3.setImgAddr("test3");
		product3.setPriority(0);
		product3.setEnableStatus(1);
		product3.setCreateTime(new Date());
		product3.setLastEditTime(new Date());
		product3.setShop(shop2);
		product3.setProductCategory(pc3);
		int effectedNum = productDao.insertProduct(product1);
		assertEquals(1, effectedNum);
		effectedNum = productDao.insertProduct(product2);
		assertEquals(1, effectedNum);
		effectedNum = productDao.insertProduct(product3);
		assertEquals(1, effectedNum);
	}

//	@Test
//	@Ignore
//	public void testBQueryProductList() throws Exception {
//		Product product = new Product();
//		List<Product> productList = productDao.queryProductList(product, 0, 3);
//		assertEquals(3, productList.size());
//		int count = productDao.queryProductCount(product);
//		assertEquals(4, count);
//		product.setProductName("测试");
//		productList = productDao.queryProductList(product, 0, 3);
//		assertEquals(3, productList.size());
//		count = productDao.queryProductCount(product);
//		assertEquals(3, count);
//		Shop shop = new Shop();
//		shop.setShopId(2L);
//		product.setShop(shop);
//		productList = productDao.queryProductList(product, 0, 3);
//		assertEquals(1, productList.size());
//		count = productDao.queryProductCount(product);
//		assertEquals(1, count);
//	}
//
	@Test
	@Ignore
	public void testCQueryProductByProductId() throws Exception {
		long productId = 1;
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
		Product product = productDao.queryProductByProductId(productId);
		assertEquals(2, product.getProductImgList().size());
		effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}

	@Test
	@Ignore
	public void testDUpdateProduct() throws Exception {
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		product.setProductId(1L);
		product.setShop(shop);
		product.setProductName("第二个产品");
		//修改productId为1的商品的名称
		//以及商品类别并校验影响的行数是否为1
		int effectedNum=productDao.updateProduct(product);
		assertEquals(1,effectedNum);
	}
//
//	@Ignore
//	@Test
//	public void testEDeleteShopAuthMap() throws Exception {
//		int effectedNum = productDao.deleteProduct(2, 1);
//		assertEquals(1, effectedNum);
//	}
	@Test
	public void testQueryProductList() throws Exception {
		Product productCondition = new Product();
		//分页查询、预期返回三条结果
		List<Product> productList = productDao.queryProductList(productCondition, 0, 3);
		assertEquals(3, productList.size());
		//查询名称为测试的商品总数
		int count = productDao.queryProductCount(productCondition);
		assertEquals(13,count);
		//使用商品名称模糊查询，预期返回结果4条
		productCondition.setProductName("测试");
		productList = productDao.queryProductList(productCondition, 0, 6);
		assertEquals(4,productList.size());
		count = productDao.queryProductCount(productCondition);
		assertEquals(4,count);
	}
}


















