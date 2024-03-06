package org.hbhbnr;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Session {

    private final UUID sessionID;
    private Instant expires;

    public Session() {
        this.sessionID = UUID.randomUUID();
        this.expires = Instant.now().plus(24, ChronoUnit.HOURS);
    }

    public UUID getID() {
        return sessionID;
    }

    public boolean isExpired() {
        return expires.isBefore(Instant.now());
    }

    @Override
    public String toString() {
        return sessionID.toString();
    }

}
