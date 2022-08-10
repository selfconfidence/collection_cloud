package com.manyun.admin.service;

import com.manyun.admin.domain.query.ActionTarDictQuery;
import com.manyun.admin.domain.query.DrawRulesDictQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.common.core.domain.R;

import java.util.List;

/**
 * 字典Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntDictService
{
    /***
     * 查询藏品字典
     */
    R collectionDict();

    /***
     * 查询盲盒字典
     */
    R boxDict();

    /***
     * 查询藏品系列字典
     */
    R collectionCateDict();

    /***
     * 查询创作者字典
     */
    R creationdDict();

    /***
     * 查询发行方字典
     */
    R issuanceDict();

    /***
     * 查询标签字典
     */
    R lableDict();

    /***
     * 查询客服字典
     */
    R customerServiceDict();

    /***
     * 抽签规则字典
     */
    R drawRulesDict(DrawRulesDictQuery drawRulesDictQuery);

    /***
     * 提前购配置可以购买字典
     */
    R postSellDict();

    /***
     * 提前购配置已经拥有字典
     */
    R postExistDict();

    /***
     * 活动合成材料字典
     */
    R actionTarDict(ActionTarDictQuery tarDictQuery);

}
