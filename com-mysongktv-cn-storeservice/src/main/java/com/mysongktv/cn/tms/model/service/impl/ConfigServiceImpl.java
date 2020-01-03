package com.mysongktv.cn.tms.model.service.impl;

import com.mysongktv.cn.tms.model.mapper.ConfigMapper;
import com.mysongktv.cn.tms.model.service.ConfigItemService;
import com.mysongktv.cn.tms.model.service.ConfigService;
import com.mysongktv.cn.tms.sotre.ConfigEntity;
import com.mysongktv.cn.tms.sotre.ConfigPageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ConfigItemService configItemService;

    @Cacheable(keyGenerator = "storeKeyGenerator", value = "nodeInfo")
    @Override
    public ConfigEntity selectConfigByConfigId(Integer configId) {
        return configMapper.selectConfigByConfigId(configId);
    }

    @Cacheable(keyGenerator = "storeKeyGenerator", value = "nodeInfo")
    @Override
    public List<ConfigEntity> selectConfigByConfigIdList(List<Integer> configIdList) {
        Map<String, List<Integer>> configIdMap = new HashMap<>(1);
        configIdMap.put("configId", configIdList);

        return configMapper.selectConfigByConfigIdList(configIdMap);
    }

    @Cacheable(keyGenerator = "storeKeyGenerator", value = "nodeInfo")
    @Override
    public ConfigEntity selectConfigByNameAndTag(String configName, Integer tag) {
        return configMapper.selectConfigByConfigNameAndTag(configName, tag);
    }

    //    @Cacheable(keyGenerator="storeKeyGenerator", value="nodeInfo")
    @Override
    public ConfigPageEntity selectAllConfigByPage(Integer configId, String configName, Integer tag, String comment, Integer page, Integer count) {

        ConfigPageEntity configPageEntity = new ConfigPageEntity();

        Map<String, Object> data = new HashMap<>();
        data.put("id", configId);
        data.put("config_name", configName);
        data.put("tag", tag);
        data.put("comment", comment);

        Integer totalCount = configMapper.selectAllConfigCount(data);
        configPageEntity.setTotalCount(totalCount);
        //计算查询 偏移量
        int currIndex = (page - 1) * count;

        data.put("curIndex", currIndex);
        data.put("count", count);

        List<ConfigEntity> configList = configMapper.selectAllConfigByPage(data);
        configPageEntity.setPage(page);
        configPageEntity.setCount(count);
        configPageEntity.setConfigList(configList);
        //        if (configList != null && !configList.isEmpty()) {
        //            List<Integer> configIdList = new ArrayList<>();
        //            for (ConfigEntity configEntity : configList) {
        //                configIdList.add(configEntity.getId());
        //            }
        //            List<ConfigItemEntity> configItemList = configItemService.selectConfigItemByConfigIdList(configIdList);
        //
        //            for (ConfigEntity configEntity : configList) {
        //                List<ConfigItemEntity> configItemEntityList = new ArrayList<>();
        //                for (ConfigItemEntity configItemEntity : configItemList) {
        //                    if (configEntity.getId().equals(configItemEntity.getConfigId())) {
        //                        configItemEntityList.add(configItemEntity);
        //                    }
        //                }
        //                configEntity.setConfigItemList(configItemEntityList);
        //            }
        //        }
        return configPageEntity;
    }

    /**
     * 插入服务配置信息
     */
    @Override
    public Integer insertConfig(String configName, Integer tag, String comment) {
        return configMapper.insertConfig(configName, tag, comment);
    }

    /**
     * 更新服务配置信息
     */
    @Override
    public Integer updateConfig(Integer configId, String configName, Integer tag, String comment) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("id", configId);
        configMap.put("config_name", configName);
        configMap.put("tag", tag);
        configMap.put("comment", comment);
        return configMapper.updateConfig(configMap);
    }
}
