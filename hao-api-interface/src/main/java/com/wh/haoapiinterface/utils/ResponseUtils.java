package com.wh.haoapiinterface.utils;//package com.ghost.leapiinterface.utils;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import icu.qimuu.qiapisdk.exception.ApiException;
//import icu.qimuu.qiapisdk.model.response.ResultResponse;
//
//import java.util.Map;
//
//import static org.apache.el.util.MessageFactory.get;
//
//public class ResponseUtils {
//    public static Map<String, Object> responseToMap(String response) {
//        return new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {
//        }.getType());
//    }
//
//    public static <T> ResultResponse baseResponse(String baseUrl, T params) throws ApiException {
//        String response = null;
//        response = get(baseUrl, params);
//        Map<String, Object> fromResponse = responseToMap(response);
//        boolean success = (boolean) fromResponse.get("success");
//        ResultResponse baseResponse = new ResultResponse();
//        if (!success) {
//            baseResponse.setData(fromResponse);
//            return baseResponse;
//        }
//        fromResponse.remove("success");
//        baseResponse.setData(fromResponse);
//        return baseResponse;
//    }
//}