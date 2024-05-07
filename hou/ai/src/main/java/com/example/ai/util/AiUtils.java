package com.example.ai.util;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.imageprocess.AipImageProcess;
import com.baidu.aip.nlp.AipNlp;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

public class AiUtils {

    public static String img(MultipartFile file) throws IOException {

        //设置APPID/AK/SK
        String APP_ID = "65712117";
        String API_KEY = "mP9BHK5VrRO6l93VrGCiiYoi";
        String SECRET_KEY = "P5fkX7jA0Jm1voFae25SZ5nq6nCCyj2o";
        // 初始化一个AipImageClassify
        AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);

        // 调用接口
        JSONObject res = client.advancedGeneral(file.getBytes(), new HashMap<String, String>());
        System.out.println(res.toString(2));
        return res.toString(2);
    }

    public static String word(MultipartFile file) throws IOException {
        //设置APPID/AK/SK
        String APP_ID = "63741599";
        String API_KEY = "0s5GGhtRc6660cKdR2FAxyfs";
        String SECRET_KEY = "WuS0U80EZsVy6kmHuXAMSAVioWZWp4dd";
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 调用接口
        JSONObject res = client.basicGeneral(file.getBytes(), new HashMap<String, String>());
        System.out.println(res.toString(2));
        return res.toString(2);
    }

    public static String portrait(MultipartFile file) throws IOException {
        //设置APPID/AK/SK
        String APP_ID = "65736660";
        String API_KEY = "BVTPmo1kl7bVeUMwrpTclnhs";
        String SECRET_KEY = "mpV8Wjmy4NoanOFVisO30x27L0j2uqX1";
        // 初始化一个AipImageProcess
        AipImageProcess client = new AipImageProcess(APP_ID, API_KEY, SECRET_KEY);

        // 调用接口
        JSONObject res = client.selfieAnime(file.getBytes(), new HashMap<String, String>());
        System.out.println(res.toString(2));
        saveImageFromJSON(res);
        return res.toString(2);
    }

    public static String npl(String text) throws IOException {
        String APP_ID = "65740950";
        String API_KEY = "CX0HmvATYByO2c9B0N6ij4CB";
        String SECRET_KEY = "HaSz7xFPY6Rw2py1a9iRMAjdlpsVw6gQ";
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);


        // 调用接口
        JSONObject res = client.ecnet(text, null);
        String text1 =res.getJSONObject("item").getString("correct_query");
        return text1;
    }

    private static void saveImageFromJSON(JSONObject json) throws IOException {
        // 从 JSON 中获取图片数据
        String imageBase64 = json.optString("image", "");

        // 将 Base64 编码的图片数据解码为字节数组
        byte[] imageData = Base64.getDecoder().decode(imageBase64);

        // 保存图片到本地文件
        try (FileOutputStream fos = new FileOutputStream("portrait.jpg")) {
            fos.write(imageData);
        }
    }









}
