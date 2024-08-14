package com.wh.haoapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wh.haoapicommon.model.entity.InterfaceInfo;
import com.wh.haoapicommon.model.entity.User;
import com.wh.haoapicommon.model.entity.UserInterfaceInfo;

/**
 *
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
