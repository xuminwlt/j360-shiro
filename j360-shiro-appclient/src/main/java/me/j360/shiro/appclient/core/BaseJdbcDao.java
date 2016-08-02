package me.j360.shiro.appclient.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Peter on 15-4-23.
 */
public class BaseJdbcDao {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> query(String sql, Object... params) {
        List<Map<String, Object>> rtn = this.jdbcTemplate.queryForList(sql, params);
        if (rtn == null) {
            return new ArrayList<Map<String, Object>>(0);
        }
        return rtn;
    }

    public long insertAndGetKey(final String sql, final Object... params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
                return pstmt;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int execute(String sql, Object... params) {
        return this.jdbcTemplate.update(sql, params);
    }

    public <T> void execute(String sql, final List<T> list, final BatchUpdateCallBack callBack) {

        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                callBack.execute(ps, i);
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }


    public Map<String, Object> selectOne(String sql, Object... params) {
        List<Map<String, Object>> list = this.query(sql, params);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public boolean isExist(String tablename, String fieldname, String fiedvalue) {
        if (StringUtils.isEmpty(fiedvalue)) {
            fiedvalue = "''";
        }
        String sql = "select " + fieldname + " from " + tablename + " where " + fieldname + " = '" + fiedvalue + "'";
        List<Map<String, Object>> list = this.query(sql);
        if (list == null || list.size() <= 0) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 使用标识位而非通配符
     */
    @Deprecated
    public List<Map<String, Object>> queryForListMap(String sql, Map filter) {
        return jdbcTemplate.queryForList(sql, filter);
    }

    @Deprecated
    public Map<String, Object> get(String sql, Map filter) {
        List<Map<String, Object>> list = this.queryForListMap(sql, filter);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    @Deprecated
    public int execute(String sql, Map filter) {
        return jdbcTemplate.update(sql, filter);
    }
}
