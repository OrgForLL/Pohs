package com.md.goods.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.goods.dao.GoodsPriceMapper;
import com.md.goods.model.GoodsPrice;
import com.md.goods.service.IGoodsPriceService;
import com.stylefeng.guns.core.util.ToolUtil;

@Service
public class GoodsPriceServiceImpl extends ServiceImpl<GoodsPriceMapper, GoodsPrice> implements IGoodsPriceService {

	@Resource
	GoodsPriceMapper goodsPriceMapper;
	
	@Override
	public List<GoodsPrice> findByShopGoods(Long goodsId,Long shopId) {
		// TODO 自动生成的方法存根
		Wrapper<GoodsPrice> wrapper = new EntityWrapper<>();
		wrapper.eq("goodsId", goodsId);
		if(ToolUtil.isNotEmpty(shopId)){
			wrapper.eq("shopId", shopId);
		}
		return goodsPriceMapper.selectList(wrapper);	
	}

}
