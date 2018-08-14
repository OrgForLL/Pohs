package com.md.goods.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.md.goods.model.GoodsPrice;

/**
 * 商品展示价格服务
 *
 * @author fengshuonan
 * @date 2017-04-27 17:00
 */
public interface IGoodsPriceService extends IService<GoodsPrice> {

	List<GoodsPrice> findByShopGoods(Long goodsId, Long shopId);

}
