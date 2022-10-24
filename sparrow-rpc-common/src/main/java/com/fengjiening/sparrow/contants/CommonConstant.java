package com.fengjiening.sparrow.contants;


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
     * 访问权限认证未通过 510
     */
    public static final Integer SC_JEECG_NO_AUTHZ = 510;

}
