package com.manyun.business.controller.notify;


import com.manyun.business.domain.entity.CntUser;
import com.manyun.business.service.ICntUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里云sdk 金融版 h5对接实名认证    验证
 */
@CrossOrigin
@RestController
@RequestMapping("/notify_real")
@Slf4j
@Api(hidden = true)
public class AliRealNotifyController {

    @Autowired
    private ICntUserService cntUserService;




    @RequestMapping(value = "/real")
    @ResponseBody
    @ApiOperation(value = "阿里云sdk 金融版 h5对接实名认证回调",hidden = true)
    public String real(@RequestParam("callbackToken") String callbackToken,@RequestParam("certifyId") String certifyId,@RequestParam("passed") String passed) {
        try {
              log.info("/notify_real/real 回调成功!{},{},{}",callbackToken,certifyId,passed);
            CntUser cntUser = cntUserService.commUni(callbackToken);
            if (Objects.nonNull(cntUser))
            cntUserService.checkCertifyIdH5Status(certifyId,cntUser.getId());
          return "success";
        } catch (Exception e) {

            return "failure";
        }
    }

}
