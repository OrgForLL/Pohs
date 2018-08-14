package com.md.share.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.md.share.model.Configure;

import aj.org.objectweb.asm.Type;

public interface IConfigureService extends IService<Configure> {

	List<Configure> selectByAssociatedEntity(Long associatedEntityId,Integer Type);
	
}
