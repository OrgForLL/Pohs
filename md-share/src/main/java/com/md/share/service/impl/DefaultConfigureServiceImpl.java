package com.md.share.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.share.dao.DefaultConfigureMapper;
import com.md.share.model.Configure;
import com.md.share.model.DefaultConfigure;
import com.md.share.service.IDefaultConfigureService;

@Service
@Transactional
public class DefaultConfigureServiceImpl extends ServiceImpl<DefaultConfigureMapper,DefaultConfigure> implements IDefaultConfigureService {

	@Resource
	DefaultConfigureMapper defaultConfigureMapper;
	
	@Override
	public List<DefaultConfigure> selectByType(Integer type) {
		// TODO 自动生成的方法存根
		List<DefaultConfigure> configures = new ArrayList<>();
		Wrapper<DefaultConfigure> wrapper = new EntityWrapper<>();
		wrapper.eq("type", type);	
		configures = defaultConfigureMapper.selectList(wrapper);
		return configures;
	}
}
