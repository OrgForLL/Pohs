package com.stylefeng.guns.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.md.cart.service.ICartService;
import com.md.member.factory.PasswordFactory;
import com.md.member.model.Address;
import com.md.member.model.Balance;
import com.md.member.model.Favorite;
import com.md.member.model.FavoriteItem;
import com.md.member.model.Integral;
import com.md.member.model.Member;
import com.md.member.model.MemberCard;
import com.md.member.service.IAddressService;
import com.md.member.service.IBalanceService;
import com.md.member.service.IFavoriteItemService;
import com.md.member.service.IFavoriteService;
import com.md.member.service.IIntegralService;
import com.md.member.service.IMemberCardService;
import com.md.member.service.IMemberService;
import com.md.member.warpper.FavoriteItemWarpper;
import com.md.member.warpper.MemberWarpper;
import com.md.pay.service.IWeixinService;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.cache.CacheKit;
import com.stylefeng.guns.core.exception.ApiException;
import com.stylefeng.guns.core.util.ToolUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import weixin.popular.bean.user.User;

/**
 * 客户信息
 * 
 * @author 54476
 *
 */
@Controller
@RequestMapping("/member")
public class ApiMemberController extends BaseController {

	@Resource
	IMemberService memberServiceImpl;
	@Resource
	IAddressService addressServiceImpl;
	@Resource
	IMemberCardService memberCardService;
	@Resource
	IIntegralService integralService;
	@Resource
	IBalanceService balanceService;
	@Resource
	ICartService cartService;
	@Resource
	IFavoriteService favoriteService;
	@Resource
	IFavoriteItemService favoriteItemService;
	@Resource
	IWeixinService weixinService;
	/**
	 * 获取用户个人信息详情
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "获取用户个人信息详情", notes = "获取用户个人信息详情")
	@RequestMapping(value = "/getMemberInfo", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getMemberInfo(
			@ApiParam("用户Id，必填") @RequestParam(value = "memberId", required = true) @RequestBody long memberId) {

		JSONObject jb = new JSONObject();
		Wrapper<Member> wrapper = new EntityWrapper<>();
		wrapper.eq("id", memberId);
		List<Map<String, Object>> memberList = memberServiceImpl.selectMaps(wrapper);
		jb.put("data", super.warpObject(new MemberWarpper(memberList)));
		return jb;
	}

	/**
	 * 修改用户个人信息详情
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "修改用户个人信息详情", notes = "修改用户个人信息详情")
	@RequestMapping(value = "/updateMemberInfo", method = RequestMethod.POST)
	@ResponseBody
	@ApiImplicitParam(name = "member", value = "用户信息", required = true, dataType = "Member", paramType = "body")
	public JSONObject updateMemberInfo(@RequestBody Member member) {

		JSONObject jb = new JSONObject();
		boolean num = memberServiceImpl.updateById(member);
		if (num) {
			jb.put("data", member);
			return jb;
		}
		jb.put("errorMsg", "修改失败。");
		return jb;
	}

	/**
	 * 我的收货地址
	 */
	@ApiOperation(value = "获取我的收货地址列表", notes = "获取我的收货地址列表")
	@RequestMapping(value = "/myReceiver", method = { RequestMethod.POST })
	public @ResponseBody JSONObject listReceiver(
			@ApiParam("用户ID，必填") @RequestParam(value = "memberId", required = true) Long memberId,
			@ApiParam("是否默认地址，必填") @RequestParam(value = "isdefault", required = true) boolean isdefault,
			@ApiParam("地址id，非必填") @RequestParam(value = "addressId", required = false) Long addressId
			) {
		JSONObject jb = new JSONObject();
		Wrapper<Address> wrapper = new EntityWrapper<>();
		wrapper.eq("memberId", memberId);
		if(isdefault){
			wrapper.eq("isdefault", isdefault);
		}
		if(isdefault){
			wrapper.eq("id", addressId);
		}
		List<Map<String, Object>> addressList = addressServiceImpl.selectMaps(wrapper);
		jb.put("data", addressList);
		return jb;
	}

	/**
	 * 添加收货地址
	 */
	@ApiOperation(value = "添加我的收货地址", notes = "添加我的收货地址")
	@RequestMapping(value = "/addReceiver", method = { RequestMethod.POST })
	@ApiImplicitParam(name = "address", value = "收货地址", required = true, dataType = "Address", paramType = "body")
	public @ResponseBody Object addReceiver(@RequestBody Address address) {
		JSONObject jb = new JSONObject();
		boolean num = addressServiceImpl.insert(address);
		if (num) {
			return address;
		}
		jb.put("errorMsg", "添加失败！");
		return jb;
	}

	/**
	 * 修改收货地址
	 */
	@ApiOperation(value = "修改我的收货地址", notes = "修改我的收货地址")
	@RequestMapping(value = "/updateReceiver", method = { RequestMethod.POST })
	@ApiImplicitParam(name = "address", value = "收货地址", required = true, dataType = "Address", paramType = "body")
	public @ResponseBody Object updateReceiver(@RequestBody Address address) {
		JSONObject jb = new JSONObject();
		boolean num = addressServiceImpl.updateById(address);
		if (num) {
			return address;
		}
		jb.put("errorMsg", "修改失败！");
		return jb;
	}

	/**
	 * 删除收货地址
	 */
	@ApiOperation(value = "删除收货地址", notes = "删除收货地址")
	@RequestMapping(value = "/deleteReceiver", method = { RequestMethod.POST })
	public @ResponseBody Object deleteReceiver(
			@ApiParam("收货地址id") @RequestParam(value = "address_id", required = true) Long address_id) {
		JSONObject jb = new JSONObject();
		boolean num = addressServiceImpl.deleteById(address_id);
		if (num) {
			return SUCCESS;
		}
		jb.put("errorMsg", "修改失败！");
		return jb;
	}
	
	/**
	 * 登录
	 */
	@ApiOperation(value = "客户登录", notes = "客户登录")
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	@ResponseBody
	@ApiImplicitParam(name = "member", value = "用户信息", required = true, dataType = "Member", paramType = "body")
	public Object login(@RequestBody Member member) {
		JSONObject jb = new JSONObject();
		if(StringUtils.isEmpty(member.getPhoneNum()) && StringUtils.isEmpty(member.getPassword())){
    		return new ApiException(BizExceptionEnum.USERNAME_PWD_NULL);
		}
		member.setPassword(PasswordFactory.me().initPassowrd(member.getPassword()));
		member.setOpenId(member.getOpenId());
		String access_token = weixinService.getAccessToken(member.getConfigKey());
		User user = weixinService.getWxUserInfo(access_token, member.getOpenId());
		member.setName(user.getNickname());
		member.setCaptcha(user.getHeadimgurl());
		Wrapper<Member> wrapper = new EntityWrapper<>();
		wrapper.eq("password", member.getPassword());
		wrapper.eq("phoneNum", member.getPhoneNum());
		List<Map<String, Object>> memberList = memberServiceImpl.selectMaps(wrapper);
		member.setId(Long.valueOf(memberList.get(0).get("id").toString()));
		memberServiceImpl.updateById(member);
		jb.put("data", super.warpObject(new MemberWarpper(memberList)));
		return jb;
	}
  
	/**
	 * 保存用户微信信息
	 */
	@ApiOperation(value = "保存用户微信信息", notes = "保存用户微信信息")
	@RequestMapping(value = "/saveOpenId", method = { RequestMethod.POST })
	@ResponseBody
	public ResponseEntity<?> saveOpenId(@ApiParam("用户Id，必填") @RequestParam(value = "memberId", required = true) @RequestBody long memberId,
			@ApiParam("openId，必填") @RequestParam(value = "openId", required = true) @RequestBody String openId,
			@ApiParam("configKey，必填") @RequestParam(value = "configKey", required = true) @RequestBody String configKey) {
		Member member = new Member();
		if(ToolUtil.isNotEmpty(memberId)) {
			member.setOpenId(openId);
			member.setId(memberId);
			String access_token = weixinService.getAccessToken(configKey);
			User user = weixinService.getWxUserInfo(access_token, openId);
			member.setName(user.getNickname());
			member.setCaptcha(user.getHeadimgurl());
			memberServiceImpl.updateById(member);
		}
		return ResponseEntity.ok("success");
	}
	
	/**
	 * 获取注册验证码
	 * @throws ClientException 
	 */
	@ApiOperation(value = "获取验证码", notes = "获取验证码")
	@RequestMapping(value = "/sendRegisterVerificationCode", method = { RequestMethod.POST })
	@ResponseBody
	public Object sendRegisterVerificationCode(@ApiParam("电话号码") @RequestParam(value = "phone", required = true)String phone) throws ClientException{
		String code = String.valueOf((int)((Math.random()* 9 + 1) * 100000));	
		//SendSmsResponse sendSmsResponse = SmsUtil.sendSms(phone, code);
		System.out.println(code);
		CacheKit.put("VerificationCode", phone, code);
		return SUCCESS;
	}
	/**
	 * 获取修改密码验证码
	 * @throws ClientException 
	 */
	@ApiOperation(value = "获取修改密码验证码", notes = "获取修改密码验证码")
	@RequestMapping(value = "/sendUpdatePwdVerificationCode", method = { RequestMethod.POST })
	@ResponseBody
	public Object sendUpdatePwdVerificationCode(@ApiParam("电话号码") @RequestParam(value = "phone", required = true)String phone) throws ClientException{
		String code = String.valueOf((int)((Math.random()* 9 + 1) * 100000));	
		//SendSmsResponse sendSmsResponse = SmsUtil.sendSmsReset(phone, code);
		System.out.println(code);
		CacheKit.put("VerificationCode", phone, code);
		return SUCCESS;
	}
	
    /**
     * 注册
     */
	@ApiOperation(value = "注册", notes = "注册")
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    @ResponseBody
    public Object register(
    		@ApiParam("密码") @RequestParam(value = "password", required = true)String password,
    		@ApiParam("账户") @RequestParam(value = "phone", required = true)String phone,
    		@ApiParam("是否分配会员卡") @RequestParam(value = "isCard", required = true)Boolean isCard,
    		@ApiParam("会员卡等级") @RequestParam(value = "cardLevel", required = true)Long cardLevel,
    		@ApiParam("验证码") @RequestParam(value = "VerificationCode", required = true)String VerificationCode) {
		Member member = new Member();
		member.setPassword(password);
		member.setPhoneNum(phone);
    	String registerCodeCache = CacheKit.get("VerificationCode", member.getPhoneNum());
    	if(StringUtils.isEmpty(member.getPhoneNum()) || StringUtils.isEmpty(member.getPassword())){
    		return new ApiException(BizExceptionEnum.USERNAME_PWD_NULL);
		}  	
    	if(!VerificationCode.equals(registerCodeCache)){
    		return new ApiException(BizExceptionEnum.VERIFICATION_CODE);
    	}
    	if(memberServiceImpl.selectByPhoneNum(member.getPhoneNum()).size() > 0){
    		return new ApiException(BizExceptionEnum.USER_ALREADY_REG);
    	}
    	//设置初始化密码
    	member.setPassword(PasswordFactory.me().initPassowrd(member.getPassword()));
    	memberServiceImpl.add(member);
        //判断是否分配会员卡
    	if(isCard){
    		MemberCard card = memberCardService.init(member,cardLevel);
    		member.setCardSn(card.getCardSn());
    	}
    	//分配积分账户
    	Integral integral =integralService.init(member);
    	member.setIntegralSn(integral.getSn());
    	//分配余额账户
    	Balance balance =balanceService.init(member);
    	member.setBalanceSn(balance.getBalanceSn());
    	//分配一辆购物车
    	cartService.init(member);
    	//分配收藏夹
    	favoriteService.init(member);
    	memberServiceImpl.update(member);
        return SUCCESS;
    }
	/**
	 * 修改密码
	 */
	@ApiOperation(value = "修改密码", notes = "修改密码")
	@RequestMapping(value = "/updatePwd", method = { RequestMethod.POST })
	@ResponseBody
	public Object updatePwd(
			
			@ApiParam("客户Id") @RequestParam(value = "memberId", required = true)Long memberId,
			@ApiParam("客户手机号") @RequestParam(value = "phone", required = true)String phone,
			@ApiParam("旧密码") @RequestParam(value = "oldPassword", required = true)String oldPassword,
			@ApiParam("新密码") @RequestParam(value = "password", required = true)String password,
			@ApiParam("验证码") @RequestParam(value = "VerificationCode", required = true)String VerificationCode
			) {
		
		String registerCodeCache = CacheKit.get("VerificationCode", phone);
		if(!VerificationCode.equals(registerCodeCache)){
			return new ApiException(BizExceptionEnum.VERIFICATION_CODE);
		}
		Member member = memberServiceImpl.selectById(memberId);
		oldPassword = PasswordFactory.me().initPassowrd(oldPassword);
		password = PasswordFactory.me().initPassowrd(password);
		if(!member.getPassword().equals(oldPassword)){
			return new ApiException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
		}
		member.setPassword(password);
		memberServiceImpl.update(member);
		return SUCCESS;
	}
	/**
	 * 获取收藏商品
	 */
	@ApiOperation(value = "获取收藏商品", notes = "获取收藏商品")
	@RequestMapping(value = "/getFavoriteGoods", method = { RequestMethod.POST })
	@ResponseBody
	public Object getFavoriteGoods(
			@ApiParam("客户Id") @RequestParam(value = "memberId", required = true)Long memberId
			) {
		Member member = memberServiceImpl.selectById(memberId);
		if(ToolUtil.isEmpty(member)){
			return new ApiException(BizExceptionEnum.USER_NOT_EXISTED);
		}
		Favorite favorite = favoriteService.selectByMemberId(memberId);
		List<Map<String, Object>> favoriteItems =  favoriteItemService.selectFavoriteItemByfavoriteId(favorite.getId());
		
		JSONObject jb = new JSONObject();
		jb.put("data", super.warpObject(new FavoriteItemWarpper(favoriteItems)));
		return jb;
	}
	/**
	 * 批量删除收藏商品
	 */
	@ApiOperation(value = "批量删除收藏商品", notes = "批量删除收藏商品")
	@RequestMapping(value = "/deleteFavoriteItems", method = { RequestMethod.POST })
	@ResponseBody
	public Object deleteFavoriteItems(
			@ApiParam("收藏商品 ,样例1,2,3") @RequestParam(value = "itemIds", required = true) @RequestBody String itemIds
			) {
		
		String[] favoriteItemIds = StringUtils.split(itemIds, ",");
		for(String arr : favoriteItemIds){
			favoriteItemService.deleteById(Long.valueOf(arr));
		}
		JSONObject jb = new JSONObject();
		jb.put("data", "success");
		return jb;
	}
	
	/**
	 * 添加收藏商品
	 */
	@ApiOperation(value = "添加收藏商品", notes = "添加收藏商品")
	@RequestMapping(value = "/addFavoriteGoods", method = { RequestMethod.POST })
	@ResponseBody
	public Object addFavoriteGoods(
			@ApiParam("客户Id") @RequestParam(value = "memberId", required = true)Long memberId,
			@ApiParam("门店Id") @RequestParam(value = "shopId", required = true)Long shopId,
			@ApiParam("商品Id") @RequestParam(value = "goodsId", required = true)Long goodsId
			) {
		JSONObject jb = new JSONObject();
		Member member = memberServiceImpl.selectById(memberId);
		if(ToolUtil.isEmpty(member)){
			return new ApiException(BizExceptionEnum.USER_NOT_EXISTED);
		}
		Favorite favorite = favoriteService.selectByMemberId(memberId);
		if(ToolUtil.isEmpty(favorite)) {
			Favorite favorite1 = favoriteService.init(member);
			FavoriteItem item = new FavoriteItem();
			item.setFavoriteId(favorite1.getId());
			item.setGoodsId(goodsId);
			item.setShopId(shopId);
			FavoriteItem favoriteItem = favoriteItemService.getByOne(item);
			if(ToolUtil.isEmpty(favoriteItem)) {
				favoriteItemService.insert(item);
				jb.put("data", "success");
			}else {
				jb.put("data", "该商品已被收藏");
			}
		}else {
			FavoriteItem item = new FavoriteItem();
			item.setFavoriteId(favorite.getId());
			item.setGoodsId(goodsId);
			item.setShopId(shopId);
			FavoriteItem favoriteItem = favoriteItemService.getByOne(item);
			if(ToolUtil.isEmpty(favoriteItem)) {
				favoriteItemService.insert(item);
				jb.put("data", "success");
			}else {
				jb.put("data", "该商品已被收藏");
			}
		}
		return jb;
	}
}
