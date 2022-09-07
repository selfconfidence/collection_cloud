package com.manyun.admin.controller;

import java.util.List;

import cn.hutool.db.Page;
import com.manyun.admin.domain.dto.SaveActionCollectionDto;
import com.manyun.admin.domain.query.ActionCollectionQuery;
import com.manyun.admin.domain.vo.ActionCollectionVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.web.page.PageQuery;
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
import com.manyun.admin.domain.CntActionCollection;
import com.manyun.admin.service.ICntActionCollectionService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动合成目标藏品Controller
 *
 * @author yanwei
 * @date 2022-09-06
 */
@RestController
@RequestMapping("/actionCollection")
@Api(tags = "活动合成目标藏品apis")
public class CntActionCollectionController extends BaseController
{
    @Autowired
    private ICntActionCollectionService cntActionCollectionService;

    /**
     * 查询活动合成目标藏品列表
     */
    //@RequiresPermissions("admin:collection:list")
    @GetMapping("/list")
    @ApiOperation("查询活动合成目标藏品列表")
    public TableDataInfo<ActionCollectionVo> list(ActionCollectionQuery actionCollectionQuery)
    {
        return cntActionCollectionService.selectCntActionCollectionList(actionCollectionQuery);
    }

    /**
     * 新增活动合成目标藏品
     */
    //@RequiresPermissions("admin:collection:add")
    @PostMapping
    @ApiOperation("新增活动合成目标藏品")
    public R add(@RequestBody SaveActionCollectionDto saveActionCollectionDto)
    {
        if(StringUtils.isBlank(saveActionCollectionDto.getActionId()) || saveActionCollectionDto.getActionId()==null){
            return R.fail("缺失必要参数");
        }
        return toResult(cntActionCollectionService.insertCntActionCollection(saveActionCollectionDto));
    }

}
