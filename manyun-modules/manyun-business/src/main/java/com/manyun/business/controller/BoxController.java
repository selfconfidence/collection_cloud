package com.manyun.business.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.vo.BoxListVo;
import com.manyun.business.domain.vo.BoxVo;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.domain.vo.UserBoxVo;
import com.manyun.business.service.IBoxService;
import com.manyun.business.service.IUserBoxService;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 盲盒;盲盒主体表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/box")
@Api(tags = "盲盒相关apis")
public class BoxController extends BaseController {

    @Autowired
    private IBoxService boxService;

    @Autowired
    private IUserBoxService userBoxService;



    @GetMapping("/queryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询盲盒完整 标题信息",notes = "返回的都是 盲盒词条完整信息 ")
    public R<List<String>> queryDict(@PathVariable String keyword){
        return R.ok(boxService.queryDict(keyword));
    }


    @PostMapping("/pageList")
    @ApiOperation("分页查询盲盒列表")
    public R<TableDataInfo<BoxListVo>>  pageList(@RequestBody BoxQuery boxQuery){
        return R.ok(boxService.pageList(boxQuery));
    }


    @GetMapping("/info/{id}")
    @ApiOperation("根据盲盒编号,查询盲盒的详细信息")
    public R<BoxVo> info(@PathVariable String id){
        return R.ok(boxService.info(id));
    }

    @PostMapping("/sellBox")
    @ApiOperation("购买普通盲盒")
    public R<PayVo> sellBox(@Valid @RequestBody BoxSellForm boxSellForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.sellBox(boxSellForm,notNullLoginBusinessUser.getUserId()));
    }

    @PostMapping("/userBoxPageList")
    @ApiOperation("查询用户的盲盒列表")
    public R<TableDataInfo<UserBoxVo>> userBoxPageList(@RequestBody PageQuery pageQuery){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.userBoxPageList(pageQuery,loginBusinessUser.getUserId()));
    }

    @GetMapping("/openBox/{boxId}")
    @ApiOperation(value = "开启盲盒",notes = "盲盒编号  点击后, 返回的 data 会弹出对应的提示信息给用户即可.")
    public R<String> openBox(@PathVariable String boxId){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.openBox(boxId,notNullLoginBusinessUser.getUserId()));
    }
    
    @GetMapping("/openPleaseBox")
    @ApiOperation(value = "用户领取邀请福利,绑定盲盒", hidden = true)
    @InnerAuth
    public R openPleaseBox(@RequestBody OpenPleaseBoxDto pleaseBoxDto){
        userBoxService.bindPleaseBoxDto(pleaseBoxDto);
       return R.ok();
    }

/*
    public static void main(String[] args) {
        createUser("xx","09");
        listUser();

    }

    public static void listUser(){
        String appId = "123456789";
        String t = "111111";
        long current = System.currentTimeMillis() / 1000;
        // long current1 = DateUtil.current();
        HashMap<String, String> headParams = Maps.newHashMap();

        HttpRequest httpRequest = HttpUtil.createGet("http://study01.manyunkj.cn/user/index");
        headParams.put("x-appid",appId);
        headParams.put("x-t",String.valueOf(current));
        headParams.put("x-sign",DigestUtils.md5Hex(appId  + t  +  current));
        // 2147483647 1656149676291
        httpRequest.addHeaders(headParams);
        String body = httpRequest.execute().body();
        JSONArray jsonArray = JSON.parseObject(body).getJSONObject("data").getJSONArray("list");
        List<TempUser> tempUsers = jsonArray.toJavaList(TempUser.class);
        List<TempUser> collect = tempUsers.stream().sorted((item1,item2)->{
            System.out.println(DateUtil.parse(item1.getMonth(),"mm"));
            return   DateUtil.parse(item1.getMonth(),"mm").compareTo(DateUtil.parse(item2.getMonth(),"mm"));
                }
        ).collect(Collectors.toList());
        System.out.println(collect);
    }

    public static void createUser(String userName,String month){
        String appId = "123456789";
        String t = "111111";
        long current = System.currentTimeMillis() / 1000;
        // long current1 = DateUtil.current();
        HashMap<String, String> headParams = Maps.newHashMap();
        HashMap<String, Object> body = Maps.newHashMap();
        body.put("username",userName);
        body.put("month",month);
        //appid+appkey+timestamp
        headParams.put("x-appid",appId);
        headParams.put("x-t",String.valueOf(current));
        headParams.put("x-sign",DigestUtils.md5Hex(appId  + t  +  current));
        // 2147483647 1656149676291
        HttpRequest httpRequest = HttpUtil.createPost("http://study01.manyunkj.cn/user/create");
        httpRequest.addHeaders(headParams);
        httpRequest.form(body);
        HttpResponse execute = httpRequest.execute();
        System.out.println(execute.body());
    }

    @Data
    static class TempUser{
        private String uid;
        private String month;
        private String username;
        private Date createTime;
        private Date updateTime;

    }*/
}



