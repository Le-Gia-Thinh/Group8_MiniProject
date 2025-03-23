<%-- 
    Document   : user
    Created on : May 28, 2024, 9:00:02 AM
    Author     : LENOVO
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.dto.UserDTO"%>
<%@page import="model.utils.Constants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
                integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
        crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
                integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/2b9cdc1c9a.js" crossorigin="anonymous"></script>
        <link href="./css/style.css" rel="stylesheet"/>
        <title>BlindBox_Store</title>
        <style>
            .nav-hover .nav-link:hover {
                background-color: white !important;
                color: black !important;
            }

            .shadow-md {
                box-shadow: 0 10px 15px 10px rgba(0, 0, 0, 0.07), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            }

            .fs-8 {
                font-size: 0.9rem;
            }

            .btn-bg-active {
                background-color: white !important;
                color: black !important;
            }

            a:hover{
                color: red !important;
            }

            .gvi {
                margin-top: -20px;
            }

            .quantity{
                max-width: 30%;
            }
        </style>
    </head>
    <body>
         <%
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            %>
        <c:if test="${sessionScope.LOGIN_USER == null || sessionScope.LOGIN_USER.role ne 'USER'} ">
            <c:redirect url="login.jsp"></c:redirect>
        </c:if>
        <header class="container-fluid bg-white p-0">
            <div class="header-top bg-gray  border-bottom">
                <div class="container">
                    <div class="row">
                        <div class="col-md-8">
                            <ul class="d-inline-flex pt-0 pt-md-2 fs-6">
                                <li class="p-2 d-none d-md-block"><i class="fa-solid fa-envelope"></i> Support@legiathinh0508gmail.com</li>
                                <li class="p-2 d-none d-md-block"><i class="fa-solid fa-phone"></i> +999 999 999 999</li>
                            </ul>
                        </div>
                        <div class="col-md-4 d-flex align-items-end">
                            <c:if test="${sessionScope.LOGIN_USER == null}" >
                                <ul class="ms-auto d-inline-flex">
                                    <li class="p-2 d-none d-md-block"><a href="MainController?btAction=Login"><button class="btn px-4 btn-outline-success">Login</button></a></li>
                                    <li class="p-2 d-none d-md-block"><a href="MainController?btAction=Register"><button class="btn px-4 btn-success">Sign Up</button></a></li>
                                </ul>
                            </c:if>                    
                </div>
            </div>
    
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
            </ul>
        </div>
    </div>
</nav>


<section style="background-color: #eee;">
    <div class="row p-4">
        <div class="col-lg-4">
            <div class="card mb-4 pb-5">
                <div class="card-body text-center">
                    <img src="https://s.net.vn/m4lC" alt="avatar"
                         class="rounded-circle img-fluid" style="width: 150px;">
                    <h5 class="my-3">${sessionScope.LOGIN_USER.fullName}</h5>
                    <p class="text-muted mb-1">@${sessionScope.LOGIN_USER.userID}</p>
                    <p class="text-muted mb-4">${sessionScope.LOGIN_USER.address}</p>
                </div>
            </div>
        </div>
        <div class="col-lg-8">
            <div class="card mb-4 pb-1">
                <div class="card-body">
                    <div class="row">
                        <div class="col-sm-3">
                            <p class="mb-0">Full Name</p>
                        </div>
                        <div class="col-sm-9">
                            <p class="text-muted mb-0">${sessionScope.LOGIN_USER.fullName}</p>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-sm-3">
                            <p class="mb-0">Email</p>
                        </div>
                        <div class="col-sm-9">
                            <p class="text-muted mb-0">${sessionScope.LOGIN_USER.email}</p>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-sm-3">
                            <p class="mb-0">Role</p>
                        </div>
                        <div class="col-sm-9">
                            <p class="text-muted mb-0">${sessionScope.LOGIN_USER.role}</p>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-sm-3">
                            <p class="mb-0">Phone</p>
                        </div>
                        <div class="col-sm-9">
                            <p class="text-muted mb-0">${sessionScope.LOGIN_USER.phone}</p>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-sm-3">
                            <p class="mb-0">Address</p>
                        </div>
                        <div class="col-sm-9">
                            <p class="text-muted mb-0">${sessionScope.LOGIN_USER.address}</p>
                        </div>
                    </div>
                    <hr>
                </div>
            </div>
        </div>
</section>
  <!-- Footer -->
        <footer class="bg-dark text-white mt-5 py-3">
            <div class="container text-center">
                <p>&copy; 2025 BlindBoxstore. All rights reserved.</p>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
      <!-- Footer -->    
        
    </body>
</html>
