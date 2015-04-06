<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <title>Play It <%= request.getParameter("name") %>!</title>
    </head>
    <body>
        <div class="container">
            <div class="row" >
                <div class="row col-md-offset-2">
                    <h1>Welcome <%= request.getParameter("name") %> </h1>
                </div>
                    
            </div>
            <div class="row" id="NEW" >
                <div class="row col-md-offset-2">
               
                <% if (request.getParameter("type").equals("NEW")){ %>
                Token is : <%= request.getSession().getId() %> .<br>
                Waiting for your friends to join... <br>
                <% }%>
                </div>


            </div>
                
            <div class="row" id="board">
                <div class="col-md-4 col-md-offset-2" id="tableContainer">
                    <table class='table table-bordered' id='othello'>
                        
                    </table>
                    



                </div>
                <div class="col-md-4 col-md-offset-2">
                    <h2> RED Coins(<label id="player1"></label>):  </h2> <h3 id="whiteCount">2</h3> <br>
                    <h2> Blue Coins(<label id="player2"></label>) : </h2>   <h3 id="blackCount">2</h3> <br><hr>
                    Messages: <hr>
                    <h2 id="messages">
                        
                    </h2>

                </div>


            </div>
                <script src="js/jquery-1.11.2.min.js"></script>
            <script src="js/bootstrap.min.js"></script>
            



            <script>
                $("#board").hide();
                        $(document).ready(function () {
                           
                <% String userName = request.getParameter("name");
                    int playerId;
                    boolean turn;

                    if (request.getParameter("type").equals("NEW")) {
                        playerId = 1;
                        turn = true;

                    } else {
                        playerId = 2;
                        turn = false;
                    }

                %>
                var userName ="<%= userName %>";
                var otherPlayer;
                var playerId = parseInt(<%= playerId %> );
                var gameId;  //set this vallue when u get from server in case of "JOIN"
                <% if (request.getParameter("type").equals("NEW")){ %>
                      gameId="<%= request.getSession().getId() %>";
                      <% } else if(request.getParameter("type").equals("JOIN")){ %>
                    gameId= "<%= request.getParameter("gameId") %>";
                    <% } %>
                          
                var turn =parseInt( <%= turn %> );
                
                function  createTable(matrix) {
                                var text = "";
                                for (var i = 0; i < 8; i++){
                                    text += "<tr>";
                                    for (var j = 0; j < 8; j++){
                                        text += "<td>";
                                        var path;
                                        if (matrix[i][j] == '0'){
                                                path = "board.JPG";
                                        }
                                        else if (matrix[i][j] == '1'){
                                            path = "white.JPG";
                                        }
                                        else if (matrix[i][j] == '2'){
                                            path = "black.JPG";
                                        }
                                        else{
                                            path = "red.JPG";
                                        }
                                        if(path=="board.JPG"){
                                            text += "<img src='images/" + path + "'  class='img'>";
                                            
                                        }
                                        else{
                                            text += "<img src='images/" + path + "'  class='img-circle'>";
                                        }
                                        
                                         text += "</td>"


                            }

                        text += "</tr>";
                        }
                        text += "";
                        $("#othello").html(text);
                        $("#othello td").click(function() {
                    console.log("clicked !");
                    if(turn!=playerId){
                        alert("Not your turn !");
                        return;
                    }
 
                    var y = parseInt( $(this).index() );
                    var x = parseInt( $(this).parent().index() );  
                    console.log($(this).children('img').attr("src"));
 
                    if($(this).children('img').attr("src")=="images/board.JPG"){
                        sendToSocket(2,x,y);
                        console.log("send the moce to the sendTOSocket function :)");
                    }
                    else{
                        alert("NOT a valid move !! It already has a Coin!");
                    }
                });
                        
                }
                var webSocket;
                openSocket();
                

            function openSocket() {
                // Ensures only one connection is open at a time
                if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
                console.log("WebSocket is already opened.");
                        return;
                }
                webSocket = new WebSocket("ws://localhost:8080/Othello/endpoint");
                console.log("connected to websocket !");
                // Create a new instance of the websocket


                /**
                 * Binds functions to the listeners for the websocket.
                 */
                webSocket.onopen = function () {
                
                    console.log("Connection established !");
                    sendToSocket(1,0,0);
                };
                webSocket.onmessage = function (event) {
                    var message=event.data;
                    console.log("recieved Message: "+ message );
                    var obj=jQuery.parseJSON(message);
                    
                    if(obj.type==1){//recieved only when two players are active for first time :)
                        $("#board").show();
                        $("#NEW").hide();
                        
                        otherPlayer=obj.otherPlayer;
                        if(playerId ==1){
                            $("#player2").text(obj.otherPlayer);
                            $("#player1").text(userName);
                        }
                        else{
                            $("#player1").text(obj.otherPlayer);
                            $("#player2").text(userName);
                            
                            
                            
                        }
                        turn=obj.turn;
                        constructMatrix(obj.matrix);
                        
                        if(turn==1){
                                if(playerId==1){
                                    $("#messages").html("<br> Your turn !!! <br>");
                                }
                                else{
                                    $("#messages").html(otherPlayer+ "'s turn ! <br>");
                                }
                            }
                        else{
                                if(playerId==1){
                                    $("#messages").html(otherPlayer+ "'s turn ! <br>");
                                }
                                else{
                                    $("#messages").html("<br> Your turn !!! <br>");
                                    
                                }
                                
                                
                        }
                        
                        
                        
                    }
                    else{
                        turn=obj.turn;
                        if(!obj.wasValid){
                            alert("Oppsy !! Its Not a Valid Move !!!");
                            return;
                        }
                        $("#whiteCount").text(obj.whiteCount);
                        $("#blackCount").text(obj.blackCount);
                        constructMatrix(obj.matrix);
                        
                        
                        if(obj.hasDone===true){
                            $("#messages").html("<br>"+obj.whoWon+" </br> Game is Over !");
                            
                            
                        }
                        else{
                            if(turn==1){
                                if(playerId==1){
                                    $("#messages").html("<br> Your turn !!! <br>");
                                }
                                else{
                                    $("#messages").html(otherPlayer+ "'s turn ! <br>");
                                }
                            }
                            else{
                                if(playerId==1){
                                    $("#messages").html(otherPlayer+ "'s turn ! <br>");
                                }
                                else{
                                    $("#messages").html("<br> Your turn !!! <br>");
                                    
                                }
                                
                                
                            }
                            
                        }
                        
                    }
                    
                };
                webSocket.onclose = function () {
                    alert("Connection lost From the server !");
                };
            }
            function constructMatrix(m){
                //console.log(m);
                var matrix=new Array(8);
                for(var i=0;i<64;i+=8){
                    var dd=i/8;
                    matrix[dd]=new Array(8);
                    for(var j=0;j<8;j++){
                        
                        matrix[dd][j]=m.charAt(i+j);
                        //console.log(matrix[dd][j]);
                    }
                }
               // console.log(matrix);
                createTable(matrix);
                
            }
            

                /**
                 * Sends the value of the text input to the server
                 */
                function sendToSocket(type,x,y) {
                    var obj;
                    if(type==1){
                        obj='{"type":1,"gameId":"'+gameId+'","playerId":'+playerId+'}';
                    }
                    else{
                        obj='{"type":2,"gameId":"'+gameId+'","playerId":'+playerId+',"x":'+x+',"y":'+y+'}';
                    }
                    
                    
                    webSocket.send(obj);
                    console.log(obj +" : is sent to server !");
                }

                function closeSocket() {
                    webSocket.close();
                }
             
              


                });
            </script>



    </body>
</html>