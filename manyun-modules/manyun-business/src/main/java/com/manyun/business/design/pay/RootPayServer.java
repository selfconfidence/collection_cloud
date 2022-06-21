package com.manyun.business.design.pay;

import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.vo.PayVo;

import java.math.BigDecimal;

public interface RootPayServer {

     PayVo execPayVo(PayInfoDto payInfoDto);
}
