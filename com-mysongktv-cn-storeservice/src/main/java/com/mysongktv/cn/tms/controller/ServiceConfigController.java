/*
package com.mysongktv.cn.tms.controller;

import com.mysongktv.cn.tms.config.ErrorCode;
import com.mysongktv.cn.tms.config.Router;
import com.mysongktv.cn.tms.entity.ResultData;
import com.mysongktv.cn.tms.model.service.ConfigBindService;
import com.mysongktv.cn.tms.model.service.ConfigItemService;
import com.mysongktv.cn.tms.model.service.ConfigService;

import com.mysongktv.cn.tms.sotre.*;
import com.mysongktv.cn.tms.utils.Constant;
import com.mysongktv.cn.tms.utils.ErrorMsg;
import com.mysongktv.cn.tms.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ServiceConfigController {


    @Autowired
    private ConfigBindService configBindService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigItemService configItemService;

    */
/**
     * 查询客户端信息
     *//*

    @RequestMapping(value = Router.URI_SELECT_CONFIG_INFO, method = RequestMethod.GET)
    public ResultData selectConfigDetail(@RequestParam(required = false) String configName,
                                         @RequestParam(required = false) Integer storeId) {
        ResultData resultData = Tools.getThreadResultData();

        if (StringUtils.isEmpty(configName) && storeId == null) {
            resultData.setError(ErrorCode.PARAM_ERROR);
            resultData.setMsg(ErrorMsg.PARAM_ERROR);
            return resultData;
        }
        if (storeId != null) {
            List<ConfigBindEntity> configBindEntityList = configBindService.selectConfigBindByStoreId(storeId);
            if (configBindEntityList == null || configBindEntityList.isEmpty()) {
                if (StringUtils.isEmpty(configName)) {
                    resultData.setError(ErrorCode.QUERY_EMPTY);
                    resultData.setMsg(ErrorMsg.QUERY_EMPTY);
                    return resultData;
                }
                ConfigEntity config = configService.selectConfigByNameAndTag(configName, Constant.TAG_EXTRA);
                if (config == null) {
                    resultData.setError(ErrorCode.QUERY_EMPTY);
                    resultData.setMsg(ErrorMsg.QUERY_EMPTY);
                    return resultData;
                }
                resultData.setData(getConfigEntityList(config));

            } else {
                List<Integer> configIdList = new ArrayList<>();
                for (ConfigBindEntity configBindEntity : configBindEntityList) {
                    configIdList.add(configBindEntity.getConfigId());
                }
                //校验ConfigBind查询Config ID信息是否存在
                List<ConfigEntity> configList = configService.selectConfigByConfigIdList(configIdList);

                if (configList == null || configList.isEmpty()) {
                    resultData.setError(ErrorCode.QUERY_EMPTY);
                    resultData.setMsg(ErrorMsg.QUERY_EMPTY);
                    return resultData;
                }

                configIdList.clear();//清除数据,插入刚查询的数据
                for (ConfigEntity configEntity : configList) {
                    configIdList.add(configEntity.getConfigId());
                }

                List<ConfigItemEntity> configItemList = configItemService.selectConfigItemByConfigIdList(configIdList);
                if (configItemList != null && !configItemList.isEmpty()) {
                    for (ConfigEntity configEntity : configList) {
                        List<ConfigItemEntity> configItemEntityList = new ArrayList<>();
                        for (ConfigItemEntity configItemEntity : configItemList) {
                            if (configEntity.getConfigId().equals(configItemEntity.getConfigId())) {
                                configItemEntityList.add(configItemEntity);
                            }
                        }
                        configEntity.setConfigItemList(configItemEntityList);
                    }
                }
                resultData.setData(configList);
            }
        } else if (!StringUtils.isEmpty(configName)) {
            ConfigEntity config = configService.selectConfigByNameAndTag(configName, Constant.TAG_EXTRA);
            if (config == null) {
                resultData.setError(ErrorCode.QUERY_EMPTY);
                resultData.setMsg(ErrorMsg.QUERY_EMPTY);
                return resultData;
            }
            resultData.setData(getConfigEntityList(config));
        }
        return resultData;
    }

    private List<ConfigEntity> getConfigEntityList(ConfigEntity config) {
        List<ConfigEntity> configList = new ArrayList<>();
        List<Integer> configIdList = new ArrayList<>();
        configIdList.add(config.getConfigId());
        List<ConfigItemEntity> configItemList = configItemService.selectConfigItemByConfigIdList(configIdList);
        config.setConfigItemList(configItemList);
        configList.add(config);
        return configList;
    }

    */
/**
     * 查询所有配置服务名称
     *//*

    @RequestMapping(value = Router.URI_SELECT_SERVICE_CONFIG, method = RequestMethod.GET)
    public ResultData selectServiceConfig(@RequestParam(required = false) Integer configId,
                                          @RequestParam(required = false) String configName,
                                          @RequestParam(required = false) Integer tag,
                                          @RequestParam(required = false) String comment,
                                          @RequestParam(required = false, defaultValue = "0") Integer page,
                                          @RequestParam(required = false, defaultValue = "0") Integer count) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigPageEntity configPageEntity = configService.selectAllConfigByPage(configId, configName, tag, comment, page, count);
        resultData.setData(configPageEntity);
        return resultData;
    }

    */
/**
     * 插入服务配置信息
     *//*

    @RequestMapping(value = Router.URI_INSERT_CONFIG_INFO, method = RequestMethod.POST)
    public ResultData insertServiceConfig(@RequestParam String configName,
                                          @RequestParam Integer tag,
                                          @RequestParam(required = false) String comment) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigEntity configEntity = configService.selectConfigByNameAndTag(configName, tag);
        if (configEntity != null) {
            resultData.setError(ErrorCode.WRITE_DATA_ERROR);
            resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR_2);
            return resultData;
        }

        if (configService.insertConfig(configName, tag, comment) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }

    */
/**
     * 更新服务配置信息
     *//*

    @RequestMapping(value = Router.URI_UPDATE_CONFIG_INFO, method = RequestMethod.PUT)
    public ResultData updateServiceConfig(@RequestParam Integer configId,
                                          @RequestParam String configName,
                                          @RequestParam Integer tag,
                                          @RequestParam(required = false) String comment) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigEntity configEntity = configService.selectConfigByNameAndTag(configName, tag);
        if (configEntity != null && !configEntity.getConfigId().equals(configId)) {
            resultData.setError(ErrorCode.WRITE_DATA_ERROR);
            resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR_2);
            return resultData;
        }
        if (configService.updateConfig(configId, configName, tag, comment) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }


//    @RequestMapping(value = Router.URI_SELECT_CONFIG_BIND_INFO, method = RequestMethod.GET)
//    public ResultData selectConfigBind(@RequestParam Integer configId) {
//        ResultData resultData = Tools.getThreadResultData();
//        List<ConfigBindEntity> configBindEntityList = configBindService.selectConfigBindByConfigId(configId);
//        if (configBindEntityList == null || configBindEntityList.isEmpty()) {
//            resultData.setError(ErrorCode.QUERY_EMPTY);
//            resultData.setMsg(ErrorMsg.QUERY_EMPTY);
//            return resultData;
//        }
//        List<Integer> configIdList = new ArrayList<>();
//        for (ConfigBindEntity configBindEntity : configBindEntityList) {
//            configIdList.add(configBindEntity.getConfigId());
//        }
//        List<ConfigEntity> configList = configService.selectConfigByConfigIdList(configIdList);
//        for (ConfigBindEntity configBindEntity : configBindEntityList) {
//            List<ConfigEntity> configEntityList = new ArrayList<>();
//            for (ConfigEntity configEntity : configList) {
//                if (configBindEntity.getConfigId().equals(configEntity.getConfigId())) {
//                    configEntityList.add(configEntity);
//                }
//            }
//            configBindEntity.setConfigList(configEntityList);
//        }
//        resultData.setData(configBindEntityList);
//        return resultData;
//    }

    */
/**
     * 查询所有绑定信息
     *//*

    @RequestMapping(value = Router.URI_SELECT_ALL_CONFIG_BIND, method = RequestMethod.GET)
    public ResultData selectAllConfigBind(@RequestParam(required = false) Integer configBindId,
                                          @RequestParam(required = false) Integer storeId,
                                          @RequestParam(required = false) String storeName,
                                          @RequestParam(required = false) Integer configId,
                                          @RequestParam(required = false) String configName,
                                          @RequestParam(required = false, defaultValue = "0") Integer page,
                                          @RequestParam(required = false, defaultValue = "0") Integer count) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigPageBindInfoEntity configPageBindInfoEntity = configBindService.selectAllConfigBindByPage(configBindId,
                storeId, storeName, configId, configName, page, count);
        resultData.setData(configPageBindInfoEntity);
        return resultData;
    }

    */
/**
     * 添加配置名的例外项目
     *//*

    @RequestMapping(value = Router.URI_INSERT_CONFIG_BIND_INFO, method = RequestMethod.POST)
    public ResultData insertConfigBind(@RequestParam Integer storeId,
                                       @RequestParam Integer configId) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigEntity configEntity = configService.selectConfigByConfigId(configId);
        if (configEntity == null) {
            resultData.setError(ErrorCode.PARAM_ERROR);
            resultData.setMsg(ErrorMsg.PARAM_ERROR_3);
            return resultData;
        }
        StoreEntity storeEntity = storeService.selectStoreByStoreId(storeId);
        if (storeEntity == null) {
            resultData.setError(ErrorCode.PARAM_ERROR);
            resultData.setMsg(ErrorMsg.PARAM_ERROR_2);
            return resultData;
        }

        ConfigBindEntity configBindEntity = configBindService.selectConfigBindByStoreIdAndConfigId(storeId, configId);
        if (configBindEntity != null) {
            resultData.setError(ErrorCode.WRITE_DATA_ERROR);
            resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR_2);
            return resultData;
        }
        if (configBindService.insertConfigBind(storeId, configId) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }

    */
/**
     * 修改配置名的例外项目
     *//*

    @RequestMapping(value = Router.URI_UPDATE_CONFIG_BIND_INFO, method = RequestMethod.PUT)
    public ResultData updateConfigBind(@RequestParam Integer configBindId,
                                       @RequestParam Integer storeId,
                                       @RequestParam Integer configId) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigEntity configEntity = configService.selectConfigByConfigId(configId);
        if (configEntity == null) {
            resultData.setError(ErrorCode.PARAM_ERROR);
            resultData.setMsg(ErrorMsg.PARAM_ERROR_3);
            return resultData;
        }
        StoreEntity storeEntity = storeService.selectStoreByStoreId(storeId);
        if (storeEntity == null) {
            resultData.setError(ErrorCode.PARAM_ERROR);
            resultData.setMsg(ErrorMsg.PARAM_ERROR_2);
            return resultData;
        }
        ConfigBindEntity configBindEntity = configBindService.selectConfigBindByStoreIdAndConfigId(storeId, configId);
        if (configBindEntity != null && !configBindEntity.getConfigBindId().equals(configBindId)) {
            resultData.setError(ErrorCode.WRITE_DATA_ERROR);
            resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR_2);
            return resultData;
        }
        if (configBindService.updateConfigBind(configBindId, storeId, configId) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }

    */
/**
     * 删除配置名的例外项目
     *//*

    @RequestMapping(value = Router.URI_DELETE_CONFIG_BIND_INFO, method = RequestMethod.DELETE)
    public ResultData deleteConfigBind(@RequestParam Integer configBindId) {
        ResultData resultData = Tools.getThreadResultData();
        if (configBindService.deleteConfigBind(configBindId) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }


    @RequestMapping(value = Router.URI_SELECT_CONFIG_ITEM_INFO, method = RequestMethod.GET)
    public ResultData selectConfigItemByConfigId(@RequestParam Integer configId,
                                                 @RequestParam(required = false) Integer configItemId,
                                                 @RequestParam(required = false) String configKey,
                                                 @RequestParam(required = false) String configValue,
                                                 @RequestParam(required = false) String comment,
                                                 @RequestParam(required = false, defaultValue = "0") Integer page,
                                                 @RequestParam(required = false, defaultValue = "0") Integer count) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigPageItemEntity configPageItemEntity = configItemService.selectConfigItemByConfigId(configId,
                configItemId, configKey, configValue, comment, page, count);
        resultData.setData(configPageItemEntity);
        return resultData;
    }


    */
/**
     * 添加配置项目
     *//*

    @RequestMapping(value = Router.URI_INSERT_CONFIG_ITEM_INFO, method = RequestMethod.POST)
    public ResultData insertConfigItem(@RequestParam Integer configId,
                                       @RequestParam String configKey,
                                       @RequestParam String configValue,
                                       @RequestParam(required = false) String comment) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigEntity configEntity = configService.selectConfigByConfigId(configId);
        if (configEntity == null) {
            resultData.setError(ErrorCode.PARAM_ERROR);
            resultData.setMsg(ErrorMsg.PARAM_ERROR_3);
            return resultData;
        }
        ConfigItemEntity configItemEntity = configItemService.selectConfigItemByConfigIdAndConfigKey(configId, configKey);
        if (configItemEntity != null) {
            resultData.setError(ErrorCode.WRITE_DATA_ERROR);
            resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR_2);
            return resultData;
        }
        if (configItemService.insertConfigItem(configId, configKey, configValue, comment) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }

    */
/**
     * 修改配置项目
     *//*

    @RequestMapping(value = Router.URI_UPDATE_CONFIG_ITEM_INFO, method = RequestMethod.PUT)
    public ResultData updateConfigItem(@RequestParam Integer configItemId,
                                       @RequestParam Integer configId,
                                       @RequestParam String configKey,
                                       @RequestParam String configValue,
                                       @RequestParam(required = false) String comment) {
        ResultData resultData = Tools.getThreadResultData();
        ConfigItemEntity configItemEntity = configItemService.selectConfigItemByConfigIdAndConfigKey(configId, configKey);
        if (configItemEntity != null && !configItemEntity.getConfigItemId().equals(configItemId)) {
            resultData.setError(ErrorCode.WRITE_DATA_ERROR);
            resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR_2);
            return resultData;
        }
        if (configItemService.updateConfigItem(configItemId, configId, configKey, configValue, comment) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }

    */
/**
     * 删除配置项目
     *//*

    @RequestMapping(value = Router.URI_DELETE_CONFIG_ITEM_INFO, method = RequestMethod.DELETE)
    public ResultData deleteConfigItem(@RequestParam Integer configItemId) {
        ResultData resultData = Tools.getThreadResultData();
        if (configItemService.deleteConfigItem(configItemId) > 0) {
            return resultData;
        }
        resultData.setError(ErrorCode.WRITE_DATA_ERROR);
        resultData.setMsg(ErrorMsg.WRITE_DATA_ERROR);
        return resultData;
    }

}
*/
