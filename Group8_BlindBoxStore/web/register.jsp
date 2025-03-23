<html>
    <head>
        <title>Register Page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
                integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
        crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
                integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
        crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/2b9cdc1c9a.js" crossorigin="anonymous"></script>
        <link href="./css/register.css" rel="stylesheet">
        <style>
            .error {
                color: red;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
         <div class="bg-img">
            <div class="content-register mt-5 pt-4">
                <h3 class="text-center text-light">Registration Form</h3>
                <form action="MainController" method="POST">
                    <div class="row space">
                        <div data-mdb-input-init class="form-outline">     
                            <label class="form-label error" for="userID">${requestScope.USER_ERROR.userIDError}</label>
                            <input type="text" required placeholder="UserID" id="" class="form-control form-control-lg" name="userID" required />
                        </div>                       
                    </div>
                    <div class="row space">
                        <div data-mdb-input-init class="form-outline">
                            <input type="password" placeholder="Password" id="" class="form-control form-control-lg" name="password" required/>
                        </div>
                    </div>

                    <div class="row space">
                        <div data-mdb-input-init class="form-outline">
                            <label class="form-label error" for="firstName">${requestScope.USER_ERROR.confirmError}</label>
                            <input type="password" placeholder="Confirm password" id="" class="form-control form-control-lg" name="confirm" required/>
                        </div>
                    </div>

                    <div class="row space">
                        <div data-mdb-input-init class="form-outline">
                            <label class="form-label error" for="fullName">${requestScope.USER_ERROR.fullNameError}</label>
                            <input type="text" required placeholder="Full Name" id="" class="form-control form-control-lg" name="fullName" required />
                        </div>                                                          
                    </div>
                    <div class="row space">
                        <div class="col-md-10 mb-6">
                            <div data-mdb-input-init class="form-outline">
                                <input type="text" required placeholder="Phone" id="" class="form-control form-control-lg" name="phone" />
                            </div>
                        </div>
                        <div class="col-md-2 mb-2">
                            <div data-mdb-input-init class="form-outline">
                                <input type="text" class="form-control form-control-lg text-center" value="USER" name="role" readonly=""/>
                            </div>
                        </div>
                    </div>

                    <div class="row space">
                        <div data-mdb-input-init class="form-outline">
                            <label class="form-label error" for="fullName">${requestScope.USER_ERROR.emailError}</label>
                            <input type="email" placeholder="Email" id="emailAddress" class="form-control form-control-lg" name="email" required=""/>
                        </div>
                    </div>

                    <div class="row space">
                        <div data-mdb-input-init class="form-outline">
                            <input type="text" placeholder="Address" id="phoneNumber" class="form-control form-control-lg" name="address" />
                        </div>
                    </div>
                    <div class="pass space">
                        <input type="checkbox" class="form-check-input" id="agreeTerms" name="agreeTerms" required>
                        <a href="#">Agree to the terms</a>
                    </div>
                    <div class="field space">
                        <input type="submit" name="btAction" value="Register">
                    </div>
                </form>
                <p class="text-light space">Do you already have an account? <a class="text-primary" href="MainController?btAction=Login">Login</a></p>
            </div>
        </div>
    </body>
</html>
