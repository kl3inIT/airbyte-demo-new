package com.company.airbyte.utils;

import io.jmix.core.EntitySerialization;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class EntitySerializationUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EntitySerializationUtils.applicationContext = applicationContext;
    }

    public static EntitySerialization getEntitySerialization() {
        if (applicationContext != null) {
            try {
                return applicationContext.getBean(EntitySerialization.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
