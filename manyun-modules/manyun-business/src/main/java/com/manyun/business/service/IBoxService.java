package com.manyun.business.service;

import com.manyun.business.domain.entity.Box;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.vo.BoxListVo;
import com.manyun.business.domain.vo.BoxVo;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.domain.vo.UserBoxVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

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

    TableDataInfo<BoxListVo> pageList(BoxQuery boxQuery);

    BoxVo info(String id,String userId);

    PayVo sellBox(BoxSellForm boxSellForm, String userId);

    TableDataInfo<UserBoxVo> userBoxPageList(PageQuery pageQuery, String userId);

    String openBox(String boxId, String userId);

    List<String> queryDict(String keyword);

    Integer tarBox(String id, String userId);

    BoxListVo getBaseBoxListVo(String boxId);
}
