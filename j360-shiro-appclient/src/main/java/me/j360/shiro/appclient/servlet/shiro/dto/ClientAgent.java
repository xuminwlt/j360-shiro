package me.j360.shiro.appclient.servlet.shiro.dto;

import com.app.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Package: me.j360.shiro.appclient.servlet.shiro.dto
 * User: min_xu
 * Date: 16/5/13 下午4:03
 * 说明：
 */
//表达式：appName/version/buildVersion/osNumber/osVersion/deviceModel/deviceUUID
public class ClientAgent extends BaseEntity{
    @Getter
    @Setter
    private String appName;
    @Getter
    @Setter
    private String version;
    @Getter
    @Setter
    private int buildVersion;
    @Getter
    @Setter
    private String osNumber;
    @Getter
    @Setter
    private String osVersion;
    @Getter
    @Setter
    private String deviceModel;
    @Getter
    @Setter
    private String deviceUUID;


}