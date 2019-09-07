package org.fox.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fox.o2o.dto.ProductExecution;
import org.fox.o2o.entity.Product;
import org.fox.o2o.entity.ProductCategory;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.service.ProductCategoryService;
import org.fox.o2o.service.ProductService;
import org.fox.o2o.service.ShopService;
import org.fox.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/frontend")
public class ShopDetailController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	/**
	 * 获取店铺信息以及该店铺下的商品分类类别列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listshopdetailpageinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//获取前台传递过来的shopId
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		if(shopId != -1) {
			//获取店铺Id为shopId的店铺信息
			shop = shopService.getByShopId(shopId);
			//获取店铺下面的商品类别列表
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop",shop);
			modelMap.put("productCategoryList",productCategoryList);
			modelMap.put("success",true);
		}
		else {
			modelMap.put("errorMessage","shopId为空！");
			modelMap.put("success",false);
		}
		return modelMap;
	}
	
	/**
	 * 依据查询条件分页列出该店铺下面所有商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/listproductsbyshop",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listProductByShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//获取页码
		int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
		//获取数据行数
		int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
		//获取店铺Id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		//空值判断
		if((pageIndex >-1) && (pageSize > -1) && (shopId > -1)) {
			//获取商品类别Id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			//获取模糊查询的商品名
			String productName = HttpServletRequestUtil.getString(request, "productName");
			//组合查询条件
			Product productCondition = compactProductConditionSerach(shopId,productCategoryId,productName);
			//调用Service，返回对应查询条件及分页信息的商品列表和总数
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList",pe.getProductList());
			modelMap.put("count",pe.getCount());
			modelMap.put("success",true);
		}else {
			modelMap.put("success",false);
			modelMap.put("errorMessage","shopId或pageIdex或pageSize为空！");
		}
		return modelMap;
	}
	
	/**
	 * 组合查询条件，并将条件封装到ProductCondition对象里返回
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductConditionSerach(long shopId,long productCategoryId,String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		if(productCategoryId != -1L) {
			//查询某个商品类别下面的商品列表
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName != null) {
			//查询名子包含procuctName的店铺列表
			productCondition.setProductName(productName);
		}
		//只允许选出状态为上架的商品
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
