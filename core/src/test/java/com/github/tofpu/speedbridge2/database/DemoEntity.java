package com.github.tofpu.speedbridge2.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "entities")
public class DemoEntity {

    @Id
    private final UUID id;
    private int number;

    public DemoEntity() {
        this.id = null;
    }

    public DemoEntity(UUID id, int number) {
        this.id = id;
        this.number = number;
    }

    public void number(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public UUID getId() {
        return id;
    }
}
