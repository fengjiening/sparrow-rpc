package com.fengjiening.sparrow.exception;

import com.fengjiening.sparrow.enums.SparrowCode;

/**
 * @ClassName: SparrowException
 * @Description: 编码
 * @Date: 2022/10/26 10:47
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public interface SparrowExceptionCode {
    public static final SparrowCode REQUEST = new SparrowCode((short)1);
}
