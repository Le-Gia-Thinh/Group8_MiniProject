<%@page import="model.dto.OrderDetailDTO"%>
<%@page import="model.dto.OrderDTO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="model.utils.Constants"%>
<%@page import="model.dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Track Order - Bookstore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <%
            UserDTO user = (UserDTO) session.getAttribute("USER");
            if (user == null) {
                response.sendRedirect("MainController?btAction=Login");
                return;
            }
            
            OrderDTO order = (OrderDTO) request.getAttribute("ORDER");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        %>
        
        <!-- Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="MainController?btAction=Search">Bookstore</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Search">Home</a>
                        </li>
                        <% if (user != null && Constants.ADMIN_ROLE.equals(user.getRole())) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Update&action=view">Manage Books</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Create&action=view">Add Book</a>
                        </li>
                        <% } %>
                        <% if (user != null && !Constants.ADMIN_ROLE.equals(user.getRole())) { %>
                        <li class="nav-item">
                            <a class="nav-link active" href="MainController?btAction=TrackOrder">Track Order</a>
                        </li>
                        <% } %>
                    </ul>
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=ViewCart">
                                <i class="fas fa-shopping-cart"></i> Cart
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Welcome, <%= user.getFullName() %>
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><a class="dropdown-item" href="MainController?btAction=Logout">Logout</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        
        <!-- Main Content -->
        <div class="container mt-4">
            <% if (request.getAttribute("ERROR") != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("ERROR") %>
            </div>
            <% } %>
            
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Track Order</h4>
                </div>
                <div class="card-body">
                    <form action="MainController" method="GET" class="row g-3">
                        <div class="col-md-8">
                            <label for="orderID" class="form-label">Order ID</label>
                            <input type="number" class="form-control" id="orderID" name="orderID" required>
                        </div>
                        <div class="col-md-4 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary w-100" name="btAction" value="TrackOrder">Track</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <% if (order != null) { %>
            <div class="card mb-4">
                <div class="card-header bg-primary text-white">
                    <h4>Order #<%= order.getOrderID() %></h4>
                </div>
                <div class="card-body">
                    <div class="row mb-4">
                        <div class="col-md-6">
                            <h5>Order Information</h5>
                            <p><strong>Order ID:</strong> #<%= order.getOrderID() %></p>
                            <p><strong>Order Date:</strong> <%= dateFormat.format(order.getOrderDate()) %></p>
                            <p><strong>Payment Method:</strong> <%= order.getPaymentMethod() %></p>
                            <p><strong>Payment Status:</strong> 
                                <span class="badge <%= "COMPLETED".equals(order.getPaymentStatus()) ? "bg-success" : "bg-warning" %>">
                                    <%= order.getPaymentStatus() %>
                                </span>
                            </p>
                            <p><strong>Total Amount:</strong> $<%= String.format("%.2f", order.getTotalAmount()) %></p>
                        </div>
                        <div class="col-md-6">
                            <h5>Customer Information</h5>
                            <p><strong>Name:</strong> <%= order.getCustomerName() %></p>
                            <p><strong>Email:</strong> <%= order.getCustomerEmail() %></p>
                            <p><strong>Phone:</strong> <%= order.getCustomerPhone() %></p>
                            <p><strong>Address:</strong> <%= order.getCustomerAddress() %></p>
                        </div>
                    </div>
                    
                    <h5>Order Details</h5>
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Book</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (order.getOrderDetails() != null) {
                                    for (OrderDetailDTO detail : order.getOrderDetails()) { %>
                                <tr>
                                    <td><%= detail.getBookTitle() %></td>
                                    <td>$<%= String.format("%.2f", detail.getPrice()) %></td>
                                    <td><%= detail.getQuantity() %></td>
                                    <td>$<%= String.format("%.2f", detail.getTotal()) %></td>
                                </tr>
                                <% }
                                } %>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="3" class="text-end"><strong>Total:</strong></td>
                                    <td><strong>$<%= String.format("%.2f", order.getTotalAmount()) %></strong></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
        
        <!-- Footer -->
        <footer class="bg-dark text-white mt-5 py-3">
            <div class="container text-center">
                <p>&copy; 2023 Bookstore. All rights reserved.</p>
            </div>
        </footer>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>