package com.happy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happy.model.Military;
import com.happy.service.ICarService;
import com.happy.service.IMilitaryService;
import com.happy.service.IUserService;
import com.happy.utils.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MilitaryController {

    @Resource
    private IMilitaryService militaryService;

    @RequestMapping("/index")
    public String index(String accessToken, HttpSession session) {
        if (StringUtils.isNotEmpty(accessToken)) {
            String result = HttpClientUtil.getInstance().sendHttpsGet("https://jrwh.wuhunews.cn/server/e/member/getMemberInfo?accessToken=" + accessToken);
            JSONObject res = JSON.parseObject(result);
            if ("ok".equalsIgnoreCase(res.getString("message"))) {
                // 成功
                session.setAttribute("user", res.getJSONObject("data"));
                session.setAttribute("accessToken", accessToken);
            } else {

            }
        }
        return "index";
    }


    @RequestMapping("/vote")
    public String vote() {
        return "vote";
    }

    @RequestMapping(value = "/contribution", method = RequestMethod.GET)
    public ModelAndView upload(ModelAndView mv, String accessToken) {
        if (StringUtils.isNotEmpty(accessToken)) {
            String result = HttpClientUtil.getInstance().sendHttpsGet("https://jrwh.wuhunews.cn/server/e/member/getMemberInfo?accessToken=" + accessToken);
            System.out.println(result);
        }
        //https://jrwh.wuhunews.cn/server/e/member/getMemberInfo?accessToken=xxxxx
        return mv;
    }


    @RequestMapping(value = "/contribution", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject upload(MultipartFile image, HttpServletRequest request, HttpSession session, String name, String phone, String school, String photoName, String profile) {
        JSONObject user = (JSONObject) session.getAttribute("user");
        Map res = new HashMap();
        res.put("success", true);

        //判断如果获取到了AccessToken
//        if (user == null) {
//            res.put("success", false);
//            res.put("ErrorMessage", "登录超时或未在今日芜湖客户端内参加活动");
//            return (JSONObject) JSON.toJSON(res);
//        }
//        String userPhone = user.getString("phone");

        //判断表单不为空
        if("".equals(name)|| "".equals(phone)||"".equals(school) ||"".equals(photoName) ||"".equals(profile) ){
            res.put("success", false);
            res.put("ErrorMessage", "请填写所有的报名资料！");
            return (JSONObject) JSON.toJSON(res);
        }

        //自定义存储图片文件夹
        String dir = request.getServletContext().getRealPath("/uploads");

        List<String> errorMsg = new ArrayList<>(5);
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();

            //如果服务器是Linux系统
            dirFile.setWritable(true, false);
            dirFile.setReadable(true, false);
            //
        }
        String ext = StringUtils.getFileExt(image.getOriginalFilename()).toLowerCase();
        if ("jpg".equals(ext) || "jpeg".equals(ext) || "gif".equals(ext) || "png".equals(ext)) {
            if (image.getSize() < 5 * 1024 * 1024) {
                String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "." + ext;
                File target = new File(dirFile, filename);
                String path = "/uploads/" + filename;
                res.put("path", path);
                try {
                    image.transferTo(target);

                    // linux
                    target.setReadable(true, false);
                    Military military = new Military();
                    military.setPhotoUrl("http://hd.wuhunews.cn/military" + path);
                    military.setIsCheck(0);
                    military.setName(name);
                    military.setPhone(phone);
                    military.setSchool(school);
                    military.setPhotoName(photoName);
                    military.setProfile(profile);
                    military.setCreateTime(new Date());
                    System.out.println(military.getCheckTime());

                    militaryService.save(military);

                } catch (IOException e) {
                    e.printStackTrace();
                    errorMsg.add(e.getMessage());
                    res.put("success", false);
                }

            } else {
                // 提示
                errorMsg.add("文件不能超过5M");
                res.put("success", false);
            }
        } else {
            errorMsg.add("文件类型不正确");
            res.put("success", false);
        }

        res.put("ErrorMessage", StringUtils.join(errorMsg, ","));
        return (JSONObject) JSON.toJSON(res);
    }


    /**
     * 查询所有投稿
     */
    @RequestMapping("/listMilitary")
    public String listMilitary(Model model, HttpServletRequest request, Integer pn, Integer ps) {
        if (pn == null) pn = 1;
        if (ps == null || ps < 0 || ps > 50) ps = 20;

        Page<Military> page = new Page<>(pn, ps);
        page.setOrderBy("id desc");

        PageHelper.startPage(page);
        List<Military> military = militaryService.findAll();
        model.addAttribute("military", military);

        PageInfo pageInfo = PageInfo.fromPage(page);
        pageInfo.setRequest(request);

        model.addAttribute("pageInfo", pageInfo);
        return "listMilitary";
    }

    @RequestMapping("/check")
    @ResponseBody
    public String check(int id) {
        Military military = militaryService.findById(id);
        if (military != null) {
            military.setIsCheck(1);
            int number = militaryService.findMaxNumber() + 1;
            military.setNumber(number);

            militaryService.update(military);
            return "ok";
        }
        return "未发现数据";
    }

}
