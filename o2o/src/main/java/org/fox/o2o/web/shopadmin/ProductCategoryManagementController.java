package org.fox.o2o.web.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fox.o2o.dto.ProductCategoryExecution;
import org.fox.o2o.entity.ProductCategory;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.enums.ProductCategoryStateEnum;
import org.fox.o2o.exceptions.ProductCategoryOperationException;
import org.fox.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/shopadmin")
public class ProductCategoryManagementController {
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value="/getproductcategorylist",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getProductCategoryList(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		Shop shop = new Shop();
		shop.setShopId(2L);
		request.getSession().setAttribute("currentShop", shop);
		shop = (Shop) request.getSession().getAttribute("currentShop");
		
		if(shop != null && shop.getShopId() >0 ) {
			List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shop.getShopId());
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success",true);
		}else {
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			modelMap.put("errorMessage",ps.getState());
			modelMap.put("errorMessage",ps.getStateInfo());
			modelMap.put("success", false);
		}
		return modelMap;
	}
	
	@RequestMapping(value="/addproductcategorys",method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for(ProductCategory pc:productCategoryList) {
			pc.setShopId(currentShop.getShopId());
		}
		if(productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
				if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);
					System.out.println("添加成功");
				}else {
					modelMap.put("success", false);
					modelMap.put("errorMessage",pe.getStateInfo());
				}
			}catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errorMessage",e.toString());
			}
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId,HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId());
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errorMessage", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errorMessage", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errorMessage", "请至少选择一个商品类别");
		}
		return modelMap;
	}
}
















