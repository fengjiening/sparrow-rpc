package com.fengjiening.sparrow.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SparrowCode
 * @Description: TODO
 * @Date: 2022/10/26 10:51
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
public class SparrowCode implements Serializable {
    private String code;
}
