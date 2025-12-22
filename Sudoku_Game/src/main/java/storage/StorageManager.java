package storage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class StorageManager {
    
    private static final String ROOT = "storage";
    private final LogHandler logHandler;

    public StorageManager() {
        for (String d : new String[]{"easy", "medium", "hard", "current"})
            new File(ROOT, d).mkdirs();
            
        this.logHandler = new LogHandler(ROOT); 
    }
    public void saveBoard(int[][] b,String p)throws IOException{
        try(PrintWriter pw=new PrintWriter(p)){
            for(int[] r:b){
                for(int v:r) pw.print(v+" ");
                pw.println();
            }
        }
    }

    public int[][] loadBoard(String p)throws IOException{
        int[][] b=new int[9][9];
        try(Scanner sc=new Scanner(new File(p))){
            for(int i=0;i<9;i++)
                for(int j=0;j<9;j++)
                    b[i][j]=sc.nextInt();
        }
        return b;
    }

    public void log(String entry) throws IOException {
        logHandler.log(entry); // Delegate to LogHandler
    }

    public void undo(int[][] board) throws IOException {
        logHandler.undo(board); // Delegate to LogHandler
    }

    public void deleteCurrent() {
        new File(ROOT + "/current/game.txt").delete();
        logHandler.deleteLog(); // Delegate delete
    }
}
