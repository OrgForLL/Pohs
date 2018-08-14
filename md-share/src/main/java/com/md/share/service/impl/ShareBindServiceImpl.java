package com.md.share.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.share.dao.ShareBindMapper;
import com.md.share.model.ShareBind;
import com.md.share.service.IShareBindService;

@Service
@Transactional
public class ShareBindServiceImpl extends ServiceImpl<ShareBindMapper,ShareBind> implements IShareBindService {

	@Resource
	ShareBindMapper shareBindMapper;
	
	@Override
	public List<ShareBind> selectByBoundMemberId(Long boundMemberId) {
		// TODO 自动生成的方法存根
		List<ShareBind> shareBinds = new ArrayList<>();
		Wrapper<ShareBind> wrapper = new EntityWrapper<>();
			wrapper.eq("boundMemberId", boundMemberId);	
			shareBinds = shareBindMapper.selectList(wrapper);
		return shareBinds;
	}


}
