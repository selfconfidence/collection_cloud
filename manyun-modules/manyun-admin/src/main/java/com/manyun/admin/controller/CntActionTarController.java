package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.dto.SaveActionTarDto;
import com.manyun.admin.domain.query.ActionTarQuery;
import com.manyun.admin.domain.vo.CntActionTarVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntActionTar;
import com.manyun.admin.service.ICntActionTarService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动合成附属信息Controller
 *
 * @author yanwei
 * @date 2022-07-21
 */
@RestController
@RequestMapping("/tar")
@Api(tags = "活动合成附属信息apis")
public class CntActionTarController extends BaseController
{
    @Autowired
    private ICntActionTarService cntActionTarService;

    /**
     * 查询活动合成附属信息列表
     */
    //@RequiresPermissions("admin:tar:list")
    @GetMapping("/list")
    @ApiOperation("查询活动合成附属信息列表")
    public TableDataInfo<CntActionTarVo> list(ActionTarQuery actionTarQuery)
    {
        startPage();
        List<CntActionTarVo> list = cntActionTarService.selectCntActionTarList(actionTarQuery);
        return getDataTable(list);
    }

    /**
     * 新增活动合成附属信息
     */
    //@RequiresPermissions("admin:tar:add")
    @Log(title = "活动合成附属信息", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增活动合成附属信息")
    public R add(@RequestBody SaveActionTarDto saveActionTarDto)
    {
        if(StringUtils.isBlank(saveActionTarDto.getActionId())){
            return R.fail("缺失必要参数!");
        }
        return toResult(cntActionTarService.insertCntActionTar(saveActionTarDto));
    }

}
