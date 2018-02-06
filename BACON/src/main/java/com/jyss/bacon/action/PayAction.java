package com.jyss.bacon.action;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.jyss.bacon.constant.AliConfig;
import com.jyss.bacon.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class PayAction {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(PayAction.class);

    /**
     * 支付宝充值,服务端验证异步通知信息
     */
    @RequestMapping(value = "/DlrAliNotify",method = RequestMethod.POST)
    public String updateUserBalance(HttpServletRequest request){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            logger.info(">>>>>key: " + name + ", value: " + valueStr);
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, AliConfig.ALIPAY_PUBLIC_KEY, "utf-8","RSA2");
            if(flag){
                String outTradeNo = request.getParameter("out_trade_no");
                String totalAmount = request.getParameter("total_amount");
                String sellerId = request.getParameter("seller_id");
                String appId = request.getParameter("app_id");

                if(){

                }

            }
            return "failure";          // 验签失败


        } catch (AlipayApiException e) {
            return "failure";          // 验签发生异常,则直接返回失败
        }


    }



}
