package com.stylefeng.guns.adminapi;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.goods.model.PriceTag;
import com.md.goods.service.IPriceTagService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.ToolUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 库存接口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/adminapi/stock")
public class AdminApiStockController extends BaseController {

	@Resource
	IPriceTagService priceTagService;
	
	@ApiOperation(value = "读取指定商品、产品、仓库（门店）的库存", notes = "读取指定商品、产品、仓库（门店）的库存 ")
	@RequestMapping(value = "/getStockByCondition", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getStockByCondition(
			@ApiParam("商品id") @RequestParam(value = "goodsId", required = false) @RequestBody String goodsId,
			@ApiParam("门店id") @RequestParam(value = "shopId", required = true) @RequestBody String shopId,
			@ApiParam("产品id") @RequestParam(value = "productId", required = false) @RequestBody String productId) {
		JSONObject jb = new JSONObject();
		Long stock = 0l;
		if(ToolUtil.isNotEmpty(goodsId)) {
			stock = priceTagService.getSumByStock(Long.valueOf(goodsId), Long.valueOf(shopId));
		}else if(ToolUtil.isNotEmpty(goodsId)) {
			PriceTag tag = priceTagService.findByShopAndProduct(Long.valueOf(productId), Long.valueOf(shopId));
			if(ToolUtil.isNotEmpty(tag)) {
				stock = tag.getInventory().longValue();
			}
		}
		jb.put("data", stock);
		jb.put("errcode", 0);
		jb.put("errmsg", "");
		return jb;
	}
	
	@ApiOperation(value = "设置指定产品、仓库的库存、预警库存", notes = "设置指定产品、仓库的库存、预警库存 ")
	@RequestMapping(value = "/setStockByProId", method = RequestMethod.POST)
	@ResponseBody
	@ApiImplicitParam(name = "priceTagList", value = "产品信息", required = true, dataType = "List<PriceTag>", paramType = "body")
	public JSONObject setStockByProId(@RequestBody List<PriceTag> priceTagList) {
		JSONObject jb = new JSONObject();
		if(ToolUtil.isNotEmpty(priceTagList)) {
			for(PriceTag priceTag:priceTagList) {
				PriceTag tag = priceTagService.findByShopAndProduct(priceTag.getProductId(), priceTag.getShopId());
				if(ToolUtil.isNotEmpty(tag)) {
					if(ToolUtil.isNotEmpty(priceTag.getInventory())) {
						tag.setInventory(priceTag.getInventory());
					}
					if(ToolUtil.isNotEmpty(priceTag.getThreshold())) {
						tag.setThreshold(priceTag.getThreshold());
					}
					if(ToolUtil.isNotEmpty(priceTag.getMarketable())) {
						tag.setMarketable(priceTag.getMarketable());
					}
					if(ToolUtil.isNotEmpty(priceTag.getMarketPrice())) {
						tag.setMarketPrice(priceTag.getMarketPrice());
					}
					if(ToolUtil.isNotEmpty(priceTag.getPrice())) {
						tag.setPrice(priceTag.getPrice());
					}
					if(ToolUtil.isNotEmpty(priceTag.getIntegral())) {
						tag.setIntegral(priceTag.getIntegral());
					}
					priceTagService.updateById(tag);
					jb.put("data", "SUCCESS");
					jb.put("errcode", 0);
					jb.put("errmsg", "");
				}else {
					//String data = "goodsId:"+tag.getGoodsId()+"shopId:"+tag.getShopId()+"productId:"+tag.getProductId();
					jb.put("data", "ERROR");
					jb.put("errcode", -1);
					jb.put("errmsg", "该产品不存在");
				}
			}
		}else {
			jb.put("data", "ERROR");
			jb.put("errcode", -1);
			jb.put("errmsg", "参数错误");
		}
		return jb;
	}
	
}
