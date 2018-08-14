package com.md.pay.service;

import java.math.BigDecimal;

public interface IALiPayService {
	
	String aLiPay(BigDecimal amount,String orderSn,String subject ,String openid); 
}
