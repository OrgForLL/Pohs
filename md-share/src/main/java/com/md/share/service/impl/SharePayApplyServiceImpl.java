package com.md.share.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.share.dao.SharePayApplyMapper;
import com.md.share.model.SharePayApply;
import com.md.share.service.ISharePayApplyService;
import com.stylefeng.guns.core.util.ToolUtil;

@Service
@Transactional
public class SharePayApplyServiceImpl extends ServiceImpl<SharePayApplyMapper, SharePayApply>
		implements ISharePayApplyService {

	@Resource
	SharePayApplyMapper sharePayApplyMapper;

	@Override
	public List<SharePayApply> selectAmountByStatus(Long memberId, Integer status) {
		// TODO 自动生成的方法存根
		List<SharePayApply> sharePayApplys = new ArrayList<>();
		Wrapper<SharePayApply> wrapper = new EntityWrapper<>();
		if(ToolUtil.isNotEmpty(memberId)){
			wrapper.eq("memberId", memberId);
		}
		if(ToolUtil.isNotEmpty(status)){
			wrapper.eq("status", status);
		}
		sharePayApplys = sharePayApplyMapper.selectList(wrapper);
		return sharePayApplys;
	}
	
	@Override
	public List<Map<String, Object>> selectAmountByStatusToo(Long memberId, Integer status) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> sharePayApplys = new ArrayList<>();
		Wrapper<SharePayApply> wrapper = new EntityWrapper<>();
		if(ToolUtil.isNotEmpty(memberId)){
			wrapper.eq("memberId", memberId);
		}
		if(ToolUtil.isNotEmpty(status)){
			wrapper.eq("status", status);
		}
		sharePayApplys = sharePayApplyMapper.selectMaps(wrapper);
		return sharePayApplys;
	}

}
