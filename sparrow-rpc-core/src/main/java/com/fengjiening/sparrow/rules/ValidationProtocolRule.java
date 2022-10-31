package com.fengjiening.sparrow.rules;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Rule;

/**
 * @ClassName: ValidationProtocolRules
 * @Description: 校验协议
 * @Date: 2022/10/28 21:10
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@Rule(name = "ValidationProtocolRule", description = "ValidationProtocolRule")
public class ValidationProtocolRule extends SparrowRule{

    public ValidationProtocolRule(RemotingCommand command) {
        super(command);
    }
    @Action
    public void then() throws Exception {

    }


}
