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
                        <c:if test="${sessionScope.LOGIN_USER != null && sessionScope.LOGIN_USER.role == 'AD'}">
                            <li class="nav-item">
                                <a class="nav-link" href="MainController?btAction=Update&action=view">Manage BlindBoxs</a>
                            </li>
                        </c:if>
                        <c:if test="${sessionScope.LOGIN_USER != null}">
                            <li class="nav-item">
                                <a class="nav-link active" href="MainController?btAction=TrackOrder">Track Order</a>
                            </li>
                            <li class="nav-i
                            <li class="nav-item">
                                <a class="nav-link" href="MainController?btAction=Create&action=view">Add BlindBox</a>
                            </li>tem">
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
        
        <!-- Main content -->
        <c:if test="${not empty requestScope.ORDER}">
                <div class="card mb-4">
                    <div class="card-header bg-warning text-dark">
                        <h4>Order #${requestScope.ORDER.orderID}</h4>
                    </div>
                    <div class="card-body">
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h5>Order Information</h5>
                                <p><strong>Order ID:</strong> #${requestScope.ORDER.orderID}</p>
                                <p><strong>Order Date:</strong> <fmt:formatDate value="${requestScope.ORDER.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                                <p><strong>Payment Method:</strong> <c:out value="${requestScope.ORDER.paymentMethod}"/></p>
                                <p><strong>Payment Status:</strong>
                                    <span class="badge ${requestScope.ORDER.paymentStatus == 'COMPLETED' ? 'bg-success' : 'bg-warning'}">
                                        <c:out value="${requestScope.ORDER.paymentStatus}"/>
                                    </span>
                                </p>
                                <p><strong>Total Amount:</strong> $<fmt:formatNumber value="${requestScope.ORDER.totalAmount}" pattern="#,##0.00"/></p>
                            </div>
                            <div class="col-md-6">
                                <h5>Customer Information</h5>
                                <p><strong>Name:</strong> <c:out value="${requestScope.ORDER.customerName}"/></p>
                                <p><strong>Email:</strong> <c:out value="${requestScope.ORDER.customerEmail}"/></p>
                                <p><strong>Phone:</strong> <c:out value="${requestScope.ORDER.customerPhone}"/></p>
                                <p><strong>Address:</strong> <c:out value="${requestScope.ORDER.customerAddress}"/></p>
                            </div>
                        </div>

                        <h5>Order Details</h5>
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>BlindBox</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty requestScope.ORDER.orderDetails}">
                                        <c:forEach var="detail" items="${requestScope.ORDER.orderDetails}">
                                            <tr>
                                                <td><c:out value="${detail.productName}"/></td>
                                                <td>$<fmt:formatNumber value="${detail.price}" pattern="#,##0.00"/></td>
                                                <td>${detail.quantity}</td>
                                                <td>$<fmt:formatNumber value="${detail.total}" pattern="#,##0.00"/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3" class="text-end"><strong>Total:</strong></td>
                                        <td><strong>$<fmt:formatNumber value="${requestScope.ORDER.totalAmount}" pattern="#,##0.00"/></strong></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
            </c:if>
                            
        
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