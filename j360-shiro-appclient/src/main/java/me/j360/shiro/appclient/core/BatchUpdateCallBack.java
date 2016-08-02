package me.j360.shiro.appclient.core;

import java.sql.PreparedStatement;

public interface BatchUpdateCallBack {

    void execute(PreparedStatement ps, int i);
}
