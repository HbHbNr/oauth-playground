package org.hbhbnr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionStorage {

    private final Map<UUID, Session> sessions;

    public SessionStorage(HashMap<String, Session> sessions) {
        this.sessions = new HashMap<>();
    }

    public Session getSession(final Session session) {
        return sessions.put(session.getID(), session);
    }

    public Session getSession(final UUID sessionID) {
        return sessions.get(sessionID);
    }

    public boolean sessionsExists(final UUID sessionID) {
        return sessions.containsKey(sessionID);
    }

}
