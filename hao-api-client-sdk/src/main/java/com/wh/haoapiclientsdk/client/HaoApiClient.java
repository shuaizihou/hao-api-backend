package com.wh.haoapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.wh.haoapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.wh.haoapiclientsdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author wuhao
 */
public class HaoApiClient {

    private static final String EXTRA_BODY = "userInfoHaoAPI";

    public static final String GATEWAY_HOST = "http://localhost:8090";

    private String accessKey;

    private String secretKey;

    public HaoApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 随机获取一句毒鸡汤
     * @return
     */
    public String getPoisonousChickenSoup() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/poisonousChickenSoup")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机壁纸
     * @return
     */
    public String getRandomWallpaper() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/randomWallpaper")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机土味情话
     * @return
     */
    public String getLoveTalk() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/loveTalk")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 每日一句励志英语
     * @return
     */
    public String getDailyEnglish() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/en")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }

    /**
     * 随机笑话
     * @return
     */
    public String getRandomJoke() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/joke")
                .addHeaders(getHeaderMap(EXTRA_BODY))
                .body(EXTRA_BODY)
                .execute();
        return httpResponse.body();
    }


    public String getUserNameByPost(User user) {
        // 将User对象转换为JSON字符串
        String json = JSONUtil.toJsonStr(user);
        // 使用HttpRequest工具发起POST请求，并获取服务器的响应
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name")
                .addHeaders(getHeaderMap(json))
                .body(json) // 将JSON字符串设置为请求体
                .execute(); // 执行请求
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body) {
        // 创建一个HashMap对象，用于存储请求头信息
        HashMap<String, String> hashMap = new HashMap<>();
        // 将accessKey添加到请求头中
        hashMap.put("accessKey", accessKey);

        // 不能直接发送密钥
        //hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }
}

