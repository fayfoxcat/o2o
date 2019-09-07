package org.fox.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin" ,method = {RequestMethod.GET},produces="text/html;charset=UTF-8")
public class ShopAdminController {
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		return "/shop/shopoperation";
	}
	
	@RequestMapping(value="/shoplist")
	public String shopList() {
		//转发到店铺列表页面
		return "/shop/shoplist";
	}
	
	@RequestMapping(value="/shopmanagement")
	public String shopManagement() {
		//转发到店铺管理页面
		return "/shop/shopmanagement";
	}
	
	@RequestMapping(value="/productcategorymanagement")
	public String productCategoryManagement() {
		//转发到商品类别管理页面
		return "/shop/productcategorymanagement";
	}
	
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		//转发到商品添加/编辑页面
		return "/shop/productoperation";
	}
	
	@RequestMapping(value="/productmanagement")
	public String productManagement() {
		//转发到商品管理界面
		return "/shop/productmanagement";
	}
}
