package nl.bioinf.imgorter.primer_evaluator.controller;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dev.array21.jdbd.exceptions.SqlException;
import nl.bioinf.imgorter.primer_evaluator.controller.startup.Application;
import nl.bioinf.imgorter.primer_evaluator.model.WebUtil;
import nl.bioinf.imgorter.primer_evaluator.model.gson.SessionCreatedResponse;

/**
 * Class used to create the first session when first launched
 */

@WebServlet(urlPatterns = "/session/create")
public class CreateSessionServlet extends HttpServlet {
    private static final long serialVersionUID = 7227066937321193295L;
    private static final Gson GSON = new Gson();

    public void doOptions(HttpServletRequest req, HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        process(req, res);
    }

    public void process(HttpServletRequest req, HttpServletResponse res) throws IOException {

        long expiry = Instant.now().getEpochSecond() + 3600;

        String sessionId;
        try {
            sessionId = Application.sessionBroker.createSession(expiry);
        } catch(SqlException e) {
            WebUtil.returnError(res, "internal", "An internal error occurred", 500);
            System.err.println(e);
            return;
        }

        SessionCreatedResponse resp = new SessionCreatedResponse(sessionId, expiry);
        String responsePayload = GSON.toJson(resp);

        res.getWriter().write(responsePayload);
        res.getWriter().flush();
        res.setStatus(200);
        res.addCookie(new Cookie("sessionid", sessionId));
    }
}
