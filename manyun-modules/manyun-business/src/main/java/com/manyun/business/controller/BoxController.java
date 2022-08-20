package com.manyun.business.controller;
import cn.hutool.core.bean.BeanUtil;
import com.manyun.business.domain.form.BoxOrderSellForm;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.query.UseAssertQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.IBoxService;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IUserBoxService;
import com.manyun.comm.api.domain.dto.BoxListDto;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private ILogsService logsService;


    @GetMapping("/queryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询盲盒完整 标题信息",notes = "返回的都是 盲盒词条完整信息 ")
    @Deprecated
    public R<List<String>> queryDict(@PathVariable String keyword){
        return R.ok(boxService.queryDict(keyword));
    }


    @PostMapping("/pageList")
    @ApiOperation("分页查询盲盒列表")
    public R<TableDataInfo<BoxListVo>>  pageList(@RequestBody BoxQuery boxQuery){
        return R.ok(boxService.pageList(boxQuery));
    }

    @GetMapping("/tarBox/{id}")
    @ApiOperation(value = "对需要抽签的盲盒,进行抽签",notes = " id = 盲盒编号  \rdata = 状态,(1=抽中,2=未抽中)")
    public R<Integer> tarBox(@PathVariable String id){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.tarBox(id,notNullLoginBusinessUser.getUserId()));
    }


    @GetMapping("/info/{id}")
    @ApiOperation("根据盲盒编号,查询盲盒的详细信息 -需登录")
    public R<BoxVo> info(@PathVariable String id){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getLoginBusinessUser();
        return R.ok(boxService.info(id, Objects.nonNull(loginBusinessUser) ? loginBusinessUser.getUserId() : null));
    }

    @GetMapping("/innerInfo/{id}")
    @ApiOperation(value = "根据盲盒编号,查询盲盒的详细信息 -需登录",hidden = true)
    @InnerAuth
    public R<BoxListDto> innerInfo(@PathVariable String id){
        BoxVo boxVo = boxService.info(id, null);
        BoxListVo boxListVo = boxVo.getBoxListVo();
        BoxListDto listDto = Builder.of(BoxListDto::new).build();
        BeanUtil.copyProperties(boxListVo, listDto );
        return R.ok(listDto);
    }

    @PostMapping("/sellBox")
    @ApiOperation("购买普通盲盒")
    @Deprecated
    public synchronized R<PayVo> sellBox(@Valid @RequestBody BoxSellForm boxSellForm){
        String userId = SecurityUtils.getBuiUserId();
        return R.ok(boxService.sellBox(boxSellForm,userId));
    }

    @PostMapping("/sellOrderBox")
    @ApiOperation(value = "购买盲盒_预先_生成订单",notes = "用来预先 生成一个待支付订单,返回订单编号,用来二次提交支付\n version 1.0.1")
    public synchronized R<String> sellOrderBox(@Valid @RequestBody BoxOrderSellForm boxOrderSellForm){
        String userId = SecurityUtils.getBuiUserId();
        return R.ok(boxService.sellOrderBox(boxOrderSellForm,userId));
    }

    @PostMapping("/userBoxPageList")
    @ApiOperation("查询用户的盲盒列表")
    public R<TableDataInfo<UserBoxVo>> userBoxPageList(@RequestBody UseAssertQuery useAssertQuery){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.userBoxPageList(useAssertQuery,loginBusinessUser.getUserId()));
    }

    @GetMapping("/openBox/{userBoxId}")
    @ApiOperation(value = "开启盲盒",notes = "盲盒编号  点击后, 返回的 data 会弹出对应的提示信息给用户即可.")
    public R<String> openBox(@PathVariable String userBoxId){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.openBox(userBoxId,notNullLoginBusinessUser.getUserId()));
    }

    @PostMapping("/logsPage")
    @ApiOperation("分页查询盲盒相关记录信息")
    public R<TableDataInfo<BoxLogPageVo>> logsPage(@RequestBody PageQuery pageQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(logsService.logsBoxPage(pageQuery,notNullLoginBusinessUser.getUserId()));
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



