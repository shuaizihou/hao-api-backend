# Hao API

>
> 管理员账户密码：wuhao / 123456789
>
> 项目后端开源地址：[https://github.com/shuaizihou/hao-api-backend](https://github.com/shuaizihou/hao-api-backend)
>
> 项目前端开源地址：[https://github.com/shuaizihou/hao-api-frontend](https://github.com/shuaizihou/hao-api-frontend)


## 项目简介

本项目是一个提供开放 API 接口供开发者调用的平台，基于 Spring Boot 后端 + React 前端的全栈微服务项目。
- 管理员可以接入、发布、下线、修改接口、统计分析各接口调用情况；
- 用户可以注册登录并开通接口调用权限、浏览接口、在线调试，还能使用客户端SDK轻松在代码中调用接口。


## 项目背景

&emsp;&emsp;我的初衷是尽可能帮助和服务更多的用户和开发者，让他们更加方便快捷的获取他们想要的信息和功能。
接口平台可以帮助开发者快速接入一些常用的服务，从而提高他们的开发效率，比如随机毒鸡汤、每日一句励志英语、随机笑话等服务，他们是一些应用或者小程序常见的功能，所以提供这些接口可以帮助开发者更加方便地实现这些功能。这些接口也可以让用户在使用应用时获得更加全面的功能和服务，从而提高他们的用户体验


## 项目技术栈

### 后端技术栈

- 框架：SpringBoot 2.7.0、Mybatis-plus、Spring Cloud
- 数据库：Mysql 8.0、Redis
- 注册中心：Nacos
- 服务调用：Dubbo
- 网关：Spring Cloud Gateway

### 前端技术栈

- 开发框架：React、Umi
- 脚手架：Ant Design Pro
- 组件库：Ant Design、Ant Design Components
- 语法扩展：TypeScript、Less
- 打包工具：Webpack
- 代码规范：ESLint、StyleLint、Prettier


## 项目模块

- api-frontend ：为项目前端，前端项目启动具体看readme.md文档
- api-common ：为公共封装类（如公共实体、公共常量，统一响应实体，统一异常处理）
- api-backend ：为接口管理平台，主要包括用户、接口相关的功能
- api-gateway ：为网关服务，**涉及统一日志、路由转发、访问控制、流量染色，以及集中处理签名校验、请求参数校验、接口调用统计等逻辑**
- api-interface：为接口服务，提供可供调用的接口
- api-sdk：**提供给开发者的SDK**


## 项目启动

### 前端

#### Install `node_modules`:

```
npm install 
```

OR

```
yarn
```

#### 启动项目

```3#
npm start
```

#### 构建项目

```
npm run dev
```



### 后端

1. 下载/拉取本项目到本地
2. 通过 IDEA 代码编辑器进行打开项目，等待依赖的下载
3. 修改配置文件 `application.yml` 的信息，比如 Nacos、Mysql、Redis等
4. 启动 Nacos、Mysql、Redis
5. 将 客户端 SDK 安装到本地仓库
6. 修改信息完成后，一键运行项目
7. 按顺序启动服务

#### 服务启动顺序参考
1. api-backend 
2. api-gateway
3. api-interface


