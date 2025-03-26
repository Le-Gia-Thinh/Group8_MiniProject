<%@page import="java.util.Map"%>
<%@page import="model.dto.UserDTO"%>
<%@page import="model.utils.Constants"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Revenue Report - Admin</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link href="./css/search.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            #revenueChart, #revenueColumnChart {
                width: 100%;
                height: 400px;
            }
        </style>
    </head>
    <body>
        <%-- Kiểm tra quyền admin --%>
        <% UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            if (user == null || !Constants.ADMIN_ROLE.equals(user.getRole())) {
                response.sendRedirect("MainController?btAction=Login");
                return;
            }
        %>

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
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Update&action=view">Manage Products</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="MainController?btAction=Create&action=view">Add Product</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="MainController?btAction=TrackOrder">Track Order</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="MainController?btAction=ViewRevenue">
                                <i class="fas fa-chart-bar"></i> Revenue Report
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
            <div class="card mb-4">
                <div class="card-header">
                    <h4>Revenue Report</h4>
                </div>
                <div class="card-body">
                    <div class="row mb-3">
                        <div class="col-md-4">
                            <div class="border rounded p-3 bg-light text-center">
                                <h6>Daily Revenue</h6>
                                <h4 class="text-primary">
                                    $<%= request.getAttribute("DAILY_REVENUE") != null ? String.format("%.2f", (Double) request.getAttribute("DAILY_REVENUE")) : "0.00"%>
                                </h4>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="border rounded p-3 bg-light text-center">
                                <h6>Monthly Revenue</h6>
                                <h4 class="text-success">
                                    $<%= request.getAttribute("MONTHLY_REVENUE") != null ? String.format("%.2f", (Double) request.getAttribute("MONTHLY_REVENUE")) : "0.00"%>
                                </h4>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="border rounded p-3 bg-light text-center">
                                <h6>Yearly Revenue</h6>
                                <h4 class="text-warning">
                                    $<%= request.getAttribute("YEARLY_REVENUE") != null ? String.format("%.2f", (Double) request.getAttribute("YEARLY_REVENUE")) : "0.00"%>
                                </h4>
                            </div>
                        </div>
                    </div>

                    <form action="MainController" method="GET" class="mb-3">
                        <input type="hidden" name="btAction" value="ViewRevenue">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="selectedDate" class="form-label">Select Date</label>
                                <input type="date" id="selectedDate" name="selectedDate" class="form-control" required 
                                       value="<%= request.getAttribute("SELECTED_DATE") != null ? request.getAttribute("SELECTED_DATE") : ""%>">
                            </div>
                            <div class="col-md-6 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary">View Revenue</button>
                            </div>
                        </div>
                    </form>

                    <div class="mt-4">
                        <h5>Revenue in the Last 7 Days</h5>
                        <div id="noWeeklyData" class="text-muted" style="display: none;">No data available for the last 7 days.</div>
                        <canvas id="revenueChart"></canvas>
                    </div>

                    <div class="mt-4">
                        <h5>Daily, Monthly, and Yearly Revenue (Column Chart)</h5>
                        <div id="noColumnData" class="text-muted" style="display: none;">No data available for this period.</div>
                        <canvas id="revenueColumnChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const selectedDate = document.getElementById("selectedDate").value || new Date().toISOString().split('T')[0];

                fetch(`MainController?btAction=ViewRevenue&selectedDate=${selectedDate}&json=true`)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error(`HTTP error! status: ${response.status}`);
                            }
                            return response.json();
                        })
                        .then(data => {
                            console.log("API Response:", data); 

                            // Chart rendering for last 7 days revenue
                            const weeklyChartElement = document.getElementById("revenueChart");
                            const noWeeklyDataElement = document.getElementById("noWeeklyData");
                            if (data.last7Days && data.last7Days.length > 0) {
                                noWeeklyDataElement.style.display = "none";
                                weeklyChartElement.style.display = "block";
                                const ctx = weeklyChartElement.getContext("2d");
                                new Chart(ctx, {
                                    type: "bar",
                                    data: {
                                        labels: data.last7Days.map(d => d.date),
                                        datasets: [{
                                                label: "Revenue ($)",
                                                data: data.last7Days.map(d => d.revenue || 0),
                                                backgroundColor: "rgba(54, 162, 235, 0.5)",
                                                borderColor: "rgba(54, 162, 235, 1)",
                                                borderWidth: 1
                                            }]
                                    },
                                    options: {
                                        responsive: true,
                                        scales: {
                                            y: {beginAtZero: true}
                                        }
                                    }
                                });
                            } else {
                                noWeeklyDataElement.style.display = "block";
                                weeklyChartElement.style.display = "none";
                                console.warn("No data available for the last 7 days.");
                            }

                            // Column chart for daily, monthly, and yearly revenue
                            const columnChartElement = document.getElementById("revenueColumnChart");
                            const noColumnDataElement = document.getElementById("noColumnData");
                            const hasColumnData = data.dayRevenue != null || data.monthRevenue != null || data.yearRevenue != null;
                            if (hasColumnData) {
                                noColumnDataElement.style.display = "none";
                                columnChartElement.style.display = "block";
                                const columnChartCtx = columnChartElement.getContext("2d");
                                new Chart(columnChartCtx, {
                                    type: "bar",
                                    data: {
                                        labels: ['Daily', 'Monthly', 'Yearly'],
                                        datasets: [{
                                                label: "Revenue ($)",
                                                data: [
                                                    data.dayRevenue || 0,
                                                    data.monthRevenue || 0,
                                                    data.yearRevenue || 0
                                                ],
                                                backgroundColor: ["#FF5733", "#33FF57", "#3357FF"],
                                                borderColor: ["#FF5733", "#33FF57", "#3357FF"],
                                                borderWidth: 1
                                            }]
                                    },
                                    options: {
                                        responsive: true,
                                        scales: {
                                            y: {beginAtZero: true}
                                        },
                                        plugins: {
                                            title: {
                                                display: true,
                                                text: 'Daily, Monthly, and Yearly Revenue'
                                            }
                                        }
                                    }
                                });
                            } else {
                                noColumnDataElement.style.display = "block";
                                columnChartElement.style.display = "none";
                                console.warn("No data available for daily, monthly, or yearly revenue.");
                            }
                        })
                        .catch(error => {
                            console.error("Fetch Error:", error);
                            document.getElementById("noWeeklyData").style.display = "block";
                            document.getElementById("revenueChart").style.display = "none";
                            document.getElementById("noColumnData").style.display = "block";
                            document.getElementById("revenueColumnChart").style.display = "none";
                        });
            });
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>