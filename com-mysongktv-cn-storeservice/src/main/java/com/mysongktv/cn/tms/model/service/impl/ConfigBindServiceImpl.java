package com.mysongktv.cn.tms.model.service.impl;

import com.mysongktv.cn.tms.model.mapper.ConfigBindMapper;
import com.mysongktv.cn.tms.model.service.ConfigBindService;
import com.mysongktv.cn.tms.sotre.ConfigBindEntity;
import com.mysongktv.cn.tms.sotre.ConfigBindInfoEntity;
import com.mysongktv.cn.tms.sotre.ConfigPageBindInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigBindServiceImpl implements ConfigBindService {

    @Autowired
    private ConfigBindMapper configBindMapper;

    @Cacheable(keyGenerator="storeKeyGenerator", value="nodeInfo")
    @Override
    public List<ConfigBindEntity> selectConfigBindByStoreId(Integer storeId) {
        return configBindMapper.selectConfigBindByStoreId(storeId);
    }

    @Cacheable(keyGenerator="storeKeyGenerator", value="nodeInfo")
    @Override
    public List<ConfigBindEntity> selectConfigBindByConfigId(Integer configId) {
        return configBindMapper.selectConfigBindByConfigId(configId);
    }

    @Cacheable(keyGenerator="storeKeyGenerator", value="nodeInfo")
    @Override
    public ConfigBindEntity selectConfigBindByStoreIdAndConfigId(Integer storeId, Integer configId) {
        return configBindMapper.selectConfigBindByStoreIdAndConfigId(storeId, configId);
    }

//    @Cacheable(keyGenerator="storeKeyGenerator", value="nodeInfo")
    @Override
    public ConfigPageBindInfoEntity selectAllConfigBindByPage(Integer configBindId, Integer storeId, String storeName,
                                                              Integer configId, String configName,
                                                              Integer page, Integer count) {
        ConfigPageBindInfoEntity configPageBindInfoEntity = new ConfigPageBindInfoEntity();
        Map<String, Object> data = new HashMap<>();
        data.put("id", configBindId);
        data.put("store_id", storeId);
        data.put("store_name", storeName);
        data.put("config_id", configId);
        data.put("config_name", configName);

        //查询门店总条数
        int totalRecord = configBindMapper.selectAllConfigBindCount(data);
        //计算查询 偏移量
        int currIndex = (page - 1) * count;

        data.put("curIndex", currIndex);
        data.put("count", count);

        List<ConfigBindInfoEntity> configBindInfoEntityList = configBindMapper.selectAllConfigBindByPage(data);
        configPageBindInfoEntity.setPage(page);
        configPageBindInfoEntity.setCount(count);
        configPageBindInfoEntity.setTotalCount(totalRecord);
        configPageBindInfoEntity.setConfigBindList(configBindInfoEntityList);
        return configPageBindInfoEntity;
    }

    /**
     * 添加配置名的例外项目
     */
    @Override
    public Integer insertConfigBind(Integer storeId, Integer configId) {
        return configBindMapper.insertConfigBind(storeId, configId);
    }

    /**
     * 修改配置名的例外项目
     */
    @Override
    public Integer updateConfigBind(Integer configBindId, Integer storeId, Integer configId) {
        return configBindMapper.updateConfigBind(configBindId, storeId, configId);
    }

    /**
     * 删除配置名的例外项目
     */
    @Override
    public Integer deleteConfigBind(Integer configBindId) {
        return configBindMapper.deleteConfigBind(configBindId);
    }
}
