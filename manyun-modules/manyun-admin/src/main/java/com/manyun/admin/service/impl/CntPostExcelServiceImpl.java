package com.manyun.admin.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.excel.PostExcel;
import com.manyun.admin.domain.query.PostExcelQuery;
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
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntPostExcelMapper;
import com.manyun.admin.service.ICntPostExcelService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Size;

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
     * @param postExcelQuery
     * @return 提前购格
     */
    @Override
    public TableDataInfo<CntPostExcelVo> selectCntPostExcelList(PostExcelQuery postExcelQuery)
    {
        PageHelper.startPage(postExcelQuery.getPageNum(),postExcelQuery.getPageSize());
        List<CntPostExcel> cntPostExcels = cntPostExcelMapper.selectCntPostExcelList(postExcelQuery);
        List<CntUser> userList = userService.list();
        return TableDataInfoUtil.pageTableDataInfo(cntPostExcels.parallelStream().map(m->{
            Optional<CntUser> user = userList.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
            CntPostExcelVo postExcelVo=new CntPostExcelVo();
            BeanUtil.copyProperties(m,postExcelVo);
            if(user.isPresent()){
                postExcelVo.setNickName(user.get().getNickName());
            }
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

    public static void main(String[] args) {
        List<PostExcel> postExcels = new ArrayList<>();
        PostExcel postExcel=new PostExcel();
        postExcel.setPhone("15036380340");
        postExcel.setBuiName("123321");
        postExcel.setTypeName("1");
        PostExcel postExcel1=new PostExcel();
        postExcel1.setPhone("15036380340");
        postExcel1.setBuiName("123321");
        postExcel1.setTypeName("1");
        PostExcel postExcel2=new PostExcel();
        postExcel2.setPhone("15036380340");
        postExcel2.setBuiName("123321");
        postExcel2.setTypeName("1");
        //postExcels.add(postExcel);
        postExcels.add(postExcel1);
        postExcels.add(postExcel2);
        ArrayList<PostExcel> collect = postExcels.parallelStream().collect(
                Collectors
                        .collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(
                                        Comparator.comparing(o -> o.getPhone() + ";" + o.getBuiName() + ";" + o.getTypeName()))), ArrayList::new
                        )
        );
        System.out.println(collect.size());
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
           return R.fail("导入提前购数据不能为空!");
        }

        List<PostExcel> postExcels = postExcelList.parallelStream().filter(f -> !f.getTypeName().equals("1") && !f.getTypeName().equals("2")).collect(Collectors.toList());
        Assert.isTrue(postExcels.size()==0,"请求数据有误!");

        ArrayList<PostExcel> collect = postExcelList.parallelStream().collect(
                Collectors
                        .collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(
                                        Comparator.comparing(o -> o.getPhone() + ";" + o.getBuiName() + ";" + o.getTypeName()))), ArrayList::new
                        )
        );
        Assert.isTrue(postExcelList.size()==collect.size(),"导入数据中存在重复数据,请检查!");

        //查询用户
        List<CntUser> userList = userService.list(Wrappers.<CntUser>lambdaQuery().in(CntUser::getPhone, postExcelList.parallelStream().filter(f -> StringUtils.isNotBlank(f.getPhone())).map(PostExcel::getPhone).collect(Collectors.toList())));
        Assert.isTrue(userList.size()>0,"用户不存在!");

        //查询盲盒和藏品
        List<String> boxNames = postExcelList.parallelStream().filter(f -> f.getTypeName().equals("1") && StringUtils.isNotBlank(f.getBuiName())).map(PostExcel::getBuiName).collect(Collectors.toList());
        List<String> collectionNames = postExcelList.parallelStream().filter(f -> f.getTypeName().equals("2") && StringUtils.isNotBlank(f.getBuiName())).map(PostExcel::getBuiName).collect(Collectors.toList());
        List<CntBox> boxList = boxService.list(Wrappers.<CntBox>lambdaQuery().in(boxNames.size()>0,CntBox::getBoxTitle, boxNames));
        List<CntCollection> cntCollectionList = collectionService.list(Wrappers.<CntCollection>lambdaQuery().in(collectionNames.size()>0,CntCollection::getCollectionName,collectionNames ));


        List<CntPostExcel> savePostExcel = new ArrayList<>();
        List<CntPostExcel> updatePostExcel = new ArrayList<>();
        postExcelList.parallelStream().forEach(e->{
            if(StringUtils.isNotBlank(e.getPhone()) && StringUtils.isNotBlank(e.getBuiName())) {
                Optional<CntUser> user = userList.parallelStream().filter(ff -> ff.getPhone().equals(e.getPhone())).findFirst();
                Optional<CntBox> box = boxList.parallelStream().filter(ff -> ff.getBoxTitle().equals(e.getBuiName())).findFirst();
                Optional<CntCollection> collection = cntCollectionList.parallelStream().filter(ff -> ff.getCollectionName().equals(e.getBuiName())).findFirst();
                if (("1".equals(e.getTypeName()) && box.isPresent() && user.isPresent()) || ("2".equals(e.getTypeName()) && collection.isPresent() && user.isPresent())) {
                    CntPostExcel cntPostExcel = getOne(
                            Wrappers.<CntPostExcel>lambdaQuery()
                                    .eq(CntPostExcel::getUserId, user.get().getId())
                                    .eq(CntPostExcel::getBuiId, "1".equals(e.getTypeName()) ? box.get().getId() : collection.get().getId())
                                    .eq(CntPostExcel::getTypeName, e.getTypeName()));
                    if (Objects.isNull(cntPostExcel)) {
                        savePostExcel.add(
                                Builder.of(CntPostExcel::new)
                                        .with(CntPostExcel::setId, IdUtils.getSnowflakeNextIdStr())
                                        .with(CntPostExcel::setUserId, user.get().getId())
                                        .with(CntPostExcel::setPhone, user.get().getPhone())
                                        .with(CntPostExcel::setBuiId, "1".equals(e.getTypeName()) ? box.get().getId() : collection.get().getId())
                                        .with(CntPostExcel::setBuiName, "1".equals(e.getTypeName()) ? box.get().getBoxTitle() : collection.get().getCollectionName())
                                        .with(CntPostExcel::setBuyFrequency, e.getBuyFrequency() == null ? 1 : e.getBuyFrequency())
                                        .with(CntPostExcel::setTypeName, e.getTypeName())
                                        .with(CntPostExcel::setReMark, e.getReMark())
                                        .with(CntPostExcel::setCreatedBy, SecurityUtils.getUsername())
                                        .with(CntPostExcel::setCreatedTime, DateUtils.getNowDate())
                                        .build()
                        );
                    } else {
                        updatePostExcel.add(
                                Builder.of(CntPostExcel::new)
                                        .with(CntPostExcel::setId, cntPostExcel.getId())
                                        .with(CntPostExcel::setUserId, user.get().getId())
                                        .with(CntPostExcel::setPhone, user.get().getPhone())
                                        .with(CntPostExcel::setBuiId, "1".equals(e.getTypeName()) ? box.get().getId() : collection.get().getId())
                                        .with(CntPostExcel::setBuiName, "1".equals(e.getTypeName()) ? box.get().getBoxTitle() : collection.get().getCollectionName())
                                        .with(CntPostExcel::setBuyFrequency, e.getBuyFrequency() == null ? 1 : e.getBuyFrequency())
                                        .with(CntPostExcel::setTypeName, e.getTypeName())
                                        .with(CntPostExcel::setReMark, e.getReMark())
                                        .with(CntPostExcel::setUpdatedBy, SecurityUtils.getUsername())
                                        .with(CntPostExcel::setUpdatedTime, DateUtils.getNowDate())
                                        .build()
                        );
                    }
                }
            }
        });

        if(savePostExcel.size()>0){
            saveBatch(savePostExcel);
        }

        if(updatePostExcel.size()>0){
            updateBatchById(updatePostExcel);
        }

        return R.ok("导入提前购数据成功!");
    }

}
