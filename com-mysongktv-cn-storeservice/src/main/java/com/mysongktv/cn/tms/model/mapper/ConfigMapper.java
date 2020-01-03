package com.mysongktv.cn.tms.model.mapper;

import com.mysongktv.cn.tms.model.mapper.provider.ConfigSqlProvider;
import com.mysongktv.cn.tms.sotre.ConfigEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ConfigMapper {

    @Select("SELECT id, config_name, tag, comment, create_at, update_at FROM t_config WHERE id = #{id}")
    ConfigEntity selectConfigByConfigId(@Param("id") Integer configId);

    @Results({
        @Result(column = "id", property = "configId"),
        @Result(column = "config_name", property = "configName"),
        @Result(column = "create_at", property = "createAt"),
        @Result(column = "update_at", property = "updateAt")
    })
    @SelectProvider(type = ConfigSqlProvider.class, method = "selectConfigByConfigIdList")
    List<ConfigEntity> selectConfigByConfigIdList(Map<String, List<Integer>> configIdMap);

    @Results({
        @Result(column = "id", property = "configId"),
        @Result(column = "config_name", property = "configName"),
        @Result(column = "create_at", property = "createAt"),
        @Result(column = "update_at", property = "updateAt")
    })
    @Select("SELECT id, config_name, tag, comment, create_at, update_at FROM t_config WHERE config_name = #{config_name} AND tag = #{tag};")
    ConfigEntity selectConfigByConfigNameAndTag(@Param("config_name") String configName, @Param("tag") Integer tag);

    @SelectProvider(type = ConfigSqlProvider.class, method = "selectAllConfigCount")
    Integer selectAllConfigCount(Map<String, Object> data);


    @Results({
        @Result(column = "id", property = "configId"),
        @Result(column = "config_name", property = "configName"),
        @Result(column = "create_at", property = "createAt"),
        @Result(column = "update_at", property = "updateAt")
    })
    @SelectProvider(type = ConfigSqlProvider.class, method = "selectAllConfigByPage")
    List<ConfigEntity> selectAllConfigByPage(Map<String, Object> configMap);


    /**
     * 插入服务配置信息
     */
    @Insert("INSERT INTO t_config (config_name, tag, comment, create_at, update_at) VALUES (#{config_name}, #{tag}, #{comment}, NOW(), NOW());")
    Integer insertConfig(@Param("config_name") String configName, @Param("tag") Integer tag, @Param("comment") String comment);

    /**
     * 更新服务配置信息
     */
    @UpdateProvider(type = ConfigSqlProvider.class, method = "updateConfig")
    Integer updateConfig(Map<String, Object> configMap);
}
