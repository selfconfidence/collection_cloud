package com.manyun.business.service;

import com.manyun.business.domain.entity.Cate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.CateVo;

import java.util.List;

/**
 * <p>
 * 藏品系列_分类 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface ICateService extends IService<Cate> {

    List<CateVo> cateAll(Integer type);

    List<CateVo> cateTopLevel(Integer type);

    List<CateVo> childCate(String parentId, Integer type);

    List<CateVo> cateChildAll(Integer type);
}
