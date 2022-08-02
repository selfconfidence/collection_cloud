package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.SaveBoxCollectionDto;
import com.manyun.admin.domain.query.BoxCollectionQuery;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
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
import com.manyun.admin.service.ICntBoxCollectionService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/boxCollection")
@Api(tags = "盲盒中的物品对象")
public class CntBoxCollectionController extends BaseController
{
    @Autowired
    private ICntBoxCollectionService cntBoxCollectionService;

    //@RequiresPermissions("admin:collection:list")
    @GetMapping("/list")
    @ApiOperation("查询盲盒中的物品对象")
    public TableDataInfo<CntBoxCollectionVo> list(BoxCollectionQuery boxCollectionQuery)
    {
        return cntBoxCollectionService.selectCntBoxCollectionList(boxCollectionQuery);
    }

    //@RequiresPermissions("admin:collection:add")
    @Log(title = "新增盲盒中的物品对象", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增盲盒中的物品对象")
    public R add(@RequestBody SaveBoxCollectionDto boxCollectionDto)
    {
        if(StringUtils.isBlank(boxCollectionDto.getBoxId())){
            return R.fail("缺失必要参数");
        }
        return toResult(cntBoxCollectionService.insertCntBoxCollection(boxCollectionDto));
    }

}
