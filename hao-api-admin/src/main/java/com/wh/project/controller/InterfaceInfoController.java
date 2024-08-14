package com.wh.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.wh.haoapicommon.model.entity.InterfaceInfo;
import com.wh.haoapicommon.model.entity.User;
import com.wh.project.annotation.AuthCheck;
import com.wh.project.common.*;
import com.wh.project.constant.CommonConstant;
import com.wh.project.exception.BusinessException;
import com.wh.project.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.wh.project.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.wh.project.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.wh.project.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.wh.project.model.enums.InterfaceInfoStatusEnum;
import com.wh.project.service.InterfaceInfoService;
import com.wh.project.service.UserService;
import com.wh.haoapiclientsdk.client.HaoApiClient;
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
 * @author Haoapi
 */
@RestController
@RequestMapping("/InterfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService InterfaceInfoService;

    @Resource
    private UserService userService;

    @Resource
    private HaoApiClient yuApiClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, InterfaceInfo);
        // 校验
        InterfaceInfoService.validInterfaceInfo(InterfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        InterfaceInfo.setUserId(loginUser.getId());
        boolean result = InterfaceInfoService.save(InterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = InterfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = InterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param InterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest InterfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (InterfaceInfoUpdateRequest == null || InterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoUpdateRequest, InterfaceInfo);
        // 参数校验
        InterfaceInfoService.validInterfaceInfo(InterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = InterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = InterfaceInfoService.updateById(InterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfo = InterfaceInfoService.getById(id);
        return ResultUtils.success(InterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param InterfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest InterfaceInfoQueryRequest) {
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        if (InterfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        List<InterfaceInfo> InterfaceInfoList = InterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(InterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param InterfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest InterfaceInfoQueryRequest, HttpServletRequest request) {
        if (InterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo InterfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(InterfaceInfoQueryRequest, InterfaceInfoQuery);
        long current = InterfaceInfoQueryRequest.getCurrent();
        long size = InterfaceInfoQueryRequest.getPageSize();
        String sortField = InterfaceInfoQueryRequest.getSortField();
        String sortOrder = InterfaceInfoQueryRequest.getSortOrder();
        String content = InterfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        InterfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(InterfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> InterfaceInfoPage = InterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(InterfaceInfoPage);
    }

    // endregion

    /**
     * 发布
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
        // 如果id为null或者id小于等于0，则抛出异常
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.校验该接口是否存在
        // 获取idRequest中的id
        long id = idRequest.getId();
        // 根据id获取接口信息
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        // 如果查询结果为空，则抛出异常
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 2.校验该接口是否可以调用
        // 创建一个User对象(这里先模拟一下，搞个假数据)
        com.wh.haoapiclientsdk.model.User user = new com.wh.haoapiclientsdk.model.User();
        // 设置user对象的username属性为"test"
        user.setUsername("test");
        // 通过yuApiClient的getUsernameByPost方法传入user对象，并将返回的username赋值给username变量
        String username = yuApiClient.getUserNameByPost(user);
        // 如果username为空，则抛出异常
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        // 创建一个InterfaceInfo对象
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        // 3.修改接口数据库中的状态字段为上线
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = InterfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                      HttpServletRequest request) {
        // 如果id为null或者id小于等于0，则抛出异常
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.校验该接口是否存在
        // 获取idRequest中的id
        long id = idRequest.getId();
        // 根据id获取接口信息
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        // 如果查询结果为空，则抛出异常
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 创建一个InterfaceInfo对象
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        // 2.修改接口数据库中的状态字段为关闭
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        // 调用interfaceInfoService的updateById方法，传入interfaceInfo对象，并将返回的结果赋值给result变量
        boolean result = InterfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 测试调用
     *
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    // 这里给它新封装一个参数InterfaceInfoInvokeRequest
    // 返回结果把对象发出去就好了，因为不确定接口的返回值到底是什么
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        // 检查请求对象是否为空或者接口id是否小于等于0
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取接口id
        long id = interfaceInfoInvokeRequest.getId();
        // 获取用户请求参数
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = InterfaceInfoService.getById(id);
        if (oldInterfaceInfo == null && id == 7) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 检查接口状态是否为下线状态
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        // 接口请求信息
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        HaoApiClient apiClient = new HaoApiClient(accessKey, secretKey);
        if (id == 6) {// 调用随机笑话接口
            String randomJoke = apiClient.getRandomJoke();
            return ResultUtils.success(randomJoke);
        } else if (id == 2){// 调用随机获取鸡汤接口
            String poisonousChickenSoup = apiClient.getPoisonousChickenSoup();
            return ResultUtils.success(poisonousChickenSoup);
        } else if (id == 3){// 调用随机壁纸接口
            String randomWallpaper = apiClient.getRandomWallpaper();
            return ResultUtils.success(randomWallpaper);
        } else if (id == 4){// 调用随机土味情话接口
            String loveTalk = apiClient.getLoveTalk();
            return ResultUtils.success(loveTalk);
        } else if (id == 5){// 调用每日一句励志英语接口
            String dailyEnglish = apiClient.getDailyEnglish();
            return ResultUtils.success(dailyEnglish);
        } else {// id == 1 调用获取输入名称接口
            Gson gson = new Gson();
            com.wh.haoapiclientsdk.model.User user = gson.fromJson(userRequestParams, com.wh.haoapiclientsdk.model.User.class);
            String nameByJSON = apiClient.getUserNameByPost(user);
            return ResultUtils.success(nameByJSON);
        }
    }
}
