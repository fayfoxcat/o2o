package org.fox.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fox.o2o.dto.ImageHolder;
import org.fox.o2o.dto.ShopExecution;
import org.fox.o2o.entity.Area;
import org.fox.o2o.entity.PersonInfo;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.entity.ShopCategory;
import org.fox.o2o.enums.ShopStateEnum;
import org.fox.o2o.exceptions.ShopOperationException;
import org.fox.o2o.service.AreaService;
import org.fox.o2o.service.ShopCategoryService;
import org.fox.o2o.service.ShopService;
import org.fox.o2o.util.Codeutil;
import org.fox.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value="/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
		
	/**
	 * 初始化注册店铺页面
	 * @return
	 */
	@RequestMapping(value="/getshopinitinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getshopInitInfo(){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategorylist",shopCategoryList );
			modelMap.put("arealist", areaList);
			modelMap.put("success", true);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errorMessage",e.getMessage());
		}
		return modelMap;
	}
	
	/**
	 * 根据shopId获取用户注册店铺信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshopbyid",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopById(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		Long shopId = HttpServletRequestUtil.getLong(request,"shopId");
		if(shopId>-1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop",shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			}catch(Exception e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage",e.toString());
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errorMessage","ShopId为空！");
		}
		return modelMap;
 	}
	
	/**
	 * 注册店铺信息
	 *接受并转化相应的参数，包括店铺信息以及图片信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/registershop",method = RequestMethod.POST)
	@ResponseBody	//该注解将对象类型的返回值变为json对象
	private Map<String,Object> registerShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		
		//判读验证码是否正确
		if(!Codeutil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errorMessage","输入了错误的验证码！");
			return modelMap;
		}
		
		String ShopStr = HttpServletRequestUtil.getString(request,"shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(ShopStr,Shop.class);
		}
		catch(Exception e) {
			modelMap.put("success",false);
			modelMap.put("errorMessage",e.getMessage());
			System.out.println(e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//、判断文件流是否存在  不存在：即返回错误信息
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else {
			modelMap.put("success",false);
			modelMap.put("errorMessage","上传图片不能为空");
			return modelMap;
		}
		
		//2、注册店铺
		if(shop!=null&&shopImg!=null) {
			//通过Session中保存的信息获取owner信息进行店铺注册
			PersonInfo user = new PersonInfo();
			user.setUserId(2L);
			request.getSession().setAttribute("user", user);;
			user =(PersonInfo)request.getSession().getAttribute("user");
			shop.setOwner(user);
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop,imageHolder);
				if(se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success",true);
					//用户可操作的店铺列表(放入Session中，以备之后使用)
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
				}else {
					modelMap.put("success", false);
					modelMap.put("errorMessage",se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage",e.getMessage());
			} catch (IOException e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage",e.getMessage());
			}
			//3、返回结果
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errorMessage","请输入店铺信息");
			return modelMap;
		}
	}
	
	/**
	 * 修改店铺信息：Controller层
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/modifyshop",method = RequestMethod.POST)
	@ResponseBody	//该注解将对象类型的返回值变为json对象
	private Map<String,Object> modifyShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		
		//、判读验证码是否正确
		if(!Codeutil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errorMessage","输入了错误的验证码！");
			return modelMap;
		}
		
		String ShopStr = HttpServletRequestUtil.getString(request,"shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(ShopStr,Shop.class);
		}
		catch(Exception e) {
			modelMap.put("success",false);
			modelMap.put("errorMessage",e.getMessage());
			System.out.println(e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//、判断文件流是否存在 ,不存在：即图片不更新
		if(commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}
		
		//2、修改店铺信息
		if(shop!=null&&shop.getShopId()!=null) {
			ShopExecution se;
			try {
				if(shopImg == null) {
					se = shopService.modifyShop(shop,null);
				}else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					se = shopService.modifyShop(shop,imageHolder);
				}
				if(se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success",true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errorMessage",se.getStateInfo());
				}
			} catch (ShopOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage",e.getMessage());
			} catch (IOException e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage",e.getMessage());
			}
			//3、返回结果
			return modelMap;
		}else {
			modelMap.put("success", false);
			modelMap.put("errorMessage","请输入店铺信息");
			return modelMap;
		}

	}
	/**
	 * 通过Session获取当前用户的名下所有商铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getshoplist",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopList(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		PersonInfo user = new PersonInfo();
		user.setUserId(2L);
		request.getSession().setAttribute("user", user);;
		user =(PersonInfo)request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList",se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success",true);
		}catch(Exception e) {
			modelMap.put("success",false);
			modelMap.put("errorMessage",e.getMessage());
		}
		return modelMap;
	}
	
	@RequestMapping(value="/getshopmanagementinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getShopMansgementInfo(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0) {
			Object currentShopObject = request.getSession().getAttribute("currentShop");
			if(currentShopObject == null) {
				modelMap.put("redirect",true);
				modelMap.put("url","/o2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop) currentShopObject;
				modelMap.put("redirect",false);
				modelMap.put("redirect", currentShop.getShopId());
			}
		}else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
}


















