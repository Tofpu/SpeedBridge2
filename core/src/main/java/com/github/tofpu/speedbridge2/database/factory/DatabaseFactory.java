package com.github.tofpu.speedbridge2.database.factory;

import com.github.tofpu.speedbridge2.database.Database;
import com.github.tofpu.speedbridge2.database.DatabaseType;
import com.github.tofpu.speedbridge2.database.OperationType;
import com.github.tofpu.speedbridge2.database.driver.ConnectionDetails;
import com.github.tofpu.speedbridge2.database.driver.DriverOptions;
import com.github.tofpu.speedbridge2.database.impl.AsyncDatabase;
import com.github.tofpu.speedbridge2.database.impl.BasicDatabase;
import jakarta.persistence.Entity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.Properties;
import java.util.Set;

public class DatabaseFactory {
    public Database construct(String packageName, DatabaseType type, OperationType operationType, ConnectionDetails data) {
        SessionFactory factory = getSessionFactory(packageName, type.getDriverOptions(), data);
        switch (operationType) {
            case SYNC -> {
                return new BasicDatabase(factory);
            }
            case ASYNC -> {
                return new AsyncDatabase(new BasicDatabase(factory));
            }
        }
        throw new IllegalArgumentException("Unknown operation type: " + operationType);
    }

    private static SessionFactory getSessionFactory(String packageName, DriverOptions options, ConnectionDetails data) {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", options.connectionUrl(data));

        properties.setProperty("hibernate.dialect", options.dialect());

        properties.setProperty("hibernate.connection.username", "test");
        properties.setProperty("hibernate.connection.password", "test");
        properties.setProperty("hibernate.connection.driver_class", options.driverClass());
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("show_sql", "true");

        Configuration configuration = new Configuration();
        getEntityClasses(packageName).forEach(configuration::addAnnotatedClass);

        return configuration
                .setProperties(properties)
                .buildSessionFactory();
    }

    private static Set<Class<?>> getEntityClasses(String packageName) {
        return new Reflections(packageName).getTypesAnnotatedWith(Entity.class);
    }
}
