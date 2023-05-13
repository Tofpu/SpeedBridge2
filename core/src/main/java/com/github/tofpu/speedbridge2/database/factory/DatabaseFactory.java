package com.github.tofpu.speedbridge2.database.factory;

import com.github.tofpu.speedbridge2.database.Database;
import org.hibernate.SessionFactory;

public abstract class DatabaseFactory<T extends Database> {

    public abstract T create(SessionFactory sessionFactory);
}
