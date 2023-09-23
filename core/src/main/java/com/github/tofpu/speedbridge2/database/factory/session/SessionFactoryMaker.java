package com.github.tofpu.speedbridge2.database.factory.session;

import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import java.lang.annotation.Annotation;
import java.util.Properties;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

public class SessionFactoryMaker {

    public static SessionFactory create(String packageName, DriverOptions options) {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", options.connectionUrl());

        properties.setProperty("hibernate.dialect", options.dialect());

        String username = options.username();
        String password = options.password();
        if (username != null && password != null) {
            properties.setProperty("hibernate.connection.username", username);
            properties.setProperty("hibernate.connection.password", password);
        }
        properties.setProperty("hibernate.connection.driver_class", options.driverClass());
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");

        Configuration configuration = new Configuration();
        getTypesAnnotatedWith(packageName, Entity.class).forEach(aClass -> {
            System.out.println("adding " + aClass);
            configuration.addAnnotatedClass(aClass);
        });
        getTypesAnnotatedWith(packageName, Embeddable.class).forEach(aClass -> {
            System.out.println("adding " + aClass);
            configuration.addAnnotatedClass(aClass);
        });

        return configuration
            .setProperties(properties)
            .buildSessionFactory();
    }

    private static Set<Class<?>> getTypesAnnotatedWith(String packageName,
        Class<? extends Annotation> annotation) {
        return new Reflections(packageName).getTypesAnnotatedWith(annotation);
    }
}
