package com.fengjiening.sparrow.rules.base;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.manager.CilentManager;
import com.fengjiening.sparrow.manager.SparrowManage;
import io.netty.bootstrap.AbstractBootstrap;
import lombok.AllArgsConstructor;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;

/**
 * @ClassName: InstallRule
 * @Description: TODO
 * @Date: 2022/10/31 16:30
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
@AllArgsConstructor
public abstract class InstallRule {

    @Condition
    public boolean when() {
        return true;
    }
    public abstract void then() throws Exception;
}
