package nl.bioinf.imgorter.primer_evaluator.model;

import java.util.UUID;

import dev.array21.jdbd.DatabaseDriver;
import dev.array21.jdbd.datatypes.PreparedStatement;
import dev.array21.jdbd.datatypes.SqlRow;
import dev.array21.jdbd.exceptions.SqlException;
import nl.bioinf.imgorter.primer_evaluator.model.annotations.Nullable;

public class SessionBroker {

    private DatabaseDriver database;

    public SessionBroker(DatabaseDriver database) {
        this.database = database;
    }

    /**
     * Create a session with a specific expiry
     * @param expiry timestamp
     * @return String session ID
     * @throws SqlException Thrown when the database query throws an exception
     */
    public String createSession(long expiry) throws SqlException {
        UUID sessionIdent = UUID.randomUUID();
        PreparedStatement pr = new PreparedStatement("INSERT INTO sessions (id, expiry) VALUES ('?', '?')");
        pr.bind(0, sessionIdent.toString());
        pr.bind(1, expiry);

        this.database.execute(pr);

        return sessionIdent.toString();
    }

    /**
     * Get the expiry of a particular session
     * @param sessionId String session id
     * @return timestamp of the session or null when there is not a session made yet
     * @throws SqlException Thrown when the database query throws an exception
     */
    @Nullable
    public Long getSessionExpiry(String sessionId) throws SqlException {
        PreparedStatement pr = new PreparedStatement("SELECT id,expiry FROM sessions WHERE id = '?'");
        pr.bind(0, sessionId);

        SqlRow[] resultSet = this.database.query(pr);
        if(resultSet.length == 0) {
            return null;
        }

        SqlRow row = resultSet[0];
        String id = row.getString("id");
        if(!sessionId.equals(id)) {
            return null;
        }

        Long expiry = row.getLong("expiry");

        return expiry;
    }
}