package com.md.pay.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.md.order.model.Order;
import com.md.order.model.RefundApply;
import com.md.pay.model.PaymentOrder;

import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.bean.paymch.MchPayRefundNotify;
import weixin.popular.bean.paymch.SecapiPayRefundResult;
import weixin.popular.bean.paymch.UnifiedorderResult;

public interface IwxPayService {
	
	/**
	 * 微信支付下单
	 * @param order
	 * @return
	 */
	UnifiedorderResult wxPayUnifiedorder (BigDecimal amount,String orderSn,String ip,String openid);

	String webwxPayNotify(Map<String, String> mapData, MchPayNotify payNotify, List<Order> orderList);
	
	SecapiPayRefundResult wxPayRefund (Order order,PaymentOrder paymentOrder,String money, RefundApply refundApply);

	String webwxPayRrfundNotify(Map<String, String> mapData, MchPayRefundNotify payNotify);

	SecapiPayRefundResult wxPayRefund(Order order, PaymentOrder paymentOrder, BigDecimal actualPay);
	
}
