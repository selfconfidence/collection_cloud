/**
 * Copyright : http://www.sandpay.com.cn , 2011-2014
 * Project : sandpay-cashier-webgateway
 * $Id$
 * $Revision$
 * Last Changed by pxl at 2016-12-28 上午10:49:46
 * $URL$
 *
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * pxl         2016-12-28        Initailized
 */
package com.manyun.business.config;
import com.manyun.business.config.cashier.sdk.CertUtil;
import com.manyun.business.config.cashier.sdk.SDKConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author pan.xl
 *
 */
@Component
public class ContextLoadListener implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ContextLoadListener.class);

	@Override
	@Order(1)
	public void run(String... args) throws Exception {
		logger.info("加载证书...");
		// 加载证书
		try {
			//加载配置文件
			SDKConfig.getConfig().loadPropertiesFromSrc();
			//加载证书
			CertUtil.init(SDKConfig.getConfig().getSandCertPath(),SDKConfig.getConfig().getSandProCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
