package com.manyun.base.utils;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.manyun.base.config.AliOssConfig;
import com.manyun.base.config.AliSmsConfig;
import com.manyun.common.core.utils.SpringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;


@Slf4j
public class AliUtil {

//
//    private static final String accessKeyId = "LTAI5t8m7GGthpTmbyhMW4MU";
//    private static final String secret = "wOjfyavkD4GUZjwtZAaDMGjxeJxdXg";
//    private static final String signName = "数位链盟";
//    private static final String folderUser = "user/images";



    /**
     * 发送验证码
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public static boolean sendSms(String phoneNumber, String ...code) {

        return send(phoneNumber,   SpringUtils.getBean(AliSmsConfig.class).getTemplateCode(),code);
    }

    /**
     * 发送通知
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public static boolean sendNotice(String phoneNumber, String ...code) {
        return send(phoneNumber,   SpringUtils.getBean(AliSmsConfig.class).getTemplateCode(),code);
    }
    private static boolean send(String phoneNumber, String templateCode, String ...code) {
        DefaultProfile profile = DefaultProfile.getProfile(SpringUtils.getBean(AliSmsConfig.class).getRegionId(),  SpringUtils.getBean(AliSmsConfig.class).getAccessKey(),  SpringUtils.getBean(AliSmsConfig.class).getSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", SpringUtils.getBean(AliSmsConfig.class).getRegionId());
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName",  SpringUtils.getBean(AliSmsConfig.class).getSing());
        request.putQueryParameter("TemplateCode", templateCode);
        JSONObject templateParam = new JSONObject();
        if (code.length == 1){
            templateParam.put("code", code[0]);
        }/*else{
            templateParam.put("vipName", code[0]);
            templateParam.put("timeNode", code[1]);
            templateParam.put("subTime", code[2]);
        }
*/
        request.putQueryParameter("TemplateParam", templateParam.toJSONString());
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject responseJSON = JSONObject.parseObject(response.getData(),JSONObject.class);
            if ("OK".equals(responseJSON.getString("Message"))) {
                return true;
            }
            log.error("【阿里云短信】:发送短信失败，手机号：{},验证码：{},错误信息：{}", phoneNumber, code, responseJSON.getString("Message"));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        //存储空间
        if (!ossClient.doesBucketExist(bucketName)) {

            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            // 设置bucket权限为公共读，默认是私有读写
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            // 设置bucket存储类型为低频访问类型，默认是标准类型
            createBucketRequest.setStorageClass(StorageClass.IA);
            ossClient.createBucket(createBucketRequest);
            log.info("创建存储空间成功");
            return createBucketRequest.getBucketName();
        }

        return bucketName;
    }

    /**
     * 创建模拟文件夹
     *
     * @param ossClient oss连接
     * @param folder    模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public static String createFolder(OSSClient ossClient, String folder) {
        //文件夹名
        final String keySuffixWithSlash = folder;
        //判断文件夹是否存在，不存在则创建
        if (!ossClient.doesObjectExist(SpringUtils.getBean(AliOssConfig.class).getBucketName(), keySuffixWithSlash)) {
            //创建文件夹
            ossClient.putObject(SpringUtils.getBean(AliOssConfig.class).getBucketName(), keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            log.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(SpringUtils.getBean(AliOssConfig.class).getBucketName(), keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }
/*

    */
/**
     * 根据key删除OSS服务器上的文件
     *
     * @param // 模拟文件夹名 如"qj_nanjing/"
     * @param // Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     *//*

    public boolean deleteFile(String folder, String key) {
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(prop.getOssEndPoint(), prop.getOssKeyId(), prop.getOssSecret());
            ossClient.deleteObject(prop.getOssBucketName(), folder + key);
            log.info("删除[{}]下的文件[{}{}]成功...", prop.getOssBucketName(), folder, key);
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (com.aliyun.oss.ClientException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return true;
    }

    public String getImgUrl(String folder, String key) {
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        String url = "";
        if (prop.isMappingDomain()) {
            url = "https://" + prop.getOssEndPoint() + "/" + folder + key;
        } else {
            url = "https://" + prop.getOssBucketName() + "." + prop.getOssEndPoint() + "/" + folder + key;
        }
        return url;
    }

    public AliyunOSSResult isFileExists(String folder, String key) {
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        OSSClient ossClient = null;
        try {
            ossClient = new OSSClient(prop.getOssEndPoint(), prop.getOssKeyId(), prop.getOssSecret());
            if (ossClient.doesObjectExist(prop.getOssBucketName(), folder + key)) {
                String url = "";
                if (prop.isMappingDomain()) {
                    url = "https://" + prop.getOssEndPoint() + "/" + folder + key;
                } else {
                    url = "https://" + prop.getOssBucketName() + "." + prop.getOssEndPoint() + "/" + folder + key;
                }
                log.info("key [{}{}] exists.", folder, key);
                return new AliyunOSSResult(key, key, url, "");
            }
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (com.aliyun.oss.ClientException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return null;
    }
*/

    public static String uploadImage(InputStream inputStream,String fileName){
        return uploadObject(inputStream, SpringUtils.getBean(AliOssConfig.class).getFileName(),fileName,Boolean.TRUE,0);
    }
    /**
     * 上传图片至OSS
     *
     * @param is       上传数据流
     * @param folder   模拟文件夹名 如"img/ or head/ ...."
     * @param fileName 保存文件名
     * @param encode   是否对文件名进行加密
     * @param expire   0 代表永不过期
     * @return
     */
    private static String uploadObject(InputStream is, String folder, String fileName,boolean encode, int expire) {
        log.debug("要上传的文体名称：" + fileName);
        OSSClient ossClient = null;
        try {
            if (!folder.endsWith("/")) {
                folder += "/";
            }
            ossClient = new OSSClient(SpringUtils.getBean(AliOssConfig.class).getEndpoint(),  SpringUtils.getBean(AliOssConfig.class).getAccessKey(), SpringUtils.getBean(AliOssConfig.class).getSecret());
            createBucketName(ossClient,  SpringUtils.getBean(AliOssConfig.class).getBucketName());
            createFolder(ossClient, folder);
            String key = null;
            if (encode) {
                key = UUID.randomUUID().toString().replaceAll("-", "");
            } else {
                key = fileName;
            }

            //以输入流的形式上传文件
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");

            if (expire != 0) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.SECOND, c.get(Calendar.SECOND) + expire + 60 * 60 * 8);
                // 设置缓存过期时间，格式是格林威治时间（GMT）。
                metadata.setExpirationTime(c.getTime());
            }

            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 指定上传的内容类型。内容类型决定浏览器将以什么形式、什么编码读取文件。如果没有指定则根据文件的扩展名生成，如果没有扩展名则为默认值application/octet-stream。
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename=" + fileName);
            //上传文件   (上传文件流的形式)

            PutObjectResult putResult = ossClient.putObject(SpringUtils.getBean(AliOssConfig.class).getBucketName(), folder + key, is, metadata);

            String hash = putResult.getETag();
            String url = "";
                url = "https://" + SpringUtils.getBean(AliOssConfig.class).getBucketName()+".oss-cn-chengdu.aliyuncs.com" + "/" + folder + key;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }




/*
    public static ArrayList<String> GetFileAllContent(String FileName)
    {
        String url ="https://guns-shop.oss-cn-beijing.aliyuncs.com/";
        OSSClient ossClient = new OSSClient("https://oss-cn-beijing.aliyuncs.com", accessKeyId, secret);

        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest("guns-shop");
        //Delimiter 设置为 “/” 时，罗列该文件夹下的文件
        listObjectsRequest.setDelimiter("/");
        //Prefix 设为某个文件夹名，罗列以此 Prefix 开头的文件
        listObjectsRequest.setPrefix(FileName);

        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        ArrayList<String> list = new ArrayList<String>();
        // 遍历所有Object:目录下的文件
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary);
            String key = objectSummary.getKey();
            list.add(url.concat(URLUtil.encode(key)));
        }
        ossClient.shutdown();
        return list;
    }
*/

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        /**
         * "video/mp4",
         *           "video/ogg",
         *           "video/flv",
         *           "video/avi",
         *           "video/wmv",
         *           "video/rmvb",
         *           "video/mov",
         */
        if (".mp4".equalsIgnoreCase(fileExtension) || ".ogg".equalsIgnoreCase(fileExtension) || ".flv".equalsIgnoreCase(fileExtension) || ".avi".equals(fileExtension) || ".wmv".equals(fileExtension) || ".rmvb".equalsIgnoreCase(fileExtension) || ".mov".equalsIgnoreCase(fileExtension)){
            return "video/mp4";
        }

            if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }

/*
    @SneakyThrows
    public static void main(String[] args) {
      // sendNotice("15036596617", "123456");
        //System.out.println(uploadImage(new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\实用工具\\纯净系统基地公众号.jpg")), "纯净系统基地公众号.jpg"));
    }*/
}
