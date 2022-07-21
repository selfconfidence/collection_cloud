package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;

/**
 * 用户Mapper接口
 *
 * @author yanwei
 * @date 2022-07-12
 */
public interface CntUserMapper
{
    /**
     * 查询用户
     *
     * @param id 用户主键
     * @return 用户
     */
    public CntUser selectCntUserById(String id);

    /**
     * 查询用户列表
     *
     * @param cntUser 用户
     * @return 用户集合
     */
    public List<CntUser> selectCntUserList(CntUser cntUser);

    /**
     * 新增用户
     *
     * @param cntUser 用户
     * @return 结果
     */
    public int insertCntUser(CntUser cntUser);

    /**
     * 修改用户
     *
     * @param cntUser 用户
     * @return 结果
     */
    public int updateCntUser(CntUser cntUser);

    /**
     * 删除用户
     *
     * @param id 用户主键
     * @return 结果
     */
    public int deleteCntUserById(String id);

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntUserByIds(String[] ids);

    /**
     * 用户和钱包信息
     *
     * @param userMoneyQuery 用户和钱包信息
     * @return 结果
     */
    List<UserMoneyVo> selectUserMoneyList(UserMoneyQuery userMoneyQuery);

    /**
     * 我的藏品
     */
    List<UserCollectionVo> myCollectionList(String userId);
}
