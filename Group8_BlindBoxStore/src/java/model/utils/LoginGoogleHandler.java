package model.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;
import model.dto.UserDTO;
import model.dto.UserGoogleDTO;

/**
 * @author heaty566
 */
@WebServlet(urlPatterns = {"/LoginGoogleHandler"})
public class LoginGoogleHandler extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String code = request.getParameter("code");
    String accessToken = null;
    
    try {
        if (code != null && !code.isEmpty()) {
            accessToken = LoginGoogleHandler.getToken(code);
        }

        if (accessToken != null && !accessToken.isEmpty()) {
            UserGoogleDTO googleUser = LoginGoogleHandler.getUserInfo(accessToken);
            if (googleUser != null) {
                System.out.println("Google User: " + googleUser.toString());
                request.setAttribute("USER", googleUser);
                request.getRequestDispatcher("/searchPage.jsp").forward(request, response);
            } else {
                throw new IOException("Failed to fetch Google user information.");
            }
        } else {
            throw new IOException("Access token is null or empty.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("ERROR", "Google Login failed: " + e.getMessage());
        request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
    }
}

public static String getToken(String code) throws ClientProtocolException, IOException {
    String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
            .bodyForm(Form.form()
                    .add("client_id", Constants.GOOGLE_CLIENT_ID)
                    .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                    .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI)
                    .add("code", code)
                    .add("grant_type", Constants.GOOGLE_GRANT_TYPE)
                    .build())
            .execute().returnContent().asString();

    System.out.println("Google Response: " + response);  // Debugging

    JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
    String accessToken = jobj.get("access_token").getAsString();
    return accessToken;
}

public static UserGoogleDTO getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
    String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
    String response = Request.Get(link).execute().returnContent().asString();
    System.out.println("Google User Info Response: " + response);

    if (response == null || response.isEmpty()) {
        throw new IOException("Received empty response from Google.");
    }

    JsonObject userJson = new Gson().fromJson(response, JsonObject.class);

    if (userJson == null) {
        throw new IOException("Failed to parse Google response.");
    }

    // Đảm bảo kiểm tra các trường trước khi gán
    UserGoogleDTO googleUser = new UserGoogleDTO();
    googleUser.setId(userJson.has("id") ? userJson.get("id").getAsString() : null);
    googleUser.setEmail(userJson.has("email") ? userJson.get("email").getAsString() : null);
    googleUser.setVerified_email(userJson.has("verified_email") && userJson.get("verified_email").getAsBoolean());
    googleUser.setName(userJson.has("name") ? userJson.get("name").getAsString() : null);
    googleUser.setGiven_name(userJson.has("given_name") ? userJson.get("given_name").getAsString() : null);
    googleUser.setFamily_name(userJson.has("family_name") ? userJson.get("family_name").getAsString() : null);
    googleUser.setPicture(userJson.has("picture") ? userJson.get("picture").getAsString() : null);

    return googleUser;
}


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the +
    // sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
