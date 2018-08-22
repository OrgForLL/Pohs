package com.stylefeng.guns.adminapi;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.stylefeng.guns.core.base.controller.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户会员接口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/adminapi/member")
public class AdminApiMemberController extends BaseController {
	
	@Resource
	IMemberService memberService;
	

	@ApiOperation(value = "读取指定条件的用户信息列表", notes = "读取指定条件的用户信息列表    \r\n"
			+ "password:密码;  \r\n"
			+ "captcha:头像;  \r\n"
			+ "openId:微信openId;  \r\n"
			+ "sex:(1 男,2 女);  \r\n"
			+ "name:姓名;  \r\n"
			+ "phoneNum:手机号;  \r\n"
			+ "id:主键id,用户编号\"")
	@RequestMapping(value = "/getMemberListByCondition", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getMemberListByCondition (@ApiParam("用户名") @RequestParam(value = "name", required = false) @RequestBody String name,
			@ApiParam("用户Id") @RequestParam(value = "memberId", required = false) @RequestBody String memberId,
			@ApiParam("用户openId") @RequestParam(value = "openId", required = false) @RequestBody String openId,
			@ApiParam("手机号") @RequestParam(value = "phone", required = false) @RequestBody String phone){
		JSONObject jb = new JSONObject();
		List<Map<String, Object>> list = memberService.getListByCondition(name,memberId,openId,phone);
		jb.put("data", list);
		jb.put("errcode", 0);
		jb.put("errmsg", "");
		return jb;
	}
	
	@ApiOperation(value = "读取指定ID的用户信息", notes = "读取指定ID的用户信息    \r\n"
			+ "password:密码;  \r\n"
			+ "captcha:头像;  \r\n"
			+ "openId:微信openId;  \r\n"
			+ "sex:(1 男,2 女);  \r\n"
			+ "name:姓名;  \r\n"
			+ "phoneNum:手机号;  \r\n"
			+ "id:主键id,用户编号\"")
	@RequestMapping(value = "/getMemberInfo", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getMemberInfo (
			@ApiParam("用户Id") @RequestParam(value = "memberId", required = true) @RequestBody String memberId){
		JSONObject jb = new JSONObject();
		Member member = memberService.findById(Long.valueOf(memberId));
		jb.put("data", member);
		jb.put("errcode", 0);
		jb.put("errmsg", "");
		return jb;
	}
	
}
