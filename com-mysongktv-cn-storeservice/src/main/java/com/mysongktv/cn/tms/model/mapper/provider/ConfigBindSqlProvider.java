package com.mysongktv.cn.tms.model.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class ConfigBindSqlProvider {

    public String selectAllConfigBindCount(Map<String, Integer> configBindMap) {
        SQL sql_obj = new SQL();
        sql_obj.SELECT("COUNT(id)");
        sql_obj.FROM("t_config_bind");
        setWhere(configBindMap, sql_obj);
        return sql_obj.toString();
    }

    private void setWhere(Map<String, Integer> configBindMap, SQL sql_obj) {
        if (configBindMap.get("id") != null) {
            sql_obj.WHERE("id = #{id}");
        }
        if (configBindMap.get("store_name") != null) {
            sql_obj.WHERE("store_id IN (SELECT store_id FROM t_store_info WHERE name LIKE CONCAT('%',#{store_name},'%'))");
        } else if (configBindMap.get("store_id") != null) {
            sql_obj.WHERE("store_id = #{store_id}");
        }
        if (configBindMap.get("config_name") != null) {
            sql_obj.WHERE("config_id IN (SELECT id FROM t_config WHERE config_name LIKE CONCAT('%',#{config_name},'%'))");
        } else if (configBindMap.get("config_id") != null) {
            sql_obj.WHERE("config_id = #{config_id}");
        }
    }


    public String selectAllConfigBindByPage(Map<String, Integer> configBindMap) {
        SQL sql_obj = new SQL();
        sql_obj.SELECT("id, store_id, config_id, create_at, update_at, " +
                "(SELECT config_name FROM t_config WHERE id = t_config_bind.config_id) AS config_name," +
                "(SELECT name FROM t_store_info WHERE store_id = t_config_bind.store_id) AS store_name");
        sql_obj.FROM("t_config_bind");
        setWhere(configBindMap, sql_obj);
        String sql = sql_obj.toString();
        Integer curIndex = (Integer) configBindMap.get("curIndex");
        Integer count = (Integer) configBindMap.get("count");
        if (curIndex >= 0 && count > 0) {
            sql = sql + " LIMIT #{curIndex}, #{count}";
        }
        return sql;
    }

}
