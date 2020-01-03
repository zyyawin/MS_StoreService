package com.mysongktv.cn.tms.model.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class ConfigItemsSqlProvider {

    public String selectConfigItemByConfigIdList(Map<String, List<Integer>> configIdMap) {
        List<Integer> configIdList = configIdMap.get("configId");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT id, parent_id, config_key, config_value, comment, create_at, update_at FROM t_config_item");
        stringBuilder.append(" WHERE parent_id IN (");
        for (int i = 0; i < configIdList.size(); i++) {
            stringBuilder.append(configIdList.get(i));
            if (i != configIdList.size() - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public String selectConfigItemByConfigIdCount(Map<String, Object> configItemMap) {
        SQL sql_obj = new SQL();
        sql_obj.SELECT("COUNT(id)");
        sql_obj.FROM("t_config_item");
        setWhere(configItemMap, sql_obj);
        return sql_obj.toString();
    }

    private void setWhere(Map<String, Object> configItemMap, SQL sql_obj) {
        if (configItemMap.get("parent_id") != null) {
            sql_obj.WHERE("parent_id = #{parent_id}");
        }
        if (configItemMap.get("id") != null) {
            sql_obj.WHERE("id = #{id}");
        }
        if (configItemMap.get("config_key") != null) {
            sql_obj.WHERE("config_key LIKE CONCAT('%',#{config_key},'%')");
        }
        if (configItemMap.get("config_value") != null) {
            sql_obj.WHERE("config_value LIKE CONCAT('%',#{config_value},'%')");
        }
        if (configItemMap.get("comment") != null) {
            sql_obj.WHERE("comment LIKE CONCAT('%',#{comment},'%')");
        }
    }

    public String selectConfigItemByConfigId(Map<String, Object> configItemMap) {
        SQL sql_obj = new SQL();
        sql_obj.SELECT("id, parent_id, config_key, config_value, comment, create_at, update_at");
        sql_obj.FROM("t_config_item");
        setWhere(configItemMap, sql_obj);
        String sql = sql_obj.toString();
        Integer curIndex = (Integer) configItemMap.get("curIndex");
        Integer count = (Integer) configItemMap.get("count");
        if (curIndex >= 0 && count > 0) {
            sql = sql + " LIMIT #{curIndex}, #{count}";
        }
        return sql;
    }
}
