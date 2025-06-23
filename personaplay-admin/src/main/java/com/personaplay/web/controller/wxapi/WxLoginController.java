package com.personaplay.web.controller.wxapi;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.personaplay.common.config.RuoYiConfig;
import com.personaplay.common.constant.Constants;
import com.personaplay.common.core.domain.AjaxResult;
import com.personaplay.common.core.domain.entity.SysUser;
import com.personaplay.common.core.domain.model.LoginUser;
import com.personaplay.common.utils.DateUtils;
import com.personaplay.common.utils.file.FileUploadUtils;
import com.personaplay.common.utils.file.FileUtils;
import com.personaplay.common.utils.http.HttpUtils;
import com.personaplay.framework.config.ServerConfig;
import com.personaplay.framework.manager.AsyncManager;
import com.personaplay.framework.manager.factory.AsyncFactory;
import com.personaplay.framework.web.service.SysLoginService;
import com.personaplay.framework.web.service.TokenService;
import com.personaplay.mbti.domain.MbtiUser;
import com.personaplay.mbti.service.IMbtiUserService;
import com.personaplay.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/wxapi/")
public class WxLoginController {

    private static final Logger log = LoggerFactory.getLogger(WxLoginController.class);

    /**
     * 测试接口
     * @return
     */
    @GetMapping("test")
    public AjaxResult test(){
        return AjaxResult.success("小程序api调试成功！~");
    }

    @Autowired
    private IMbtiUserService mbtiUserService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 你自己的微信小程序APPID
     */
    @Value("${wx.appid}")
    private String AppID;
    /**
     * 你自己的微信APP密钥
     */
    @Value("${wx.secret}")
    private String AppSecret;

    @Value("${wx.jscode2session.url}")
    private String jscode2sessionUrl;

    /**
     * 微信登录授权
     * 前端传递code和用户信息(昵称、头像等)
     * @param object
     * @return
     */
    @PostMapping("/wxlogin")
    public AjaxResult wxLogin(@RequestBody JSONObject object){
        //微信官方提供的微信小程序登录授权时使用的URL地址
        String url = jscode2sessionUrl;
        System.out.println("收到微信授权请求: " + object);

        JSONObject userInfo = null;
        if (object.containsKey("userInfo") && object.get("userInfo") != null) {
            userInfo = (JSONObject) object.get("userInfo");
        }

        /**
         * 拼接需要的参数
         * appid = AppID 你自己的微信小程序APPID
         * js_code = AppSecret 你自己的微信APP密钥
         * grant_type=authorization_code = code 微信官方提供的临时凭证
         */
        String params = StrUtil.format("appid={}&secret={}&js_code={}&grant_type=authorization_code", AppID, AppSecret, object.get("code"));

        //开始发起网络请求,若依管理系统自带网络请求工具，直接使用即可
        String res = HttpUtils.sendGet(url, params);
        JSONObject jsonObject = JSONUtil.parseObj(res);
        String openid = (String) jsonObject.get("openid");
        String session_key = (String) jsonObject.get("session_key");

        if (StrUtil.isEmpty(openid)) {
            return AjaxResult.error("未获取到openid");
        }

        String nickname = userInfo != null ? String.valueOf(userInfo.get("nickName")) : "微信用户";
        String avatarUrl = userInfo != null ? String.valueOf(userInfo.get("avatarUrl")) : "";

        // 查询该openid是否已在mbti用户表中存在
        MbtiUser mbtiUser = mbtiUserService.selectMbtiUserByOpenID(openid);

        if (mbtiUser == null) {
            // 新用户，需要注册
            mbtiUser = new MbtiUser();
            mbtiUser.setNickname(nickname);
            mbtiUser.setAvatar(avatarUrl);
            mbtiUser.setUsername(nickname);
            mbtiUser.setOpenId(openid);
            mbtiUser.setCreateTime(DateUtils.getNowDate());
            mbtiUser.setCreateBy("wx_login");
            mbtiUser.setStatus("0"); // 正常状态
            mbtiUserService.insertMbtiUser(mbtiUser);
        } else {
            // 更新用户信息
            if (userInfo != null) {
                //此处不更新昵称和头像
//                mbtiUser.setNickname(nickname);
//                mbtiUser.setUsername(nickname);
//                mbtiUser.setAvatar(avatarUrl);
                mbtiUser.setUpdateTime(DateUtils.getNowDate());
                mbtiUser.setUpdateBy("wx_login");
                mbtiUserService.updateMbtiUser(mbtiUser);
            }
        }

        // 记录登录信息
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(mbtiUser.getUsername(), Constants.LOGIN_SUCCESS, "微信授权登录成功"));

        // 创建登录用户对象，生成token
        LoginUser loginUser = createLoginUser(mbtiUser);
        String token = tokenService.createToken(loginUser);

        // 构建返回结果
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("openid", openid);

        // 这里直接放入MbtiUser对象，前端可以获取用户信息
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", mbtiUser.getUserId());
        userMap.put("username", mbtiUser.getUsername());
        userMap.put("nickName", mbtiUser.getNickname());
        userMap.put("avatar", mbtiUser.getAvatar());
        data.put("userInfo", userMap);

        return AjaxResult.success("登录成功", data);
    }

    /**
     * 创建登录用户
     */
    private LoginUser createLoginUser(MbtiUser mbtiUser) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(mbtiUser.getUserId());
        loginUser.setUser(convertToSysUser(mbtiUser));
        return loginUser;
    }

    /**
     * 将MbtiUser转换为SysUser，用于token生成
     */
    private SysUser convertToSysUser(MbtiUser mbtiUser) {
        SysUser user = new SysUser();
        user.setUserId(mbtiUser.getUserId());
        user.setUserName(mbtiUser.getUsername());
        user.setNickName(mbtiUser.getNickname());
        user.setAvatar(mbtiUser.getAvatar());
        user.setEmail(mbtiUser.getEmail());
        user.setPhonenumber(mbtiUser.getPhone());
        user.setSex(mbtiUser.getSex());
        user.setStatus(mbtiUser.getStatus());
        user.setRemark(mbtiUser.getRemark());
        return user;
    }

    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        System.out.println(file);
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 保存昵称与头像信息到用户信息里
     * @param object
     * @return
     */
    @PostMapping("/saveUserInfo")
    @ResponseBody
    public AjaxResult saveUserInfo(@RequestBody JSONObject object){
        System.out.println(object);
        MbtiUser mbtiUser = mbtiUserService.selectMbtiUserByOpenID(String.valueOf(object.get("openid")));
//        if (StringUtils.hasLength(String.valueOf(object.get("nickName")))){
//            mbtiUser.setNickname(String.valueOf(object.get("nickName")));
//            mbtiUser.setUpdateBy(String.valueOf(object.get("nickName")));
//        }
//        if (StringUtils.hasLength(String.valueOf(object.get("avatarUrl")))){
//            mbtiUser.setAvatar(String.valueOf(object.get("avatarUrl")));
//        }
        mbtiUser.setUpdateTime(DateUtils.getNowDate());
        mbtiUserService.updateMbtiUser(mbtiUser);

        // 返回前端需要的数据
        return AjaxResult.success(mbtiUser);
    }

    /**
     * 获取微信小程序码
     * 通过调用微信接口生成小程序码
     * @param scene 场景值（房间号等参数）
     * @param page 小程序页面路径
     * @param width 码的宽度
     * @param autoColor 自动配置线条颜色
     * @param lineColor 线条颜色（autoColor 为 false 时使用）
     * @param isHyaline 是否需要透明底色
     * @return 小程序码图片
     */
    @GetMapping("/getWxaCode")
    public void getWxaCode(HttpServletResponse response,
                           @RequestParam String scene,
                           @RequestParam(required = false, defaultValue = "pages/mbti/join-room") String page,
                           @RequestParam(required = false, defaultValue = "280") Integer width,
                           @RequestParam(required = false, defaultValue = "false") Boolean autoColor,
                           @RequestParam(required = false) String lineColor,
                           @RequestParam(required = false, defaultValue = "false") Boolean isHyaline) {

        try {
            log.info("开始生成小程序码，参数：scene={}, page={}, width={}, autoColor={}, isHyaline={}",
                    scene, page, width, autoColor, isHyaline);

            // 获取微信小程序access_token
            String accessToken = getAccessToken();
            if (StrUtil.isEmpty(accessToken)) {
                log.error("获取access_token失败，无法生成小程序码");
                sendErrorResponse(response, "获取access_token失败");
                return;
            }
            log.info("获取到access_token: {}", accessToken);

            // 微信获取无限制小程序码接口地址
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

            // 构建请求参数
            JSONObject params = new JSONObject();
            params.set("scene", scene);
            params.set("page", page);
            params.set("width", width);
            params.set("auto_color", autoColor);

            if (!autoColor && StrUtil.isNotEmpty(lineColor)) {
                params.set("line_color", JSONUtil.parseObj(lineColor));
            } else {
                // 默认黑色
                JSONObject lineColorObj = new JSONObject();
                lineColorObj.set("r", 0);
                lineColorObj.set("g", 0);
                lineColorObj.set("b", 0);
                params.set("line_color", lineColorObj);
            }

            params.set("is_hyaline", isHyaline);
            log.info("请求微信小程序码，URL：{}, 参数：{}", url, params);

            // 发起HTTP请求获取小程序码图片数据
            HttpResponse resp = HttpRequest.post(url)
                    .body(params.toString())
                    .execute();

            int status = resp.getStatus();
            log.info("微信API响应状态码: {}", status);

            if (status != 200) {
                log.error("微信API返回异常状态码: {}", status);
                sendErrorResponse(response, "微信API返回状态码: " + status);
                return;
            }

            byte[] result = resp.bodyBytes();

            // 检查返回的内容类型，如果是JSON则可能是错误信息
            String contentType = resp.header("Content-Type");
            log.info("微信API响应Content-Type: {}, 内容大小: {} 字节", contentType, result.length);

            if (contentType != null && contentType.contains("application/json")) {
                // 尝试解析错误信息
                String jsonStr = new String(result, StandardCharsets.UTF_8);
                log.error("微信返回错误：{}", jsonStr);

                JSONObject error = JSONUtil.parseObj(jsonStr);
                sendErrorResponse(response, "生成小程序码失败: " + error.getStr("errmsg", "未知错误"));
                return;
            }

            if (result.length < 1000) {
                // 文件太小，可能是错误信息
                String content = new String(result, StandardCharsets.UTF_8);
                log.warn("返回内容大小异常小，内容：{}", content);

                // 尝试判断是否为JSON
                if (content.startsWith("{") && content.endsWith("}")) {
                    log.error("返回内容疑似JSON错误信息：{}", content);
                    try {
                        JSONObject error = JSONUtil.parseObj(content);
                        sendErrorResponse(response, "生成小程序码失败: " + error.getStr("errmsg", "未知错误"));
                        return;
                    } catch (Exception e) {
                        // 解析JSON失败，继续处理
                        log.warn("尝试解析JSON失败，继续处理返回内容", e);
                    }
                }
            }

            log.info("成功获取小程序码图片，大小：{} 字节", result.length);

            // 设置响应头
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "max-age=86400"); // 缓存一天

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(result);
            outputStream.flush();
            outputStream.close();

            log.info("小程序码生成成功并返回");

        } catch (Exception e) {
            log.error("生成小程序码失败", e);
            sendErrorResponse(response, "生成小程序码失败: " + e.getMessage());
        }
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String errorMsg) {
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":500,\"msg\":\"" + errorMsg + "\"}");
        } catch (IOException ex) {
            log.error("返回错误信息失败", ex);
        }
    }

    /**
     * 获取微信小程序access_token
     * @return access_token
     */
    private String getAccessToken() {
        // 微信获取access_token接口地址
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        String params = "grant_type=client_credential&appid=" + AppID + "&secret=" + AppSecret;

        log.info("开始获取微信access_token, URL: {}", url);

        try {
            // 发起HTTP请求
            String result = HttpUtils.sendGet(url, params);
            log.info("获取access_token响应: {}", result);

            JSONObject jsonObject = JSONUtil.parseObj(result);
            String accessToken = jsonObject.getStr("access_token");

            if (StrUtil.isEmpty(accessToken)) {
                String errorMsg = jsonObject.getStr("errmsg", "未知错误");
                log.error("获取微信access_token失败: {}", errorMsg);
                return null;
            }

            return accessToken;
        } catch (Exception e) {
            log.error("获取微信access_token异常", e);
            return null;
        }
    }
}
