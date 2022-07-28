package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.admin.domain.vo.UserCollectionVo;

/**
 * 用户购买藏品中间Service接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface ICntUserCollectionService extends IService<CntUserCollection>
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
