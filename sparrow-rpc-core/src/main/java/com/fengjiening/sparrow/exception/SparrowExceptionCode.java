package com.fengjiening.sparrow.exception;

import com.fengjiening.sparrow.enums.SparrowCode;
import com.fengjiening.sparrow.contsants.CommonConstant;

/**
 * @ClassName: SparrowException
 * @Description: 编码
 * @Date: 2022/10/26 10:47
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public interface SparrowExceptionCode {
    public static final SparrowCode FILE_NOT_FIND_CODE = new SparrowCode(CommonConstant.NO_ERROR_CODE);
}
