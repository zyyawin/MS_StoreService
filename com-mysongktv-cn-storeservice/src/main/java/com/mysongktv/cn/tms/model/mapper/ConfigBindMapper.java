package com.mysongktv.cn.tms.model.mapper;

import com.mysongktv.cn.tms.model.mapper.provider.ConfigBindSqlProvider;
import com.mysongktv.cn.tms.sotre.ConfigBindEntity;
import com.mysongktv.cn.tms.sotre.ConfigBindInfoEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ConfigBindMapper {


    @Results({
            @Result(column = "id",        property = "configBindId"),
            @Result(column = "store_id",  property = "storeId"),
            @Result(column = "config_id", property = "configId"),
            @Result(column = "create_at", property = "createAt"),
            @Result(column = "update_at", property = "updateAt")
    })
    @Select("SELECT id, store_id, config_id, create_at FROM t_config_bind WHERE store_id = #{store_id}")
    List<ConfigBindEntity> selectConfigBindByStoreId(@Param("store_id") Integer storeId);

    @Results({
            @Result(column = "id",        property = "configBindId"),
            @Result(column = "store_id",  property = "storeId"),
            @Result(column = "config_id", property = "configId"),
            @Result(column = "create_at", property = "createAt"),
            @Result(column = "update_at", property = "updateAt")
    })
    @Select("SELECT id, store_id, config_id, create_at FROM t_config_bind WHERE config_id = #{config_id}")
    List<ConfigBindEntity> selectConfigBindByConfigId(@Param("config_id") Integer configId);


    @Results({
            @Result(column = "id",        property = "configBindId"),
            @Result(column = "store_id",  property = "storeId"),
            @Result(column = "config_id", property = "configId"),
            @Result(column = "create_at", property = "createAt"),
            @Result(column = "update_at", property = "updateAt")
    })
    @Select("SELECT id, store_id, config_id, create_at FROM t_config_bind WHERE store_id = #{store_id} AND config_id = #{config_id}")
    ConfigBindEntity selectConfigBindByStoreIdAndConfigId(@Param("store_id") Integer storeId, @Param("config_id") Integer configId);

    @SelectProvider(type = ConfigBindSqlProvider.class, method = "selectAllConfigBindCount")
    Integer selectAllConfigBindCount(Map<String, Object> data);

    @Results({
            @Result(column = "id",           property = "configBindId"),
            @Result(column = "store_id",     property = "storeId"),
            @Result(column = "store_name",   property = "storeName"),
            @Result(column = "config_id",    property = "configId"),
            @Result(column = "config_name", property = "configName"),
            @Result(column = "create_at",    property = "createAt"),
            @Result(column = "update_at",    property = "updateAt")
    })
    @SelectProvider(type = ConfigBindSqlProvider.class, method = "selectAllConfigBindByPage")
    List<ConfigBindInfoEntity> selectAllConfigBindByPage(Map<String, Object> data);

    /**
     * 添加配置名的例外项目
     */
    @Insert("INSERT INTO t_config_bind (store_id, config_id, create_at, update_at) VALUES (#{store_id}, #{config_id}, NOW(), NOW());")
    Integer insertConfigBind(@Param("store_id") Integer storeId, @Param("config_id") Integer configId);

    /**
     * 修改配置名的例外项目
     */
    @Update("UPDATE t_config_bind SET store_id = #{store_id}, config_id = #{config_id}, update_at = NOW() WHERE id = #{id};")
    Integer updateConfigBind(@Param("id") Integer configBindId, @Param("store_id") Integer storeId, @Param("config_id") Integer configId);

    /**
     * 删除配置名的例外项目
     */
    @Delete("DELETE FROM t_config_bind WHERE id = #{id};")
    Integer deleteConfigBind(@Param("id") Integer configBindId);
}
