package com.github.tofpu.speedbridge2.lobby;

import com.github.tofpu.speedbridge2.object.Position;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity(name = "lobby")
@Table(name = "lobbies")
@Data
@NoArgsConstructor(force = true)
public class Lobby {

    @Id
    private final int id;

    @Embedded
    @NotNull
    private final Position position;

    public Lobby(int id, @NotNull Position position) {
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }
}
