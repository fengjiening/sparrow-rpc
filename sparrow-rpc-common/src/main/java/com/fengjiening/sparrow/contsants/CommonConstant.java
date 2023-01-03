package com.fengjiening.sparrow.contsants;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface CommonConstant {
    /**
     * 注册超时时间，60s
     */
    public static final long REGISTER_TIMEOUT = 60 * 1000;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    /**
     * {@code 520 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final String INTERNAL_SERVER_ERROR_500 ="520";
    /**
     * {@code 404 not found} (HTTP/1.0 - RFC 1945)
     */
    public static final String SC_404 ="404";
    /**
     * {@code 404 Biz Error} (HTTP/1.0 - RFC 1945)
     */
    public static final String SC_500 ="500";

    /**
     * {@code 0 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final String SC_OK = "200";

    /**
     * 配置常量
     */
    public static final String  PROPERTIES_CONSTANT_RESOLVER= "sparrow.rpc.resolver";
    public static final String  PROPERTIES_TIME_OUT= "sparrow.rpc.timeout";
    public static final String  SERVICE_DIRECTORY= "META-INF/sparrow-ext/";

    /**
     * 访问权限认证未通过 510
     */
    public static final Integer SC_JEECG_NO_AUTHZ = 510;

    public static final String NO_ERROR_CODE = "0000";
    //配置找不到
    public static final String PROPERTIES_NOT_FIND_CODE = "9021";
    //文件找不到
    public static final String FILE_NOT_FIND_CODE = "9020";
    // 解析器找不到
    public static final String SPARROW_TYPE_NOT_FIND_ERROR_CODE = "9021";
    //连接异常
    public static final String SPARROW_TYPE_CONNECT_FAILD_ERROR_CODE = "9091";
    //通用错误
    public static final String SPARROW_TYPE_COMMON_ERROR_CODE = "9500";
    /**
     * zookeeper配置
     */
    //节点根路径
    public static final String  ZOOKEEPER_ROOT_PATH = "/sparrow/services";
    public static final int ZOOKEEPER_SESSION_TIMEOUT = 30000;
    public static final int ZOOKEEPER_CONNECTION_TIMEOUT = 30000;
}
