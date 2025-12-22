package app;

import view.MainWindow;
import controller.GameController;
import javax.swing.SwingUtilities;



public class Main {
   public static void main(String[] args){
       SwingUtilities.invokeLater(() -> {
            try {
                // 1. Create the Controller (The Application Logic)
                GameController controller = new GameController();

                // 2. Create the View (The GUI), passing the controller
                MainWindow gui = new MainWindow(controller);

                // 3. Start the application flow
                gui.start();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
   } 

