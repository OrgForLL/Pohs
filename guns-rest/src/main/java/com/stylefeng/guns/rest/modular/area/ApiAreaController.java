package com.stylefeng.guns.rest.modular.area;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.md.delivery.model.Area;
import com.md.delivery.service.IAreaService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.rest.modular.area.dto.AreaRquest;

import io.swagger.annotations.ApiOperation;

/**
 * 地区接口
 * @author 54476
 *
 */

@RestController
@RequestMapping("/area")
public class ApiAreaController extends BaseController{

	@Resource
	IAreaService areaService;
	
	@ApiOperation(value = "获取省市列表", notes = "获取省市列表")
	@RequestMapping(value = "/getAreaList",method = RequestMethod.POST)
	public ResponseEntity<?> getAreaList(@RequestBody AreaRquest areaRquest) {
		if(areaRquest.getType() == 1) {
			return ResponseEntity.ok(areaService.getProvince());
		}else if(areaRquest.getType() == 2){
			return ResponseEntity.ok(areaService.getCity(areaRquest.getPid()));
		}else if(areaRquest.getType() == 3){
			return ResponseEntity.ok(areaService.getCounty(areaRquest.getPid()));
		}
		return ResponseEntity.ok("");
	}
	
	@ApiOperation(value = "获取店铺所在省列表", notes = "获取店铺所在省列表")
	@RequestMapping(value = "/getProList",method = RequestMethod.POST)
	public ResponseEntity<?> getProList() {
		List<Area> list = areaService.getProListByShop();
		return ResponseEntity.ok(list);
	}
	
}
