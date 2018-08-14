package com.md.goods.dao;

import java.util.List;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.md.goods.model.Shop;

public interface ShopMapper extends BaseMapper<Shop>{

	/**
	 * 获取店铺所在省份id列表
	 */
    List<Long>  getProIdList();
}
