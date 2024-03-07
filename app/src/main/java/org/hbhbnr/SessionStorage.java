package org.hbhbnr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {

    private final Map<UUID, Session> sessions;

    public SessionStorage(HashMap<String, Session> sessions) {
        this.sessions = new ConcurrentHashMap<>();
    }

    public Session getSession(final Session session) {
        return sessions.put(session.getID(), session);
    }

    public Session getSessionOrNull(final UUID sessionID) {
        return sessions.getOrDefault(sessionID, null);
    }

}
