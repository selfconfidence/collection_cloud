package com.manyun.business.service;

import com.manyun.business.domain.entity.Box;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.vo.BoxListVo;
import com.manyun.business.domain.vo.BoxVo;
import com.manyun.business.domain.vo.PayVo;

import java.util.List;

/**
 * <p>
 * 盲盒;盲盒主体表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IBoxService extends IService<Box> {

    List<BoxListVo> pageList(BoxQuery boxQuery);

    BoxVo info(String id);

    PayVo sellBox(BoxSellForm boxSellForm, String userId);
}
