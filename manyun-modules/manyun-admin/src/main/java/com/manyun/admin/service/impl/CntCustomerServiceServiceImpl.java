package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.vo.CntCustomerServiceVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntCustomerServiceMapper;
import com.manyun.admin.domain.CntCustomerService;
import com.manyun.admin.service.ICntCustomerServiceService;

/**
 * 客服Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-21
 */
@Service
public class CntCustomerServiceServiceImpl extends ServiceImpl<CntCustomerServiceMapper,CntCustomerService> implements ICntCustomerServiceService
{
    @Autowired
    private CntCustomerServiceMapper cntCustomerServiceMapper;

    /**
     * 查询客服详情
     *
     * @param menuId 客服主键
     * @return 客服
     */
    @Override
    public CntCustomerService selectCntCustomerServiceByMenuId(Long menuId)
    {
        return getById(menuId);
    }

    /**
     * 查询客服列表
     *
     * @param pageQuery
     * @return 客服
     */
    @Override
    public TableDataInfo<CntCustomerServiceVo> selectCntCustomerServiceList(PageQuery pageQuery)
    {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<CntCustomerService> cntCustomerServices = cntCustomerServiceMapper.selectCntCustomerServiceList(new CntCustomerService());
        return TableDataInfoUtil.pageTableDataInfo(cntCustomerServices.parallelStream().map(m ->{
            CntCustomerServiceVo cntCustomerServiceVo=new CntCustomerServiceVo();
            BeanUtil.copyProperties(m,cntCustomerServiceVo);
            if(m.getParentId()==0){
                cntCustomerServiceVo.setParentName("父菜单");
            }else {
                Optional<CntCustomerService> optional = cntCustomerServiceMapper.selectCntCustomerServiceList(new CntCustomerService()).parallelStream().filter(f -> f.getId().equals(m.getParentId())).findFirst();
                if(optional.isPresent()){
                    cntCustomerServiceVo.setParentName(optional.get().getMenuName());
                }
            }
            return cntCustomerServiceVo;
        }).collect(Collectors.toList()),cntCustomerServices);
    }

    /**
     * 新增客服
     *
     * @param cntCustomerService 客服
     * @return 结果
     */
    @Override
    public int insertCntCustomerService(CntCustomerService cntCustomerService)
    {
        cntCustomerService.setCreateBy(SecurityUtils.getUsername());
        cntCustomerService.setCreateTime(DateUtils.getNowDate());
        return save(cntCustomerService)==true?1:0;
    }

    /**
     * 修改客服
     *
     * @param cntCustomerService 客服
     * @return 结果
     */
    @Override
    public int updateCntCustomerService(CntCustomerService cntCustomerService)
    {
        cntCustomerService.setUpdateBy(SecurityUtils.getUsername());
        cntCustomerService.setUpdateTime(DateUtils.getNowDate());
        return updateById(cntCustomerService)==true?1:0;
    }

    /**
     * 批量删除客服
     *
     * @param menuIds 需要删除的客服主键集
     * @return 结果
     */
    @Override
    public R deleteCntCustomerServiceByMenuIds(Long[] menuIds)
    {
        List<CntCustomerService> list = list(Wrappers.<CntCustomerService>lambdaQuery().in(CntCustomerService::getParentId, menuIds));
        if(list.size()>0){
            return R.fail("菜单id为: "+ StringUtils.join(list.stream().map(CntCustomerService::getParentId).distinct().collect(Collectors.toList()), ",") + " 有子菜单,请先删除子菜单!");
        }
        return remove(Wrappers.<CntCustomerService>lambdaQuery().in(CntCustomerService::getId,menuIds))==true?R.ok():R.fail();
    }

}
