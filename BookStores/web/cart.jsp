<%@page import="model.dto.OrderDetailDTO"%>
<%@page import="java.util.Map"%>
<%@page import="model.utils.Constants"%>
<%@page import="model.dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shopping Cart - Bookstore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <%
            UserDTO user = (UserDTO) session.getAttribute("USER");
            Map<Integer, OrderDetailDTO> cart = (Map<Integer, OrderDetailDTO>) session.getAttribute("CART");
            UserDTO userInfo = (UserDTO) request.getAttribute("USER_INFO");
            Integer orderID = (Integer) request.getAttribute("ORDER_ID");
            
            double totalAmount = 0;
            if (cart != null) {
                for (OrderDetailDTO item : cart.values()) {
                    totalAmount += item.getTotal();
                }
            }
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
                            <a class="nav-link" href="MainController?btAction=TrackOrder">Track Order</a>
                        </li>
                        <% } %>
                    </ul>
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link active" href="MainController?btAction=ViewCart">
                                <i class="fas fa-shopping-cart"></i> Cart
                            </a>
                        </li>
                        <% if (user == null) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Login">Login</a>
                        </li>
                        <% } else { %>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Welcome, <%= user.getFullName() %>
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li><a class="dropdown-item" href="MainController?btAction=Logout">Logout</a></li>
                            </ul>
                        </li>
                        <% } %>
                    </ul>
                </div>
            </div>
        </nav>
        
        <!-- Main Content -->
        <div class="container mt-4">
            <% if (request.getAttribute("SUCCESS") != null) { %>
            <div class="alert alert-success" role="alert">
                <%= request.getAttribute("SUCCESS") %>
            </div>
            <% } %>
            <% if (request.getAttribute("ERROR") != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("ERROR") %>
            </div>
            <% } %>
            
            <% if (orderID != null) { %>
            <!-- Order Confirmation -->
            <div class="card mb-4">
                <div class="card-header bg-success text-white">
                    <h4>Order Placed Successfully!</h4>
                </div>
                <div class="card-body">
                    <p class="lead">Thank you for your order. Your order ID is <strong>#<%= orderID %></strong>.</p>
                    <p>You can track your order status using this order ID.</p>
                    <div class="d-flex justify-content-between mt-4">
                        <a href="MainController?btAction=Search" class="btn btn-primary">Continue Shopping</a>
                        <a href="MainController?btAction=TrackOrder&orderID=<%= orderID %>" class="btn btn-info">Track Order</a>
                    </div>
                </div>
            </div>
            <% } else if (cart == null || cart.isEmpty()) { %>
            <!-- Empty Cart -->
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Shopping Cart</h4>
                </div>
                <div class="card-body text-center py-5">
                    <i class="fas fa-shopping-cart fa-4x mb-3 text-muted"></i>
                    <h5>Your cart is empty</h5>
                    <p>Looks like you haven't added any books to your cart yet.</p>
                    <a href="MainController?btAction=Search" class="btn btn-primary mt-3">Continue Shopping</a>
                </div>
            </div>
            <% } else { %>
            <!-- Cart Items -->
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Shopping Cart</h4>
                </div>
                <div class="card-body">
                    <form action="MainController" method="POST">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>Book</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Total</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (OrderDetailDTO item : cart.values()) { %>
                                    <tr>
                                        <td><%= item.getBookTitle() %></td>
                                        <td>$<%= String.format("%.2f", item.getPrice()) %></td>
                                        <td>
                                            <input type="hidden" name="bookID" value="<%= item.getBookID() %>">
                                            <input type="number" class="form-control form-control-sm" style="width: 80px;" name="quantity" value="<%= item.getQuantity() %>" min="1">
                                        </td>
                                        <td>$<%= String.format("%.2f", item.getTotal()) %></td>
                                        <td>
                                            <a href="MainController?btAction=RemoveFromCart&bookID=<%= item.getBookID() %>" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to remove this item?')">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3" class="text-end"><strong>Total:</strong></td>
                                        <td><strong>$<%= String.format("%.2f", totalAmount) %></strong></td>
                                        <td></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                        <div class="d-flex justify-content-between">
                            <a href="MainController?btAction=Search" class="btn btn-secondary">Continue Shopping</a>
                            <button type="submit" class="btn btn-primary" name="btAction" value="UpdateCart">Update Cart</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Checkout Form -->
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Checkout</h4>
                </div>
                <div class="card-body">
                    <form action="MainController" method="POST">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="customerName" class="form-label">Full Name</label>
                                <input type="text" class="form-control" id="customerName" name="customerName" value="<%= userInfo != null ? userInfo.getFullName() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="customerEmail" class="form-label">Email</label>
                                <input type="email" class="form-control" id="customerEmail" name="customerEmail" value="<%= userInfo != null ? userInfo.getEmail() : "" %>" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="customerPhone" class="form-label">Phone</label>
                                <input type="tel" class="form-control" id="customerPhone" name="customerPhone" value="<%= userInfo != null ? userInfo.getPhone() : "" %>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="paymentMethod" class="form-label">Payment Method</label>
                                <select class="form-select" id="paymentMethod" name="paymentMethod">
                                    <option value="CASH">Cash on Delivery</option>
                                    <option value="PAYPAL">PayPal</option>
                                </select>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="customerAddress" class="form-label">Delivery Address</label>
                            <textarea class="form-control" id="customerAddress" name="customerAddress" rows="3" required><%= userInfo != null ? userInfo.getAddress() : "" %></textarea>
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-success" name="btAction" value="ConfirmOrder">Place Order</button>
                        </div>
                    </form>
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