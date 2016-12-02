package service;

import org.hibernate.Session;

public class Client {
    public Session getSession() {
        return session;
    }

    public Session session = null;

    public Client(Session session) {
        this.session = session;
    }
}
