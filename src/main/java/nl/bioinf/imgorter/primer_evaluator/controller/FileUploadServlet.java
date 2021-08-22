package nl.bioinf.imgorter.primer_evaluator.controller;

import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
import nl.bioinf.imgorter.primer_evaluator.model.*;
import nl.bioinf.imgorter.primer_evaluator.model.gson.ManualUploadResponse;
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
        try{
            String sequenceFile = request.getParameter("sequence_file");
            FileInputHandler fh = new FileInputHandler(sequenceFile);
            InputValidator IV = new InputValidator();
            String fileSequences = fh.readFile();
            System.out.println(fileSequences);
            IV.isValid(fileSequences);



        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
