package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Version;
import com.manyun.business.domain.vo.VersionVo;
import com.manyun.business.mapper.VersionMapper;
import com.manyun.business.service.IVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 版本表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements IVersionService {

    @Override
    public VersionVo newVersion(Integer isType) {
        Version version = getOne(Wrappers.<Version>lambdaQuery().eq(Version::getIsType, isType).orderByDesc(Version::getCreatedTime).last(" limit 1"));
       return Objects.nonNull(version) ? initVersionVo(version) : Builder.of(VersionVo::new).build();
    }

    @Override
    public List<VersionVo> allVersionList(Integer isType) {
        List<Version> versionList = list(Wrappers.<Version>lambdaQuery().eq(Version::getIsType, isType).orderByDesc(Version::getCreatedTime));
        return versionList.isEmpty() ? Collections.emptyList() : versionList.stream().map(this::initVersionVo).collect(Collectors.toList());
    }

    private VersionVo initVersionVo(Version version) {
        VersionVo versionVo = Builder.of(VersionVo::new).build();
        BeanUtil.copyProperties(version,versionVo);
        return versionVo;

    }
}
