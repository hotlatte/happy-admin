package com.happy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.happy.model.Car;
import com.happy.service.ICarService;
import com.happy.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ImageUploadController {

    @Resource
    private ICarService carService;


    @RequestMapping("/car")
    @ResponseBody
    public String car(long id) {
        Car car = carService.findById(id);
        if (car != null)
            return car.getImageUrl();
        return "not found";
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.GET)
    public ModelAndView upload(ModelAndView mv) {
        return mv;
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject upload(MultipartFile image, HttpServletRequest request) {
        String dir = request.getServletContext().getRealPath("/uploads");
        Map res = new HashMap();
        res.put("success", true);
        List<String> errorMsg = new ArrayList<>(5);
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();

            //
            dirFile.setWritable(true, false);
            dirFile.setReadable(true, false);
            //
        }

        String ext = StringUtils.getFileExt(image.getOriginalFilename()).toLowerCase();
        if ("jpg".equals(ext) || "jpeg".equals(ext) || "gif".equals(ext) || "png".equals(ext)) {
            if (image.getSize() < 10 * 1024 * 1024) {
                String filename = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "." + ext;
                File target = new File(dirFile, filename);
                String path = "/uploads/" + filename;
                res.put("path", path);
                try {
                    image.transferTo(target);

                    // linux
                    target.setReadable(true, false);
                    Car car = new Car();
                    car.setImageUrl("321321321/" + path);
                    car.setBrand(request.getParameter("brand"));
                    car.setCountry(request.getParameter("country"));
                    carService.save(car);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                // 提示
                errorMsg.add("文件不能超过10M");
                res.put("success", false);
            }
        } else {
            errorMsg.add("文件类型不正确");
            res.put("success", false);
        }

        res.put("ErrorMessage", StringUtils.join(errorMsg, ","));
        return (JSONObject) JSON.toJSON(res);
    }
}
