package com.manyun.business.service;

import com.manyun.business.domain.entity.Box;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.BoxOrderSellForm;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.query.UseAssertQuery;
import com.manyun.business.domain.vo.*;
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

    PayVo sellBox(BoxSellForm boxSellForm, String userId, String ipaddr);

    TableDataInfo<UserBoxVo> userBoxPageList(UseAssertQuery useAssertQuery, String userId);

    OpenBoxCollectionVo openBox(String userBoxId, String userId);

    List<String> queryDict(String keyword);

    String tarBox(String id, String userId);

    BoxListVo getBaseBoxListVo(String boxId);

    void checkBalance(String boxId, Integer sellNum);

    String sellOrderBox(BoxOrderSellForm boxOrderSellForm, String userId);
}
