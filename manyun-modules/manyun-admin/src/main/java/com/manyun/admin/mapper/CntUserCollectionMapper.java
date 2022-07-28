package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.admin.domain.vo.UserCollectionVo;

/**
 * 用户购买藏品中间Mapper接口
 *
 * @author yanwei
 * @date 2022-07-22
 */
public interface CntUserCollectionMapper  extends BaseMapper<CntUserCollection>
{

    /**
     * 我的藏品
     */
    List<UserCollectionVo> myCollectionList(String userId);

    /**
     * 重试上链列表
     */
    List<MyChainxVo> myChainxList();
}
