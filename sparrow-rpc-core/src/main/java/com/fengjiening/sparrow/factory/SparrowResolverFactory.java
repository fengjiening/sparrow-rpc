package com.fengjiening.sparrow.factory;

import com.fengjiening.sparrow.config.vo.RemotingCommand;
import com.fengjiening.sparrow.contsants.CommonConstant;
import com.fengjiening.sparrow.enums.ResolverType;
import com.fengjiening.sparrow.resolver.SparrowResolver;
import com.fengjiening.sparrow.utill.SparrowOptional;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @ClassName: SparrowResolverFactory
 * @Description: 数据交互工厂
 * @Date: 2022/10/27 19:27
 * @Author: fengjiening::joko
 * @Version: 1.0
 */
public class SparrowResolverFactory implements SparrowFactory{
    private static final SparrowResolverFactory resolverFactory = new SparrowResolverFactory();
    private static final List<SparrowResolver> resolvers = new CopyOnWriteArrayList<>();
    private static RulesEngine rulesEngine = new DefaultRulesEngine();
    public static SparrowResolverFactory getInstance(){
        return resolverFactory;
    }

    public int addSparrowResolver(SparrowResolver resolver){
      resolvers.add(resolver);
      return resolvers.size();
    }

    public SparrowResolver getResolver(RemotingCommand command) {
        ResolverType type=command.getResolverType();
        //获取解析器
        Optional<SparrowResolver> sparrowResolverOptional= resolvers.stream().filter(resolver -> type.equals(resolver.resolverType())).findFirst();
        SparrowResolver sparrowResolver = sparrowResolverOptional.get();
        //校验
        SparrowOptional.ture(sparrowResolver==null).throwsException(
                CommonConstant.SPARROW_TYPE_NOT_FIND_ERROR_CODE,
                        String.format("%s is unsupported",type));

        //刷新数据
        sparrowResolver.flushData(command);
        return sparrowResolver;
    }

}
