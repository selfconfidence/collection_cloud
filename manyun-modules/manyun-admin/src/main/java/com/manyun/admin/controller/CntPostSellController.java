package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.SavePostSellDto;
import com.manyun.admin.domain.query.PostSellQuery;
import com.manyun.admin.domain.vo.CntPostSellVo;
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
import com.manyun.admin.service.ICntPostSellService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置可以购买Controller
 *
 * @author yanwei
 * @date 2022-08-19
 */
@RestController
@RequestMapping("/sell")
@Api(tags = "提前购配置可以购买apis")
public class CntPostSellController extends BaseController
{
    @Autowired
    private ICntPostSellService cntPostSellService;

    /**
     * 查询提前购配置可以购买列表
     */
    //@RequiresPermissions("admin:sell:list")
    @GetMapping("/list")
    @ApiOperation("查询提前购配置可以购买列表")
    public TableDataInfo<CntPostSellVo> list(PostSellQuery postSellQuery)
    {
        return cntPostSellService.selectCntPostSellList(postSellQuery);
    }

    /**
     * 新增提前购配置可以购买
     */
    //@RequiresPermissions("admin:sell:add")
    @Log(title = "提前购配置可以购买", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增提前购配置可以购买")
    public R add(@RequestBody SavePostSellDto savePostSellDto)
    {
        if(StringUtils.isBlank(savePostSellDto.getConfigId())){
            return R.fail("缺失必要参数!");
        }
        return toResult(cntPostSellService.insertCntPostSell(savePostSellDto));
    }

}
