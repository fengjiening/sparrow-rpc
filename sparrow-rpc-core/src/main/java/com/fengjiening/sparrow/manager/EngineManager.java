package com.fengjiening.sparrow.manager;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import java.util.Objects;

/**
 * @ClassName: EngineManager
 * @Description: 引擎管理器
 * @Date: 2022/10/28 20:57
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class EngineManager {

    private  RulesEngine rulesEngine ;
    private Facts facts = new Facts();
    {
        facts.put("sparrow", true);
    }

    public EngineManager with(RulesEngine rulesEngine){
        this.rulesEngine=createEngine();
        return this;
    }

    private RulesEngine createEngine() {
        return new DefaultRulesEngine();
    }
    public void execute(Rules rules){
        if(Objects.isNull(rulesEngine)){
            this.rulesEngine=createEngine();
        }
        this.rulesEngine.fire(rules,facts);
    }
}
