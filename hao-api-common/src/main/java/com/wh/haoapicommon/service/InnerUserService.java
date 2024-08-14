package com.wh.haoapicommon.service;


import com.wh.haoapicommon.model.entity.User;


/**
 * 用户服务
 *
 * @author Haoapi
 */
public interface InnerUserService {

    /**
     * 数据库查是否已分配给用户密钥（accessKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
