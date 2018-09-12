package com.stylefeng.guns.adminapi;

import java.math.BigDecimal;
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
import com.md.delivery.model.Area;
import com.md.delivery.model.DeliveryCost;
import com.md.delivery.service.IAreaService;
import com.md.delivery.service.IDeliveryCostService;
import com.md.delivery.service.imp.DeliveryCostServiceImpl;
import com.md.delivery.warpper.DeliveryCostWarpper;
import com.md.goods.model.Shop;
import com.md.goods.service.IShopService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.ToolUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 物流接口
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/adminapi/delivery")
public class AdminApiDeliveryController extends BaseController {

	@Resource
	IAreaService areaService;
	@Resource
	IShopService shopService;
	@Resource
	IDeliveryCostService deliveryCostService;

	@ApiOperation(value = "获取省市列表", notes = "获取省市列表")
	@RequestMapping(value = "/getAreaList", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getAreaList(
			@ApiParam("parentid") @RequestParam(value = "parentid", required = true) @RequestBody long parentid,
			@ApiParam("是否返回子孙项，参数允许缺省，缺省值为0，表示是不返回子孙项") @RequestParam(value = "all", required = false) @RequestBody Integer all) {
		JSONObject jb = new JSONObject();
		if (ToolUtil.isEmpty(all)) {
			all = 0;
		}
		if (ToolUtil.isEmpty(parentid)) {
			parentid = 0;
		}
		if (parentid == 0) {
			if (all == 0) {
				jb.put("errcode", 0);
				jb.put("errmsg", "ok");
				jb.put("data", areaService.getProvince());
			} else {
				jb.put("errcode", 0);
				jb.put("errmsg", "ok");
				jb.put("data", areaService.findAreaList(null, null, null));
			}
		} else {
			Area area = areaService.selectById(parentid);
			if (area.getGrade() == 2) {
				if (all == 0) {
					jb.put("errcode", 0);
					jb.put("errmsg", "ok");
					jb.put("data", "[]");
				}
			}
			if (area.getGrade() == 1) {
				if (all == 0) {
					jb.put("errcode", 0);
					jb.put("errmsg", "ok");
					jb.put("data", areaService.getCounty(parentid));
				} else {
					jb.put("errcode", 0);
					jb.put("errmsg", "ok");
					jb.put("data", areaService.findAreaList(null, parentid, null));
				}
			}
			if (area.getGrade() == 0) {
				if (all == 0) {
					jb.put("errcode", 0);
					jb.put("errmsg", "ok");
					jb.put("data", areaService.getCity(parentid));
				} else {
					jb.put("errcode", 0);
					jb.put("errmsg", "ok");
					jb.put("data", areaService.findAreaList(parentid, null, null));
				}
			}
		}
		return jb;
	}

	@ApiOperation(value = "获取门店运费配置信息", notes = "获取门店运费配置信息")
	@RequestMapping(value = "/getDeliveryCostList", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getDeliveryCostList(
			@ApiParam("门店Id") @RequestParam(value = "shopId", required = false) @RequestBody long shopId) {
		JSONObject jb = new JSONObject();
		List<Map<String, Object>> deliveryCosts = deliveryCostService.findCostsByShopId(shopId);
		jb.put("errcode", 0);
		jb.put("errmsg", "ok");
		jb.put("data", super.warpObject(new DeliveryCostWarpper(deliveryCosts)));
		return jb;
	}

	@ApiOperation(value = "添加门店运费配置信息", notes = "添加门店运费配置信息")
	@RequestMapping(value = "/addDeliveryCost", method = RequestMethod.POST)
	@ResponseBody
	public Object addCost(@ApiParam("门店Id") @RequestParam(value = "shopId", required = false) @RequestBody long shopId,
			@ApiParam("配送方式Id") @RequestParam(value = "modeId", required = false) @RequestBody long modeId,
			@ApiParam("地区Id组，例如1，2，3") @RequestParam(value = "areaIds", required = false) @RequestBody String areaIds,
			@ApiParam("首价") @RequestParam(value = "startPrice", required = false) @RequestBody BigDecimal startPrice,
			@ApiParam("续价") @RequestParam(value = "addedPrice", required = false) @RequestBody BigDecimal addedPrice,
			@ApiParam("首重") @RequestParam(value = "ykg", required = false) @RequestBody BigDecimal ykg,
			@ApiParam("续重") @RequestParam(value = "addedWeight", required = false) @RequestBody BigDecimal addedWeight) {
		JSONObject jb = new JSONObject();
		String[] areaIdArray = areaIds.split(",");
		Shop shop = shopService.selectById(shopId);
		for (String areaId : areaIdArray) {
			DeliveryCost deliveryCost = new DeliveryCost();
			deliveryCost.setAreaId(Long.valueOf(areaId));
			deliveryCost.setModeId(modeId);
			deliveryCost.setIsdelivery(true);
			deliveryCost.setShopId(shopId);
			if(ToolUtil.isNotEmpty(shop)) {
				deliveryCost.setDeliveryArea(shop.getCountyId());
			}else {
				deliveryCost.setDeliveryArea(0l);
			}
			
			if(ToolUtil.isNotEmpty(startPrice)){
				deliveryCost.setStartPrice(startPrice);
			}else{
				deliveryCost.setStartPrice(new BigDecimal(0));
			}
			if(ToolUtil.isNotEmpty(ykg)){
				deliveryCost.setYkg(ykg);
			}else{
				deliveryCost.setYkg(new BigDecimal(0));
			}
			if(ToolUtil.isNotEmpty(addedPrice)){
				deliveryCost.setAddedPrice(addedPrice);
			}else{
				deliveryCost.setAddedPrice(new BigDecimal(0));
			}
			if(ToolUtil.isNotEmpty(addedWeight)){
				deliveryCost.setAddedWeight(addedWeight);
			}else{
				deliveryCost.setAddedWeight(new BigDecimal(0));
			}
			List<DeliveryCost> list = deliveryCostService.getListByCondition(deliveryCost);
			if(list.size() > 0) {
				DeliveryCost deliveryCost2 = list.get(0);
				Long id = deliveryCost2.getId();
				deliveryCost2 = deliveryCost;
				deliveryCost2.setId(id);
				deliveryCostService.update(deliveryCost2);
			}else {
				deliveryCostService.insert(deliveryCost);
			}
		}
		jb.put("errcode", 0);
		jb.put("errmsg", "ok");
		return jb;
	}
}
