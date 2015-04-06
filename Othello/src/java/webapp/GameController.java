
package webapp;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GameController extends HttpServlet {
    public static Map<String,Game> idToGame = Collections.synchronizedMap(new HashMap<String,Game>());

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      String type=request.getParameter("type");
      String ssid=request.getSession().getId();
      Game g=null;
      
      switch(type){
          
          case "NEW":
             
              
              g=idToGame.get(ssid);
              if(g==null){
                  
              }
              else{
                  idToGame.remove(ssid);
              }
              g=new Game();
              
              g.player1=request.getParameter("name");
              idToGame.put(ssid,g );
              request.getRequestDispatcher("/playGame.jsp").forward(request, response);
              
              break;
          case "JOIN":
              String gameId=request.getParameter("gameId");
              g=idToGame.get(gameId);
              if(g!=null){
                  
                  g.player2=request.getParameter("name");
                  request.getRequestDispatcher("/playGame.jsp").forward(request, response);
                  
              }
              else{
                  System.out.println(request.getSession().getId()+" : "+"Invalid Game id ! ");
                  request.getRequestDispatcher("/index.jsp").forward(request, response);
                  
              }
              break;
          case "CLOSE":
              break;
      }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(request.getSession().getId()+" : "+"U can't access URL directly !");
                  request.getRequestDispatcher("index.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
