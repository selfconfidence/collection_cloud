package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntSystem;
import com.manyun.admin.domain.dto.PosterDto;
import com.manyun.admin.domain.query.SystemQuery;
import com.manyun.admin.domain.vo.CntSystemVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 平台规则Service接口
 *
 * @author yanwei
 * @date 2022-07-28
 */
public interface ICntSystemService extends IService<CntSystem>
{
    /**
     * 查询平台规则详情
     *
     * @param id 平台规则主键
     * @return 平台规则
     */
    public CntSystemVo selectCntSystemById(String id);

    /**
     * 查询平台规则列表
     *
     * @param systemQuery
     * @return 平台规则集合
     */
    public TableDataInfo<CntSystemVo> selectCntSystemList(SystemQuery systemQuery);

    /**
     * 修改平台规则
     *
     * @param cntSystemVo
     * @return 结果
     */
    public int updateCntSystem(CntSystemVo cntSystemVo);

    /**
     * 更新邀请海报
     * @param posterDto
     * @return
     */
    public int updatePoster(PosterDto posterDto);

    /**
     * 查询邀请海报详情
     */
    public PosterDto queryPosterInfo();

    /**
     * 更新用户默认头像
     */
    public int updateUserDeafultAvatar(PosterDto posterDto);

    /**
     * 查询用户默认头像
     */
    public PosterDto queryUserDeafultAvatar();

    /**
     * 查询合成动图
     */
    public PosterDto querySyntheticAnimation();

    /**
     * 更新活动动图
     */
    public int updateSyntheticAnimation(PosterDto posterDto);

    /**
     * 查询开盲合动图
     */
    public PosterDto queryOpenBoxGif();

    /**
     * 更新开盲合动图
     */
    public int updateOpenBoxGif(PosterDto posterDto);
}
