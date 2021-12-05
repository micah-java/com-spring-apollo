package com.apollo.listener;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ApolloListener {

    @ApolloConfigChangeListener
    public void apolloConfigChange(ConfigChangeEvent event){
        String namespace = event.getNamespace();
        System.out.println("namespace: " + namespace);
        Set<String> changedKeys = event.changedKeys();
        changedKeys.forEach(key -> {
            ConfigChange change = event.getChange(key);
            System.out.println(change);
        });
    }

















}
