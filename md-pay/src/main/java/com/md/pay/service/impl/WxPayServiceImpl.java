package com.md.pay.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.md.order.dao.OrderMapper;
import com.md.order.dao.RefundApplyMapper;
import com.md.order.model.Order;
import com.md.order.model.RefundApply;
import com.md.pay.dao.PaymentOrderMapper;
import com.md.pay.dao.RefundOrderMapper;
import com.md.pay.dao.WeiXinMapper;
import com.md.pay.model.PaymentOrder;
import com.md.pay.model.RefundOrder;
import com.md.pay.model.WeiXin;
import com.md.pay.service.IwxPayService;
import com.stylefeng.guns.core.util.ToolUtil;

import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.bean.paymch.MchPayRefundNotify;
import weixin.popular.bean.paymch.SceneInfo;
import weixin.popular.bean.paymch.SecapiPayRefund;
import weixin.popular.bean.paymch.SecapiPayRefundResult;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.bean.paymch.SceneInfo.H5Info;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.SignatureUtil;

@Service
public class WxPayServiceImpl implements IwxPayService {

	@Resource
	OrderMapper orderMapper;
	@Resource
	PaymentOrderMapper paymentOrderMapper;
	@Resource
	RefundOrderMapper refundOrderMapper;
	@Resource
	WeiXinMapper weiXinMapper;
	@Resource
	RefundApplyMapper refundApplyMapper;
	
	
	// 微信商户号：*****
	private static final String APPNAME = "利郎商城";

	
	@Override
	public UnifiedorderResult wxPayUnifiedorder(BigDecimal amount,String orderSn,String ip ,String openid, String notifyUrl,String tradeType,String configKey) {
		// TODO 自动生成的方法存根
		WeiXin weiXin = weiXinMapper.selectById(configKey);
		Unifiedorder unifiedorder = new Unifiedorder();
		SceneInfo sceneInfo = new SceneInfo();
		SceneInfo.H5Info h5Info = new SceneInfo.H5Info();
		h5Info.setApp_name(APPNAME);
		h5Info.setType("Wap");
		
		unifiedorder.setAppid(weiXin.getAppid());
		unifiedorder.setMch_id(weiXin.getMchid());
		unifiedorder.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		unifiedorder.setBody("利郎商城");
		unifiedorder.setOut_trade_no(orderSn);
		unifiedorder.setTotal_fee(String.valueOf(amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP)));
		unifiedorder.setSpbill_create_ip(ip);
		unifiedorder.setNotify_url(notifyUrl);
		unifiedorder.setTrade_type(tradeType);
		unifiedorder.setOpenid(openid);
		if(tradeType.equals("MWEB")){
			unifiedorder.setScene_info(sceneInfo);
		}
		return PayMchAPI.payUnifiedorder(unifiedorder, weiXin.getApikey());
	}

	@Override
	public String webwxPayNotify(Map<String, String> mapData, MchPayNotify payNotify, List<Order> orderList) {
		// TODO 自动生成的方法存根
		WeiXin weiXin = weiXinMapper.selectList(null).get(0);
		if (SignatureUtil.validateSign(mapData, weiXin.getApikey())) {
			String return_code = mapData.get("return_code");
			System.out.println(return_code.equals("SUCCESS"));
			System.out.println(ToolUtil.isNotEmpty(orderList));
			if (return_code.equals("SUCCESS") && ToolUtil.isNotEmpty(orderList)) {
				for(Order order : orderList){
					order.setStatus(1);
					orderMapper.updateById(order);
				}
				//记录付款通知
				PaymentOrder paymentOrder = new PaymentOrder(orderList.get(0), payNotify);
				paymentOrderMapper.insert(paymentOrder);
				return "success";
				
			} else {
				return "ERROR";
			}
		} else {
			return "ERROR";	
		}
	}

	@Override
	public SecapiPayRefundResult wxPayRefund(Order order,PaymentOrder paymentOrder, String money,RefundApply refundApply) {
		// TODO 自动生成的方法存根
		WeiXin weiXin = weiXinMapper.selectList(null).get(0);
		String refundOrderSn = String.valueOf(new Date().getTime())+String.valueOf((int)((Math.random()* 9 + 1) * 100000));
		
		SecapiPayRefund secapiPayRefund = new SecapiPayRefund();
		secapiPayRefund.setAppid(weiXin.getAppid());
		secapiPayRefund.setMch_id(weiXin.getMchid());
		secapiPayRefund.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		secapiPayRefund.setTransaction_id(paymentOrder.getTransactionId());
		secapiPayRefund.setOut_refund_no(refundOrderSn);
		secapiPayRefund.setTotal_fee(paymentOrder.getTotalFee());
		secapiPayRefund.setRefund_fee(Integer.valueOf(String.valueOf(new BigDecimal(Double.valueOf(money) * 100).setScale(0, BigDecimal.ROUND_HALF_UP))));
		String keystorepath= this.getClass().getResource("/apiclient_cert.p12").getPath();
		LocalHttpClient.initMchKeyStore(weiXin.getMchid(), keystorepath);
		SecapiPayRefundResult secapiPayRefundResult = PayMchAPI.secapiPayRefund(secapiPayRefund, weiXin.getApikey());
		if(secapiPayRefundResult.getReturn_code().equals("SUCCESS")){
			refundApply.setRefundOrderSn(refundOrderSn);
			refundApplyMapper.updateById(refundApply);
		}
		return secapiPayRefundResult;
	}
	@Override
	public SecapiPayRefundResult wxPayRefund(Order order,PaymentOrder paymentOrder, BigDecimal money) {
		// TODO 自动生成的方法存根
		WeiXin weiXin = weiXinMapper.selectList(null).get(0);
		String refundOrderSn = String.valueOf(new Date().getTime())+String.valueOf((int)((Math.random()* 9 + 1) * 100000));
		
		SecapiPayRefund secapiPayRefund = new SecapiPayRefund();
		secapiPayRefund.setAppid(weiXin.getAppid());
		secapiPayRefund.setMch_id(weiXin.getMchid());
		secapiPayRefund.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
		secapiPayRefund.setTransaction_id(paymentOrder.getTransactionId());
		secapiPayRefund.setOut_refund_no(refundOrderSn);
		secapiPayRefund.setTotal_fee(paymentOrder.getTotalFee());
		secapiPayRefund.setRefund_fee(Integer.valueOf(String.valueOf(money.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP))));
		String keystorepath= this.getClass().getResource("/apiclient_cert.p12").getPath();
		LocalHttpClient.initMchKeyStore(weiXin.getMchid(), keystorepath);
		SecapiPayRefundResult secapiPayRefundResult = PayMchAPI.secapiPayRefund(secapiPayRefund, weiXin.getApikey());
		if(secapiPayRefundResult.getReturn_code().equals("SUCCESS")){
			
		}
		return secapiPayRefundResult;
	}

	@Override
	public String webwxPayRrfundNotify(Map<String, String> mapData, MchPayRefundNotify payNotify) {
		// TODO 自动生成的方法存根
		System.out.println("退款回调成功啦！----------111111");
		WeiXin weiXin = weiXinMapper.selectList(null).get(0);
		if (SignatureUtil.validateSign(mapData, weiXin.getApikey())) {
			String return_code = mapData.get("return_code");
			if (return_code.equalsIgnoreCase("SUCCESS")) {
				String out_trade_no = mapData.get("out_trade_no");
				Order orderInfo = orderMapper.selectById(Long.valueOf(out_trade_no));	
				//记录退款通知
				RefundOrder refundOrder = new RefundOrder(orderInfo,payNotify);
				refundOrderMapper.insert(refundOrder);
				return "success";
			} else {
				return "ERROR";
			}
		} else {
			return "ERROR";	
		}
	}

}
