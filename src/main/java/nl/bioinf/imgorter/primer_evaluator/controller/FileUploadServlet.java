package nl.bioinf.imgorter.primer_evaluator.controller;

import com.google.gson.Gson;
import dev.array21.jdbd.exceptions.SqlException;
import nl.bioinf.imgorter.primer_evaluator.config.WebConfig;
import nl.bioinf.imgorter.primer_evaluator.controller.startup.Application;
import nl.bioinf.imgorter.primer_evaluator.model.*;
import nl.bioinf.imgorter.primer_evaluator.model.gson.FileUploadResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

@MultipartConfig(location="C:\\Users\\Iris\\Documents\\",
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)

@WebServlet(name = "FileUploadServlet", urlPatterns = "/data_upload", initParams =
        {
        @WebInitParam(name = "upload_dir", value = "C:\\Users\\Iris\\Documents\\"),
        })

/**
 * Servlet used to handle all file uploads
 */

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
        //code to return to result page in case of Get request
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        templateEngine.process("results", ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        String fileName = "";
        File fileSaveDir = new File(this.uploadDir);
        if (! fileSaveDir.exists()) {
            throw new IllegalStateException("Upload directory does not exist: " + this.uploadDir);
        }

        File generatedFile = File.createTempFile("primer_evaluator_upload", ".txt");
        for (Part part : request.getParts()) {
            // Upload all contents from the uploaded file that are now stored in a temporary file to a permanent file
            fileName  = this.uploadDir + File.separator + generatedFile.getName();
            part.write(fileName);
        }

        // Open file
        FileInputHandler FI = new FileInputHandler(fileName);
        // Get sequences
        String[] sequences = FI.readFile().split("\n");

        String primerASeq = "";
        String primerBSeq = "";

        primerASeq = sequences[0];
        // Primer B is optional, the second line in the file will be empty and therefore no sequences[1]
        try{
            primerBSeq = sequences[1];
        }
        catch (IndexOutOfBoundsException IOBexception){
            // catch the exception
            System.out.println("no second primer given");
        }

        PrimerEvaluator evaluator = new PrimerEvaluator();

        Primer primerA = new Primer(primerASeq);
        // Check input validity before calculating
        primerA.setIsValidSequence(InputValidator.checkValidSequence(primerA.getSequence()));
        primerA.setIsValidNucs(InputValidator.checkValidNucleotides(primerA.getBaseSequence()));
        primerA.setIsValidLength(InputValidator.checkValidLength(primerA.getBaseSequence()));

        // Evaluator checks if one of the above validity checks returns false
        // If false, the rest will not be returned.
        PrimerResult[] resultsA = evaluator.evaluateOne(primerA);
        // Primer B is optional, will be filled in if-statement
        PrimerResult[] resultsB = null;



        // If second primer exists
        if(primerBSeq != ""){
            Primer primerB = new Primer(primerBSeq);
            // Validity check of the primer
            primerB.setIsValidSequence(InputValidator.checkValidSequence(primerB.getSequence()));
            primerB.setIsValidNucs(InputValidator.checkValidNucleotides(primerB.getBaseSequence()));
            primerB.setIsValidLength(InputValidator.checkValidLength(primerB.getBaseSequence()));
            // Make a paired list of both results so evaluateOne can be reused and to calculate the intermol identity
            Pair<PrimerResult[], PrimerResult[]> results = evaluator.evaluateTwo(primerA, primerB);
            // return the PrimerResult list of primerB
            resultsB = results.getB();
        }

        // Send back to front end using GSON and Json
        FileUploadResponse r = new FileUploadResponse(resultsA, resultsB);

        String rJson = GSON.toJson(r);

        String js = "" +
            "<script>\n" +
                "let json = '" + rJson + "'\n" +
                "window.sessionStorage.setItem('results', json)\n" +
                "window.location.href = 'results'\n" +
            "</script>\n";

        response.getWriter().write(js);
        response.getWriter().flush();
        response.setHeader("Content-Type", "text/html");

    }
}