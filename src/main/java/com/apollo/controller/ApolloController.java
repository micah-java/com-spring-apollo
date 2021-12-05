package com.apollo.controller;

import com.apollo.config.ApolloConfig;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ApolloController {
    @Autowired
    private ApolloConfig apolloConfig;

    @Autowired
    private S3Client s3Client;

    @Autowired
    S3Presigner s3Presigner;

    @RequestMapping("/apollo")
    public Map<String, String> getApolloConfig(){
        Map<String,String> map = new HashMap<>();
        map.put("host",apolloConfig.getHost());
        map.put("port",apolloConfig.getPort());
        map.put("username",apolloConfig.getUsername());
        map.put("password",apolloConfig.getPassword());
        return map;
    }

    @RequestMapping("/java")
    public Map<String, String> getApolloConfigJava(){

        Config appConfig = ConfigService.getAppConfig();
        Map<String,String> map = new HashMap<>();
        map.put("host", appConfig.getProperty("host",""));
        map.put("port", appConfig.getProperty("port",""));
        map.put("username",appConfig.getProperty("username",""));
        map.put("password",appConfig.getProperty("password",""));
        return map;
    }

    @RequestMapping("/upload")
    public Map<String,String> xxx(MultipartFile file){

        Map<String,String> map = new HashMap<>();

        String bucketName = "a06imdata";
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(file.getOriginalFilename());
        String mimeType = extensionMatch.getMimeType();
        String now = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fileName = now + "/" + file.getOriginalFilename();
        try(ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes())){
            s3Client.putObject(
                    PutObjectRequest.builder().bucket(bucketName)
                            .key(fileName)
                            .contentType(mimeType).build(),
                    RequestBody.fromInputStream(bais, bais.available())
            );

            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName)
                    .key(fileName).build();

            GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(fileName).build();
            String url1 = s3Client.utilities().getUrl(getUrlRequest).toExternalForm();


            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofDays(1))
                    .getObjectRequest(getObjectRequest).build();
            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

            String url2 = presignedGetObjectRequest.url().toString();

            map.put("url1",url1);
            map.put("url2",url2);
        }catch (Exception e){}

        return map;
    }
}
