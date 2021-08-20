package nl.bioinf.imgorter.primer_evaluator.controller;

import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
import nl.bioinf.imgorter.primer_evaluator.model.Evaluator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@WebServlet(name = "FileUploadServlet", urlPatterns = "/file_upload.do")

public class FileUploadServlet extends HttpServlet{
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        final ServletContext servletContext = this.getServletContext();
        WebConfig.createTemplateEngine(servletContext);
        this.templateEngine = WebConfig.createTemplateEngine(servletContext);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sequenceFile = request.getParameter("sequence_file");
        Locale locale = request.getLocale();
        WebContext ctx = new WebContext(request, response, request.getServletContext(), locale);
        if (sequenceFile != null) {
            ctx.setVariable("sequenceFile", sequenceFile);
        }
    }
}
