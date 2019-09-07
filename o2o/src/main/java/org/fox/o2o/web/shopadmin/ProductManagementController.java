package org.fox.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.fox.o2o.dto.ImageHolder;
import org.fox.o2o.dto.ProductExecution;
import org.fox.o2o.entity.Product;
import org.fox.o2o.entity.ProductCategory;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.enums.ProductStateEnum;
import org.fox.o2o.exceptions.ProductOperationException;
import org.fox.o2o.service.ProductCategoryService;
import org.fox.o2o.service.ProductService;
import org.fox.o2o.util.Codeutil;
import org.fox.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value="/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	//定义上传商品详情图的最大数量：6
	private static final int IMAGEMAXCOUNT = 6;
	
	/**
	 * 通过商品id获取商品信息
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//非空判断
		if (productId > -1) {
			//获取商品信息
			Product product = productService.getProductById(productId);
			//获取该店铺下的商品类别列表
			List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errorMessage", "productId为空！");
		}
		return modelMap;
	}
	
	@RequestMapping(value="/addproduct",method = RequestMethod.POST)
	@ResponseBody
	private Map<String,Object> addProduct(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//验证码
		//判读验证码是否正确
		if(!Codeutil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errorMessage","输入了错误的验证码！");
			return modelMap;
		}
		//接受前端参数的变量的初始化，包括商品、缩略图、详情图列表实体类
		ObjectMapper mapper = new ObjectMapper();
		Product product = new Product();
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		//解析请求中是否包含文件流
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		try {
			//若请求中存在文件流，则取出相关的文件（包括所缩略图和详情图）
			if(multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}else {
				modelMap.put("success",false);
				modelMap.put("errorMessage","上传图片不能为空！");
				return modelMap;
			}
		}catch(Exception e) {
			modelMap.put("success",false);
			modelMap.put("errorMessage",e.toString());
			return modelMap;
		}
		
		//尝试获取前端传递表单String流并将其转换为Product实体类
		try {
			String productStr = HttpServletRequestUtil.getString(request,"productStr");
			product = mapper.readValue(productStr,Product.class);
		}catch(Exception e) {
			modelMap.put("success",false);
			System.out.println(e.toString());
			return modelMap;
		}
		
		/**
		 * 构建的缩略图、详情图列表对象、表单信息非空、则调用Service
		 */
		if(product != null && thumbnail != null && productImgList.size() > 0) {
			try {
				//从session中获取当前店铺Id并赋值给product，减少对前端数据的依赖
				Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//调用Service层，执行添加操作
				ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
				if(pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success",false);
					modelMap.put("errorMessage",pe.getStateInfo());
				}
			}catch(ProductOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage",e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errorMessage","请输入商品信息");
		}
		return modelMap;
	}
	
	@RequestMapping(value="/getproductlistbyshop",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getProductListByShop(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//获取前端传过来的页码
		int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
		//获取前台传过来的每页要求返回的商品数上限
		int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
		//从当前session中获取店铺信息：shopId
		Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
		//空值判断
		if((pageIndex)>-1 && (pageSize >-1) && (currentShop !=null ) && (currentShop.getShopId() != null)) {
			//获取传入的需要检索的条件，包括是否需要某个商品类别以及模糊查询商品名去筛选某个店铺下的商品列表，对筛选条件进行排列组合
			long productCategoryId = HttpServletRequestUtil.getInteger(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId, productName);
			//传入查询条件以及分页信息进行查询，返回响应的列表以及总数
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		}
		else {
			modelMap.put("success", false);
			modelMap.put("errorMessage","ShopId不能为空！");
		}
		return modelMap;
	}
	
	
	/**
	 * 商品编辑
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
		boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!statusChange && !Codeutil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errorMessage", "输入了错误的验证码");
		}
		//接受前端参数的变量初始化、包括商品、缩略图、详情图列表实体类
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		//若请求中存在文件流，则取出相关的文件(包括缩略图、详情图)
		try {
			if(multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}else {
				modelMap.put("success", false);
				modelMap.put("errorMessage","上传图片不能为空！");
			}
		}catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMessage",e.getMessage());
			return modelMap;
		}
		try {
			String productStr = HttpServletRequestUtil.getString(request, "productStr");
			//尝试获取前端传过来的表单string流并将其转换成Product实体类
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMessage", e.toString());
			return modelMap;
		}
		//非空判断
		if (product != null) {
			try {
				Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//开始进行商品信息变更操作
				ProductExecution pe = productService.modifyProduct(product,thumbnail, productImgList);
				if(pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success",false);
					modelMap.put("errorMessage",pe.getStateInfo());
				}
			}catch(ProductOperationException e) {
				modelMap.put("success",false);
				modelMap.put("errorMessage",e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success",false);
			modelMap.put("errorMessage","请输入商品信息");
		}
		return modelMap;
	}
	
	/**
	 * 
	 * @param request
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws IOException
	 */
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws IOException {
		MultipartHttpServletRequest	multipartRequest = (MultipartHttpServletRequest) request;
		//扫描缩略图、构建ImageHolder对象
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		if(thumbnailFile != null) {
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
		}
		//取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图片上传
		for(int i = 0; i<IMAGEMAXCOUNT;i++) {
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
			if(productImgFile != null) {
				ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
				productImgList.add(productImg);
			}else {
				//若取出的文件流为空，则终止循环
				break;
			}
		}
		return thumbnail;
	}
	
	/**
	 * 将查询条件组合成Product对象，一遍Service调用
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	public Product compactProductCondition(long shopId,long productCategoryId,String productName) {
		Product procutCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		procutCondition.setShop(shop);
		//若有指定类别的要求则添加进去
		if(productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setShopId(productCategoryId);
			procutCondition.setProductCategory(productCategory);
		}
		//若有商品名模糊查询的要求则添加进去
		if(productName != null) {
			procutCondition.setProductName(productName);
		}
		return procutCondition;
	}
}



















