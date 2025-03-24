package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.dao.*;
import model.dto.*;
import model.utils.Constants;
//import model.utils.LoginGoogleHandler;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)

public class MainController extends HttpServlet {

    private static final String REGISTER_PAGE = "Create_Page";
    private static final String REGISTER_PAGE_VIEW = "create.jsp";
    private static final String REGISTER = "Register";
    private static final String REGISTER_CONTROLLER = "RegisterController";
    private static final String USER_PAGE = "User_Page";
    private static final String USER_PAGE_VIEW = "user.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = Constants.SEARCH_PAGE; // Default page
        try {
            String action = request.getParameter("btAction");

            if (action == null || action.isEmpty()) {
                url = Constants.SEARCH_PAGE;
            } else if (action.equals(Constants.LOGIN_ACTION)) {
                 url = processLogin(request, response);
            } else if (action.equals(Constants.LOGOUT_ACTION)) {
                  url = processLogout(request, response);
            } else if (USER_PAGE.equals(action)) {
                url = USER_PAGE_VIEW;
            } else if (REGISTER_PAGE.equals(action)) {
                url = REGISTER_PAGE_VIEW;
            } else if (REGISTER.equals(action)) {
                url = REGISTER_CONTROLLER;
            } else if (action.equals(Constants.SEARCH_ACTION)) {
                url = processSearch(request);
            } else if (action.equals(Constants.UPDATE_ACTION)) {
                url = processUpdate(request);
            } else if (action.equals(Constants.CREATE_ACTION)) {
                url = processCreate(request);
            } else if (action.equals(Constants.ADD_TO_CART_ACTION)) {
                url = processAddToCart(request);
            } else if (action.equals(Constants.VIEW_CART_ACTION)) {
                url = Constants.CART_PAGE;
            } else if (action.equals(Constants.REMOVE_FROM_CART_ACTION)) {
                url = processRemoveFromCart(request);
            } else if (action.equals(Constants.UPDATE_CART_ACTION)) {
                url = processUpdateCart(request);
            } else if (action.equals(Constants.CHECKOUT_ACTION)) {
                url = processCheckout(request);
            } else if (action.equals(Constants.CONFIRM_ORDER_ACTION)) {
                url = processConfirmOrder(request);
            } else if (action.equals(Constants.TRACK_ORDER_ACTION)) {
                url = processTrackOrder(request);
            } else {
                request.setAttribute("ERROR", "Action not supported");
                url = Constants.ERROR_PAGE;
            }
        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
            request.setAttribute("ERROR", e.getMessage());
            url = Constants.ERROR_PAGE;
        } finally {
            request.getRequestDispatcher(url).forward(request, response);  // Always forward to the correct page
        }
    }

   private String processLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String url = Constants.LOGIN_PAGE;
    request.removeAttribute("ERROR");
    String userID = request.getParameter("userID");
    String password = request.getParameter("password");
    if (userID != null && !userID.isEmpty() && password != null && !password.isEmpty()) {
        UserDAO userDAO = new UserDAO();
        UserDTO user = userDAO.checkLogin(userID, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("LOGIN_USER", user);
            Cookie userCookie = new Cookie("userID", user.getUserID());
            userCookie.setMaxAge(60 * 60 * 7200); 
            response.addCookie(userCookie);

            url = Constants.SEARCH_PAGE;
        } else {
            request.setAttribute("ERROR", "Invalid UserID or Password");
        }
    }
    return url;
}
private void checkUserCookie(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, IOException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userID")) {
                String userID = cookie.getValue();
                try {
                    UserDAO userDAO = new UserDAO();
                    UserDTO user = userDAO.getUserByID(userID);  
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("LOGIN_USER", user);
                    } else {
                        response.sendRedirect("login.jsp");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
private String processLogout(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }

    Cookie userCookie = new Cookie("userID", "");
    userCookie.setMaxAge(0);
    response.addCookie(userCookie);

    return Constants.LOGIN_PAGE;
}


    private String processSearch(HttpServletRequest request) throws Exception {
        String searchValue = request.getParameter("searchValue");
        String categoryIDStr = request.getParameter("categoryID");
        Integer categoryID = null;

        if (categoryIDStr != null && !categoryIDStr.isEmpty()) {
            categoryID = Integer.parseInt(categoryIDStr);
        }

        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        ProductDAO productDao = new ProductDAO();
        List<ProductDTO> products = productDao.searchProducts(searchValue, categoryID, page, Constants.PRODUCTS_PER_PAGE);
        int totalProducts = productDao.countProducts(searchValue, categoryID);
        int totalPages = (int) Math.ceil((double) totalProducts / Constants.PRODUCTS_PER_PAGE);

        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();

        request.setAttribute("PRODUCTS", products);
        request.setAttribute("CATEGORIES", categories);
        request.setAttribute("SEARCH_VALUE", searchValue);
        request.setAttribute("CATEGORY_ID", categoryID);
        request.setAttribute("CURRENT_PAGE", page);
        request.setAttribute("TOTAL_PAGES", totalPages);

        return Constants.SEARCH_PAGE;
    }

    private String processUpdate(HttpServletRequest request) throws Exception {
        String url = Constants.UPDATE_PAGE;

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            request.setAttribute("ERROR", "Please login to continue");
            return Constants.LOGIN_PAGE;
        }

        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (!Constants.ADMIN_ROLE.equals(user.getRole())) {
            request.setAttribute("ERROR", "You do not have permission to access this page");
            return Constants.ERROR_PAGE;
        }

        String action = request.getParameter("action");

        if ("view".equals(action)) {
            // Load products for update page - MODIFIED TO SHOW ALL PRODUCTS
            ProductDAO productDao = new ProductDAO();
            int page = 1;
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }

            // Use the new method to get all products including inactive ones
            List<ProductDTO> products = productDao.getAllProductsForAdmin(page, Constants.PRODUCTS_PER_PAGE);
            int productsTotal = productDao.countAllProductsForAdmin();
            int totalPages = (int) Math.ceil((double) productsTotal / Constants.PRODUCTS_PER_PAGE);

            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getAllCategories();

            request.setAttribute("PRODUCTS", products);
            request.setAttribute("CATEGORIES", categories);
            request.setAttribute("CURRENT_PAGE", page);
            request.setAttribute("TOTAL_PAGES", totalPages);
        } else if ("edit".equals(action)) {
            // Load product details for editing
            int productID = Integer.parseInt(request.getParameter("productID"));

            ProductDAO productDAO = new ProductDAO();
            ProductDTO product = productDAO.getProductID(productID);

            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getAllCategories();

            request.setAttribute("PRODUCT", product);
            request.setAttribute("CATEGORIES", categories);
        } else if ("update".equals(action)) {
            // Process product update
            int productID = Integer.parseInt(request.getParameter("productID"));
            String productName = request.getParameter("productName");
            String series = request.getParameter("series");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            boolean status = "on".equals(request.getParameter("status"));

            // Handle image upload
            String imageUrl = request.getParameter("currentImageUrl");
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getFileName(filePart);
                if (fileName != null && !fileName.isEmpty()) {
                    // Save the file to the server
                    String uploadPath = getServletContext().getRealPath("/assets/images/");
                    filePart.write(uploadPath + fileName);
                    imageUrl = "assets/images/" + fileName;
                }
            }

            ProductDTO product = new ProductDTO();
            product.setProductID(productID);
            product.setProductName(productName);
            product.setSeries(series);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImageUrl(imageUrl);
            product.setCategoryID(categoryID);
            product.setLastUpdateUser(user.getUserID());
            product.setStatus(status);

            ProductDAO productDao = new ProductDAO();
            boolean updated = productDao.updateProduct(product);

            if (updated) {
                request.setAttribute("SUCCESS", "Product updated successfully");
            } else {
                request.setAttribute("ERROR", "Failed to update product");
            }

            // Redirect to view all products
            url = "MainController?btAction=" + Constants.UPDATE_ACTION + "&action=view";
        }

        return url;
    }

    private String processCreate(HttpServletRequest request) throws Exception {
        String url = Constants.CREATE_PAGE;

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            request.setAttribute("ERROR", "Please login to continue");
            return Constants.LOGIN_PAGE;
        }

        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (!Constants.ADMIN_ROLE.equals(user.getRole())) {
            request.setAttribute("ERROR", "You do not have permission to access this page");
            return Constants.ERROR_PAGE;
        }

        String action = request.getParameter("action");

        if ("view".equals(action)) {
            // Load categories for create page
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getAllCategories();

            request.setAttribute("CATEGORIES", categories);
        } else if ("create".equals(action)) {
            // Process product creation
            String productName = request.getParameter("productName");
            String series = request.getParameter("series");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));

            // Handle image upload
            String imageUrl = "assets/images/default-product.jpg"; // Default image
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = getFileName(filePart);
                if (fileName != null && !fileName.isEmpty()) {
                    // Save the file to the server
                    String uploadPath = getServletContext().getRealPath("/assets/images/");
                    filePart.write(uploadPath + fileName);
                    imageUrl = "assets/images/" + fileName;
                }
            }

            ProductDTO product = new ProductDTO();
            product.setProductName(productName);
            product.setSeries(series);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImageUrl(imageUrl);
            product.setCategoryID(categoryID);
            product.setLastUpdateUser(user.getUserID());
            product.setStatus(true); // Default status is active

            ProductDAO productDAO = new ProductDAO();
            boolean created = productDAO.createProduct(product);

            if (created) {
                request.setAttribute("SUCCESS", "Product created successfully");
                // Load categories for create page
                CategoryDAO categoryDAO = new CategoryDAO();
                List<CategoryDTO> categories = categoryDAO.getAllCategories();
                request.setAttribute("CATEGORIES", categories);
            } else {
                request.setAttribute("ERROR", "Failed to create product");
            }
        }

        return url;
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return null;
    }

    private String processAddToCart(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        // Get the cart from session or create a new one
        Map<Integer, OrderDetailDTO> cart = (Map<Integer, OrderDetailDTO>) session.getAttribute("CART");
        if (cart == null) {
            cart = new HashMap<>();
        }

        int productID = Integer.parseInt(request.getParameter("productID"));

        // Get product details
        ProductDAO productDao = new ProductDAO();
        ProductDTO product = productDao.getProductID(productID);

        if (product != null && product.isStatus() && product.getQuantity() > 0) {
            // Check if product already in cart
            if (cart.containsKey(productID)) {
                OrderDetailDTO item = cart.get(productID);
                item.setQuantity(item.getQuantity() + 1);
            } else {
                OrderDetailDTO item = new OrderDetailDTO();
                item.setProductID(productID);
                item.setProductName(product.getProductName());
                item.setPrice(product.getPrice());
                item.setQuantity(1);
                cart.put(productID, item);
            }

            session.setAttribute("CART", cart);
            request.setAttribute("SUCCESS", "product added to cart");
        } else {
            request.setAttribute("ERROR", "product not available");
        }

        return processSearch(request);
    }

    private String processRemoveFromCart(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<Integer, OrderDetailDTO> cart = (Map<Integer, OrderDetailDTO>) session.getAttribute("CART");
            if (cart != null) {
                int productID = Integer.parseInt(request.getParameter("productID"));
                cart.remove(productID);
                session.setAttribute("CART", cart);
            }
        }

        return Constants.CART_PAGE;
    }

    private String processUpdateCart(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<Integer, OrderDetailDTO> cart = (Map<Integer, OrderDetailDTO>) session.getAttribute("CART");
            if (cart != null) {
                String[] productIDs = request.getParameterValues("productID");
                String[] quantities = request.getParameterValues("quantity");

                if (productIDs != null && quantities != null && productIDs.length == quantities.length) {
                    for (int i = 0; i < productIDs.length; i++) {
                        int productID = Integer.parseInt(productIDs[i]);
                        int quantity = Integer.parseInt(quantities[i]);

                        if (cart.containsKey(productID)) {
                            if (quantity <= 0) {
                                cart.remove(productID);
                            } else {
                                OrderDetailDTO item = cart.get(productID);
                                item.setQuantity(quantity);
                            }
                        }
                    }

                    session.setAttribute("CART", cart);
                }
            }
        }

        return Constants.CART_PAGE;
    }

    private String processCheckout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("CART") == null) {
            request.setAttribute("ERROR", "Your cart is empty");
            return Constants.CART_PAGE;
        }

        Map<Integer, OrderDetailDTO> cart = (Map<Integer, OrderDetailDTO>) session.getAttribute("CART");
        if (cart.isEmpty()) {
            request.setAttribute("ERROR", "Your cart is empty");
            return Constants.CART_PAGE;
        }

        // Pre-fill user information if logged in
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user != null) {
            request.setAttribute("USER_INFO", user);
        }

        return Constants.CART_PAGE;
    }

    private String processConfirmOrder(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("CART") == null) {
            request.setAttribute("ERROR", "Your cart is empty");
            return Constants.CART_PAGE;
        }

        Map<Integer, OrderDetailDTO> cart = (Map<Integer, OrderDetailDTO>) session.getAttribute("CART");
        if (cart.isEmpty()) {
            request.setAttribute("ERROR", "Your cart is empty");
            return Constants.CART_PAGE;
        }

        // Get user information
        String userID = null;
        UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
        if (user != null) {
            userID = user.getUserID();

            // Admin cannot place orders
            if (Constants.ADMIN_ROLE.equals(user.getRole())) {
                request.setAttribute("ERROR", "Admin cannot place orders");
                return Constants.CART_PAGE;
            }
        }

        if (userID == null) {
            request.setAttribute("ERROR", "You must log in to place an order");
            return Constants.LOGIN_PAGE; // hoặc redirect về login
        }
   
        // Get customer information
        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");
        String customerPhone = request.getParameter("customerPhone");
        String customerAddress = request.getParameter("customerAddress");
        String paymentMethod = request.getParameter("paymentMethod");

        if (customerName == null || customerName.trim().isEmpty()
                || customerEmail == null || customerEmail.trim().isEmpty()
                || customerPhone == null || customerPhone.trim().isEmpty()
                || customerAddress == null || customerAddress.trim().isEmpty()) {
            request.setAttribute("ERROR", "Please fill in all required fields");
            return Constants.CART_PAGE;
        }

        // Calculate total amount
        double totalAmount = 0;
        List<OrderDetailDTO> orderDetails = new ArrayList<>(cart.values());
        for (OrderDetailDTO detail : orderDetails) {
            totalAmount += detail.getTotal();
        }

        // Create order
        OrderDTO order = new OrderDTO();
        order.setUserID(userID);
        order.setTotalAmount(totalAmount);
        order.setCustomerName(customerName);
        order.setCustomerEmail(customerEmail);
        order.setCustomerPhone(customerPhone);
        order.setCustomerAddress(customerAddress);
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "CASH");
        order.setPaymentStatus("PENDING");
        order.setOrderDetails(orderDetails);

        OrderDAO orderDAO = new OrderDAO();
        int orderID = orderDAO.createOrder(order);

        if (orderID > 0) {
            // Clear cart
            session.removeAttribute("CART");

            // Set order ID for confirmation
            request.setAttribute("ORDER_ID", orderID);
            request.setAttribute("SUCCESS", "Order placed successfully. Your order ID is " + orderID);

            // If payment method is PayPal, redirect to PayPal
            if ("PAYPAL".equals(paymentMethod)) {
                // Implement PayPal integration here
                // For now, just update payment status
                orderDAO.updatePaymentStatus(orderID, "COMPLETED");
            }
        } else {
            request.setAttribute("ERROR", "Failed to place order. Some items may be out of stock.");
        }

        return Constants.CART_PAGE;
    }

    private String processTrackOrder(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            request.setAttribute("ERROR", "Please login to track your order");
            return Constants.LOGIN_PAGE;
        }

        String orderIDStr = request.getParameter("orderID");
        if (orderIDStr != null && !orderIDStr.trim().isEmpty()) {
            int orderID = Integer.parseInt(orderIDStr);

            OrderDAO orderDAO = new OrderDAO();
            OrderDTO order = orderDAO.getOrderByID(orderID);

            if (order != null) {
                UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");

                // Check if order belongs to user or user is admin
                if (order.getUserID() != null && order.getUserID().equals(user.getUserID())
                        || Constants.ADMIN_ROLE.equals(user.getRole())) {
                    request.setAttribute("ORDER", order);
                } else {
                    request.setAttribute("ERROR", "Order not found");
                }
            } else {
                request.setAttribute("ERROR", "Order not found");
            }
        }

        return Constants.ORDER_TRACKING_PAGE;
    }

   @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            checkUserCookie(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    processRequest(request, response);
}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            checkUserCookie(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    processRequest(request, response);
}

    @Override
    public String getServletInfo() {
        return "Main Controller Servlet";
    }
}
