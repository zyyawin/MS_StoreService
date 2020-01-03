package com.mysongktv.cn.tms.model.mapper;

import com.mysongktv.cn.tms.model.mapper.provider.ConfigItemsSqlProvider;
import com.mysongktv.cn.tms.sotre.ConfigItemEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ConfigItemMapper {

    @Results({
            @Result(column = "id",           property = "configItemId"),
            @Result(column = "parent_id",    property = "configId"),
            @Result(column = "config_key",   property = "configKey"),
            @Result(column = "config_value", property = "configValue"),
            @Result(column = "create_at",    property = "createAt"),
            @Result(column = "update_at",    property = "updateAt")
    })
    @SelectProvider(type = ConfigItemsSqlProvider.class, method = "selectConfigItemByConfigIdList")
    List<ConfigItemEntity> selectConfigItemByConfigIdList(Map<String, List<Integer>> configIdMap);

    @SelectProvider(type = ConfigItemsSqlProvider.class, method = "selectConfigItemByConfigIdCount")
    Integer selectConfigItemByConfigIdCount(Map<String, Object> data);

    @Results({
            @Result(column = "id",           property = "configItemId"),
            @Result(column = "parent_id",    property = "configId"),
            @Result(column = "config_key",   property = "configKey"),
            @Result(column = "config_value", property = "configValue"),
            @Result(column = "create_at",    property = "createAt"),
            @Result(column = "update_at",    property = "updateAt")
    })
    @SelectProvider(type = ConfigItemsSqlProvider.class, method = "selectConfigItemByConfigId")
    List<ConfigItemEntity> selectConfigItemByConfigId(Map<String, Object> data);

    @Results({
            @Result(column = "id",           property = "configItemId"),
            @Result(column = "parent_id",    property = "configId"),
            @Result(column = "config_key",   property = "configKey"),
            @Result(column = "config_value", property = "configValue"),
            @Result(column = "create_at",    property = "createAt"),
            @Result(column = "update_at",    property = "updateAt")
    })
    @Select("SELECT id, parent_id, config_key, config_value, create_at, update_at FROM t_config_item WHERE parent_id = #{parent_id} AND config_key = #{config_key}")
    ConfigItemEntity selectConfigItemByConfigIdAndConfigKey(@Param("parent_id") Integer configId, @Param("config_key") String configKey);


    /**
     * 添加配置项目
     */
    @Insert("INSERT INTO t_config_item (parent_id, config_key, config_value, comment, create_at, update_at) " +
            "VALUES (#{parent_id}, #{config_key}, #{config_value}, #{comment}, NOW(), NOW());")
    Integer insertConfigItem(@Param("parent_id") Integer configId, @Param("config_key") String configKey,
                             @Param("config_value") String configValue, @Param("comment") String comment);

    /**
     * 修改配置项目
     */
    @Update("UPDATE t_config_item SET parent_id = #{parent_id}, config_key = #{config_key}, config_value = #{config_value}, comment = #{comment}, update_at = NOW() WHERE id = #{id};")
    Integer updateConfigItem(@Param("id") Integer configItemId, @Param("parent_id") Integer configId, @Param("config_key") String configKey,
                             @Param("config_value") String configValue, @Param("comment") String comment);

    /**
     * 删除配置项目
     */
    @Delete("DELETE FROM t_config_item WHERE id = #{id};")
    Integer deleteConfigItem(@Param("id") Integer configItemId);
}
