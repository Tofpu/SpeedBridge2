package com.github.tofpu.speedbridge2.database;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "demo_entities_with_uuid")
public class DemoEntity {

    @Id
    @Type(type = "com.github.tofpu.speedbridge2.database.customtypes.UUIDType")
    private UUID id;
    private int number;

    public DemoEntity() {
        this.id = UUID.randomUUID();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DemoEntity that = (DemoEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + number;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DemoEntity.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("number=" + number)
            .toString();
    }
}
