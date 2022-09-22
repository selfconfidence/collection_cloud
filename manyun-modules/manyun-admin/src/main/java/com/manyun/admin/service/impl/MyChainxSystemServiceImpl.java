package com.manyun.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.dto.MyChainxDto;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.admin.service.ICntUserCollectionService;
import com.manyun.admin.service.MyChainxSystemService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 重试上链Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-25
 */
@Service
public class MyChainxSystemServiceImpl  implements  MyChainxSystemService
{

    @Autowired
    private ICntUserCollectionService userCollectionService;

    @Autowired
    private ICntMediaService mediaService;

    @Autowired
    private com.manyun.comm.api.MyChainxSystemService chainxSystemService;

    /**
     * 查询重试上链藏品列表
     */
    @Override
    public TableDataInfo<MyChainxVo> list(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<MyChainxVo> myChainxVos = userCollectionService.myChainxList();
        return TableDataInfoUtil.pageTableDataInfo(myChainxVos.stream().map(m->{
            m.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(m.getCollectionId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            return m;
        }).collect(Collectors.toList()),myChainxVos);
    }

    /**
     * 重新上链
     */
    @Override
    public R update(MyChainxDto myChainxDto) {
        return chainxSystemService.resetUpLink(myChainxDto.getUserId(), myChainxDto.getId(), SecurityConstants.INNER);
    }

}
