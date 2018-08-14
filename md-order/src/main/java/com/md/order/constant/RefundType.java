package com.md.order.constant;

/**
 * 订单的状态
 *
 * @author 
 * @Date 
 */
public enum RefundType {
    
    MONEY(0, "仅退款"),
    ALL(1, "退货退款");

    int code;
    String message;

    RefundType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer status) {
        if (status == null) {
            return "";
        } else {
            for (RefundType s : RefundType.values()) {
                if (s.getCode() == status) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
