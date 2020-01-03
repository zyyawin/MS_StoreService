package com.mysongktv.cn.tms.config;

public class Router {
    /**
     * 添加新店
     */
    public static final String URI_INSERT_STORE = "/v1/store";
    public static final String URI_BATCH_INSERT_STORE = "/v1/store/batch";
    /**
     * 修改信息
     */
    public static final String URI_UPDATE_STORE = "/v1/store";
    /**
     * 删除门店
     */
    public static final String URI_DELETE_STORE = "/v1/store";
    /**
     * 根据 store_info.id查询
     */
    public static final String URI_SELECT_INFO_BY_ID = "/v1/store/id";
    /**
     * 根据 store_info.store_id查询
     */
    public static final String URI_SELECT_INFO_BY_STORE_ID = "/v1/store/storeId";
    /**
     * 根据 store_info.wanIP查询
     */
    public static final String URI_SELECT_INFO_BY_WAN_IP = "/v1/store/wanIp";
    /**
     * 根据 store_info.name查询
     */
    public static final String URI_SELECT_INFO_BY_NAME = "/v1/store/name";

    /**
     * 根据 store_info.key查询
     */
    public static final String URI_SELECT_STORE_INFO_BY_KEY = "/v1/store/search";
    /**
     * 查询所有门店信息
     */
    public static final String URI_SELECT_ALL_STORE = "/v1/stores";


    /**
     * 添加门店设备类型
     */
    public static final String URI_INSERT_DEVICE_TYPE = "/v1/store/deviceType";
    public static final String URI_UPDATE_DEVICE_TYPE = "/v1/store/deviceType";
    public static final String URI_DELETE_DEVICE_TYPE = "/v1/store/deviceType";
    /**
     * 查询设备类型
     */
    public static final String URI_SELECT_DEVICE_TYPE = "/v1/store/deviceTypes";



    /**
     * 新增门店设备
     */
    public static final String URI_INSERT_STORE_DEVICE = "/v1/storeDevice";
    public static final String URI_BATCH_INSERT_STORE_DEVICE = "/v1/storeDevice/batch";
    /**
     * 修改门店设备
     */
    public static final String URI_UPDATE_STORE_DEVICE = "/v1/storeDevice";
    /**
     * 删除门店设备
     */
    public static final String URI_DELETE_STORE_DEVICE = "/v1/storeDevice";
    /**
     * 查询门店设备列表
     */
    public static final String URI_SELECT_STORE_DEVICES = "/v1/storeDevice/search";
    public static final String URI_SELECT_STORE_DEVICES_BY_MAC = "/v1/storeDevice/mac";
    public static final String URI_SELECT_STORE_DEVICES_BY_SERIAL_ID = "/v1/storeDevice/serialId";
    public static final String URI_SELECT_STORE_DEVICES_BY_DEVICE_TYPE_MAC = "/v1/storeDevice/deviceType/mac";

    /**
     * 查询客户端信息
     */
    public static final String URI_SELECT_CONFIG_INFO = "/v1/config";


    /**
     * 查询所有配置服务名称
     */
    public static final String URI_SELECT_SERVICE_CONFIG = "/v1/configs";

    /**
     * 插入服务配置信息
     */
    public static final String URI_INSERT_CONFIG_INFO = "/v1/config";
    /**
     * 更新服务配置信息
     */
    public static final String URI_UPDATE_CONFIG_INFO = "/v1/config";


//    public static final String URI_SELECT_CONFIG_BIND_INFO = "/v1/config/bind/{configId}";
    /**
     * 查询所有绑定信息
     */
    public static final String URI_SELECT_ALL_CONFIG_BIND = "/v1/config/binds";
    /**
     * 添加配置名的例外项目
     */
    public static final String URI_INSERT_CONFIG_BIND_INFO = "/v1/config/bind";
    /**
     * 修改配置名的例外项目
     */
    public static final String URI_UPDATE_CONFIG_BIND_INFO = "/v1/config/bind";
    /**
     * 删除配置名的例外项目
     */
    public static final String URI_DELETE_CONFIG_BIND_INFO = "/v1/config/bind";



    public static final String URI_SELECT_CONFIG_ITEM_INFO = "/v1/config/items";
    /**
     * 添加配置项目
     */
    public static final String URI_INSERT_CONFIG_ITEM_INFO = "/v1/config/item";
    /**
     * 修改配置项目
     */
    public static final String URI_UPDATE_CONFIG_ITEM_INFO = "/v1/config/item";
    /**
     * 删除配置项目
     */
    public static final String URI_DELETE_CONFIG_ITEM_INFO = "/v1/config/item";

    /**
     * 根据 store_info.store_id查询 房间列表
     */
    public static final String URI_SELECT_STORE_ROOMLIST_BY_STORE_ID = "/v1/store/roomlist";

    /**
     * 添加房间
     */
    public static final String URI_INSERT_STORE_ROOMLIST_BY_STORE_ID = "/v1/store/room";
    /**
     * 更新房间
     */
    public static final String URI_UPDATE_STORE_ROOMLIST_BY_STORE_ID = URI_INSERT_STORE_ROOMLIST_BY_STORE_ID;
    /**
     * 删除房间
     */
    public static final String URI_DELETE_STORE_ROOMLIST_BY_STORE_ID = URI_INSERT_STORE_ROOMLIST_BY_STORE_ID;
}
