package org.fox.o2o.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.fox.o2o.dao.ShopDao;
import org.fox.o2o.dto.ImageHolder;
import org.fox.o2o.dto.ShopExecution;
import org.fox.o2o.entity.Shop;
import org.fox.o2o.enums.ShopStateEnum;
import org.fox.o2o.exceptions.ShopOperationException;
import org.fox.o2o.service.ShopService;
import org.fox.o2o.util.ImageUtil;
import org.fox.o2o.util.PageCalculator;
import org.fox.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShopServiceImpl implements ShopService{
	@Autowired
	private ShopDao shopDao;
	
	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail){
		//空值判断
		if(shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			//给店铺信息赋值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息（写入数据库）
			int effectedNum = shopDao.insertShop(shop);
			if(effectedNum <=0) {
				throw new ShopOperationException("店铺创建失败");
			}else {
				if(thumbnail.getImage() != null) {
					try {
						addShopImg(shop,thumbnail);
					}catch(Exception e) {
						throw new ShopOperationException("addShopImg error:"+e.getMessage());
					}
					//更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if(effectedNum <=0) {
						throw new ShopOperationException("更新图片的地址失败");
					}
				}
			}
		}catch(Exception e){
			//异常将进行事务回滚（更新店铺失败，回滚至未创建店铺之前）
			throw new ShopOperationException("addShop error:"+e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	
	private void addShopImg(Shop shop,ImageHolder thumbnail) {
		
		//获取shop图片目录的绝对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
			throws ShopOperationException {
		if(shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.INNER_ERROR);
		}else {
			//1.判断是否需要处理图片:删除原文件、生成新文件
			try {
				if(thumbnail.getImage() != null && thumbnail.getImageName() !=null && !"".equals(thumbnail.getImageName())) {
					Shop tempshop = shopDao.queryByShopId(shop.getShopId());
					if(tempshop.getShopImg()!=null) {
						ImageUtil.deleteFileOrPath(tempshop.getShopImg());
					}
					addShopImg(shop,thumbnail);
				}
				//2.更新店铺相关的其他信息
				shop.setLastEditTime(new Date());
				int effectedNum =shopDao.updateShop(shop);
				if(effectedNum<=0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				}else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS,shop);
				}
			}catch(Exception e) {
				throw new ShopOperationException("更新店铺信息发生异常："+e.getMessage());
			}
		}
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if(shopList !=null) {
			se.setShopList(shopList);
			se.setCount(count);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
}






















