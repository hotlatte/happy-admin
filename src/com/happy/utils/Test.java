package com.happy.utils;


import com.baidu.aip.speech.AipSpeech;
import it.sauronsoftware.jave.AudioInfo;
import org.json.JSONObject;


public class Test {
    public static void main(String[] args) {
        AipSpeech client = new AipSpeech("10618615", "G9nZFq4vwbnmG1CG0oWwMjgg",
                "La580GvFtrWWodNXNQSVQkBaYtPoScIw");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        JSONObject res = client.asr("test.pcm", "pcm", 16000, null);
        System.out.println(res.toString(2));


        /*String s = "20180103100609333-107890987";
        System.out.println(s);
        System.out.println(StringUtils.encodeToBase64(s));
        System.out.println(StringUtils.fromBase64("MjAxODAxMDMxMDA2MDkzMzMtMTA3ODkwOTg3"));*/
       /* //https://1234298049626266.mns.cn-hangzhou.aliyuncs.com/queues/test
        HttpClientUtil client = HttpClientUtil.getInstance();
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "    <Message xmlns=\"http://mns.aliyuncs.com/doc/v1/\">\n" +
                "        <MessageBody>测试消息</MessageBody>\n" +
                "        <DelaySeconds>0</DelaySeconds>\n" +
                "        <Priority>1</Priority>\n" +
                "    </Message>";
        String HTTP_METHOD = "POST";
        String CONTENT_MD5 = "";
        String DATE = new Date().toGMTString();
        String sign = StringUtils.encodeToBase64(StringUtils.makeSHA1(HTTP_METHOD + "\n"
                + CONTENT_MD5 + "\n"
                + "text/xml;charset=utf-8" + "\n"
                + DATE + "\n"
                + CanonicalizedMNSHeaders
                + CanonicalizedResource));
        Map<String, String> headers = new HashMap<>();
        headers.put("Date", DATE);
        headers.put("x-mns-version", "2015-06-06");
        headers.put("Host", "1234298049626266.mns.cn-hangzhou.aliyuncs.com");
        headers.put("Authorization","MNS LTAIIfxQjNjNCWp:");
        String result = client.sendHttpPost("https://1234298049626266.mns.cn-hangzhou.aliyuncs.com/queues/test/messages",
                data, "text/xml;charset=utf-8", headers);
        System.out.println(result);*/
    }
}
