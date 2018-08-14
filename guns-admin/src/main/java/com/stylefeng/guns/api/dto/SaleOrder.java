package com.stylefeng.guns.api.dto;

import java.util.List;

public class SaleOrder {

	private String VoucherDate;  //单据日期 此参数可不传，默认系统日期。
	
	private String ExternalCode;		//外部系统单据编码，编码必须唯一，且此字段不为空。
	
	private Customer Customer;		//T+系统单据编码
	
	private String Address;		//送货地址 可為空
	
	private String LinkMan;     //联系人  可为空
	
	private String ContactPhone; 	//联系电话  可为空
	
	private String Memo;		//备注 可为空
	
	private List<SaleOrderDetails> SaleOrderDetails;  //单据明细信息

	public String getVoucherDate() {
		return VoucherDate;
	}

	public void setVoucherDate(String voucherDate) {
		VoucherDate = voucherDate;
	}

	public String getExternalCode() {
		return ExternalCode;
	}

	public void setExternalCode(String externalCode) {
		ExternalCode = externalCode;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer customer) {
		Customer = customer;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getLinkMan() {
		return LinkMan;
	}

	public void setLinkMan(String linkMan) {
		LinkMan = linkMan;
	}

	public String getContactPhone() {
		return ContactPhone;
	}

	public void setContactPhone(String contactPhone) {
		ContactPhone = contactPhone;
	}

	public String getMemo() {
		return Memo;
	}

	public void setMemo(String memo) {
		Memo = memo;
	}

	public List<SaleOrderDetails> getSaleOrderDetails() {
		return SaleOrderDetails;
	}

	public void setSaleOrderDetails(List<SaleOrderDetails> saleOrderDetails) {
		SaleOrderDetails = saleOrderDetails;
	}
	
}
