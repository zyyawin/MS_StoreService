package com.mysongktv.cn.tms.model.service;

import com.mysongktv.cn.tms.sotre.ConfigItemEntity;
import com.mysongktv.cn.tms.sotre.ConfigPageItemEntity;

import java.util.List;

public interface ConfigItemService {

    List<ConfigItemEntity> selectConfigItemByConfigIdList(List<Integer> configIdList);

    ConfigPageItemEntity selectConfigItemByConfigId(Integer configId,
                                                    Integer configItemId,
                                                    String configKey,
                                                    String configValue,
                                                    String comment,
                                                    Integer page,
                                                    Integer count);

    ConfigItemEntity selectConfigItemByConfigIdAndConfigKey(Integer configId, String configKey);

    /**
     * 添加配置项目
     */
    Integer insertConfigItem(Integer configId, String configKey, String configValue, String comment);

    /**
     * 修改配置项目
     */
    Integer updateConfigItem(Integer configItemId, Integer configId, String configKey, String configValue, String comment);

    /**
     * 删除配置项目
     */
    Integer deleteConfigItem(Integer configItemId);

}
