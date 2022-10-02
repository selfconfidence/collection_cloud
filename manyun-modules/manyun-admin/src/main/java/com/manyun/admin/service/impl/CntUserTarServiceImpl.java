package com.manyun.admin.service.impl;

import java.nio.file.Watchable;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.excel.UserTarExcel;
import com.manyun.admin.domain.query.UserTarQuery;
import com.manyun.admin.domain.vo.CntUserTarVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.service.*;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntUserTarMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户抽签购买藏品或盲盒中间Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-08
 */
@Service
@Slf4j
public class CntUserTarServiceImpl extends ServiceImpl<CntUserTarMapper,CntUserTar> implements ICntUserTarService
{
    @Autowired
    private CntUserTarMapper cntUserTarMapper;

    @Autowired
    private ICntCollectionService collectionService;

    @Autowired
    private ICntBoxService boxService;

    @Autowired
    private ICntUserService userService;

    @Autowired
    private ICntTarService tarService;

    /**
     * 查询用户抽签购买藏品或盲盒中间列表
     *
     * @param userTarQuery
     * @return 用户抽签购买藏品或盲盒中间
     */
    @Override
    public TableDataInfo<CntUserTarVo> selectCntUserTarList(UserTarQuery userTarQuery)
    {
        PageHelper.startPage(userTarQuery.getPageNum(),userTarQuery.getPageSize());
        List<CntUserTarVo> cntUserTarVoList = cntUserTarMapper.selectCntUserTarList(userTarQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntUserTarVoList, cntUserTarVoList);
    }


    /**
     * 批量删除用户抽签购买藏品或盲盒中间
     *
     * @param ids 需要删除的用户抽签购买藏品或盲盒中间主键
     * @return 结果
     */
    @Override
    public int deleteCntUserTarByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }


    /**
     * 导入抽签记录
     * @param userTarExcelList
     * @return
     */
    @Override
    @Transactional
    public R importPostExcel(List<UserTarExcel> userTarExcelList) {
        log.info("导入表格条数-------------------------" + userTarExcelList.size());
        List<UserTarExcel> userTarExcels = userTarExcelList.parallelStream().filter(f -> (StringUtils.isNotBlank(f.getTarId()) && StringUtils.isNotBlank(f.getUserId()) && f.getIsFull() != null)).collect(Collectors.toList());
        log.info("过滤后条数---------------" + userTarExcels);
        if (StringUtils.isNull(userTarExcelList) || userTarExcelList.size() == 0 || userTarExcels.size() == 0)
        {
           return R.fail("导入数据为空或导入数据有误!");
        }
        List<CntUserTar> insertList = new ArrayList<CntUserTar>();
        List<CntUserTar> updateList = new ArrayList<CntUserTar>();
        List<String> awardUserIds = new ArrayList<>();
        Set<String> tarIds = userTarExcels.parallelStream().map(UserTarExcel::getTarId).collect(Collectors.toSet());
        Assert.isTrue(tarIds.size()==1,"所选抽签编号不一致!");
        CntTar cntTar = tarService.getById(tarIds.parallelStream().findFirst().get());
        Assert.isTrue(Objects.nonNull(cntTar),"未查询到抽签配置信息!");
        Assert.isTrue(cntTar.getEndFlag()==1,"改抽签配置已开奖!");
        Assert.isTrue((cntTar.getTarType()==1 || cntTar.getTarType()==2),"抽签配置信息有误!");
        CntBox cntBox = boxService.getOne(Wrappers.<CntBox>lambdaQuery().eq(CntBox::getTarId, tarIds.parallelStream().findFirst().get()));
        CntCollection cntCollection = collectionService.getOne(Wrappers.<CntCollection>lambdaQuery().eq(CntCollection::getTarId, tarIds.parallelStream().findFirst().get()));
        Assert.isTrue(cntTar.getTarType()==1?Objects.nonNull(cntBox):Objects.nonNull(cntCollection),"藏品或盲盒不存在!");
        List<CntUserTar> userTars = list();
        List<String> userIds = userService.list(Wrappers.<CntUser>lambdaQuery().in(CntUser::getId, userTarExcels.parallelStream().map(UserTarExcel::getUserId).collect(Collectors.toList()))).parallelStream().map(CntUser::getId).collect(Collectors.toList());
        userTarExcels.parallelStream().forEach(e -> {
            Optional<CntUserTar> optional = userTars.parallelStream().filter(ff -> (e.getTarId().equals(ff.getTarId()) && e.getUserId().equals(ff.getUserId()))).findFirst();
            Optional<String> optional1 = userIds.parallelStream().filter(ff -> e.getUserId().equals(ff)).findFirst();
            if(optional1.isPresent()){
                if(optional.isPresent()){
                    updateList.add(
                            Builder.of(CntUserTar::new)
                                    .with(CntUserTar::setId,optional.get().getId())
                                    .with(CntUserTar::setTarId, e.getTarId())
                                    .with(CntUserTar::setUserId, e.getUserId())
                                    .with(CntUserTar::setBuiId,cntTar.getTarType()==1?cntBox.getId():cntCollection.getId())
                                    .with(CntUserTar::setIsFull,e.getIsFull())
                                    .with(CntUserTar::setUpdatedBy, SecurityUtils.getUsername())
                                    .with(CntUserTar::setUpdatedTime, DateUtils.getNowDate()).build()
                    );
                    if(e.getIsFull()==1)awardUserIds.add(e.getUserId());
                }else {
                    insertList.add(
                            Builder.of(CntUserTar::new)
                                    .with(CntUserTar::setId, IdUtils.getSnowflakeNextIdStr())
                                    .with(CntUserTar::setTarId, e.getTarId())
                                    .with(CntUserTar::setUserId, e.getUserId())
                                    .with(CntUserTar::setBuiId,cntTar.getTarType()==1?cntBox.getId():cntCollection.getId())
                                    .with(CntUserTar::setIsFull,e.getIsFull())
                                    .with(CntUserTar::setCreatedBy, SecurityUtils.getUsername())
                                    .with(CntUserTar::setCreatedTime, DateUtils.getNowDate())
                                    .build()
                    );
                    if(e.getIsFull()==1)awardUserIds.add(e.getUserId());
                }
            }
        });


        Integer balance = cntTar.getTarType()==1?cntBox.getBalance():cntCollection.getBalance();
        Assert.isTrue(balance>=awardUserIds.size(),"商品库存不足!");
        log.info("更新记录条数---------------" + updateList.size());
        log.info("新增记录条数---------------" + insertList.size());
        log.info("记录总条数-----------------" + awardUserIds.size());
        if(updateList.size()>0){
            updateBatchById(updateList);
        }
        if(insertList.size()>0){
            saveBatch(insertList);
        }
        return R.ok("导入抽签记录数据成功!");
    }
}
