package com.md.share.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.share.dao.ConfigureMapper;
import com.md.share.model.Configure;
import com.md.share.service.IConfigureService;

@Service
@Transactional
public class ConfigureServiceImpl extends ServiceImpl<ConfigureMapper,Configure> implements IConfigureService {

	@Resource
	ConfigureMapper configureMapper;
	
	@Override
	public List<Configure> selectByAssociatedEntity(Long associatedEntityId, Integer type) {
		// TODO 自动生成的方法存根
		List<Configure> configures = new ArrayList<>();
		Wrapper<Configure> wrapper = new EntityWrapper<>();
			wrapper.eq("associatedEntityId", associatedEntityId);	
			wrapper.eq("type", type);	
			configures = configureMapper.selectList(wrapper);
		return configures;
	}
}
