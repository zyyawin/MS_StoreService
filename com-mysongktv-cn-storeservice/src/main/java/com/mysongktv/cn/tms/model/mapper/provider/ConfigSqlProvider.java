package com.mysongktv.cn.tms.model.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class ConfigSqlProvider {

    public String selectConfigByConfigIdList(Map<String, List<Integer>> configIdMap) {
        List<Integer> configIdList = configIdMap.get("configId");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT id, config_name, tag, comment, create_at, update_at FROM t_config");
        stringBuilder.append(" WHERE id IN (");
        for (int i = 0; i < configIdList.size(); i++) {
            stringBuilder.append(configIdList.get(i));
            if (i != configIdList.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public String selectAllConfigCount(Map<String, Object> configMap) {
        SQL sql_obj = new SQL();
        sql_obj.SELECT("COUNT(id)");
        sql_obj.FROM("t_config");
        setWhere(configMap, sql_obj);
        return sql_obj.toString();
    }

    public String selectAllConfigByPage(Map<String, Object> configMap) {
        SQL sql_obj = new SQL();
        sql_obj.SELECT("id, config_name, tag, comment, create_at, update_at");
        sql_obj.FROM("t_config");
        setWhere(configMap, sql_obj);
        String sql = sql_obj.toString();
        sql = sql + " ORDER BY create_at DESC";
        Integer curIndex = (Integer) configMap.get("curIndex");
        Integer count = (Integer) configMap.get("count");
        if (curIndex >= 0 && count > 0) {
            sql = sql + " LIMIT #{curIndex}, #{count}";
        }
        return sql;
    }

    private void setWhere(Map<String, Object> configMap, SQL sql_obj) {
        if (configMap.get("id") != null) {
            sql_obj.WHERE("id = #{id}");
        }
        if (configMap.get("config_name") != null) {
            sql_obj.WHERE("config_name LIKE CONCAT('%',#{config_name},'%')");
        }
        if (configMap.get("tag") != null) {
            sql_obj.WHERE("tag = #{tag}");
        }
        if (configMap.get("comment") != null) {
            sql_obj.WHERE("comment LIKE CONCAT('%',#{comment},'%')");
        }
    }

    public String updateConfig(Map<String, Object> configMap) {
        SQL sql_obj = new SQL();
        sql_obj.UPDATE("t_config");

        if (configMap.get("config_name") != null) {
            sql_obj.SET("config_name = #{config_name}");
        }
        if (configMap.get("tag") != null) {
            sql_obj.SET("tag = #{tag}");
        }
        if (configMap.get("comment") != null) {
            sql_obj.SET("comment = #{comment}");
        }
        sql_obj.SET("update_at = NOW()");

        if (configMap.get("id") != null) {
            sql_obj.WHERE("id = #{id}");
        }
        return sql_obj.toString();
    }
}
