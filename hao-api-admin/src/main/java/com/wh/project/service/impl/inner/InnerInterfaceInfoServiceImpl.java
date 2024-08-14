package com.wh.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wh.haoapicommon.model.entity.InterfaceInfo;
import com.wh.haoapicommon.service.InnerInterfaceInfoService;
import com.wh.project.common.ErrorCode;
import com.wh.project.exception.BusinessException;
import com.wh.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        // 参数校验
        if (StringUtils.isAnyBlank(url, method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建查询条件包装器
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);

        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
