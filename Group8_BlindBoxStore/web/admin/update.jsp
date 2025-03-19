<%@page import="model.dto.CategoryDTO"%>
<%@page import="model.dto.BookDTO"%>
<%@page import="java.util.List"%>
<%@page import="model.utils.Constants"%>
<%@page import="model.dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Books - Bookstore</title>
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

            BookDTO book = (BookDTO) request.getAttribute("BOOK");
            List<BookDTO> books = (List<BookDTO>) request.getAttribute("BOOKS");
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
                            <a class="nav-link active" href="MainController?btAction=Update&action=view">Manage Books</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Create&action=view">Add Book</a>
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

            <% if (book != null) {%>
            <!-- Edit Book Form -->
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Edit Book</h4>
                </div>
                <div class="card-body">
                    <form action="MainController" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="bookID" value="<%= book.getBookID()%>">
                        <input type="hidden" name="currentImageUrl" value="<%= book.getImageUrl()%>">

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="title" class="form-label">Title</label>
                                <input type="text" class="form-control" id="title" name="title" value="<%= book.getTitle()%>" required>
                            </div>
                            <div class="col-md-6">
                                <label for="author" class="form-label">Author</label>
                                <input type="text" class="form-control" id="author" name="author" value="<%= book.getAuthor()%>" required>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label for="price" class="form-label">Price</label>
                                <input type="number" class="form-control" id="price" name="price" value="<%= book.getPrice()%>" step="0.01" min="0" required>
                            </div>
                            <div class="col-md-4">
                                <label for="quantity" class="form-label">Quantity</label>
                                <input type="number" class="form-control" id="quantity" name="quantity" value="<%= book.getQuantity()%>" min="0" required>
                            </div>
                            <div class="col-md-4">
                                <label for="categoryID" class="form-label">Category</label>
                                <select class="form-select" id="categoryID" name="categoryID" required>
                                    <% if (categories != null) {
                                            for (CategoryDTO category : categories) {%>
                                    <option value="<%= category.getCategoryID()%>" <%= (book.getCategoryID() == category.getCategoryID()) ? "selected" : ""%>>
                                        <%= category.getCategoryName()%>
                                    </option>
                                    <% }
                                        }%>
                                </select>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3" required><%= book.getDescription()%></textarea>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="imageFile" class="form-label">Image</label>
                                <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*">
                                <div class="form-text">Current image: <%= book.getImageUrl()%></div>
                            </div>
                            <div class="col-md-6">
                                <label for="status" class="form-label">Status</label>
                                <div class="form-check form-switch mt-2">
                                    <input class="form-check-input" type="checkbox" id="status" name="status" <%= book.isStatus() ? "checked" : ""%>>
                                    <label class="form-check-label" for="status">Active</label>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between">
                            <a href="MainController?btAction=Update&action=view" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary" name="btAction" value="Update">Update Book</button>
                            <input type="hidden" name="action" value="update">
                        </div>
                    </form>
                </div>
            </div>
            <% } else { %>
            <!-- Book List -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h4>Manage Books</h4>
                    <a href="MainController?btAction=Create&action=view" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Add New Book
                    </a>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Image</th>
                                    <th>Title</th>
                                    <th>Author</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (books != null && !books.isEmpty()) {
                                        for (BookDTO bookItem : books) {%>
                                <tr>
                                    <td><%= bookItem.getBookID()%></td>
                                    <td>
                                        <% String path = bookItem.getImageUrl(); %>
                                        <img src="<%= path%>" alt="<%= bookItem.getTitle()%>" style="width: 50px; height: 70px; object-fit: cover;">
                                    </td>
                                    <td><%= bookItem.getTitle()%></td>
                                    <td><%= bookItem.getAuthor()%></td>
                                    <td><%= bookItem.getCategoryName()%></td>
                                    <td>$<%= String.format("%.2f", bookItem.getPrice())%></td>
                                    <td><%= bookItem.getQuantity()%></td>
                                    <td>
                                        <span class="badge <%= bookItem.isStatus() ? "bg-success" : "bg-danger"%>">
                                            <%= bookItem.isStatus() ? "Active" : "Inactive"%>
                                        </span>
                                    </td>
                                    <td>
                                        <a href="MainController?btAction=Update&action=edit&bookID=<%= bookItem.getBookID()%>" class="btn btn-sm btn-warning">
                                            <i class="fas fa-edit"></i> Edit
                                        </a>
                                    </td>
                                </tr>
                                <% }
                                } else { %>
                                <tr>
                                    <td colspan="9" class="text-center">No books found</td>
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
                <p>&copy; 2023 Bookstore. All rights reserved.</p>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>