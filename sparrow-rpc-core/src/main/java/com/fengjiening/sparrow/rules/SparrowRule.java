package com.fengjiening.sparrow.rules;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import lombok.AllArgsConstructor;
import org.jeasy.rules.annotation.Condition;

/**
 * @ClassName: SparrowRule
 * @Description: TODO
 * @Date: 2022/10/28 21:11
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@AllArgsConstructor
public class SparrowRule {
    protected RemotingCommand command;

    @Condition
    public boolean when() {
        return true;
    }
}
