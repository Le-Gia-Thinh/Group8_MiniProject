/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.dao.UserDAO;
import model.dto.UserDTO;
import model.dto.UserError;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/RegisterController"})
public class RegisterController extends HttpServlet {

    private static final String ERROR = "register.jsp";
    private static final String SUCCESS_USER = "login.jsp";
    private static final String SUCCESS_ADMIN = "create.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        UserError userError = new UserError();
        try {
            boolean checkValidation = true;
        UserDAO dao = new UserDAO();
        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
//
            HttpSession session = request.getSession();
            UserDTO checkLogin = (UserDTO) session.getAttribute("LOGIN_USER");
            UserDTO checkEmail = dao.checkEmail(email);
            if(checkEmail != null){
                checkValidation = false;
                userError.setEmailError("Email already exist!");
            }
            if (userID.length() < 2 || userID.length() > 10) {
                checkValidation = false;
                userError.setUserIDError("UserID must be in [2, 10]");
            }
            if (fullName.length() < 5 || fullName.length() > 50) {
                checkValidation = false;
                userError.setFullNameError("FullName must be in [5, 50]");
            }
            if (!confirm.equals(password)) {
                checkValidation = false;
                userError.setConfirmError("Repassword does not match!");
            }
            if (checkValidation) {
                 UserDTO user = new UserDTO(userID, password, fullName, email, phone, address, role, true);
                boolean checkInsert = dao.createUser(user);
                if (checkInsert) {
                    if (checkLogin != null && checkLogin.getRole().equals("ADMIN")) {
                        url = SUCCESS_ADMIN;
                    } else {
                        url = SUCCESS_USER;
                    }
                } else {
                    userError.setError("Unknown error !");
                    request.setAttribute("USER_ERROR", userError);
                }
            } else {
                request.setAttribute("USER_ERROR", userError);
            }

        } catch (Exception e) {
            log("Error at Register: " + e.toString());
            if (e.toString().contains("duplicate")) {
                userError.setUserIDError("Duplicate userID !");
                request.setAttribute("USER_ERROR", userError);
            }
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
