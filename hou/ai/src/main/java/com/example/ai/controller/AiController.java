package com.example.ai.controller;

import com.example.ai.util.AiUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static java.lang.System.out;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class AiController {

    @RequestMapping(value = "/img",method = RequestMethod.POST)
    public String pic(@RequestParam("file")MultipartFile file) throws IOException {
        System.out.println("file::");
        System.out.println(file);
        return AiUtils.img(file);
    }


    @RequestMapping(value = "/word",method = RequestMethod.POST)
    public String img(@RequestParam("file")MultipartFile file) throws IOException {

        return AiUtils.word(file);
    }

    @RequestMapping(value = "/portrait",method = RequestMethod.POST)
    public String portrait(@RequestParam("file")MultipartFile file) throws IOException {

        return AiUtils.portrait(file);
    }
    @RequestMapping(value = "/npl",method = RequestMethod.POST)
    public String npl(@RequestParam("text") String text) throws IOException {

        return AiUtils.npl(text);
    }
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {
            // 指定文件保存路径，这里保存在项目根目录的 uploads 文件夹中
            String filePath = new File("").getAbsolutePath() + "/uploads/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return "File uploaded successfully: " + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file";
        }
    }



}
