package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntConsignment;
import com.manyun.business.domain.form.UserConsignmentForm;
import com.manyun.business.domain.query.ConsignmentQuery;
import com.manyun.business.domain.vo.ConsignmentBoxListVo;
import com.manyun.business.domain.vo.ConsignmentCollectionListVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * <p>
 * 寄售市场主表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-30
 */
public interface ICntConsignmentService extends IService<CntConsignment> {

    void consignmentAssets(UserConsignmentForm userConsignmentForm,String userId);

    TableDataInfo<ConsignmentCollectionListVo> pageConsignmentList(ConsignmentQuery consignmentQuery);

    TableDataInfo<ConsignmentBoxListVo> pageConsignmentBoxList(ConsignmentQuery consignmentQuery);
}
