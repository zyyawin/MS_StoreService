package com.mysongktv.cn.tms.model.service.impl;

import com.mysongktv.cn.tms.model.mapper.ConfigItemMapper;
import com.mysongktv.cn.tms.model.service.ConfigItemService;
import com.mysongktv.cn.tms.sotre.ConfigItemEntity;
import com.mysongktv.cn.tms.sotre.ConfigPageItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigItemServiceImpl implements ConfigItemService {

    @Autowired
    private ConfigItemMapper configItemMapper;

    @Cacheable(keyGenerator = "storeKeyGenerator", value = "nodeInfo")
    @Override
    public List<ConfigItemEntity> selectConfigItemByConfigIdList(List<Integer> configIdList) {
        Map<String, List<Integer>> configIdMap = new HashMap<>(1);
        configIdMap.put("configId", configIdList);
        return configItemMapper.selectConfigItemByConfigIdList(configIdMap);
    }

    @Override
    public ConfigPageItemEntity selectConfigItemByConfigId(Integer configId,
                                                           Integer configItemId,
                                                           String configKey,
                                                           String configValue,
                                                           String comment,
                                                           Integer page,
                                                           Integer count) {
        ConfigPageItemEntity configPageItemEntity = new ConfigPageItemEntity();

        Map<String, Object> data = new HashMap<>();

        data.put("parent_id", configId);
        data.put("id", configItemId);
        data.put("config_key", configKey);
        data.put("config_value", configValue);
        data.put("comment", comment);

        //查询门店总条数
        int totalRecord = configItemMapper.selectConfigItemByConfigIdCount(data);
        //计算查询 偏移量
        int currIndex = (page - 1) * count;

        data.put("curIndex", currIndex);
        data.put("count", count);

        List<ConfigItemEntity> configItemEntityList =
            configItemMapper.selectConfigItemByConfigId(data);
        configPageItemEntity.setPage(page);
        configPageItemEntity.setCount(count);
        configPageItemEntity.setTotalCount(totalRecord);
        configPageItemEntity.setConfigItemList(configItemEntityList);
        return configPageItemEntity;
    }

    @Cacheable(keyGenerator = "storeKeyGenerator", value = "nodeInfo")
    @Override
    public ConfigItemEntity selectConfigItemByConfigIdAndConfigKey(Integer configId, String configKey) {
        return configItemMapper.selectConfigItemByConfigIdAndConfigKey(configId, configKey);
    }

    /**
     * 添加配置项目
     */
    @Override
    public Integer insertConfigItem(Integer configId, String configKey, String configValue, String comment) {
        return configItemMapper.insertConfigItem(configId, configKey, configValue, comment);
    }

    /**
     * 修改配置项目
     */
    @Override
    public Integer updateConfigItem(Integer configItemId, Integer configId, String configKey, String configValue, String comment) {
        return configItemMapper.updateConfigItem(configItemId, configId, configKey, configValue, comment);
    }

    /**
     * 删除配置项目
     */
    @Override
    public Integer deleteConfigItem(Integer configItemId) {
        return configItemMapper.deleteConfigItem(configItemId);
    }
}
