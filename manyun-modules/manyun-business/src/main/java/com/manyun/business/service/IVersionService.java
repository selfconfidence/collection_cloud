package com.manyun.business.service;

import com.manyun.business.domain.entity.Version;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.VersionVo;

import java.util.List;

/**
 * <p>
 * 版本表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
public interface IVersionService extends IService<Version> {

    VersionVo newVersion(Integer isType);

    List<VersionVo> allVersionList(Integer isType);
}
