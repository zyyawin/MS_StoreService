package com.mysongktv.cn.tms.model.service;

import com.mysongktv.cn.tms.sotre.ConfigBindEntity;
import com.mysongktv.cn.tms.sotre.ConfigPageBindInfoEntity;

import java.util.List;

public interface ConfigBindService {

    List<ConfigBindEntity> selectConfigBindByStoreId(Integer storeId);

    List<ConfigBindEntity> selectConfigBindByConfigId(Integer configId);

    ConfigPageBindInfoEntity selectAllConfigBindByPage(Integer configBindId, Integer storeId, String storeName,
                                                       Integer configId, String configName,
                                                       Integer page, Integer count);

    ConfigBindEntity selectConfigBindByStoreIdAndConfigId(Integer storeId, Integer configId);

    /**
     * 添加配置名的例外项目
     */
    Integer insertConfigBind(Integer storeId, Integer configId);

    /**
     * 修改配置名的例外项目
     */
    Integer updateConfigBind(Integer configBindId, Integer storeId, Integer configId);

    /**
     * 删除配置名的例外项目
     */
    Integer deleteConfigBind(Integer configBindId);


}
