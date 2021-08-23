package nl.bioinf.imgorter.primer_evaluator.controller;

import jdk.internal.util.xml.impl.Input;
import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
import nl.bioinf.imgorter.primer_evaluator.model.FileInputHandler;
import nl.bioinf.imgorter.primer_evaluator.model.InputValidator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

@MultipartConfig(location="C:\\Users\\Iris\\Documents\\",
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)

@WebServlet(name = "FileUploadServlet", urlPatterns = "/data_upload", initParams =
        {
        @WebInitParam(name = "upload_dir", value = "C:\\Users\\Iris\\Documents\\"),
        })

public class FileUploadServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private String uploadDir;

    @Override
    public void init() throws ServletException {
        this.templateEngine = WebConfig.createTemplateEngine(getServletContext());
        this.uploadDir = getInitParameter("upload_dir");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("in do get");
        //simply go back to upload form
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        templateEngine.process("results", ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = "";
        System.out.println("in do post");
        File fileSaveDir = new File(this.uploadDir);
        System.out.println(fileSaveDir);
        if (! fileSaveDir.exists()) {
            throw new IllegalStateException("Upload dir does not exist: " + this.uploadDir);
        }

        File generatedFile = File.createTempFile("primer_evaluator_upload", ".txt");
        for (Part part : request.getParts()) {
            fileName  = this.uploadDir + File.separator + generatedFile.getName();
            part.write(fileName);
        }

        FileInputHandler FI = new FileInputHandler(fileName);
        StringBuilder inputText = FI.readFile();

        String[] sequences = inputText.toString().split("\n");

        String primerA = "";
        String primerB = "";

        primerA = sequences[0];
        try{
            primerB = sequences[1];
        }
        catch (IndexOutOfBoundsException IOBexception){
            System.out.println("no second primer");
        }

        System.out.println(primerA);
        System.out.println(primerB);

        //dan de test logica voor primerA
        InputValidator IV = new InputValidator();
        InputValidator.isValid(primerA);
        if(primerB != ""){
            System.out.println("primer B exists");
            InputValidator.isValid(primerB);
        }



        // if primerB != "" dan de test logica voor primerB


        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        ctx.setVariable("message", "upload successfull, processed data.");
        templateEngine.process("results", ctx, response.getWriter());
    }
}