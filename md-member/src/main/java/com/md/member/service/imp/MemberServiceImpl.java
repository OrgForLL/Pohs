package com.md.member.service.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.member.dao.MemberMapper;
import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.stylefeng.guns.core.util.ToolUtil;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

	@Resource
	MemberMapper memberMapper;

	@Override
	public List<Map<String, Object>> find(Member member) {
		Wrapper<Member> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(member)) {
			if (ToolUtil.isNotEmpty(member.getName())) {
				wrapper.like("name", member.getName());
			}
			if (ToolUtil.isNotEmpty(member.getPhoneNum())) {
				wrapper.like("phoneNum", member.getPhoneNum());
			}
			if (ToolUtil.isNotEmpty(member.getOpenId())) {
				wrapper.like("openId", member.getOpenId());
			}
		}
		return memberMapper.selectMaps(wrapper);
	}

	@Override
	public void add(Member member) {
		memberMapper.insert(member);
	}

	@Override
	public Member findById(Long id) {
		return memberMapper.selectById(id);
	}

	@Override
	public void update(Member member) {
		memberMapper.updateById(member);
	}

	@Override
	public List<Member> selectByPhoneNum(String phoneNum) {
		// TODO 自动生成的方法存根
		List<Member> shareUsers = new ArrayList<>();
		Wrapper<Member> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(phoneNum)) {
			wrapper.eq("phoneNum", phoneNum);	
			shareUsers = memberMapper.selectList(wrapper);
		}
		return shareUsers;
	}

	@Override
	public List<Map<String, Object>> findShareUser(Integer status) {
		// TODO 自动生成的方法存根
		List<Map<String, Object>> shareUsers = new ArrayList<>();
		Wrapper<Member> wrapper = new EntityWrapper<>();
		if(ToolUtil.isNotEmpty(status)){
			wrapper.eq("status", status);
		}
		wrapper.eq("type", "分销员");
		shareUsers = memberMapper.selectMaps(wrapper);
		return shareUsers;
	}

	@Override
	public List<Map<String, Object>> getListByCondition(String name, Long memberId, String openId, String phone) {
		System.out.println(name+","+memberId+","+openId+","+phone);
		Wrapper<Member> wrapper = new EntityWrapper<>();
		if(memberId != 0l) {
			wrapper.eq("id", memberId);
		}
		if(ToolUtil.isNotEmpty(openId)) {
			wrapper.eq("openId", openId);
		}
		if(ToolUtil.isNotEmpty(name)) {
			wrapper.like("name", name);
		}
		if(ToolUtil.isNotEmpty(phone)) {
			wrapper.like("phoneNum", phone);
		}
		
		return memberMapper.selectMaps(wrapper);
	}
}
