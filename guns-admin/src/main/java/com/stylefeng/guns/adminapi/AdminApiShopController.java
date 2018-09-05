package com.stylefeng.guns.adminapi;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.goods.factory.PriceTagFactory;
import com.md.goods.model.Goods;
import com.md.goods.model.Shop;
import com.md.goods.service.IShopService;
import com.stylefeng.guns.common.persistence.dao.DeptMapper;
import com.stylefeng.guns.common.persistence.model.Dept;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.modular.system.service.IDeptService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/adminapi/shop")
public class AdminApiShopController extends BaseController{

	@Resource
	IShopService shopService;
    @Resource
    IDeptService deptService;
    @Resource
    DeptMapper deptMapper;
    
	@ApiOperation(value = "读取门店详情", notes = "读取门店详情")
	@RequestMapping(value = "/getShopDetail", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getShopDetail(
			@ApiParam("门店id") @RequestParam(value = "shopId", required = true) @RequestBody String shopId) {
		JSONObject jb = new JSONObject();
		if(ToolUtil.isNotEmpty(shopId)) {
			Shop shop = shopService.findById(Long.valueOf(shopId));
			jb.put("data", shop);
			jb.put("errcode",0);
			jb.put("errmsg", "");
		}else {
			jb.put("data", "");
			jb.put("errcode",-1);
			jb.put("errmsg", "门店id不能为空");
		}
		return jb;
	}
	
	@ApiOperation(value = "新增门店", notes = "新增门店")
	@RequestMapping(value = "/addShop", method = RequestMethod.POST)
	@ApiImplicitParam(name = "shop", value = "门店信息", required = true, dataType = "Shop", paramType = "body")
	@ResponseBody
	public JSONObject addShop(@RequestBody Shop shop) {
		JSONObject jb = new JSONObject();
		Long shopId = shop.getId();
		Dept dept = new Dept();
    	dept.setFullname(shop.getName());
    	dept.setSimplename(shop.getName());
    	dept.setPid(30);
    	dept.setNum(1);
    	deptService.deptSetPids(dept);
        deptMapper.insert(dept);
        shop.setDeptId(dept.getId());
        shopService.insert(shop);
        shopService.changeId(shopId,shop.getId());
        shop.setId(shopId);
        //为门店初始化所有的价格标签
        PriceTagFactory.me().createPriceTag(shop.getId());
		jb.put("data", shop);
		jb.put("errcode",0);
		jb.put("errmsg", "");
		return jb;
	}
	
}
