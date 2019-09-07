package org.fox.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fox.o2o.dto.ShopExecution;
import org.fox.o2o.entity.Area;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.entity.ShopCategory;
import org.fox.o2o.service.AreaService;
import org.fox.o2o.service.ShopCategoryService;
import org.fox.o2o.service.ShopService;
import org.fox.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;
	
	/**
	 * 返回商品列表页里的ShopCategory列表（一级或二级）、以及区域信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前端请求中的parentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if(parentId != -1){
			//如果parentId存在，则取出该一级ShopCategory下的二级ShopCategory列表
			try{
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			}catch(Exception e){
				modelMap.put("success", false);
				modelMap.put("errorMessage", e.toString());
			}
		}else{
			try{
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			}catch(Exception e){
				modelMap.put("success", false);
				modelMap.put("errorMessage", e.toString());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			//获取区域列表信息
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMessage", e.toString());
		}
		return modelMap;
	}

	/**
	 * 获取指定查询条件下的店铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取页码、数据条数
		int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
		//非控判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			//获取一级类别ID、二级类别ID、区域类别ID、模糊查询名
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");			
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			long areaId = HttpServletRequestUtil.getLong(request, "areaId");
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			//获取组合之后的查询条件
			Shop shopCondition = compactShopConditionSearch(parentId, shopCategoryId, areaId, shopName);
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("erreorMessage", "页码或数据行为空！");
		}
		return modelMap;
	}

	private Shop compactShopConditionSearch(long parentId, long shopCategoryId, long areaId, String shopName) {
		Shop shopCondition = new Shop();
		//查询某个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
		if (parentId != -1L) {
			ShopCategory parentCategory = new ShopCategory();
			ShopCategory childCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		//查询某个二级ShopCategory下面的店铺列表
		if (shopCategoryId != -1L) {
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		//查询位于某个区域Id下的店铺列表
		if (areaId != -1L) {
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}

		if (shopName != null) {
			shopCondition.setShopName(shopName);
		}
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
