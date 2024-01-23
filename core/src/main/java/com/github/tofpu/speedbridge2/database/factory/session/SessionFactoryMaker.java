package com.github.tofpu.speedbridge2.database.factory.session;

import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.util.ReflectionUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.Properties;

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
        ReflectionUtil.getTypesAnnotatedWith(packageName, Entity.class).forEach(aClass -> {
            System.out.println("adding " + aClass);
            configuration.addAnnotatedClass(aClass);
        });
        ReflectionUtil.getTypesAnnotatedWith(packageName, Embeddable.class).forEach(aClass -> {
            System.out.println("adding " + aClass);
            configuration.addAnnotatedClass(aClass);
        });

        return configuration
            .setProperties(properties)
            .buildSessionFactory();
    }
}
