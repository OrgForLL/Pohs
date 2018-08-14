package com.md.share.factory;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.md.goods.dao.ShopMapper;
import com.md.goods.model.Shop;
import com.stylefeng.guns.core.util.SpringContextHolder;

/**
 * 分销包装工厂
 *
 * @author
 * @date
 */
@Component
@DependsOn("springContextHolder")
public class ShareFactory {
	private ShopMapper shopMapper = SpringContextHolder.getBean(ShopMapper.class);

	public static ShareFactory me() {
		return SpringContextHolder.getBean(ShareFactory.class);
	}

	/**
	 * 获取门店名称组
	 * 
	 * @param shopId
	 * @return
	 */
	public String getShopName(String shopIds) {
		String[] shopIdArray = shopIds.split(",");
		String shopNames = "";
		for(String shopId : shopIdArray){
			Shop shop = shopMapper.selectById(shopId);
			shopNames = shopNames + ","+ shop.getName();
		}
		
		return shopNames;
	}

	

}
