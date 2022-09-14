package com.manyun.common.pays.utils.llpay.config;

/**
 * 连连账户系统常量
 *
 * @author linys
 */
public class LLianPayConstant {
    // 连连账户+ 公钥（无需进行替换）
    public final static String LLianPayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";
    // 商户系统（服务器）私钥（测试环境无需替换，正式环境需要替换）
    // 目前配置的为测试商户号：2020042200284052 的私钥，需要替换成商户自己的私钥
    public final static String MerchantPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPD+malK2W3UJXfX5rO79gRUbLe+kwCskw7VzETXA4Qf/5VWlPxOb16SlflkE4zyInhGwehjUTvXPvebNtZJJpKS+Q/7oWw6hMQ1cIC99DWmV6Orjtz61Tmi5A/4QnYqUm2GRScfrnyILQw9/qikkvyjo0pPsIMT2rhmu31LSySNAgMBAAECgYEAokaubeKq2lu6ByLohCqTFINM2cWH8zJBrAGnFMu74GIzlfnBRMwEDiiiuFX9HDGHqHns5HDMKIFeMxjfKhgD0exp3S06xpSbmkIbvWLM+xBl70/+SLG7wztZ4KtdKu7PR26xJht6zM/KDrovuRzFYNB6ZbyO3My9CJXaZS6GU/kCQQD+/wsf0M7Byp+sPzy3SEn8katFopVOz8oESBBuSNNXl1rgyWfgVXBUKRDAus8oa/Nhx2zWNqpuchrHerPp5McHAkEA8fFyidW4nMkL3x4ULQmbsZBqsNEXoKv3fDDvHWRljX0AElel+XaVuxrtpYiDxwqFSM0s92nCBj2ZXt4O+d2eywJAS5mFzMr1YZMXP9QHxjcSaGUvqBeJuLH2LMrIxEmnDuL6uIY928643NrH8rvvywYmRCkB5YiTgucldVq1mHSRZQJAYny8+WrsqbYVhQ/DesnsfQ2iwLN9AMTAC+gHjlluFXiK7OyM/c3OCcpebwHxUrbvpsEOyvBcMRomMr4GLqSOnQJAcDKoXpkYFGakejn6LQj57EBtMgfVNatTipBnQxPaHMGGO9V9SzedbkgNg0NBSzsNsauKnFOy+yFwFqf6oGHm0A==";
    // 连连测试环境测试商户号（测试环境无需替换，正式环境需要替换）
    public final static String OidPartner = "2020042200284052";
}
