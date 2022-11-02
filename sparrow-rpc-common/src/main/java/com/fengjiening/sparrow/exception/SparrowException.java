package com.fengjiening.sparrow.exception;

import com.fengjiening.sparrow.contsants.CommonConstant;

/**
 * @ClassName: SparrowException
 * @Description: TODO
 * @Date: 2022/10/26 10:47
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowException extends RuntimeException {
    protected SparrowCode code;

    public SparrowException(SparrowCode errorCode) {
        this(errorCode, (String)null, (Throwable)null);
    }
    public SparrowException(SparrowCode errorCode, String message) {
        this(errorCode, message, (Throwable)null);
    }
    public SparrowException(SparrowCode errorCode, String message, Throwable cause) {
        super(message, cause);
        if (errorCode == null) {
            this.code = new SparrowCode(CommonConstant.NO_ERROR_CODE);
        } else {
            this.code = errorCode;
        }

    }
    public SparrowException(String errorCode, String message) {
        this(new SparrowCode(errorCode), message, (Throwable)null);
    }

    public SparrowCode getErrorCode() {
        return this.code;
    }

}
