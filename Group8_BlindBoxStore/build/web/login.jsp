<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login - BlindBoxstore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="text-center">Login</h3>
                        </div>
                        <div class="card-body">
                            <% if (request.getAttribute("ERROR") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= request.getAttribute("ERROR") %>
                            </div>
                            <% } %>
                            <form action="MainController" method="POST">
                                <div class="mb-3">
                                    <label for="userID" class="form-label">User ID</label>
                                    <input type="text" class="form-control" id="userID" name="userID" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary" name="btAction" value="Login">Login</button>
                                </div>
                            </form>
                        </div>
                        <div class="card-footer text-center">
                            <a href="MainController?btAction=Search" class="btn btn-link">Continue as Guest</a>
                            <a href="https://accounts.google.com/o/oauth2/auth?scope=profile&redirect_uri=http://localhost:8080/Group8_BlindBoxStore/LoginGoogleHandler&response_type=code
		   &client_id=909740785037-q5g15o1mrs61fei85u4jvrvkqc7vao92.apps.googleusercontent.com&approval_prompt=force">Login With Google</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>