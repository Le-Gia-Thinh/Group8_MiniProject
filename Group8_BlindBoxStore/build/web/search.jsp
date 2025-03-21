<%-- 
    Document   : newjsp
    Created on : 16-Mar-2025, 02:09:22
    Author     : hoang an
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.dto.CategoryDTO"%>
<%@page import="model.dto.ProductDTO"%>
<%@page import="java.util.List"%>
<%@page import="model.utils.Constants"%>
<%@page import="model.dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BlindBoxStore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <%
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("PRODUCTS");
            List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("CATEGORIES");
            String searchValue = (String) request.getAttribute("SEARCH_VALUE");
            Integer categoryID = (Integer) request.getAttribute("CATEGORY_ID");
            Integer currentPage = (Integer) request.getAttribute("CURRENT_PAGE");
            Integer totalPages = (Integer) request.getAttribute("TOTAL_PAGES");

            if (searchValue == null) {
                searchValue = "";
            }
            if (currentPage == null) {
                currentPage = 1;
            }
            if (totalPages == null) {
                totalPages = 1;
            }
        %>
            <!-- Navigation Bar -->
          <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="MainController?btAction=Search">BlindBoxStore</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="MainController?btAction=Search">Home</a>
                </li>
                <% if (user != null && Constants.ADMIN_ROLE.equals(user.getRole())) { %>
                <li class="nav-item">
                    <a class="nav-link" href="MainController?btAction=Update&action=view">Manage BlindBoxs</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="MainController?btAction=Create&action=view">Add BlindBox</a>
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
                    <a class="nav-link" href="MainController?btAction=ViewCart">
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
                        <li><a class="dropdown-item" href="MainController?btAction=User_Page">Profile</a></li>
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
            <% if (request.getAttribute("SUCCESS") != null) {%>
            <div class="alert alert-success" role="alert">
                <%= request.getAttribute("SUCCESS")%>
            </div>
            <% } %>
            <% if (request.getAttribute("ERROR") != null) {%>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("ERROR")%>
            </div>
            <% }%>

            <!-- Search Form -->
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Search Product</h4>
                </div>
                <div class="card-body">
                    <form action="MainController" method="GET" class="row g-3">
                        <div class="col-md-6">
                            <label for="searchValue" class="form-label"> Product Name</label>
                            <input type="text" class="form-control" id="searchValue" name="searchValue" value="<%= searchValue%>">
                        </div>
                        <div class="col-md-4">
                            <label for="categoryID" class="form-label">Category</label>
                            <select class="form-select" id="categoryID" name="categoryID">
                                <option value="">All Categories</option>
                                <% if (categories != null) {
                                        for (CategoryDTO category : categories) {%>
                                <option value="<%= category.getCategoryID()%>" <%= (categoryID != null && categoryID == category.getCategoryID()) ? "selected" : ""%>>
                                    <%= category.getCategoryName()%>
                                </option>
                                <% }
                                    } %>
                            </select>
                        </div>
                        <div class="col-md-2 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary w-100" name="btAction" value="Search">Search</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Product List -->
            <div class="row">
                <% String part = "assets/images/"; %>
                <% if (products != null && !products.isEmpty()) {
                        for (ProductDTO product : products) {%>
                <div class="col-md-3 mb-4">
                    <div class="card h-100">
                        <img src="<%= product.getImageUrl()%>" class="card-img-top" alt="<%= product.getProductName()%>" style="height: 200px; object-fit: cover;"> 

                        <div class="card-body">
                            <h5 class="card-title"><%= product.getProductName()%></h5>
                            <p class="card-text text-muted">By <%= product.getSeries()%></p>
                            <p class="card-text"><small><%= product.getCategoryName()%></small></p>
                            <p class="card-text"><%= product.getDescription().length() > 100 ? product.getDescription().substring(0, 100) + "..." : product.getDescription()%></p>
                            <h6 class="card-subtitle mb-2 text-primary">$<%= String.format("%.2f", product.getPrice())%></h6>
                            <% if (product.getQuantity() > 0) { %>
                            <p class="card-text text-success">In Stock</p>
                            <% } else { %>
                            <p class="card-text text-danger">Out of Stock</p>
                            <% } %>
                        </div>
                        <div class="card-footer">
                            <% if (product.getQuantity() > 0 && (user == null || !Constants.ADMIN_ROLE.equals(user.getRole()))) {%>
                            <form action="MainController" method="POST">
                                <input type="hidden" name="productID" value="<%= product.getProductID()%>">
                                <button type="submit" class="btn btn-primary w-100" name="btAction" value="AddToCart">
                                    <i class="fas fa-cart-plus"></i> Add to Cart
                                </button>
                            </form>
                            <% } else if (user != null && Constants.ADMIN_ROLE.equals(user.getRole())) {%>
                            <a href="MainController?btAction=Update&action=edit&productID=<%= product.getProductID()%>" class="btn btn-warning w-100">
                                <i class="fas fa-edit"></i> Edit
                            </a>
                            <% } %>
                        </div>
                    </div>
                </div>
                <% }
                } else { %>
                <div class="col-12">
                    <div class="alert alert-info" role="alert">
                        No product found.
                    </div>
                </div>
                <% } %>
            </div>

            <!-- Pagination -->
            <% if (totalPages > 1) {%>
            <nav aria-label="Page navigation" class="mt-4">
                <ul class="pagination justify-content-center">
                    <li class="page-item <%= currentPage == 1 ? "disabled" : ""%>">
                        <a class="page-link" href="MainController?btAction=Search&searchValue=<%= searchValue%>&categoryID=<%= categoryID != null ? categoryID : ""%>&page=<%= currentPage - 1%>">Previous</a>
                    </li>
                    <% for (int i = 1; i <= totalPages; i++) {%>
                    <li class="page-item <%= i == currentPage ? "active" : ""%>">
                        <a class="page-link" href="MainController?btAction=Search&searchValue=<%= searchValue%>&categoryID=<%= categoryID != null ? categoryID : ""%>&page=<%= i%>"><%= i%></a>
                    </li>
                    <% }%>
                    <li class="page-item <%= currentPage == totalPages ? "disabled" : ""%>">
                        <a class="page-link" href="MainController?btAction=Search&searchValue=<%= searchValue%>&categoryID=<%= categoryID != null ? categoryID : ""%>&page=<%= currentPage + 1%>">Next</a>
                    </li>
                </ul>
            </nav>
            <% }%>
        </div>

        <!-- Footer -->
        <footer class="bg-dark text-white mt-5 py-3">
            <div class="container text-center">
                <p>&copy; 2025 BlindBoxstore. All rights reserved.</p>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>