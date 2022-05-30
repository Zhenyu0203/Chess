import view.ChessGameFrame;
import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            ChessGameFrame mainFrame = new ChessGameFrame(1200, 675);//800*608
//            mainFrame.setVisible(true);
            try {
                MainFrame mainFrame=new MainFrame(1200,675);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

