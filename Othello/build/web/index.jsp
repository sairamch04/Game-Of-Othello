<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Othello !</title>
        <!-- Latest compiled and minified CSS -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" >
        <!-- jQuery library -->
        <script src="js/jquery-1.11.2.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        

    </head>
    <body>
        <div class="container">
            <div class="row">
                <form action="${pageContext.request.contextPath}/play" method="POST">
                    <div class="form-group">
                        <label for="name">Name : </label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="Enter Name">
                        <label for="gameId">Game ID : </label>
                        <input type="text" class="form-control" id="gameId" name="gameId" placeholder="Enetr Game ID if JOIN">
                        <button class="btn btn-primary"  type="submit" name="type" value="NEW">New Game</button>
                        <button class="btn btn-success"  type="submit" name="type" value="JOIN">Join Existing!</button>
                    </div>


            </div>

        </div>
    </body>
</html>
