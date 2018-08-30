package com.stylefeng.guns.adminapi;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.md.goods.constant.IsUse;
import com.md.goods.factory.PriceTagFactory;
import com.md.goods.model.CategoryRelation;
import com.md.goods.model.Goods;
import com.md.goods.model.Product;
import com.md.goods.model.Specification;
import com.md.goods.model.SpecificationItem;
import com.md.goods.model.TagRelation;
import com.md.goods.model.UploadFile;
import com.md.goods.oe.GoodsObject;
import com.md.goods.service.IBrandService;
import com.md.goods.service.ICategoryRelationService;
import com.md.goods.service.IGoodsPriceService;
import com.md.goods.service.IGoodsService;
import com.md.goods.service.IParamItemsService;
import com.md.goods.service.IParamService;
import com.md.goods.service.IPriceTagService;
import com.md.goods.service.IProductService;
import com.md.goods.service.ISpecificationItemService;
import com.md.goods.service.ISpecificationService;
import com.md.goods.service.ITagRelationService;
import com.md.goods.service.IUploadFileService;
import com.md.goods.warpper.GoodsWarpper;
import com.md.goods.warpper.ProductWarpper;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.CompressUtil;
import com.stylefeng.guns.core.util.Convert;
import com.stylefeng.guns.core.util.ToolUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 商品接口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/adminapi/goods")
public class AdminApiGoodsController extends BaseController {
	
	@Resource
	private GunsProperties gunsProperties;
	
	@Resource
	IGoodsService goodsService;
	
	@Resource
	IGoodsPriceService goodsPriceService;
	
	@Resource
	IUploadFileService uploadFileService;
	
	@Resource
	IBrandService brandService;
	
	@Resource
	IParamService paramService;
	
	@Resource
	IParamItemsService paramItemsService;
	
	@Resource
	IProductService productService;
	
	@Resource
	ICategoryRelationService categoryRelationService;
	
	@Resource
	ITagRelationService tagRelationService;

	@Resource
	IPriceTagService priceTagService;
	
	@Resource
	ISpecificationService specificationService;
	
	@Resource
	ISpecificationItemService specificationItemService;

	@ApiOperation(value = "通过商品名称获取商品列表", notes = "通过商品名称获取商品列表  \r\n"
			+ "marketPrice:活动价;  \r\n"
			+ "brandName:品牌名称;  \r\n"
			+ "specItems:规格详情;  \r\n"
			+ "categoryName:分类名称;  \r\n"
			+ "unit:单位;  \r\n"
			+ "createTime:创建时间;  \r\n"
			+ "price:商品价格 \r\n"
			+ "imageUrl:商品图列表;  \r\n"
			+ "name:商品名称;  \r\n"
			+ "id:商品编号;  \r\n"
			+ "sn:商品编码;  \r\n"
			+ "detail:商品详情;  \r\n"
			+ "isDel:是否删除（1 是 0 否）;  \r\n"
			+ "paramItems:参数详情\"")
	@RequestMapping(value = "/getGoodsListByConditon", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getGoodsListByConditon(
			@ApiParam("商品名称") @RequestParam(value = "name", required = false) @RequestBody String name,
			@ApiParam("商品id") @RequestParam(value = "goodsId", required = false) @RequestBody String goodsId,
			@ApiParam("商品编码") @RequestParam(value = "sn", required = false) @RequestBody String sn,
			@ApiParam("当前页") @RequestParam(value = "index", required = true) @RequestBody Integer index,
			@ApiParam("分页数量") @RequestParam(value = "pageSize", required = false) @RequestBody Integer pageSize) {
		JSONObject jb = new JSONObject();
		List<Map<String, Object>> result = goodsService.getListByConditon(name,goodsId,sn, index,pageSize);
		jb.put("data", super.warpObject(new GoodsWarpper(result)));
		jb.put("errcode", 0);
		jb.put("errmsg", "");
		return jb;
	}
	
	@ApiOperation(value = "获取商品详情", notes = "获取商品详情")
	@RequestMapping(value = "/getGoodDetail", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getGoodDetail(
			@ApiParam("商品id") @RequestParam(value = "goodsId", required = true) @RequestBody long goodsId) {
		JSONObject jb = new JSONObject();
		GoodsObject goodsOb = new GoodsObject();
		Goods goods = goodsService.findById(goodsId);
		if(ToolUtil.isEmpty(goods)) {
			jb.put("data", "error");
			jb.put("errcode", -1);
			jb.put("errmsg", "不存在该商品");
			return jb;
		}
		String imageUrl = "";
		if (goods.getImages() != null && !goods.getImages().equals("")) {
			String[] arr = goods.getImages().split(",");
			for (int i = 0; i < arr.length; i++) {
				UploadFile uploadFile = uploadFileService.getById(Long.valueOf(arr[i]));
				if(uploadFile != null) {
					imageUrl += uploadFile.getUrl() + ",";
				}
			}
			if(ToolUtil.isNotEmpty(imageUrl)) {
				imageUrl = imageUrl.substring(0,imageUrl.length() - 1);
			}
			goodsOb.setImages(imageUrl);
		}
		goodsOb.setGoodName(goods.getName());
		goodsOb.setBrandName(brandService.findById(goods.getBrandId()).getName());
		goodsOb.setId(goods.getId());
		goodsOb.setMarketPrice(goods.getMarketPrice());
		goodsOb.setPrice(goods.getPrice());
		goodsOb.setUnit(goods.getUnit());
		goodsOb.setParamItems(goods.getParamItems());
		goodsOb.setSpecItems(goods.getSpecItems());
		goodsOb.setSn(goods.getSn());
		jb.put("data", goodsOb);
		jb.put("errcode", 0);
		jb.put("errmsg", "0");
		return jb;
	}

	
	@ApiOperation(value = "创建、编辑商品信息", notes = "创建、编辑商品信息  ")
	@RequestMapping(value = "/saveGoodsInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiImplicitParam(name = "goods", value = "商品信息", required = true, dataType = "Goods", paramType = "body")
	public JSONObject saveGoodsInfo(
			@RequestBody Goods goods,
			@ApiParam("规格配置") @RequestParam(value = "specs", required = false) @RequestBody String specs,
			@ApiParam("分类id") @RequestParam(value = "categoryIds", required = false) @RequestBody String categoryIds,
			@ApiParam("关联标签") @RequestParam(value = "tagIds", required = false) @RequestBody String tagIds) throws UnsupportedEncodingException {
		JSONObject jb = new JSONObject();
		
		if(ToolUtil.isEmpty(goods.getId())) {
//			if (goodsService.existGoods(goods.getName(), null)) {
//				throw new GunsException(BizExceptionEnum.NAME_SAME);
//			}
			if (ToolUtil.isNotEmpty(goods.getImages())) {
				Long[] ids = Convert.toLongArray(true, Convert.toStrArray(",", goods.getImages()));
				for (Long id : ids) {
					UploadFile selectById = uploadFileService.getById(id);
					if(ToolUtil.isEmpty(selectById)) {
						throw new GunsException(BizExceptionEnum.IMAGE_NOTEXIST);
					}
				}
			}
			// 添加商品
			goods.setParamItems(HtmlUtils.htmlUnescape(goods.getParamItems()));
			goods.setSpecItems(HtmlUtils.htmlUnescape(goods.getSpecItems()));
			goods.setDetail(HtmlUtils.htmlUnescape(goods.getDetail()));
			goods.setCreateTime(new Timestamp(new Date().getTime()));
			Long goodsId = goodsService.add(goods);

			// 添加规格商品
			if(ToolUtil.isNotEmpty(specs)) {
				String htmlUnescape = HtmlUtils.htmlUnescape(specs);
				List<Product> productList = JSONArray.parseArray(htmlUnescape, Product.class);
				Set<Long> imgs = new HashSet<>();
				for (Product product : productList) {
					product.setGoodsId(goodsId);
					if (product.getImage() != null) {
						imgs.add(product.getImage());
					}
					productService.add(product);
					PriceTagFactory.me().createPriceTag(product);
				}
				// 修改上传图片的状态
				if (ToolUtil.isNotEmpty(goods.getImages())) {
					uploadFileService.allUse(Convert.toLongArray(true, Convert.toStrArray(",", goods.getImages())));
				}
				uploadFileService.allUse(imgs);
			}
			
			if(ToolUtil.isNotEmpty(categoryIds)) {
				// 添加商品分类关联
				Set<CategoryRelation> ctyRelationSet = new HashSet<>();
				for (Long id : Convert.toLongArray(true, Convert.toStrArray(",", categoryIds))) {
					CategoryRelation categoryRelation = new CategoryRelation();
					categoryRelation.setCategoryId(id);
					categoryRelation.setGoodsId(goodsId);
					ctyRelationSet.add(categoryRelation);
				}
				categoryRelationService.add(ctyRelationSet);
			}
			if(ToolUtil.isNotEmpty(tagIds)) {
			// 添加商品标签关联
				HashSet<TagRelation> tagRelationSet = new HashSet<>();
				if (ToolUtil.isNotEmpty(tagIds)) {
					for (Long id : Convert.toLongArray(true, Convert.toStrArray(",", tagIds))) {
						TagRelation tagRelation = new TagRelation();
						tagRelation.setTagId(id);
						tagRelation.setGoodsId(goodsId);
						tagRelationSet.add(tagRelation);
					}
				}
				tagRelationService.add(tagRelationSet);
			}
		}else {
//			if (goodsService.existGoods(goods.getName(), goods.getId())) {
//				throw new GunsException(BizExceptionEnum.NAME_SAME);
//			}
			// 将原先的规格商品的图片状态设为未使用
			Product queryObj = new Product();
			queryObj.setGoodsId(goods.getId());
			List<Map<String, Object>> oldProducts = productService.find(queryObj);
			for (Map<String, Object> map : oldProducts) {
				if (ToolUtil.isNotEmpty(map.get("image"))) {
					uploadFileService.unUse((Long) (map.get("image")));
				}
			}

			// 将原先的商品的图片组设置为未使用
			Goods findById = goodsService.findById(goods.getId());
			if (ToolUtil.isNotEmpty(findById.getImages())) {
				uploadFileService.allUnUse(Convert.toLongArray(true, Convert.toStrArray(",", findById.getImages())));
			}

			// 修改商品
			goods.setParamItems(HtmlUtils.htmlUnescape(goods.getParamItems()));
			goods.setSpecItems(HtmlUtils.htmlUnescape(goods.getSpecItems()));
			goods.setDetail(HtmlUtils.htmlUnescape(goods.getDetail()));
			goods.setCreateTime(new Timestamp(new Date().getTime()));
			goodsService.edit(goods);
			// 修改商品图片的状态为使用
			if (ToolUtil.isNotEmpty(goods.getImages())) {
				uploadFileService.allUse(Convert.toLongArray(true, Convert.toStrArray(",", goods.getImages())));
			}
			if(ToolUtil.isNotEmpty(specs)) {
				// 修改规格商品
				String htmlUnescape = HtmlUtils.htmlUnescape(specs);
				List<Product> productList = JSONArray.parseArray(htmlUnescape, Product.class);
				Set<Long> imgs = new HashSet<>();
				Set<Long> ids = new HashSet<>();
				for (Product product : productList) {
					product.setGoodsId(goods.getId());
					if (product.getId() == null) {
						productService.add(product);
						// 生成价格标签
						PriceTagFactory.me().createPriceTag(product);
					} else {
						productService.edit(product);
					}
					if (product.getImage() != null) {
						imgs.add(product.getImage());
					}
					ids.add(product.getId());
				}
				// 删除product数据、及其关联的价格标签
				oldProducts.stream().filter(p -> !ids.contains((Long) (p.get("id")))).collect(Collectors.toList()).stream()
						.forEach(map -> {
							productService.deleteById((Long) (map.get("id")));
							priceTagService.deleteByProductId((Long) map.get("id"));
						});
				// 修改当前规格的图片状态为使用
				if (ToolUtil.isNotEmpty(imgs)) {
					uploadFileService.allUse(imgs);
				}
			}
			if(ToolUtil.isNotEmpty(categoryIds)) {
			// 修改商品分类关联
				Set<CategoryRelation> ctyRelationSet = new HashSet<>();
				for (Long id : Convert.toLongArray(true, Convert.toStrArray(",", categoryIds))) {
					CategoryRelation categoryRelation = new CategoryRelation();
					categoryRelation.setCategoryId(id);
					categoryRelation.setGoodsId(goods.getId());
					ctyRelationSet.add(categoryRelation);
				}
				categoryRelationService.edit(goods.getId(), ctyRelationSet);
			}
			if(ToolUtil.isNotEmpty(tagIds)) {
				// 修改商品标签关联
				HashSet<TagRelation> tagRelationSet = new HashSet<>();
				if (ToolUtil.isNotEmpty(tagIds)) {
					for (Long id : Convert.toLongArray(true, Convert.toStrArray(",", tagIds))) {
						TagRelation tagRelation = new TagRelation();
						tagRelation.setTagId(id);
						tagRelation.setGoodsId(goods.getId());
						tagRelationSet.add(tagRelation);
					}
				}
				tagRelationService.edit(goods.getId(), tagRelationSet);
			}
		}
		jb.put("data", goods.getId());
		jb.put("errcode", 0);
		jb.put("errmsg", "");
		return jb;
	}
	
	/**
	 * 上传图片(上传到项目的webapp/static/img)
	 */
	@ApiOperation(value = "上传图片", notes = "上传图片  ")
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	@ResponseBody
	public JSONObject upload(@RequestPart("file") MultipartFile picture) {
		JSONObject jb = new JSONObject();
		// 保存文件
		String filename = picture.getOriginalFilename();
		String[] name = filename.split("\\.");
		String suffix = name[name.length - 1];
		String pictureName = UUID.randomUUID().toString() + "." + suffix;
		try {
			String fileSavePath = gunsProperties.getFileUploadPath();
			File tempFile = new File(fileSavePath + pictureName);
			picture.transferTo(tempFile);
			File f = new File(fileSavePath + "/goodsImg");
			if (!f.exists()) {
				f.mkdirs();// 创建目录
			}
			// 压缩图片
			CompressUtil.reduceImg2(fileSavePath + pictureName, fileSavePath + "goodsImg/" + pictureName, (float) 0.6);
			tempFile.delete();
		} catch (Exception e) {
			throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
		}
		// 将上传图片信息存到uploadfile数据表中
		UploadFile uploadFile = new UploadFile();
		uploadFile.setUrl("goodsImg/" + pictureName);
		uploadFile.setCreateTime(new Timestamp(new Date().getTime()));
		uploadFile.setIsUse(IsUse.NOTUSED.getCode());
		String pictureId = uploadFileService.add(uploadFile).toString();
		// 返回图片在uploadfile数据表里的id
		jb.put("data", pictureId);
		jb.put("errcode", 0);
		jb.put("errmsg", "");
		return jb;
	}

	@ApiOperation(value = "通过分类获取规格列表", notes = "通过分类获取规格列表")
	@RequestMapping(value = "/getSpecList", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getSpecList(
			@ApiParam("分类id") @RequestParam(value = "cateId", required = true) @RequestBody String cateId) {
		JSONObject jb = new JSONObject();
		List<Specification> list = specificationService.findByCid(Long.valueOf(cateId));
		if(list.size() > 0) {
			for(Specification item : list) {
				List<SpecificationItem> itemResult =  specificationItemService.getByPid(item.getId());
				item.setItemObject(itemResult);
			}
		}
		jb.put("data", list);
		jb.put("errcode", 0);
		jb.put("errmsg", "0");
		return jb;
	}

	@ApiOperation(value = "通过商品id获取产品列表", notes = "通过商品id获取产品列表")
	@RequestMapping(value = "/getProductList", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getProductList(
			@ApiParam("商品id") @RequestParam(value = "goodsId", required = true) @RequestBody String goodsId) {
		JSONObject jb = new JSONObject();
		List<Map<String, Object>> list = productService.findByGoodsId(Long.valueOf(goodsId));
		jb.put("data",super.warpObject(new ProductWarpper(list)));
		jb.put("errcode", 0);
		jb.put("errmsg", "0");
		return jb;
	}

}
