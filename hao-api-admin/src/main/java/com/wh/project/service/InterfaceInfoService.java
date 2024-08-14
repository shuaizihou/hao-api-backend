package com.wh.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wh.haoapicommon.model.entity.InterfaceInfo;

/**
 * @author wuhao
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2024-07-30 16:30:35
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo post, boolean add);

}
