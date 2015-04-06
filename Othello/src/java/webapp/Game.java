package webapp;

import java.util.ArrayList;
import javafx.util.Pair;
import javax.websocket.Session;

public class Game {

    Session session1;
    Session session2;
    String player1;
    String player2;
    int[][] matrix;
 
    int turn;
    int blackCount;
    int whiteCount;
    boolean isJoin1=false;
    boolean isJoin2=false;
    // 1- White
    //2 -black
    //3 - can be put
    //player1 color is white

    Game() {

       
        turn = 1;
        
        matrix = new int[8][8];
        whiteCount = 2;
        blackCount = 2;
        matrix[3][3] = 1;
        matrix[3][4] = 2;
        matrix[4][3] = 2;
        matrix[4][4] = 1;
        //updateReds();

    }

    boolean insertDisk(int x, int y) {
        int validDirections=0;
        

        
        matrix[x][y]=turn;
        if (turn == 2) {
            int tmp = whiteCount;
            whiteCount = blackCount;
            blackCount = tmp;
        }
        whiteCount++;
        //left
        for (int i = y - 1; i >= 0; i--) {
            if (matrix[x][i] == 0 || matrix[x][i] == 3) {
                break;

            }
            if (matrix[x][i] == turn) {
                if(i+1 !=y)
                validDirections++;
                for (int j = i + 1; j < y; j++) {
                    whiteCount++;
                    blackCount--;
                    matrix[x][j] = turn;

                }
                break;
            }

        }
        //right
        for (int i = y + 1; i <8; i++) {
            if (matrix[x][i] == 0 || matrix[x][i] == 3) {
                break;

            }
            if (matrix[x][i] == turn) {
                if(i-1!=y)
                validDirections++;
                for (int j = i - 1; j > y; j--) {
                    whiteCount++;
                    blackCount--;
                    matrix[x][j] = turn;

                }
                break;
            }

        }
        //bottom
        for (int i = x + 1; i <8; i++) {
            if (matrix[i][y] == 0 || matrix[i][y] == 3) {
                break;

            }
            if (matrix[i][y] == turn) {
                if(i-1!=x)
                validDirections++;
                for (int j = i - 1; j > x; j--) {
                    whiteCount++;
                    blackCount--;
                    matrix[j][y] = turn;

                }
                break;
            }

        }
        //top
        for (int i = x - 1; i >= 0; i--) {
           
            if (matrix[i][y] == 0 || matrix[i][y] == 3) {
                break;

            }
            if (matrix[i][y] == turn) {
                if(i+1!=x)
                validDirections++;
                for (int j = i + 1; j < x; j++) {
                    whiteCount++;
                    blackCount--;
                    matrix[j][y] = turn;

                }
                break;
            }

        }
        if(validDirections==0){
            //undo the steps done. Thats what othello rule states!
            matrix[x][y]=0;
            whiteCount--;
        }
        if (turn == 2) {
            int tmp = whiteCount;
            whiteCount = blackCount;
            blackCount = tmp;
        }
        if(validDirections==0){
            return false;
        }
        
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
        }
        return true;
       // updateReds();
        

    }
    void updateReds(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                matrix[i][j]=(matrix[i][j]==3)?0:matrix[i][j];
            }
        }
        //after updating : if there are no REDS THEN pASS !! change the turn simply !!
        
        
    }
    boolean isGameFinished(){
        if(whiteCount+blackCount ==64){
            return true;
        }
        else
            return false;
        
    }
    String  getWhoWon(){
        if(!isGameFinished()){
            return "";
        }
        
        if((whiteCount > blackCount) ){
            return player1;
            
            
        }
        return player2;
        
    }


}
