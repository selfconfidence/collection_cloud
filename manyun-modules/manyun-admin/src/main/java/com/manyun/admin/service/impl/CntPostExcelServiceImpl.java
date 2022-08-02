package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.excel.PostExcel;
import com.manyun.admin.domain.vo.CntPostExcelVo;
import com.manyun.admin.service.ICntBoxService;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.admin.service.ICntUserService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntPostExcelMapper;
import com.manyun.admin.service.ICntPostExcelService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提前购格Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-27
 */
@Service
public class CntPostExcelServiceImpl extends ServiceImpl<CntPostExcelMapper,CntPostExcel> implements ICntPostExcelService
{
    @Autowired
    private CntPostExcelMapper cntPostExcelMapper;

    @Autowired
    private ICntBoxService boxService;

    @Autowired
    private ICntCollectionService collectionService;

    @Autowired
    private ICntUserService userService;


    /**
     * 查询提前购格列表
     *
     * @param pageQuery
     * @return 提前购格
     */
    @Override
    public TableDataInfo<CntPostExcelVo> selectCntPostExcelList(PageQuery pageQuery)
    {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<CntPostExcel> cntPostExcels = cntPostExcelMapper.selectCntPostExcelList(new CntPostExcel());
        return TableDataInfoUtil.pageTableDataInfo(cntPostExcels.parallelStream().map(m->{
            CntPostExcelVo postExcelVo=new CntPostExcelVo();
            BeanUtil.copyProperties(m,postExcelVo);
            return postExcelVo;
        }).collect(Collectors.toList()),cntPostExcels);
    }

    /**
     * 批量删除提前购格
     *
     * @param ids 需要删除的提前购格主键
     * @return 结果
     */
    @Override
    public int deleteCntPostExcelByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /***
     * 获取导入的数据,并处理
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R importPostExcel(List<PostExcel> postExcelList)
    {
        if (StringUtils.isNull(postExcelList) || postExcelList.size() == 0)
        {
            R.fail("导入提前购数据不能为空!");
        }

        Optional<PostExcel> postExcel = postExcelList.stream().findFirst();
        String typeName = postExcel.get().getTypeName();
        //校验
        CntUser user = userService.getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, postExcel.get().getPhone()));
        Assert.isTrue(Objects.nonNull(user),"用户不存在!");

        CntBox box=null;
        CntCollection collection=null;
        if("1".equals(typeName)){
            box = boxService.getOne(Wrappers.<CntBox>lambdaQuery().eq(CntBox::getBoxTitle,postExcel.get().getBuiName()));
        }else if("2".equals(typeName)){
            collection = collectionService.getOne(Wrappers.<CntCollection>lambdaQuery().eq(CntCollection::getCollectionName, postExcel.get().getBuiName()));
        }else {
            return R.fail("参数类型有误!");
        }

        if(("1".equals(typeName) && Objects.nonNull(box)) || ("2".equals(typeName) && Objects.nonNull(collection))){
            CntPostExcel cntPostExcel = getOne(
                    Wrappers.<CntPostExcel>lambdaQuery()
                            .eq(CntPostExcel::getUserId, user.getUserId())
                            .eq(CntPostExcel::getBuiId,"1".equals(typeName)?box.getId():collection.getId())
                            .eq(CntPostExcel::getTypeName,typeName));
            if(Objects.isNull(cntPostExcel)){
                save(
                        Builder.of(CntPostExcel::new)
                                .with(CntPostExcel::setId, IdUtils.getSnowflakeNextIdStr())
                                .with(CntPostExcel::setUserId, user.getUserId())
                                .with(CntPostExcel::setPhone, user.getPhone())
                                .with(CntPostExcel::setBuiId, "1".equals(typeName)?box.getId():collection.getId())
                                .with(CntPostExcel::setBuiName,"1".equals(typeName)?box.getBoxTitle():collection.getCollectionName())
                                .with(CntPostExcel::setTypeName, postExcel.get().getTypeName())
                                .with(CntPostExcel::setReMark, postExcel.get().getReMark())
                                .with(CntPostExcel::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntPostExcel::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            }else {
                updateById(
                        Builder.of(CntPostExcel::new)
                                .with(CntPostExcel::setId, cntPostExcel.getId())
                                .with(CntPostExcel::setUserId, user.getUserId())
                                .with(CntPostExcel::setPhone, user.getPhone())
                                .with(CntPostExcel::setBuiId, "1".equals(typeName)?box.getId():collection.getId())
                                .with(CntPostExcel::setBuiName,"1".equals(typeName)?box.getBoxTitle():collection.getCollectionName())
                                .with(CntPostExcel::setTypeName, postExcel.get().getTypeName())
                                .with(CntPostExcel::setReMark, postExcel.get().getReMark())
                                .with(CntPostExcel::setUpdatedBy,SecurityUtils.getUsername())
                                .with(CntPostExcel::setUpdatedTime,DateUtils.getNowDate())
                                .build()
                );
            }
        }else {
            return R.fail("商品信息不存在!");
        }
        return R.ok(null,"导入提前购数据成功!");
    }

}
