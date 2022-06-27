package com.manyun.base.codes;

import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class Codes {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://8.131.68.152:3306/manyun-cloud", "root", "Manyun2022@")
                .globalConfig(builder -> {
                    builder.author("yanwei") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\companys\\codes\\java_codes\\manyun_server\\manyun-modules\\codes"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.manyun.business") // 设置父包名
                            //.moduleName("web") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:\\companys\\codes\\java_codes\\manyun_server\\manyun-modules\\codes")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder // 设置需要生成的表名
                            .addInclude("cnt_post_excel")
                            .addInclude("tb_post_exist")
                            .addInclude("tb_post_sell")
                            .addInclude("tb_post_config");

                            //.likeTable(new LikeTable("cnt_", SqlLike.RIGHT))
                            //.addTablePrefix("cnt_"); // 设置过滤表前缀
                })
                //.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
