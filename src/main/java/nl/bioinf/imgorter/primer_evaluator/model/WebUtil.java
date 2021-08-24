package nl.bioinf.imgorter.primer_evaluator.model;

import com.google.gson.Gson;
import nl.bioinf.imgorter.primer_evaluator.model.gson.ErrorResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtil {
    private static final Gson GSON = new Gson();



    /**
     * Utility method to return an error
     * @param response The servletresponse to set the error on
     * @param location Where did the error occured (body, params or internal)
     * @param msg Message telling what is going wrong
     * @param status The HTTP status code
     * @throws IOException Thrown when setting the response body failed
     */
    public static void returnError(HttpServletResponse response, String location, String msg, int status) throws IOException {
        ErrorResponse errResp = new ErrorResponse(location, msg);
        String responseBody = GSON.toJson(errResp);
        response.getWriter().write(responseBody);
        response.getWriter().flush();
        response.setStatus(status);
    }


}
