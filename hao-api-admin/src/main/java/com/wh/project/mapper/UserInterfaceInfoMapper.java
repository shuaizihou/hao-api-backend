package com.wh.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wh.haoapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author wuhao
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-08-05 10:48:53
* @Entity com.wh.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //select interfaceInfoId ,sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3;
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




