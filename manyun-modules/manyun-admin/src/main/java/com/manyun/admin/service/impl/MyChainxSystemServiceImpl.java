package com.manyun.admin.service.impl;


import com.manyun.admin.domain.dto.MyChainxDto;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.admin.service.ICntUserCollectionService;
import com.manyun.admin.service.MyChainxSystemService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.R;
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
    public List<MyChainxVo> list() {
        return userCollectionService.myChainxList().stream().map(m->{
            m.setMediaVos(mediaService.initMediaVos(m.getCollectionId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * 重新上链
     */
    @Override
    public R update(MyChainxDto myChainxDto) {
        return chainxSystemService.resetUpLink(myChainxDto.getUserId(), myChainxDto.getId(), SecurityConstants.INNER);
    }

}
