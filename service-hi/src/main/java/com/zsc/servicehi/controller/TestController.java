package com.zsc.servicehi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import static com.zsc.servicehi.utils.GetPollutantData.getInputStream;

@RequestMapping("/test")
@RestController
public class TestController {

//    public String findFile(@PathParam("id") Long id) throws BusinessAccessException {
//
//        String url = "http://10.110.200.157:8080/services/attachment/file/download/AttachmentDownload?id=2429";
//        ServletOutputStream outputStream = null;
//        InputStream decryptInputStream = getInputStream();
//        try{
//            httpResponse.reset();
//            outputStream = httpResponse.getOutputStream();
//            // 在http响应中输出流
//            byte[] cache = new byte[1024];
//            int nRead = 0;
//            while ((nRead = decryptInputStream.read(cache)) != -1) {
//                outputStream.write(cache, 0, nRead);
//                outputStream.flush();
//            }
//            outputStream.flush();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
}
