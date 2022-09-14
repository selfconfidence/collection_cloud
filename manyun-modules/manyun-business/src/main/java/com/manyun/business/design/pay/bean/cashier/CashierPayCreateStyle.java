package com.manyun.business.design.pay.bean.cashier;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CashierPayCreateStyle {
    // 支付页面的背景颜色。 可选范围为 000000 ~ ffffff， 默认值为ff5001
    private String bg_color;
    // 支付页面字体颜色。可选范围为 000000 ~ ffffff。
    private String font_color;
}
