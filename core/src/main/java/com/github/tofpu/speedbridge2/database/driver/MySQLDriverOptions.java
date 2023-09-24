package com.github.tofpu.speedbridge2.database.driver;

class MySQLDriverOptions implements DriverOptions {
    private final String host, database, username, password;

    public MySQLDriverOptions(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public String connectionUrl() {
        return String.format("jdbc:mysql://%s/%s", host, database);
    }

    @Override
    public String dialect() {
        return "org.hibernate.dialect.MySQL5Dialect";
    }

    @Override
    public String driverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

}
