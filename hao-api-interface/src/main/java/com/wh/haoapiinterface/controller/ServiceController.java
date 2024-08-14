package com.wh.haoapiinterface.controller;

import com.wh.haoapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static cn.hutool.http.HttpUtil.get;


@RestController
@RequestMapping("/")
public class ServiceController {

    /**
     * 1. 获取输入名称
     */
    @PostMapping("/name")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
        return "POST 你的名字是：" + user.getUsername();
    }


    /**
     * 2. 随机毒鸡汤
     *
     * @return
     */
    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");// 真实的第三方接口地址
    }

    /**
     //     * 3. 随机壁纸
     //     *
     //     * @param randomWallpaperParams
     //     * @return
     //     * @throws ApiException
     //
//    @GetMapping("/randomWallpaper")
//    public RandomWallpaperResponse randomWallpaper(RandomWallpaperParams randomWallpaperParams) throws ApiException {
////        String baseUrl = "https://api.btstu.cn/sjbz/api.php";
//        String baseUrl = "https://api.vvhan.com/api/view?type=json";
//        String url = buildUrl(baseUrl, randomWallpaperParams);
//        if (StringUtils.isAllBlank(randomWallpaperParams.getLx(), randomWallpaperParams.getMethod())) {
//            url = url + "?format=json";
//        } else {
//            url = url + "&format=json";
//        }
//        return JSONUtil.toBean(get(baseUrl), RandomWallpaperResponse.class);
//    }*/


    /**
     * 4. 随机土味情话
     *
     * @return
     */
    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return get("https://api.vvhan.com/api/love");
    }

    /**
     * 5. 每日一句励志英语
     *
     * @return
     */
    @GetMapping("/en")
    public String dailyEnglish() {
        return get("https://api.vvhan.com/api/en");
    }

    /**
     * 6. 随机笑话
     *
     * @return
     */
    @GetMapping("/joke")
    public String randomJoke() {
        return get("https://api.vvhan.com/api/joke");
    }


}
