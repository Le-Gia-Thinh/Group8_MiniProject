<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login - BlindBoxstore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link href="./css/login.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5 backgroud-img">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="text-center">Login</h3>
                        </div>
                        <div class="card-body">
                            <% if (request.getAttribute("ERROR") != null) {%>
                            <div class="alert alert-danger" role="alert">
                                <%= request.getAttribute("ERROR")%>
                            </div>
                            <% }%>
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
                            <button  type="button" data-mdb-button-init data-mdb-ripple-init class="button login">
                                <a href="MainController?btAction=Search" class="btn btn-link">Continue as Guest (Back Home) </a>
                            </button>
                            <button  type="button" data-mdb-button-init data-mdb-ripple-init class="button login">
                                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/Group8_BlindBoxStore/LoginGoogleHandler&response_type=code
                                   &client_id=909740785037-q5g15o1mrs61fei85u4jvrvkqc7vao92.apps.googleusercontent.com&approval_prompt=force">Login With Google</a>
                            </button>

                        </div >
                        <div class="card-footer text-center" >
                            <p>Not a member?</p>
                            <button  type="button" data-mdb-button-init data-mdb-ripple-init class="button login">

                                <a href="MainController?btAction=Register">Register</a>
                            </button>

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>