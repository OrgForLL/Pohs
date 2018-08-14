package com.md.share.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.md.share.model.Configure;
import com.md.share.model.DefaultConfigure;

public interface IDefaultConfigureService extends IService<DefaultConfigure> {

	List<DefaultConfigure> selectByType(Integer type);
	
}
