package nl.bioinf.imgorter.primer_evaluator.controller.startup;

import nl.bioinf.imgorter.primer_evaluator.model.annotations.Nullable;


/**
 * Database properties configuration that are retrieved from commandline arguments (in case of war) or
 * environmental variables in case of run from IDE
 */

public class Configuration {

    private final String host = System.getenv("DB_HOST");
    private final String username = System.getenv("DB_USERNAME");
    private final String password = System.getenv("DB_PASSWORD");
    private final String database = System.getenv("DB_DATABASE");

    @Nullable
    public String getHost() {
        return this.host;
    }

    @Nullable
    public String getUsername() {
        return this.username;
    }

    @Nullable
    public String getPassword() {
        return this.password;
    }

    @Nullable
    public String getDatabase() {
        return this.database;
    }
}