package com.fengjiening.sparrow.manager;

import com.fengjiening.sparrow.rules.client.InitPoolRule;
import com.fengjiening.sparrow.rules.base.InstallRule;
import com.fengjiening.sparrow.rules.client.LoadBalanceRule;
import com.fengjiening.sparrow.rules.client.LoadRegisterRule;
import io.netty.bootstrap.AbstractBootstrap;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: EngineManager
 * @Description: 引擎管理器
 * @Date: 2022/10/28 20:57
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class EngineManager implements SparrowManage{
    public static List<InstallRule> rules=new ArrayList<>();
    private  RulesEngine rulesEngine ;
    private Facts facts = new Facts();
    {
        facts.put("sparrow", true);
    }
    public  EngineManager(){
        this.rulesEngine=createEngine();
    }

    public static List<InstallRule>  installserverApp(AbstractBootstrap boot){

        return rules;
    }
    public static List<InstallRule>  installCilentApp(){
        rules.add(new InitPoolRule());
        rules.add(new LoadBalanceRule());
        rules.add(new LoadRegisterRule());
        return rules;
    }
    private RulesEngine createEngine() {
        return new DefaultRulesEngine();
    }

    public void execute(List<InstallRule>  rules){
        Rules ruleManager = new Rules();
        rules.stream().forEach((rule)->{
            ruleManager.register(rule);
        });
        if(Objects.isNull(rulesEngine)){
            this.rulesEngine=createEngine();
        }
        this.rulesEngine.fire(ruleManager,facts);
    }


}
