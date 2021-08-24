package nl.bioinf.imgorter.primer_evaluator.controller.startup;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import dev.array21.jdbd.DatabaseDriver;
import dev.array21.jdbd.datatypes.PreparedStatement;
import dev.array21.jdbd.drivers.MysqlDriverFactory;
import dev.array21.jdbd.exceptions.SqlException;
import nl.bioinf.imgorter.primer_evaluator.model.SessionBroker;


/**
 * Class used to start the session collecting and connecting to the database
 */


@WebListener
public class StartupServlet implements ServletContextListener {

    // We have this as a Field so we can stub it in tests
    private MysqlDriverFactory driverFactory = new MysqlDriverFactory();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseDriver dbDriver;
        try {
            dbDriver = loadDatabaseDriver();
        } catch(IOException | RuntimeException e) {
            e.printStackTrace();
            return;
        }

        if(dbDriver == null) {
            return;
        }

        try {
            this.migrate(dbDriver);
        } catch(SqlException e) {
            e.printStackTrace();
            return;
        }

        SessionBroker sb = new SessionBroker(dbDriver);
        Application.sessionBroker = sb;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                dbDriver.unload();
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}

    private DatabaseDriver loadDatabaseDriver() throws IOException {
        Configuration conf = new Configuration();
        return this.driverFactory
                .setHost(conf.getHost())
                .setDatabase(conf.getDatabase())
                .setUsername(conf.getUsername())
                .setPassword(conf.getPassword())
                .build();
    }

    private void migrate(DatabaseDriver driver) throws SqlException {
        driver.execute(new PreparedStatement("CREATE TABLE IF NOT EXISTS sessions (`id` varchar(36) PRIMARY KEY NOT NULL, `expiry` bigint NOT NULL)"));
    }
}
