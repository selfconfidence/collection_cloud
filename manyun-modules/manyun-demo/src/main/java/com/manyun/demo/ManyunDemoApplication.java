package com.manyun.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.manyun.common.security.annotation.EnableCustomConfig;
import com.manyun.common.security.annotation.EnableRyFeignClients;
import com.manyun.common.swagger.annotation.EnableCustomSwagger2;

/**
 *
 * 
 * @author
 */
@EnableCustomConfig    //  具体业务解析注解 业务模块必备
@EnableCustomSwagger2    // 在线文档注册，业务模块必备
@EnableRyFeignClients    // 远程feign 调用注解
@SpringBootApplication
public class ManyunDemoApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ManyunDemoApplication.class, args);
       /* System.out.println("(♥◠‿◠)ﾉﾞ  定时任务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");*/
    }
}
