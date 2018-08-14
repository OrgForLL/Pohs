package com.md.delivery.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.md.delivery.model.Area;

public interface AreaMapper extends BaseMapper<Area>{

	/**
	 * 获取店铺所在省列表
	 * @return
	 */
	List<Area> getProListByShop();
	
}
