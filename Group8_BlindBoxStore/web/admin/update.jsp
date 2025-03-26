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
            <title>Manage Products - Blind Box Store</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        </head>
        <body>
            <%
                UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
                if (user == null || !Constants.ADMIN_ROLE.equals(user.getRole())) {
                    response.sendRedirect("MainController?btAction=Login");
                    return;
                }

                ProductDTO product = (ProductDTO) request.getAttribute("PRODUCT");
                List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("PRODUCTS");
                List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("CATEGORIES");
                Integer currentPage = (Integer) request.getAttribute("CURRENT_PAGE");
                Integer totalPages = (Integer) request.getAttribute("TOTAL_PAGES");

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
                                <a class="nav-link" href="MainController?btAction=Search">Home</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="MainController?btAction=Update&action=view">Manage Products</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="MainController?btAction=Create&action=view">Add Products</a>
                            </li>
                            <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=ViewRevenue">
                                <i class="fas fa-chart-bar"></i> View Revenue
                            </a>
                        </li>
                        </ul>
                        <ul class="navbar-nav">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    Welcome, <%= user.getFullName()%>
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
                <% if (request.getAttribute("SUCCESS") != null) {%>
                <div class="alert alert-success" role="alert">
                    <%= request.getAttribute("SUCCESS")%>
                </div>
                <% } %>
                <% if (request.getAttribute("ERROR") != null) {%>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("ERROR")%>
                </div>
                <% } %>

                <% if (product != null) {%>
                <!-- Edit Product Form -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h4>Edit Product</h4>
                    </div>
                    <div class="card-body">
                        <form action="MainController" method="POST" enctype="multipart/form-data">
                            <input type="hidden" name="productID" value="<%= product.getProductID()%>">
                            <input type="hidden" name="currentImageUrl" value="<%= product.getImageUrl()%>">

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="title" class="form-label">Product Name</label>
                                    <input type="text" class="form-control" id="productName" name="productName" value="<%= product.getProductName()%>" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="author" class="form-label">Series</label>
                                    <input type="text" class="form-control" id="series" name="series" value="<%= product.getDescription()%>" required>
                                </div>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-4">
                                    <label for="price" class="form-label">Price</label>
                                    <input type="number" class="form-control" id="price" name="price" value="<%= product.getPrice()%>" step="0.01" min="0" required>
                                </div>
                                <div class="col-md-4">
                                    <label for="quantity" class="form-label">Quantity</label>
                                    <input type="number" class="form-control" id="quantity" name="quantity" value="<%= product.getQuantity()%>" min="0" required>
                                </div>
                                <div class="col-md-4">
                                    <label for="categoryID" class="form-label">Category</label>
                                    <select class="form-select" id="categoryID" name="categoryID" required>
                                        <% if (categories != null) {
                                                for (CategoryDTO category : categories) {%>
                                        <option value="<%= category.getCategoryID()%>" <%= (product.getCategoryID() == category.getCategoryID()) ? "selected" : ""%>>
                                            <%= category.getCategoryName()%>
                                        </option>
                                        <% }
                                            }%>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required><%= product.getDescription()%></textarea>
                            </div>

                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="imageFile" class="form-label">Image</label>
                                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*">
                                    <div class="form-text">Current image: <%= product.getImageUrl()%></div>
                                </div>
                                <div class="col-md-6">
                                    <label for="status" class="form-label">Status</label>
                                    <div class="form-check form-switch mt-2">
                                        <input class="form-check-input" type="checkbox" id="status" name="status" <%= product.isStatus() ? "checked" : ""%>>
                                        <label class="form-check-label" for="status">Active</label>
                                    </div>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="MainController?btAction=Update&action=view" class="btn btn-secondary">Cancel</a>
                                <button type="submit" class="btn btn-primary" name="btAction" value="Update">Update Product</button>
                                <input type="hidden" name="action" value="update">
                            </div>
                        </form>
                    </div>
                </div>
                <% } else { %>
                <!-- Product List -->
                <div class="card mb-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h4>Manage BlindBoxs</h4>
                        <a href="MainController?btAction=Create&action=view" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Product
                        </a>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Image</th>
                                        <th>Product Name</th>
                                        <th>Series</th>
                                        <th>Category</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (products != null && !products.isEmpty()) {
                                            for (ProductDTO Item : products) {%>
                                    <tr>
                                        <td><%= Item.getProductID()%></td>
                                        <td>
                                            <% String path = Item.getImageUrl(); %>
                                            <img src="<%= path%>" alt="<%= Item.getSeries()%>" style="width: 50px; height: 70px; object-fit: cover;">
                                        </td>
                                        <td><%= Item.getProductName()%></td>
                                        <td><%= Item.getSeries()%></td>
                                        <td><%= Item.getCategoryName()%></td>
                                        <td>$<%= String.format("%.2f", Item.getPrice())%></td>
                                        <td><%= Item.getQuantity()%></td>
                                        <td>
                                            <span class="badge <%= Item.isStatus() ? "bg-success" : "bg-danger"%>">
                                                <%= Item.isStatus() ? "Active" : "Inactive"%>
                                            </span>
                                        </td>
                                        <td>
                                            <a href="MainController?btAction=Update&action=edit&productID=<%= Item.getProductID()%>" class="btn btn-sm btn-warning">
                                                <i class="fas fa-edit"></i> Edit
                                            </a>
                                        </td>
                                    </tr>
                                    <% }
                                    } else { %>
                                    <tr>
                                        <td colspan="9" class="text-center">No products found</td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>

                        <!-- Pagination -->
                        <% if (totalPages > 1) {%>
                        <nav aria-label="Page navigation" class="mt-4">
                            <ul class="pagination justify-content-center">
                                <li class="page-item <%= currentPage == 1 ? "disabled" : ""%>">
                                    <a class="page-link" href="MainController?btAction=Update&action=view&page=<%= currentPage - 1%>">Previous</a>
                                </li>
                                <% for (int i = 1; i <= totalPages; i++) {%>
                                <li class="page-item <%= i == currentPage ? "active" : ""%>">
                                    <a class="page-link" href="MainController?btAction=Update&action=view&page=<%= i%>"><%= i%></a>
                                </li>
                                <% }%>
                                <li class="page-item <%= currentPage == totalPages ? "disabled" : ""%>">
                                    <a class="page-link" href="MainController?btAction=Update&action=view&page=<%= currentPage + 1%>">Next</a>
                                </li>
                            </ul>
                        </nav>
                        <% } %>
                    </div>
                </div>
                <% }%>
            </div>

            <!-- Footer -->
            <footer class="bg-dark text-white mt-5 py-3">
                <div class="container text-center">
                    <p>&copy; 2023 BlindBoxStore. All rights reserved.</p>
                </div>
            </footer>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>
    </html>