<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>退款</title>
</head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="com.alipay.api.AlipayClient" %>
<%@ page import="com.alipay.api.DefaultAlipayClient" %>
<%@ page import="com.alipay.api.request.AlipayTradeRefundRequest" %>
<%@ page import="com.alipay.config.AlipayConfig" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%
    // 获得初始化的AlipayClient
    AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

    // 设置请求参数
    AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

    // 商户订单号，商户网站订单系统中唯一订单号
    String out_trade_no = new String(request.getParameter("WIDTRout_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    // 支付宝交易号
    String trade_no = new String(request.getParameter("WIDTRtrade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    // 请二选一设置
    // 需要退款的金额，该金额不能大于订单金额，必填
    String refund_amount = new String(request.getParameter("WIDTRrefund_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    // 退款的原因说明
    String refund_reason = new String(request.getParameter("WIDTRrefund_reason").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    // 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
    String out_request_no = new String(request.getParameter("WIDTRout_request_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

    alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
            + "\"trade_no\":\"" + trade_no + "\","
            + "\"refund_amount\":\"" + refund_amount + "\","
            + "\"refund_reason\":\"" + refund_reason + "\","
            + "\"out_request_no\":\"" + out_request_no + "\"}");

    // 请求
    String result = alipayClient.execute(alipayRequest).getBody();

    // 输出
    out.println(result);
%>
<body>
</body>
</html>