package com.manyun.admin.controller;

import com.manyun.admin.domain.query.BoxScoreQuery;
import com.manyun.admin.domain.vo.CntBoxScoreVo;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.admin.domain.CntBoxScore;
import com.manyun.admin.service.ICntBoxScoreService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 盲盒评分Controller
 *
 * @author yanwei
 * @date 2022-08-19
 */
@RestController
@RequestMapping("/score")
@Api(tags = "盲盒评分apis")
public class CntBoxScoreController extends BaseController
{
    @Autowired
    private ICntBoxScoreService cntBoxScoreService;

    /**
     * 查询盲盒评分列表
     */
    //@RequiresPermissions("admin:score:list")
    @GetMapping("/list")
    @ApiOperation("查询盲盒评分列表")
    public TableDataInfo<CntBoxScoreVo> list(BoxScoreQuery boxScoreQuery)
    {
        return cntBoxScoreService.selectCntBoxScoreList(boxScoreQuery);
    }

    /**
     * 获取盲盒评分详细信息
     */
    //@RequiresPermissions("admin:score:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取盲盒评分详细信息")
    public R<CntBoxScore> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntBoxScoreService.selectCntBoxScoreById(id));
    }

    /**
     * 新增盲盒评分
     */
    //@RequiresPermissions("admin:score:add")
    @Log(title = "盲盒评分", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增盲盒评分")
    public R add(@RequestBody CntBoxScore cntBoxScore)
    {
        return toResult(cntBoxScoreService.insertCntBoxScore(cntBoxScore));
    }

    /**
     * 修改盲盒评分
     */
    //@RequiresPermissions("admin:score:edit")
    @Log(title = "盲盒评分", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改盲盒评分")
    public R edit(@RequestBody CntBoxScore cntBoxScore)
    {
        return toResult(cntBoxScoreService.updateCntBoxScore(cntBoxScore));
    }

    /**
     * 删除盲盒评分
     */
    //@RequiresPermissions("admin:score:remove")
    @Log(title = "盲盒评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除盲盒评分")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntBoxScoreService.deleteCntBoxScoreByIds(ids));
    }
}
