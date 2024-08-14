package com.wh.haoapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具
 */
public class SignUtils {

    public static String genSign(String body, String secretKey) {
        // 使用SHA256算法的Digester
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        //构建签名内容，将哈希映射转换为字符串并拼接密钥
        String content = body+ "." +secretKey;
        return md5.digestHex(content);
    }
}
