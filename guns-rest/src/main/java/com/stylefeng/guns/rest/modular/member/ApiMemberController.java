package com.stylefeng.guns.rest.modular.member;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.cache.CacheKit;
import com.stylefeng.guns.core.exception.ApiException;
import com.stylefeng.guns.core.util.HttpPostUrl;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.config.properties.RestProperties;
import com.stylefeng.guns.rest.modular.member.dto.FavoriteRequest;
import com.stylefeng.guns.rest.modular.member.dto.MemberRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import weixin.popular.bean.user.User;

/**
 * 客户信息
 * 
 * @author 54476
 *
 */
@RestController
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
	@Autowired
	RestProperties restProperties;
	/**
	 * 获取用户个人信息详情
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "获取用户个人信息详情", notes = "获取用户个人信息详情")
	@RequestMapping(value = "/getMemberInfo", method = RequestMethod.POST)
	public ResponseEntity<?> getMemberInfo(@RequestBody MemberRequest memberRequest) {
		JSONObject jb = new JSONObject();
		Wrapper<Member> wrapper = new EntityWrapper<>();
		wrapper.eq("id", memberRequest.getMemberId());
		List<Map<String, Object>> memberList = memberServiceImpl.selectMaps(wrapper);
		jb.put("data", super.warpObject(new MemberWarpper(memberList)));
		return ResponseEntity.ok(jb);
	}

	/**
	 * 修改用户个人信息详情
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "修改用户个人信息详情", notes = "修改用户个人信息详情")
	@RequestMapping(value = "/updateMemberInfo", method = RequestMethod.POST)
	@ApiImplicitParam(name = "member", value = "用户信息", required = true, dataType = "Member", paramType = "body")
	public ResponseEntity<?> updateMemberInfo(@RequestBody MemberRequest memberRequest) {
		Member member = memberServiceImpl.findById(memberRequest.getMemberId());
		member.setSex(memberRequest.getSex());
		member.setName(memberRequest.getName());
		boolean num = memberServiceImpl.updateById(member);
		if (num) {
			return ResponseEntity.ok(member);
		}
		return ResponseEntity.ok("修改失败");
	}

	/**
	 * 我的收货地址
	 */
	@ApiOperation(value = "获取我的收货地址列表", notes = "获取我的收货地址列表")
	@RequestMapping(value = "/myReceiver", method = { RequestMethod.POST })
	public   ResponseEntity<?> listReceiver(@RequestBody MemberRequest memberRequest) {
		JSONObject jb = new JSONObject();
		List<Map<String, Object>> addressList = addressServiceImpl.myReceiver(memberRequest.getMemberId(),memberRequest.isIsdefault(),memberRequest.getAddressId());
		jb.put("data", addressList);
		return ResponseEntity.ok(jb);
	}

	/**
	 * 添加收货地址
	 */
	@ApiOperation(value = "添加我的收货地址", notes = "添加我的收货地址")
	@RequestMapping(value = "/addReceiver", method = { RequestMethod.POST })
	@ApiImplicitParam(name = "address", value = "收货地址", required = true, dataType = "Address", paramType = "body")
	public ResponseEntity<?> addReceiver(@RequestBody Address address) {
		JSONObject jb = new JSONObject();
		boolean num = addressServiceImpl.insert(address);
		if (num) {
			return ResponseEntity.ok(address);
		}
		jb.put("errorMsg", "添加失败！");
		return ResponseEntity.ok(jb);
	}

	/**
	 * 修改收货地址
	 */
	@ApiOperation(value = "修改我的收货地址", notes = "修改我的收货地址")
	@RequestMapping(value = "/updateReceiver", method = { RequestMethod.POST })
	@ApiImplicitParam(name = "address", value = "收货地址", required = true, dataType = "Address", paramType = "body")
	public   ResponseEntity<?> updateReceiver(@RequestBody Address address) {
		JSONObject jb = new JSONObject();
		if(address.getIsdefault()){
			List<Map<String, Object>> addressList = addressServiceImpl.myReceiver(address.getMemberId(), true, null);
			if (ToolUtil.isNotEmpty(addressList)) {
				Address defaultAddress = addressServiceImpl.selectById(Long.valueOf(String.valueOf(addressList.get(0).get("id"))));
				defaultAddress.setIsdefault(false);
				addressServiceImpl.update(defaultAddress);
			}
		}
		boolean num = addressServiceImpl.updateById(address);
		if (num) {
			return ResponseEntity.ok(address);
		}
		jb.put("errorMsg", "修改失败！");
		return ResponseEntity.ok(jb);
	}

	/**
	 * 删除收货地址
	 */
	@ApiOperation(value = "删除收货地址", notes = "删除收货地址")
	@RequestMapping(value = "/deleteReceiver", method = { RequestMethod.POST })
	public ResponseEntity<?> deleteReceiver(@RequestBody MemberRequest memberRequest) {
		JSONObject jb = new JSONObject();
		boolean num = addressServiceImpl.deleteById(memberRequest.getAddressId());
		if (num) {
			return ResponseEntity.ok(SUCCESS_TIP);
		}
		jb.put("errorMsg", "修改失败！");
		return ResponseEntity.ok(jb);
	}
	
	/**
	 * 登录
	 */
	@ApiOperation(value = "客户登录", notes = "客户登录")
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	@ApiImplicitParam(name = "member", value = "用户信息", required = true, dataType = "Member", paramType = "body")
	public ResponseEntity<?> login(@RequestBody MemberRequest memberRequest) {
		JSONObject jb = new JSONObject();
		if(StringUtils.isEmpty(memberRequest.getPhone()) && StringUtils.isEmpty(memberRequest.getPassword())){
    		return ResponseEntity.ok(new ApiException(BizExceptionEnum.USERNAME_PWD_NULL));
		}
		/*String registerCodeCache = CacheKit.get("VerificationCode", memberRequest.getPhone());
		if(!memberRequest.getVerificationCode().equals(registerCodeCache)){
    		return ResponseEntity.ok(new ApiException(BizExceptionEnum.VERIFICATION_CODE));
    	}*/
		Member member = new Member();
		member.setPassword(memberRequest.getPassword());
		member.setPhoneNum(memberRequest.getPhone());
		member.setPassword(PasswordFactory.me().initPassowrd(member.getPassword()));
		member.setOpenId(memberRequest.getOpenId());
		String access_token = weixinService.getAccessToken();
		User user = weixinService.getWxUserInfo(access_token, memberRequest.getOpenId());
		member.setName(user.getNickname());
		member.setCaptcha(user.getHeadimgurl());
		
		Wrapper<Member> wrapper = new EntityWrapper<>();
		wrapper.eq("phoneNum", member.getPhoneNum());
		List<Map<String, Object>> memberList = memberServiceImpl.selectMaps(wrapper);
		List<Map<String, Object>> memberList2;
		if(ToolUtil.isEmpty(memberList)){
			return ResponseEntity.ok(new ApiException(BizExceptionEnum.USER_NOT_EXISTED));
		}else {
			wrapper.eq("password", member.getPassword());
			memberList2 = memberServiceImpl.selectMaps(wrapper);
			if(ToolUtil.isEmpty(memberList2)) {
				return ResponseEntity.ok(new ApiException(BizExceptionEnum.AUTH_REQUEST_ERROR));
			}
		}
		member.setId(Long.valueOf(memberList2.get(0).get("id").toString()));
		memberServiceImpl.updateById(member);
		jb.put("code", "200");
		jb.put("data", new MemberWarpper(memberList));
		return ResponseEntity.ok(jb);
	}
  
	/**
	 * 保存用户微信信息
	 */
	@ApiOperation(value = "保存用户微信信息", notes = "保存用户微信信息")
	@RequestMapping(value = "/saveOpenId", method = { RequestMethod.POST })
	public ResponseEntity<?> saveOpenId(@RequestBody MemberRequest memberRequest) {
		Member member = new Member();
		if(ToolUtil.isNotEmpty(memberRequest.getMemberId())) {
			member.setOpenId(memberRequest.getOpenId());
			member.setId(memberRequest.getMemberId());
			String access_token = weixinService.getAccessToken();
			User user = weixinService.getWxUserInfo(access_token, memberRequest.getOpenId());
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
	public ResponseEntity<?> sendRegisterVerificationCode(@RequestBody MemberRequest memberRequest) throws ClientException{
		String code = String.valueOf((int)((Math.random()* 9 + 1) * 100000));	
		//SmsUtil.sendSms(memberRequest.getPhone(), code);
		HttpPostUrl.sendPost(MessageFormat.format(restProperties.getMessage2Path() ,memberRequest.getPhone(), "您的验证码为："+code), null);
		System.out.println(code);
		CacheKit.put("VerificationCode", memberRequest.getPhone(), code);
		return ResponseEntity.ok(SUCCESS_TIP);
	}
	
	/**
	 * 获取忘记密码的验证码
	 * @throws ClientException 
	 */
	@ApiOperation(value = "获取验证码", notes = "获取验证码")
	@RequestMapping(value = "/sendForgetThePasswordVerificationCode", method = { RequestMethod.POST })
	public ResponseEntity<?> sendForgetThePasswordVerificationCode(@RequestBody MemberRequest memberRequest) throws ClientException{
		String code = String.valueOf((int)((Math.random()* 9 + 1) * 100000));	
		//SmsUtil.sendSmsReset(memberRequest.getPhone(), code);
		HttpPostUrl.sendPost(MessageFormat.format(restProperties.getMessage2Path() ,memberRequest.getPhone(), "您的验证码为："+code), null);
		CacheKit.put("VerificationCode", memberRequest.getPhone(), code);
		return ResponseEntity.ok(SUCCESS_TIP);
	}
	
	/**
     * 忘记密码
     */
	@ApiOperation(value = "忘记密码", notes = "忘记密码")
    @RequestMapping(value = "/forgetThePassword", method = { RequestMethod.POST })
    public ResponseEntity<?> forgetThePassword( @RequestBody MemberRequest memberRequest) {
		
		List<Member> members = memberServiceImpl.selectByPhoneNum(memberRequest.getPhone());
		if(members.size() != 0){
			Member member = members.get(0);
	    	String registerCodeCache = CacheKit.get("VerificationCode", member.getPhoneNum());
	    	if(!memberRequest.getVerificationCode().equals(registerCodeCache)){
	    		return ResponseEntity.ok(new ApiException(BizExceptionEnum.VERIFICATION_CODE));
	    	}
	    	//设置修改的密码
	    	member.setPassword(PasswordFactory.me().initPassowrd(memberRequest.getPassword()));
	    	memberServiceImpl.updateById(member);
		}
        return ResponseEntity.ok("SUCCESS");
    }
	
	/**
	 * 获取修改密码验证码
	 * @throws ClientException 
	 */
	@ApiOperation(value = "获取修改密码验证码", notes = "获取修改密码验证码")
	@RequestMapping(value = "/sendUpdatePwdVerificationCode", method = { RequestMethod.POST })
	public ResponseEntity<?> sendUpdatePwdVerificationCode(@RequestBody MemberRequest memberRequest) throws ClientException{
		String code = String.valueOf((int)((Math.random()* 9 + 1) * 100000));	
//		SmsUtil.sendSmsReset(memberRequest.getPhone(), code);
		HttpPostUrl.sendPost(MessageFormat.format(restProperties.getMessage2Path() ,memberRequest.getPhone(), "您的验证码为："+code), null);
		CacheKit.put("VerificationCode", memberRequest.getPhone(), code);
		return ResponseEntity.ok(SUCCESS_TIP);
	}
	
    /**
     * 注册
     */
	@ApiOperation(value = "注册", notes = "注册")
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ResponseEntity<?> register( @RequestBody MemberRequest memberRequest) {
		Member member = new Member();
		member.setPassword(memberRequest.getPassword());
		member.setPhoneNum(memberRequest.getPhone());
		member.setType("普通用户");
    	String registerCodeCache = CacheKit.get("VerificationCode", member.getPhoneNum());
    	if(ToolUtil.isEmpty(member.getPhoneNum()) || ToolUtil.isEmpty(member.getPassword())){
    		return ResponseEntity.ok(new ApiException(BizExceptionEnum.USERNAME_PWD_NULL));
		}  	
    	if(!memberRequest.getVerificationCode().equals(registerCodeCache)){
    		return ResponseEntity.ok(new ApiException(BizExceptionEnum.VERIFICATION_CODE));
    	}
    	if(memberServiceImpl.selectByPhoneNum(member.getPhoneNum()).size() > 0){
    		return ResponseEntity.ok(new ApiException(BizExceptionEnum.USER_ALREADY_REG));
    	}
    	//设置初始化密码
    	member.setPassword(PasswordFactory.me().initPassowrd(member.getPassword()));
    	memberServiceImpl.add(member);
        //判断是否分配会员卡
//    	if(memberRequest.getIsCard()){
//    		MemberCard card = memberCardService.init(member,memberRequest.getCardLevel());
//    		member.setCardSn(card.getCardSn());
//    	}
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
    	
    	Map<String, String> mapParam = new HashMap<String, String>();
		String data = "{\"MsgTypeID\":3101,\"CreateID\":3100,\"MsgJson\":{\"id\":"+member.getId()+"},\"RequestID\":\"\"}";
		mapParam.put("data", data);
		HttpPostUrl.sendPost(restProperties.getMessagePath(), mapParam);
    	
        return ResponseEntity.ok("SUCCESS");
    }
	/**
	 * 修改密码
	 */
	@ApiOperation(value = "修改密码", notes = "修改密码")
	@RequestMapping(value = "/updatePwd", method = { RequestMethod.POST })
	public ResponseEntity<?> updatePwd( @RequestBody MemberRequest memberRequest) {
		String registerCodeCache = CacheKit.get("VerificationCode", memberRequest.getPhone());
		if(!memberRequest.getVerificationCode().equals(registerCodeCache)){
			return ResponseEntity.ok(new ApiException(BizExceptionEnum.VERIFICATION_CODE));
		}
		Member member = memberServiceImpl.selectById(memberRequest.getMemberId());
		memberRequest.setOldPassword(PasswordFactory.me().initPassowrd(memberRequest.getOldPassword()));
		memberRequest.setPassword(PasswordFactory.me().initPassowrd(memberRequest.getPassword()));
		if(!member.getPassword().equals(memberRequest.getOldPassword())){
			return ResponseEntity.ok(new ApiException(BizExceptionEnum.OLD_PWD_NOT_RIGHT));
		}
		member.setPassword(memberRequest.getPassword());
		memberServiceImpl.update(member);
		return ResponseEntity.ok("SUCCESS");
	}
	/**
	 * 获取收藏商品
	 */
	@ApiOperation(value = "获取收藏商品", notes = "获取收藏商品")
	@RequestMapping(value = "/getFavoriteGoods", method = { RequestMethod.POST })
	public ResponseEntity<?> getFavoriteGoods( @RequestBody MemberRequest memberRequest) {
		Member member = memberServiceImpl.selectById(memberRequest.getMemberId());
		if(ToolUtil.isEmpty(member)){
			return ResponseEntity.ok(new ApiException(BizExceptionEnum.USER_NOT_EXISTED));
		}
		Favorite favorite = favoriteService.selectByMemberId(memberRequest.getMemberId());
		List<Map<String, Object>> favoriteItems =  favoriteItemService.selectFavoriteItemByfavoriteId(favorite.getId());
		return ResponseEntity.ok(super.warpObject(new FavoriteItemWarpper(favoriteItems)));
	}
	/**
	 * 批量删除收藏商品
	 */
	@ApiOperation(value = "批量删除收藏商品", notes = "批量删除收藏商品")
	@RequestMapping(value = "/deleteFavoriteItems", method = { RequestMethod.POST })
	public ResponseEntity<?> deleteFavoriteItems( @RequestBody MemberRequest memberRequest) {
		
		String[] favoriteItemIds = StringUtils.split(memberRequest.getItemIds(), ",");
		for(String arr : favoriteItemIds){
			favoriteItemService.deleteById(Long.valueOf(arr));
		}
		JSONObject jb = new JSONObject();
		jb.put("data", "success");
		return ResponseEntity.ok(jb);
	}
	
	/**
	 * 添加收藏商品
	 */
	@ApiOperation(value = "添加收藏商品", notes = "添加收藏商品")
	@RequestMapping(value = "/addFavoriteGoods", method = { RequestMethod.POST })
	public  ResponseEntity<?> addFavoriteGoods(@RequestBody FavoriteRequest favoriteRequest) {
		Member member = memberServiceImpl.selectById(favoriteRequest.getMemberId());
		if(ToolUtil.isEmpty(member)){
			return ResponseEntity.ok(new ApiException(BizExceptionEnum.USER_NOT_EXISTED));
		}
		Favorite favorite = favoriteService.selectByMemberId(favoriteRequest.getMemberId());
		if(ToolUtil.isEmpty(favorite)) {
			Favorite favorite1 = favoriteService.init(member);
			FavoriteItem item = new FavoriteItem();
			item.setFavoriteId(favorite1.getId());
			item.setGoodsId(favoriteRequest.getGoodsId());
			item.setShopId(favoriteRequest.getShopId());
			favoriteItemService.insert(item);
			return ResponseEntity.ok("success");
		}else {
			FavoriteItem item = new FavoriteItem();
			item.setFavoriteId(favorite.getId());
			item.setGoodsId(favoriteRequest.getGoodsId());
			item.setShopId(favoriteRequest.getShopId());
			FavoriteItem favoriteItem = favoriteItemService.getByOne(item);
			if(ToolUtil.isEmpty(favoriteItem)) {
				favoriteItemService.insert(item);
				return ResponseEntity.ok("success");
			}else {
				return ResponseEntity.ok("该商品已被收藏");
			}
		}
	}
}
