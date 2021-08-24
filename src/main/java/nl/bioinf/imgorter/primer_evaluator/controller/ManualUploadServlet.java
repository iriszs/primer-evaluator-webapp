package nl.bioinf.imgorter.primer_evaluator.controller;

import com.google.gson.Gson;
import dev.array21.jdbd.exceptions.SqlException;
import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
import nl.bioinf.imgorter.primer_evaluator.controller.startup.Application;
import nl.bioinf.imgorter.primer_evaluator.model.*;
import nl.bioinf.imgorter.primer_evaluator.model.gson.ManualUploadResponse;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * Servlet used to handle all manual uploads
 */


@WebServlet(name = "ManualUploadServlet", urlPatterns = "/manual_upload.do")
public class ManualUploadServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private static final Gson GSON = new Gson();

    public void doOptions(HttpServletRequest req, HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
    }

    @Override
    public void init() throws ServletException {
        final ServletContext servletContext = this.getServletContext();
        WebConfig.createTemplateEngine(servletContext);
        this.templateEngine = WebConfig.createTemplateEngine(servletContext);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get session ID
        String sessionId = null;
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals("sessionid")) {
                sessionId = cookie.getValue();
            }
        }

        if(sessionId == null) {
            WebUtil.returnError(response, "cookie", "Missing cookie 'sessionid'", 400);
            return;
        }

        // If the server takes too long to respond, return error code
        Long expiry;
        try {
            expiry = Application.sessionBroker.getSessionExpiry(sessionId);
        } catch(SqlException e) {
            WebUtil.returnError(response, "internal", "An internal error occurred", 500);
            System.err.println(e);
            return;
        }

        if(expiry == null) {
            WebUtil.returnError(response, "cookie", "Unknown session", 403);
            return;
        }

        if(Instant.now().getEpochSecond() > expiry) {
            WebUtil.returnError(response, "cookie", "Session has expired", 403);
            return;
        }

        try {
            // Add 5'- and -3' to the added sequence
            String primerASeq = "5'-" + request.getParameter("a") + "-3'";
            Primer primerA = new Primer(primerASeq);

            PrimerEvaluator evaluator = new PrimerEvaluator();

            // Check input validity before calculating
            // Should never fail here - since all possible errors cannot be submitted because of the way the
            // form requirement is set up
            primerA.setIsValidSequence(InputValidator.checkValidSequence(primerA.getSequence()));
            primerA.setIsValidNucs(InputValidator.checkValidNucleotides(primerA.getBaseSequence()));
            primerA.setIsValidLength(InputValidator.checkValidLength(primerA.getBaseSequence()));
            // Evaluator checks if one of the above validity checks returns false
            // If false, the rest will not be returned.
            PrimerResult[] resultsA = evaluator.evaluateOne(primerA);
            PrimerResult[] resultsB = null;

            // a second primer is not required
            String primerBValue = request.getParameter("b");
            if (primerBValue != null && !primerBValue.isEmpty()) {
                // Primer B is reverse, so add 3'- and -5'
                String primerBSeq = "3'-" + primerBValue + "-5'";
                Primer primerB = new Primer(primerBSeq);
                // Check input validity before calculating
                // Should never fail here - since all possible errors cannot be submitted because of the way the
                // form requirement is set up
                primerB.setIsValidSequence(InputValidator.checkValidSequence(primerB.getSequence()));
                primerB.setIsValidNucs(InputValidator.checkValidNucleotides(primerB.getBaseSequence()));
                primerB.setIsValidLength(InputValidator.checkValidLength(primerB.getBaseSequence()));
                // Make a paired list of both results so evaluateOne can be reused and to calculate the intermol identity
                Pair<PrimerResult[], PrimerResult[]> results = evaluator.evaluateTwo(primerA, primerB);
            }

            // Send back to front end using GSON and Json
            ManualUploadResponse r = new ManualUploadResponse(resultsA, resultsB);

            String rJson = GSON.toJson(r);

            response.getWriter().write(rJson);
            response.getWriter().flush();
            response.setHeader("Content-Type", "application/json");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}