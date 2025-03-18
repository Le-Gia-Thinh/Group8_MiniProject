<%@page import="model.dto.CategoryDTO"%>
<%@page import="java.util.List"%>
<%@page import="model.utils.Constants"%>
<%@page import="model.dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Book - Bookstore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <%
            UserDTO user = (UserDTO) session.getAttribute("USER");
            if (user == null || !Constants.ADMIN_ROLE.equals(user.getRole())) {
                response.sendRedirect("MainController?btAction=Login");
                return;
            }
            
            List<CategoryDTO> categories = (List<CategoryDTO>) request.getAttribute("CATEGORIES");
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
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Update&action=view">Manage Books</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="MainController?btAction=Create&action=view">Add Book</a>
                        </li>
                    </ul>
                    <ul class="navbar-nav">
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
            
            <!-- Create Book Form -->
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Add New Book</h4>
                </div>
                <div class="card-body">
                    <form action="MainController" method="POST" enctype="multipart/form-data">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="title" class="form-label">Title</label>
                                <input type="text" class="form-control" id="title" name="title" required>
                            </div>
                            <div class="col-md-6">
                                <label for="author" class="form-label">Author</label>
                                <input type="text" class="form-control" id="author" name="author" required>
                            </div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label for="price" class="form-label">Price</label>
                                <input type="number" class="form-control" id="price" name="price" step="0.01" min="0" required>
                            </div>
                            <div class="col-md-4">
                                <label for="quantity" class="form-label">Quantity</label>
                                <input type="number" class="form-control" id="quantity" name="quantity" min="0" required>
                            </div>
                            <div class="col-md-4">
                                <label for="categoryID" class="form-label">Category</label>
                                <select class="form-select" id="categoryID" name="categoryID" required>
                                    <% if (categories != null) {
                                        for (CategoryDTO category : categories) { %>
                                    <option value="<%= category.getCategoryID() %>">
                                        <%= category.getCategoryName() %>
                                    </option>
                                    <% }
                                    } %>
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="imageFile" class="form-label">Image</label>
                            <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*">
                            <div class="form-text">If no image is selected, a default image will be used.</div>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <a href="MainController?btAction=Update&action=view" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary" name="btAction" value="Create">Create Book</button>
                            <input type="hidden" name="action" value="create">
                        </div>
                    </form>
                </div>
            </div>
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