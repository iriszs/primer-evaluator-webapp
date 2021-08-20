package nl.bioinf.imgorter.primer_evaluator.controller;

import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
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

        String primer_a_seq = "5'-" + request.getParameter("primer_a") + "-3'";
        // a second primer is not required
        if (request.getParameter("primer_b") != "") {
            String primer_b_seq = "3'-" + request.getParameter("primer_b") + "-5'";
            System.out.println(primer_b_seq);

        }
        System.out.println(primer_a_seq);
        Locale locale = request.getLocale();

        WebContext ctx = new WebContext(request, response, request.getServletContext(), locale);

    }
}