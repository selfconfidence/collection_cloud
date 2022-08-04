package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.dto.CntBoxAlterCombineDto;
import com.manyun.admin.domain.query.BoxQuery;
import com.manyun.admin.domain.query.OrderQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.ICntBoxCollectionService;
import com.manyun.admin.service.ICntCollectionLableService;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBoxMapper;
import com.manyun.admin.service.ICntBoxService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 盲盒;盲盒主体Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-13
 */
@Service
public class CntBoxServiceImpl extends ServiceImpl<CntBoxMapper,CntBox> implements ICntBoxService
{
    @Autowired
    private CntBoxMapper cntBoxMapper;

    @Autowired
    private ICntMediaService mediaService;

    @Autowired
    private ICntBoxCollectionService boxCollectionService;

    @Autowired
    private ICntCollectionLableService collectionLableService;

    /**
     * 查询盲盒;盲盒主体详情
     *
     * @param id 盲盒;盲盒主体主键
     * @return 盲盒;盲盒主体
     */
    @Override
    public CntBoxDetailsVo selectCntBoxById(String id)
    {
        CntBoxDetailsVo boxDetailsVo = Builder.of(CntBoxDetailsVo::new).build();
        CntBox cntBox = getById(id);
        BeanUtil.copyProperties(cntBox, boxDetailsVo);
        boxDetailsVo.setLableIds(collectionLableService.list(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, id)).stream().map(CntCollectionLable::getLableId).collect(Collectors.toList()));
        boxDetailsVo.setMediaVos(mediaService.initMediaVos(cntBox.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        return boxDetailsVo;
    }

    /**
     * 查询盲盒;盲盒主体列表
     *
     * @param boxQuery 盲盒;盲盒主体
     * @return 盲盒;盲盒主体
     */
    @Override
    public TableDataInfo<CntBoxVo> selectCntBoxList(BoxQuery boxQuery)
    {
        PageHelper.startPage(boxQuery.getPageNum(),boxQuery.getPageSize());
        List<CntBox> cntBoxList = cntBoxMapper.selectSearchBoxList(boxQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntBoxList.parallelStream().map(item -> {
                    CntBoxVo cntBoxVo = new CntBoxVo();
                    BeanUtil.copyProperties(item, cntBoxVo);
                    cntBoxVo.setMediaVos(mediaService.initMediaVos(item.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
                    return cntBoxVo;
                }).collect(Collectors.toList()),cntBoxList);
    }

    /**
     * 新增盲盒;盲盒主体
     *
     * @param
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertCntBox(CntBoxAlterCombineDto boxAlterCombineDto)
    {
        //盲盒
        String idStr = IdUtils.getSnowflakeNextIdStr();
        CntBoxAlterVo cntBoxAlterVo = boxAlterCombineDto.getCntBoxAlterVo();
        Assert.isTrue(Objects.nonNull(cntBoxAlterVo), "新增盲盒失败");
        //验证盲盒名称是否已录入
        List<CntBox> boxList = list(Wrappers.<CntBox>lambdaQuery().eq(CntBox::getBoxTitle,cntBoxAlterVo.getBoxTitle()));
        String info = StrUtil.format("盲盒名称为:{}已存在!", cntBoxAlterVo.getBoxTitle());
        Assert.isFalse(boxList.size()>0,info);
        //校验
        R check = check(cntBoxAlterVo.getPostTime(), cntBoxAlterVo.getPublishTime(),boxAlterCombineDto.getCntLableAlterVo());
        if(200!=check.getCode()){
            return R.fail(check.getCode(),check.getMsg());
        }
        CntBox cntBox = Builder.of(CntBox::new)
                .with(CntBox::setId, idStr)
                .with(CntBox::setCreatedBy, SecurityUtils.getUsername())
                .with(CntBox::setCreatedTime, DateUtils.getNowDate()).build();
        BeanUtil.copyProperties(cntBoxAlterVo, cntBox);
        cntBox.setBoxOpen(1);
        if (!save(cntBox)) {
            return R.fail();
        }
        //标签
        CntLableAlterVo cntLableAlterVo = boxAlterCombineDto.getCntLableAlterVo();
        if (Objects.nonNull(cntLableAlterVo)) {
            String[] lableIds = cntLableAlterVo.getLableIds();
            if (lableIds.length>0) {
                List<CntCollectionLable> cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
                    return Builder.of(CntCollectionLable::new)
                            .with(CntCollectionLable::setId, IdUtils.getSnowflakeNextIdStr())
                            .with(CntCollectionLable::setCollectionId, idStr)
                            .with(CntCollectionLable::setLableId, m)
                            .with(CntCollectionLable::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntCollectionLable::setCreatedTime, DateUtils.getNowDate()).build();
                }).collect(Collectors.toList());
                collectionLableService.saveBatch(cntCollectionLables);
            }
        }
        //图片
        MediaAlterVo mediaAlterVo = boxAlterCombineDto.getMediaAlterVo();
        if (Objects.nonNull(mediaAlterVo)) {
            mediaService.save(
                    Builder.of(CntMedia::new)
                            .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                            .with(CntMedia::setBuiId, idStr)
                            .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                            .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                            .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                            .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build()
            );
        }
        return R.ok();
    }

    /**
     * 修改盲盒;盲盒主体
     *
     * @param boxAlterCombineDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateCntBox(CntBoxAlterCombineDto boxAlterCombineDto)
    {
        //盲盒
        CntBoxAlterVo boxAlterVo = boxAlterCombineDto.getCntBoxAlterVo();
        Assert.isTrue(Objects.nonNull(boxAlterVo), "修改盲盒失败");
        String boxId = boxAlterVo.getId();
        Assert.isTrue(StringUtils.isNotBlank(boxId), "缺失必要参数");
        //验证盲盒名称是否已录入
        List<CntBox> boxList = list(Wrappers.<CntBox>lambdaQuery().eq(CntBox::getBoxTitle,boxAlterVo.getBoxTitle()).ne(CntBox::getId,boxId));
        String info = StrUtil.format("盲盒名称为:{}已存在!", boxAlterVo.getBoxTitle());
        Assert.isFalse(boxList.size()>0,info);
        //校验
        R check = check(boxAlterVo.getPostTime(), boxAlterVo.getPublishTime(),boxAlterCombineDto.getCntLableAlterVo());
        if(200!=check.getCode()){
            return R.fail(check.getCode(),check.getMsg());
        }
        CntBox cntBox = new CntBox();
        BeanUtil.copyProperties(boxAlterVo, cntBox);
        cntBox.setUpdatedBy(SecurityUtils.getUsername());
        cntBox.setUpdatedTime(DateUtils.getNowDate());
        if (!updateById(cntBox)) {
            return R.fail();
        }
        //标签
        CntLableAlterVo cntLableAlterVo = boxAlterCombineDto.getCntLableAlterVo();
        String[] lableIds = cntLableAlterVo.getLableIds();
        if (Objects.nonNull(cntLableAlterVo)) {
            if (lableIds.length>0) {
                collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, boxId));
                List<CntCollectionLable> cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
                    return Builder.of(CntCollectionLable::new)
                            .with(CntCollectionLable::setId, IdUtils.getSnowflakeNextIdStr())
                            .with(CntCollectionLable::setCollectionId, boxId)
                            .with(CntCollectionLable::setLableId, m)
                            .with(CntCollectionLable::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntCollectionLable::setCreatedTime, DateUtils.getNowDate()).build();
                }).collect(Collectors.toList());
                collectionLableService.saveBatch(cntCollectionLables);
            } else {
                collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, boxId));
            }
        }
        //图片
        MediaAlterVo mediaAlterVo = boxAlterCombineDto.getMediaAlterVo();
        if (Objects.nonNull(mediaAlterVo)) {
            List<MediaVo> mediaVos = mediaService.initMediaVos(boxId, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
            if (mediaVos.size() == 0) {
                mediaService.save(
                        Builder.of(CntMedia::new)
                                .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())

                                .with(CntMedia::setBuiId, boxId)
                                .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                                .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                                .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE.toString())
                                .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                                .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build()
                );
            } else {
                mediaService.updateById(
                        Builder.of(CntMedia::new)
                                .with(CntMedia::setId, mediaVos.get(0).getId())
                                .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                                .with(CntMedia::setUpdatedBy, SecurityUtils.getUsername())
                                .with(CntMedia::setUpdatedTime, DateUtils.getNowDate())
                                .build()
                );
            }
        }
        return R.ok();
    }

    public R check(Integer postTime, Date publishTime, CntLableAlterVo lableAlterVo){
        //验证提前购分钟是否在范围内
        if(postTime!=null){
            if(postTime<10 || postTime>1000){
                return R.fail("提前购时间请输入大于等于10,小于1000的整数!");
            }
        }
        //验证发售时间是否小于当前时间
        //比较两个时间大小，前者大 = -1， 相等 =0，后者大 = 1
        if(publishTime!=null){
            if (DateUtils.compareTo(new Date(), publishTime, DateUtils.YYYY_MM_DD_HH_MM_SS) == -1) {
                return R.fail("发售时间不能小于当前时间!");
            }
        }
        //验证标签是否超过三个
        if(Objects.nonNull(lableAlterVo)){
            if(lableAlterVo.getLableIds().length>3){
                return R.fail("藏品标签最多可选中三个!");
            }
        }
        return R.ok();
    }

    /**
     * 批量删除盲盒;盲盒主体
     *
     * @param ids 需要删除的盲盒;盲盒主体主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCntBoxByIds(String[] ids)
    {
        if (ids.length == 0) {
            return 0;
        }
        if (!removeByIds(Arrays.asList(ids))) {
            return 0;
        } else {
            collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().in(CntCollectionLable::getCollectionId, ids));
            mediaService.remove(Wrappers.<CntMedia>lambdaQuery().in(CntMedia::getBuiId, ids).eq(CntMedia::getModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            boxCollectionService.remove(Wrappers.<CntBoxCollection>lambdaQuery().in(CntBoxCollection::getBoxId, ids));
        }
        return 1;
    }

    /**
     * 查询盲盒订单列表
     */
    @Override
    public List<CntBoxOrderVo> boxOrderList(OrderQuery orderQuery)
    {
        return cntBoxMapper.boxOrderList(orderQuery);
    }

}
