package com.manyun.comm.api;

import com.manyun.comm.api.domain.dto.BatchSmsCommDto;
import com.manyun.comm.api.domain.dto.SmsCommDto;
import com.manyun.comm.api.factory.RemoteFileFallbackFactory;
import com.manyun.comm.api.factory.RemoteSmsFallbackFactory;
import com.manyun.common.core.constant.ServiceNameConstants;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务
 * 
 * @author ruoyi
 */
@FeignClient(contextId = "remoteSmsService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteSmsFallbackFactory.class)
public interface RemoteSmsService
{



     @PostMapping(value = "/sendCommPhone",consumes = MediaType.APPLICATION_JSON_VALUE)
     R sendCommPhone(@RequestBody SmsCommDto smsCommDto);

     @PostMapping(value = "/sendBatchCommPhone",consumes = MediaType.APPLICATION_JSON_VALUE)
     R sendBatchCommPhone(@RequestBody BatchSmsCommDto batchSmsCommDto);

}
