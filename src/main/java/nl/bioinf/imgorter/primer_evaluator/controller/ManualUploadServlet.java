package nl.bioinf.imgorter.primer_evaluator.controller;

import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
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

    @Override
    public void init() throws ServletException {
        final ServletContext servletContext = this.getServletContext();
        WebConfig.createTemplateEngine(servletContext);
        this.templateEngine = WebConfig.createTemplateEngine(servletContext);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String primer_a = request.getParameter("primer_a");
        String primer_b = request.getParameter("primer_b");
        Locale locale = request.getLocale();
    }
}