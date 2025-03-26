<%@page import="model.dto.UserDTO"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Track Order - BlindBoxstore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link href="./css/search.css" rel="stylesheet">
    </head>
    <body>
        <c:if test="${empty sessionScope.LOGIN_USER}">
            <c:redirect url="MainController?btAction=Login"/>
        </c:if>

        <!-- Navigation Bar -->
        <nav class="navbar navbar-expand-lg navbar-dark">
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
                        <c:if test="${sessionScope.LOGIN_USER != null && sessionScope.LOGIN_USER.role == 'ADMIN'}">
                            <li class="nav-item">
                                <a class="nav-link" href="MainController?btAction=Update&action=view">Manage BlindBoxs</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="MainController?btAction=Create&action=view">Add BlindBox</a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.LOGIN_USER != null}">
                            <li class="nav-item">
                                <a class="nav-link active" href="MainController?btAction=TrackOrder">Track Order</a>
                            </li>
                            <li class="nav-item">
                            <a class="nav-link active" href="MainController?btAction=ViewRevenue">
                                <i class="fas fa-chart-bar"></i> Revenue Report
                            </a>
                        </li>
                        </c:if>
                    </ul>
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=ViewCart">
                                <i class="fas fa-shopping-cart"></i> Cart
                                <c:if test="${not empty sessionScope.CART}">
                                    <span class="badge bg-danger">${sessionScope.CART.size()}</span>
                                </c:if>
                            </a>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Welcome, <c:out value="${sessionScope.LOGIN_USER.fullName}"/>
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
            <c:if test="${not empty requestScope.ERROR}">
                <div class="alert alert-danger" role="alert">
                    <c:out value="${requestScope.ERROR}"/>
                </div>
            </c:if>

            <div class="card mb-4">
                <div class="card-header">
                    <h4>Track Order</h4>
                </div>
                <div class="card-body">
                    <form action="MainController" method="GET">
                        <button type="submit" class="btn btn-primary w-100" name="btAction" value="TrackOrder">Xem Đơn Hàng</button>
                    </form>
                </div>
            </div>
                        
                        
            <div class="container mt-4">
                <div class="card mb-4">
                    <div class="card-header bg-warning text-dark"">
                        <h4>Your Orders</h4>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty requestScope.USER_ORDERS}">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Order ID</th>
                                            <th>Order Date</th>
                                            <th>Total Amount</th>
                                            <th>Payment Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${requestScope.USER_ORDERS}">
                                            <tr>
                                                <td>${order.orderID}</td>
                                                <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td>$<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td>
                                                <td>
                                                    <span class="badge ${order.paymentStatus == 'COMPLETED' ? 'bg-success' : 'bg-warning'}">
                                                        ${order.paymentStatus}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="MainController?btAction=ViewOrderDetail&orderID=${order.orderID}" class="btn btn-sm btn-info">
                                                        View Details
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <p class="text-center text-muted">No orders found.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Footer -->
        <footer class="bg-dark text-white mt-5 py-3">
            <div class="container text-center">
                <p>&copy; 2025 BlindBoxStore. All rights reserved.</p>
            </div>
        </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function validateTrackForm() {
                const orderID = document.getElementById('orderID').value;
                if (orderID <= 0) {
                    alert('Please enter a valid Order ID (positive number).');
                    return false;
                }
                return true;
            }
        </script>
    </body>
</html>