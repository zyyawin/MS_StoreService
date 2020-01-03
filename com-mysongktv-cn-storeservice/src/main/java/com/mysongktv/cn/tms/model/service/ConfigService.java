package com.mysongktv.cn.tms.model.service;

import com.mysongktv.cn.tms.sotre.ConfigEntity;
import com.mysongktv.cn.tms.sotre.ConfigPageEntity;

import java.util.List;

public interface ConfigService {

    ConfigEntity selectConfigByConfigId(Integer configId);

    List<ConfigEntity> selectConfigByConfigIdList(List<Integer> configIdList);

    ConfigEntity selectConfigByNameAndTag(String configName, Integer tag);


    ConfigPageEntity selectAllConfigByPage(Integer configId, String configName, Integer tag, String comment, Integer page, Integer count);

    /**
     * 插入服务配置信息
     */
    Integer insertConfig(String configName, Integer tag, String comment);

    /**
     * 更新服务配置信息
     */
    Integer updateConfig(Integer configId, String configName, Integer tag, String comment);


}
