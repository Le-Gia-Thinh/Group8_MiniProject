package controller;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.dto.UserGoogleDTO;
import model.utils.Constants;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;
import model.dao.*;
import model.dto.*;

@WebServlet(urlPatterns = {"/LoginGoogleHandler"})
public class LoginGoogleHandler extends HttpServlet {
    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = "index.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
    String code = request.getParameter("code");
    String url = ERROR;

    try {
        String accessToken = getToken(code);
        UserGoogleDTO userG = getUserInfo(accessToken);
        System.out.println("UserGoogleDTO: " + userG);

        if (userG != null) {
            UserDAO dao = new UserDAO();
            UserDTO existingUser = dao.checkEmail(userG.getEmail());
            if (existingUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("LOGIN_USER", existingUser);
                    Cookie userCookie = new Cookie("userID", existingUser.getUserID());
                    userCookie.setMaxAge(60 * 60 * 3200); 
                    response.addCookie(userCookie);
                url = SUCCESS; 
            } else {
                Random rand = new Random();
                String userID;
                do {
                    int num = rand.nextInt(1000);
                    userID = "userGG" + num;
                } while (dao.checkDuplicate(userID));
                UserDTO newUser = new UserDTO(userID, "", userG.getName(), userG.getEmail(), "", "", "USER", true);
                boolean checkInsert = dao.createUser(newUser);
                if (checkInsert) {
                    HttpSession session = request.getSession();
                    session.setAttribute("LOGIN_USER", newUser);
                        Cookie userCookie = new Cookie("userID", newUser.getUserID());
                        userCookie.setMaxAge(60 * 60 * 3200); 
                        response.addCookie(userCookie);
                    url = SUCCESS; 
                } else {
                    request.setAttribute("ERROR", "Failed to create user in database.");
                }
            }
        } else {
            request.setAttribute("ERROR", "Failed to retrieve user information from Google.");
        }
    } catch (SQLException e) {
        log("SQL Exception at LoginGoogleHandler: " + e.toString());
        request.setAttribute("ERROR", "Database error occurred: " + e.getMessage());
    } catch (Exception e) {
        log("Error at LoginGoogleHandler: " + e.toString());
        request.setAttribute("ERROR", "An error occurred during login: " + e.getMessage());
    } finally {
        if (url.equals(SUCCESS)) {
            response.sendRedirect(request.getContextPath() + "/" + SUCCESS);
        } else {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
}
public static String getToken(String code) throws ClientProtocolException, IOException {
        // call API to get token
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE)
                        .build())
                .execute().returnContent().asString();

        JsonObject 

jobj = new Gson().fromJson(response, JsonObject.class
);
        return jobj.get("access_token").getAsString();
    }

    public static UserGoogleDTO getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();

        

return new Gson().fromJson(response, UserGoogleDTO.class
);
    }
    private void checkUserCookie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException {
    // Lấy tất cả cookies từ request
    Cookie[] cookies = request.getCookies();
    
    if (cookies != null) {
        // Duyệt qua các cookies để tìm cookie "userID"
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userID")) {
                String userID = cookie.getValue();
                // Kiểm tra xem userID có hợp lệ không (có thể kiểm tra trong cơ sở dữ liệu hoặc session)
                UserDAO userDAO = new UserDAO();
                try {
                    UserDTO user = userDAO.getUserByID(userID); // Kiểm tra người dùng theo ID
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("LOGIN_USER", user); // Đặt người dùng vào session
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new ServletException("Error accessing database for user validation", e);
                }
                break;
            }
        }
    }
}


    @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            checkUserCookie(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginGoogleHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    try {
        processRequest(request, response);
    } catch (SQLException e) {
        log("SQL Exception at doGet: " + e.getMessage());
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
    }
}

@Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        checkUserCookie(request, response);
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            log("SQL Exception at doPost: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginGoogleHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    @Override
        public String getServletInfo() {
        return "Handles Google login and user creation.";
    }
}
