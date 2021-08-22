package nl.bioinf.imgorter.primer_evaluator.controller;

import com.google.gson.Gson;
import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
import nl.bioinf.imgorter.primer_evaluator.model.Pair;
import nl.bioinf.imgorter.primer_evaluator.model.Primer;
import nl.bioinf.imgorter.primer_evaluator.model.PrimerEvaluator;
import nl.bioinf.imgorter.primer_evaluator.model.PrimerResult;
import nl.bioinf.imgorter.primer_evaluator.model.gson.ManualUploadResponse;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "ManualUploadServlet", urlPatterns = "/manual_upload.do")
public class ManualUploadServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private static final Gson GSON = new Gson();

    @Override
    public void init() throws ServletException {
        final ServletContext servletContext = this.getServletContext();
        WebConfig.createTemplateEngine(servletContext);
        this.templateEngine = WebConfig.createTemplateEngine(servletContext);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Locale locale = request.getLocale();

        try {
            String primerASeq = "5'-" + request.getParameter("a") + "-3'";
            Primer primerA = new Primer(primerASeq);

            PrimerEvaluator evaluator = new PrimerEvaluator();

            PrimerResult[] resultsA = evaluator.evaluateOne(primerA);
            PrimerResult[] resultsB = null;

            // a second primer is not required
            String bValue = request.getParameter("b");
            if (bValue != null && !bValue.isEmpty()) {
                System.out.println(" hier zouden we niet moeten zijn");

                String primerBSeq = "3'-" + bValue + "-5'";
                Primer primerB = new Primer(primerBSeq);
                Pair<PrimerResult[], PrimerResult[]> results = evaluator.evaluateTwo(primerA, primerB);
            }

            ManualUploadResponse r = new ManualUploadResponse(resultsA, resultsB);

            String rJson = GSON.toJson(r);

            System.out.println(rJson);

            response.getWriter().write(rJson);
            response.getWriter().flush();
            response.setHeader("Content-Type", "application/json");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}