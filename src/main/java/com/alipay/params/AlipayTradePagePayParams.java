package com.alipay.params;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一收单交易支付接口 请求参数 具体信息请访问: https://docs.open.alipay.com/270/alipay.trade.page.pay
 */
@Data
public class AlipayTradePagePayParams implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户订单号，64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
     */
    private String out_trade_no;

    /**
     * 销售产品码，与支付宝签约的产品码名称。 注：目前仅支持FAST_INSTANT_TRADE_PAY
     */
    private String product_code;

    /**
     * 类型:Price 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     */
    private Double total_amount;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 订单描述
     */
    private String body;

}
