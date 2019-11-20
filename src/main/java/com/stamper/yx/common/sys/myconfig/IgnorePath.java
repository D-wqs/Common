package com.stamper.yx.common.sys.myconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 从yml文件读取不被检查token的地址
 *
 * @author D-wqs
 * @data 2019/11/20 11:02
 */
@Component
@ConfigurationProperties(prefix = "ignore-paths")
public class IgnorePath {
    private List<String> paths;

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
