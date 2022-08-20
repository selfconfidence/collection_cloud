package com.manyun.base.controller;

import com.manyun.base.utils.AliUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.file.FileUtils;
import com.manyun.base.service.ISysFileService;
import com.manyun.comm.api.domain.SysFile;

/**
 * 文件请求处理
 * 
 * @author ruoyi
 */
@RestController
@Api(tags = "文件上传相关apis")
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    @ApiOperation(value = "文件上传api",notes = "支持单文件上传")
    public synchronized R<String> upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            String url = AliUtil.uploadImage(file.getInputStream(),file.getOriginalFilename());
            return R.ok(url);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }
}