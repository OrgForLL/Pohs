package com.stylefeng.guns.adminapi;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.goods.model.PriceTag;
import com.md.pay.dao.WeiXinMapper;
import com.md.pay.model.WeiXin;
import com.md.pay.service.IWeixinService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.ToolUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 微信接口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/adminapi/weixin")
public class AdminApiWeixinController extends BaseController{

	@Resource
	IWeixinService weixinService;
	
	@Resource
	WeiXinMapper weiXinMapper;
	
	@ApiOperation(value = "跟新指定微信信息", notes = "跟新指定微信信息 ")
	@RequestMapping(value = "/saveWeixinInfo", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject saveWeixinInfo(
			@ApiParam("主键id") @RequestParam(value = "id", required = true) @RequestBody String id,
			@ApiParam("token") @RequestParam(value = "accessToken", required = true) @RequestBody String accessToken,
			@ApiParam("有效时间") @RequestParam(value = "expiresIn", required = true) @RequestBody long expiresIn) {
		JSONObject jb = new JSONObject();
		WeiXin weixin = weiXinMapper.selectById(id);
		if(ToolUtil.isEmpty(weixin)) {
			jb.put("data", "");
			jb.put("errcode", -1);
			jb.put("errmsg", "不存在当前微信信息");
			return jb;
		}
		weixin.setAccessToken(accessToken);
		weixin.setExpiresIn(expiresIn);
		weiXinMapper.updateById(weixin);

		jb.put("data", weixin);
		jb.put("errcode", 0);
		jb.put("errmsg", "");
		return jb;
	}
}
