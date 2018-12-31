package com.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.config.AlipayConfig;
import com.alipay.params.AlipayTradePagePayParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝电脑网站支付demo AlipayController
 *
 * @author hansai
 * @date 2018/12/31
 */
@Slf4j
@Controller
@RequestMapping("/")
public class AlipayController {

    @RequestMapping("")
    public String index() {
        return "index.html";
    }

    @PostMapping("tradePagePay")
    public void tradePagePay(AlipayTradePagePayParams alipayTradePagePayParams, HttpServletResponse response) throws AlipayApiException, IOException {

        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = alipayTradePagePayParams.getOut_trade_no();
        // 付款金额，必填
        String total_amount = String.valueOf(alipayTradePagePayParams.getTotal_amount());
        // 订单名称，必填
        String subject = alipayTradePagePayParams.getSubject();
        // 商品描述，可空
        String body = alipayTradePagePayParams.getBody();

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(out_trade_no);
        model.setTotalAmount(total_amount);
        model.setSubject(subject);
        model.setBody(body);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        alipayRequest.setBizModel(model);

        // bizContent 方式
        // alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
        //         + "\"total_amount\":\"" + total_amount + "\","
        //         + "\"subject\":\"" + subject + "\","
        //         + "\"body\":\"" + body + "\","
        //         + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        // alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
        //         + "\"total_amount\":\"" + total_amount + "\","
        //         + "\"subject\":\"" + subject + "\","
        //         + "\"body\":\"" + body + "\","
        //         + "\"timeout_express\":\"10m\","
        //         + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        // 请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        // 请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        // 输出
        response.setContentType("text/html;charset=utf-8");
        // 直接将完整的表单html输出到页面
        response.getWriter().write(result);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @PostMapping("tradeQuery")
    public void tradeQuery(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {

        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        // 商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTQout_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 支付宝交易号
        String trade_no = new String(request.getParameter("WIDTQtrade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"trade_no\":\"" + trade_no + "\"}");

        // 请求
        String result = alipayClient.execute(alipayRequest).getBody();

        // 输出
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(result);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @PostMapping("tradeRefund")
    public void tradeRefund(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {

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
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(result);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @PostMapping("tradeRefundQuery")
    public void tradeRefundQuery(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {

        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        // 商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDRQout_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 支付宝交易号
        String trade_no = new String(request.getParameter("WIDRQtrade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 请二选一设置
        // 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号，必填
        String out_request_no = new String(request.getParameter("WIDRQout_request_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"trade_no\":\"" + trade_no + "\","
                + "\"out_request_no\":\"" + out_request_no + "\"}");

        // 请求
        String result = alipayClient.execute(alipayRequest).getBody();

        // 输出
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(result);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @PostMapping("tradeClose")
    public void tradeClose(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {

        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        // 设置请求参数
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        // 商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTCout_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 支付宝交易号
        String trade_no = new String(request.getParameter("WIDTCtrade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        // 请二选一设置

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"trade_no\":\"" + trade_no + "\"}");

        // 请求
        String result = alipayClient.execute(alipayRequest).getBody();

        // 输出
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(result);
        response.getWriter().flush();
        response.getWriter().close();

    }

    @PostMapping("notifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {

        /* *
         * 功能：支付宝服务器异步通知页面
         * 日期：2017-03-30
         * 说明：
         * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
         * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。


         *************************页面功能说明*************************
         * 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
         * 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
         * 如果没有收到该页面返回的 success
         * 建议该页面只做支付成功的业务逻辑处理，退款的处理请以调用退款查询接口的结果为准。
         */

        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(16);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }

        // 调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

        // ——请在这里编写您的程序（以下代码仅作参考）——
        /* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id / seller_email）
        4、验证app_id是否为该商户本身。
        */
        // 验证成功
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            if (trade_status.equals("TRADE_FINISHED")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                // 如果有做过处理，不执行商户的业务程序

                // 注意：
                // 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                // 判断该笔订单是否在商户网站中已经做过处理
                // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                // 如果有做过处理，不执行商户的业务程序

                // 注意：
                // 付款完成后，支付宝系统发送该交易状态通知
            }

            response.getWriter().println("success");
            response.getWriter().flush();
            response.getWriter().close();

        } else { // 验证失败
            response.getWriter().println("fail");
            response.getWriter().flush();
            response.getWriter().close();

            // 调试用，写文本函数记录程序运行情况是否正常
            String sWord = AlipaySignature.getSignCheckContentV1(params);
            AlipayConfig.logResult(sWord);
        }

        // ——请在这里编写您的程序（以上代码仅作参考）——

    }

    @GetMapping("returnUrl")
    public void returnUrl(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {

        /* *
         * 功能：支付宝服务器同步通知页面
         * 日期：2017-03-30
         * 说明：
         * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
         * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。


         *************************页面功能说明*************************
         * 该页面仅做页面展示，业务逻辑处理请勿在该页面执行
         */

        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>(16);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }

        // 调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

        // ——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount);
            response.getWriter().flush();
            response.getWriter().close();

        } else {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("验签失败");
            response.getWriter().flush();
            response.getWriter().close();

        }
        // ——请在这里编写您的程序（以上代码仅作参考）——

    }

}
