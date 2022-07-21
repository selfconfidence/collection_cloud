package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.vo.CntCustomerServiceVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.StringUtils;
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
public class CntCustomerServiceServiceImpl implements ICntCustomerServiceService
{
    @Autowired
    private CntCustomerServiceMapper cntCustomerServiceMapper;

    /**
     * 查询客服
     *
     * @param menuId 客服主键
     * @return 客服
     */
    @Override
    public CntCustomerService selectCntCustomerServiceByMenuId(Long menuId)
    {
        return cntCustomerServiceMapper.selectCntCustomerServiceByMenuId(menuId);
    }

    /**
     * 查询客服列表
     *
     * @param cntCustomerService 客服
     * @return 客服
     */
    @Override
    public List<CntCustomerServiceVo> selectCntCustomerServiceList(CntCustomerService cntCustomerService)
    {
        List<CntCustomerService> cntCustomerServices = cntCustomerServiceMapper.selectCntCustomerServiceList(cntCustomerService);
        return cntCustomerServices.stream().map(m ->{
            CntCustomerServiceVo cntCustomerServiceVo=new CntCustomerServiceVo();
            BeanUtil.copyProperties(m,cntCustomerServiceVo);
            if(m.getParentId()==0){
                cntCustomerServiceVo.setParentName("父菜单");
            }else {
                Optional<CntCustomerService> optional = cntCustomerServices.stream().filter(f -> f.getMenuId()==m.getParentId()).findFirst();
                if(optional.isPresent()){
                    cntCustomerServiceVo.setParentName(optional.get().getMenuName());
                }
            }
            return cntCustomerServiceVo;
        }).collect(Collectors.toList());
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
        return cntCustomerServiceMapper.insertCntCustomerService(cntCustomerService);
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
        return cntCustomerServiceMapper.updateCntCustomerService(cntCustomerService);
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
        List<Long> list = cntCustomerServiceMapper.selectParentIdByMenuIds(menuIds);
        if(list.size()>0){
            return R.fail("菜单id为: "+ StringUtils.join(list,",") + " 有子菜单,请先删除子菜单!");
        }
        return cntCustomerServiceMapper.deleteCntCustomerServiceByMenuIds(menuIds)==0?R.fail():R.ok();
    }

}
