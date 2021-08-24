package nl.bioinf.imgorter.primer_evaluator.controller;

import com.google.gson.Gson;
import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
import nl.bioinf.imgorter.primer_evaluator.model.*;
import nl.bioinf.imgorter.primer_evaluator.model.gson.FileUploadResponse;
import nl.bioinf.imgorter.primer_evaluator.model.gson.ManualUploadResponse;
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
    private static final Gson GSON = new Gson();


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

        String primerASeq = "";
        String primerBSeq = "";

        primerASeq = sequences[0];
        try{
            primerBSeq = sequences[1];
        }
        catch (IndexOutOfBoundsException IOBexception){
            System.out.println("no second primer");
        }

        System.out.println(primerASeq);
        System.out.println(primerBSeq);

        PrimerEvaluator evaluator = new PrimerEvaluator();


        Primer primerA = new Primer(primerASeq);
        // Check input validity before calculating
        System.out.println("getsequence of primer A used: " + primerA.getSequence());
        primerA.setValidSequence(InputValidator.checkValidSequence(primerA.getSequence()));
        //System.out.println(primerA.getValidSequence());
        primerA.setValidNucs(InputValidator.checkValidNucleotides(primerA.getBaseSequence()));
        //System.out.println(primerA.getValidNucs());
        primerA.setValidLength(InputValidator.checkValidLength(primerA.getBaseSequence()));
        //System.out.println(primerA.getValidLength());

        // Evaluator checks if one of the above validity checks returns false
        // If false, the rest will not be returned.
        PrimerResult[] resultsA = evaluator.evaluateOne(primerA);
        PrimerResult[] resultsB = null;



        if(primerBSeq != ""){
            System.out.println("primer B exists");
            Primer primerB = new Primer(primerBSeq);
            System.out.println("getsequence of primer B used: " + primerB.getSequence());
            InputValidator.checkValidSequence(primerB.getSequence());
            InputValidator.checkValidNucleotides(primerB.getBaseSequence());
            InputValidator.checkValidLength(primerB.getBaseSequence());
            Pair<PrimerResult[], PrimerResult[]> results = evaluator.evaluateTwo(primerA, primerB);
        }

        FileUploadResponse r = new FileUploadResponse(resultsA, resultsB);

        String rJson = GSON.toJson(r);

        System.out.println(rJson);

        response.getWriter().write(rJson);
        response.getWriter().flush();
        response.setHeader("Content-Type", "application/json");


        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        ctx.setVariable("message", "upload successfull, processed data.");
        templateEngine.process("results", ctx, response.getWriter());
    }
}