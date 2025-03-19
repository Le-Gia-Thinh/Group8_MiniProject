package controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.dao.*;

import model.dto.*;

import model.utils.Constants;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class MainController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String url = Constants.SEARCH_PAGE;
        try {
            String action = request.getParameter("btAction");
            
            if (action == null || action.isEmpty()) {
                url = Constants.SEARCH_PAGE;
            } else if (action.equals(Constants.LOGIN_ACTION)) {
                url = processLogin(request);
            } else if (action.equals(Constants.LOGOUT_ACTION)) {
                url = processLogout(request);
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
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
    
    private String processLogin(HttpServletRequest request) throws Exception {
        String url = Constants.LOGIN_PAGE;
        
        String userID = request.getParameter("userID");
        String password = request.getParameter("password");
        
        UserDAO userDAO = new UserDAO();
        UserDTO user = userDAO.checkLogin(userID, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("USER", user);
            
            url = Constants.SEARCH_PAGE;
        } else {
            request.setAttribute("ERROR", "Invalid UserID or Password");
        }
        
        return url;
    }
    
    private String processLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
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
        
        BookDAO bookDAO = new BookDAO();
        List<BookDTO> books = bookDAO.searchBooks(searchValue, categoryID, page, Constants.BOOKS_PER_PAGE);
        int totalBooks = bookDAO.countBooks(searchValue, categoryID);
        int totalPages = (int) Math.ceil((double) totalBooks / Constants.BOOKS_PER_PAGE);
        
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("BOOKS", books);
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
    if (session == null || session.getAttribute("USER") == null) {
        request.setAttribute("ERROR", "Please login to continue");
        return Constants.LOGIN_PAGE;
    }
    
    UserDTO user = (UserDTO) session.getAttribute("USER");
    if (!Constants.ADMIN_ROLE.equals(user.getRole())) {
        request.setAttribute("ERROR", "You do not have permission to access this page");
        return Constants.ERROR_PAGE;
    }
    
    String action = request.getParameter("action");
    
    if ("view".equals(action)) {
        // Load books for update page - MODIFIED TO SHOW ALL BOOKS
        BookDAO bookDAO = new BookDAO();
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        
        // Use the new method to get all books including inactive ones
        List<BookDTO> books = bookDAO.getAllBooksForAdmin(page, Constants.BOOKS_PER_PAGE);
        int totalBooks = bookDAO.countAllBooksForAdmin();
        int totalPages = (int) Math.ceil((double) totalBooks / Constants.BOOKS_PER_PAGE);
        
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("BOOKS", books);
        request.setAttribute("CATEGORIES", categories);
        request.setAttribute("CURRENT_PAGE", page);
        request.setAttribute("TOTAL_PAGES", totalPages);
    } else if ("edit".equals(action)) {
        // Load book details for editing
        int bookID = Integer.parseInt(request.getParameter("bookID"));
        
        BookDAO bookDAO = new BookDAO();
        BookDTO book = bookDAO.getBookByID(bookID);
        
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("BOOK", book);
        request.setAttribute("CATEGORIES", categories);
    } else if ("update".equals(action)) {
        // Process book update
        int bookID = Integer.parseInt(request.getParameter("bookID"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
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
        
        BookDTO book = new BookDTO();
        book.setBookID(bookID);
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setPrice(price);
        book.setQuantity(quantity);
        book.setImageUrl(imageUrl);
        book.setCategoryID(categoryID);
        book.setLastUpdateUser(user.getUserID());
        book.setStatus(status);
        
        BookDAO bookDAO = new BookDAO();
        boolean updated = bookDAO.updateBook(book);
        
        if (updated) {
            request.setAttribute("SUCCESS", "Book updated successfully");
        } else {
            request.setAttribute("ERROR", "Failed to update book");
        }
        
        // Redirect to view all books
        url = "MainController?btAction=" + Constants.UPDATE_ACTION + "&action=view";
    }
    
    return url;
}
    
    private String processCreate(HttpServletRequest request) throws Exception {
        String url = Constants.CREATE_PAGE;
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("USER") == null) {
            request.setAttribute("ERROR", "Please login to continue");
            return Constants.LOGIN_PAGE;
        }
        
        UserDTO user = (UserDTO) session.getAttribute("USER");
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
            // Process book creation
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            
            // Handle image upload
            String imageUrl = "assets/images/default-book.jpg"; // Default image
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
            
            BookDTO book = new BookDTO();
            book.setTitle(title);
            book.setAuthor(author);
            book.setDescription(description);
            book.setPrice(price);
            book.setQuantity(quantity);
            book.setImageUrl(imageUrl);
            book.setCategoryID(categoryID);
            book.setLastUpdateUser(user.getUserID());
            book.setStatus(true); // Default status is active
            
            BookDAO bookDAO = new BookDAO();
            boolean created = bookDAO.createBook(book);
            
            if (created) {
                request.setAttribute("SUCCESS", "Book created successfully");
                // Load categories for create page
                CategoryDAO categoryDAO = new CategoryDAO();
                List<CategoryDTO> categories = categoryDAO.getAllCategories();
                request.setAttribute("CATEGORIES", categories);
            } else {
                request.setAttribute("ERROR", "Failed to create book");
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
        
        int bookID = Integer.parseInt(request.getParameter("bookID"));
        
        // Get book details
        BookDAO bookDAO = new BookDAO();
        BookDTO book = bookDAO.getBookByID(bookID);
        
        if (book != null && book.isStatus() && book.getQuantity() > 0) {
            // Check if book already in cart
            if (cart.containsKey(bookID)) {
                OrderDetailDTO item = cart.get(bookID);
                item.setQuantity(item.getQuantity() + 1);
            } else {
                OrderDetailDTO item = new OrderDetailDTO();
                item.setBookID(bookID);
                item.setBookTitle(book.getTitle());
                item.setPrice(book.getPrice());
                item.setQuantity(1);
                cart.put(bookID, item);
            }
            
            session.setAttribute("CART", cart);
            request.setAttribute("SUCCESS", "Book added to cart");
        } else {
            request.setAttribute("ERROR", "Book not available");
        }
        
        return processSearch(request);
    }
    
    private String processRemoveFromCart(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Map<Integer, OrderDetailDTO> cart = (Map<Integer, OrderDetailDTO>) session.getAttribute("CART");
            if (cart != null) {
                int bookID = Integer.parseInt(request.getParameter("bookID"));
                cart.remove(bookID);
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
                String[] bookIDs = request.getParameterValues("bookID");
                String[] quantities = request.getParameterValues("quantity");
                
                if (bookIDs != null && quantities != null && bookIDs.length == quantities.length) {
                    for (int i = 0; i < bookIDs.length; i++) {
                        int bookID = Integer.parseInt(bookIDs[i]);
                        int quantity = Integer.parseInt(quantities[i]);
                        
                        if (cart.containsKey(bookID)) {
                            if (quantity <= 0) {
                                cart.remove(bookID);
                            } else {
                                OrderDetailDTO item = cart.get(bookID);
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
        UserDTO user = (UserDTO) session.getAttribute("USER");
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
        UserDTO user = (UserDTO) session.getAttribute("USER");
        if (user != null) {
            userID = user.getUserID();
            
            // Admin cannot place orders
            if (Constants.ADMIN_ROLE.equals(user.getRole())) {
                request.setAttribute("ERROR", "Admin cannot place orders");
                return Constants.CART_PAGE;
            }
        }
        
        // Get customer information
        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");
        String customerPhone = request.getParameter("customerPhone");
        String customerAddress = request.getParameter("customerAddress");
        String paymentMethod = request.getParameter("paymentMethod");
        
        if (customerName == null || customerName.trim().isEmpty() ||
            customerEmail == null || customerEmail.trim().isEmpty() ||
            customerPhone == null || customerPhone.trim().isEmpty() ||
            customerAddress == null || customerAddress.trim().isEmpty()) {
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
        if (session == null || session.getAttribute("USER") == null) {
            request.setAttribute("ERROR", "Please login to track your order");
            return Constants.LOGIN_PAGE;
        }
        
        String orderIDStr = request.getParameter("orderID");
        if (orderIDStr != null && !orderIDStr.trim().isEmpty()) {
            int orderID = Integer.parseInt(orderIDStr);
            
            OrderDAO orderDAO = new OrderDAO();
            OrderDTO order = orderDAO.getOrderByID(orderID);
            
            if (order != null) {
                UserDTO user = (UserDTO) session.getAttribute("USER");
                
                // Check if order belongs to user or user is admin
                if (order.getUserID() != null && order.getUserID().equals(user.getUserID()) || 
                    Constants.ADMIN_ROLE.equals(user.getRole())) {
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
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Main Controller Servlet";
    }
}