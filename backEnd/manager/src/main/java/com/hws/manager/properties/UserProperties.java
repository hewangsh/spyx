package com.hws.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
//TODO 需要在启动类上面加一个注解：EnableConfigrationProperties才能生效
@ConfigurationProperties(prefix = "spzx.auth")
public class UserProperties {

    //不需要拦截的路径名
    private List<String> noAuthUrls;    //这个名字要跟配置文件的名字一致，用list存是因为他有多个
}
