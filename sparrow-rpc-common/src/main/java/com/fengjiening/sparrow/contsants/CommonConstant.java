package com.fengjiening.sparrow.contsants;


public interface CommonConstant {


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
    /**
     * 访问权限认证未通过 510
     */
    public static final Integer SC_JEECG_NO_AUTHZ = 510;

    public static final String NO_ERROR_CODE = "0000";
    //文件找不到
    public static final String FILE_NOT_FIND_CODE = "9020";
    // 解析器找不到
    public static final String SPARROW_TYPE_NOT_FIND_ERROR_CODE = "9021";
    //连接异常
    public static final String SPARROW_TYPE_CONNECT_FAILD_ERROR_CODE = "9091";

}
