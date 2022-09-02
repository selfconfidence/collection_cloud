package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.SavePostExistDto;
import com.manyun.admin.domain.query.PostExistQuery;
import com.manyun.admin.domain.vo.CntPostExistVo;
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
import com.manyun.admin.service.ICntPostExistService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置已经拥有Controller
 *
 * @author yanwei
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/exist")
@Api(tags = "提前购配置已经拥有apis")
public class CntPostExistController extends BaseController
{
    @Autowired
    private ICntPostExistService cntPostExistService;

    /**
     * 查询提前购配置已经拥有列表
     */
    //@RequiresPermissions("admin:exist:list")
    @GetMapping("/list")
    @ApiOperation("查询提前购配置已经拥有列表")
    public TableDataInfo<CntPostExistVo> list(PostExistQuery postExistQuery)
    {
        return cntPostExistService.selectCntPostExistList(postExistQuery);
    }

    /**
     * 新增提前购配置已经拥有
     */
    //@RequiresPermissions("admin:exist:add")
    @Log(title = "提前购配置已经拥有", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增提前购配置已经拥有")
    public R add(@RequestBody SavePostExistDto savePostExistDto)
    {
        if(StringUtils.isBlank(savePostExistDto.getConfigId())){
            return R.fail("缺失必要参数!");
        }
        return toResult(cntPostExistService.insertCntPostExist(savePostExistDto));
    }

}
