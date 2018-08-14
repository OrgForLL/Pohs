package com.stylefeng.guns.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.delivery.model.Area;
import com.md.delivery.service.IAreaService;
import com.stylefeng.guns.core.base.controller.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 地区接口
 * @author 54476
 *
 */

@Controller
@RequestMapping("/area")
public class ApiAreaController extends BaseController{

	@Resource
	IAreaService areaService;
	
	@ApiOperation(value = "获取省市列表", notes = "获取省市列表")
	@RequestMapping(value = "/getAreaList",method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getAreaList(@ApiParam("pid") @RequestParam(value = "pid", required = true)@RequestBody long pid,
			@ApiParam("类型，省 1，市2 区 3") @RequestParam(value = "type", required = true)@RequestBody Integer type) {
		JSONObject jb = new JSONObject();
		if(type == 1) {
			jb.put("province", areaService.getProvince());
		}else if(type == 2){
			jb.put("city", areaService.getCity(pid));
		}else if(type == 3){
			jb.put("county", areaService.getCounty(pid));
		}
		return jb;
	}
	
	@ApiOperation(value = "获取店铺所在省列表", notes = "获取店铺所在省列表")
	@RequestMapping(value = "/getProList",method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getProList() {
		JSONObject jb = new JSONObject();
		List<Area> list = areaService.getProListByShop();
		jb.put("list", list);
		return jb;
	}
	
}
