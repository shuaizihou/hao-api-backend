package com.wh.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wh.haoapicommon.model.entity.User;
import com.wh.haoapicommon.model.entity.UserInterfaceInfo;
import com.wh.project.annotation.AuthCheck;
import com.wh.project.common.*;
import com.wh.project.constant.CommonConstant;
import com.wh.project.constant.UserConstant;
import com.wh.project.exception.BusinessException;
import com.wh.project.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.wh.project.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.wh.project.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;

import com.wh.project.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 接口管理
 *
 * @author wuhao
 */
@RestController
@RequestMapping("/UserInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService UserInterfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 创建
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userUserInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userUserInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userUserInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
        UserInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = UserInterfaceInfoService.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = UserInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = UserInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest UserInterfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (UserInterfaceInfoUpdateRequest == null || UserInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoUpdateRequest, UserInterfaceInfo);
        // 参数校验
        UserInterfaceInfoService.validUserInterfaceInfo(UserInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = UserInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = UserInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = UserInterfaceInfoService.updateById(UserInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfo = UserInterfaceInfoService.getById(id);
        return ResultUtils.success(UserInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest) {
        UserInterfaceInfo UserInterfaceInfoQuery = new UserInterfaceInfo();
        if (UserInterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(UserInterfaceInfoQueryRequest, UserInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(UserInterfaceInfoQuery);
        List<UserInterfaceInfo> UserInterfaceInfoList = UserInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(UserInterfaceInfoList);
    }

    /**
     * 分页获取列表
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (UserInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo UserInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoQueryRequest, UserInterfaceInfoQuery);
        long current = UserInterfaceInfoQueryRequest.getCurrent();
        long size = UserInterfaceInfoQueryRequest.getPageSize();
        String sortField = UserInterfaceInfoQueryRequest.getSortField();
        String sortOrder = UserInterfaceInfoQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(UserInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> UserInterfaceInfoPage = UserInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(UserInterfaceInfoPage);
    }
}
