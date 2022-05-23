package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame implements Serializable,Runnable{
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;

    Thread t=new Thread(this);
    private int timelimit =0;//as seconds
    String time="No time limit";

    JLabel timer;

    JLabel currentPlayer;


    public ChessGameFrame(int width, int height) {
        setTitle("2022 CS102A Project"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.CHESSBOARD_SIZE = HEIGTH * 4 / 5;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();
        addUndoButton();
        addLoadButton();
        addResetButton();
        addSaveButton();
        addNextMoveButton();
        addPreviousMoveButton();
        addReturnButton();
        addTLButton();
        initTimerLabel();

        t.start();
        t.suspend();
    }


    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
        add(chessboard);
    }

//    private void resetChessboard() {
//        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
//        gameController.setChessboard(chessboard);
//        chessboard.setLocation(HEIGTH / 10, HEIGTH / 10);
//        add(chessboard);
//
//    }

    /**
     * 在游戏面板中添加标签
     */
    private void initTimerLabel() {
        JLabel timer = new JLabel("No time limit");
        timer.setLocation(HEIGTH, HEIGTH / 10);
        timer.setSize(200, 60);
        timer.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(timer);
//        int timelimit=120000;
//        Chessboard chessboard=gameController.getChessboard();
//        ActionListener listener=new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (chessboard.getCurrentColor()== ChessColor.WHITE){
//                    chessboard.setCurrentColor(ChessColor.BLACK);
//                }else {
//                    chessboard.setCurrentColor(ChessColor.WHITE);
//                }
//
//            }
//        };
//        Timer timer=null;
//        timer = new Timer(timelimit,listener);
//        JLabel statusLabel = new JLabel("Sample label");
//        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
//        statusLabel.setSize(200, 60);
//        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(statusLabel);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addUndoButton() {
        JButton button = new JButton("Undo");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.undoMove());
        button.setLocation(HEIGTH, HEIGTH / 10 + 90);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
    private void addPreviousMoveButton() {
        JButton button = new JButton("Check last move");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.checkPreviousBoard());
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 90);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }
    private void addNextMoveButton() {
        JButton button = new JButton("Check next move");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.checkNextBoard());
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }
    private void addReturnButton() {
        JButton button = new JButton("Return to current chessboard");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.returnToCurrentBoard());
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 270);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 14));
        add(button);
    }
    private void addTLButton() {
        JButton button = new JButton("Set Time Limit for each round");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> {
                    String timelimit =JOptionPane.showInputDialog("Please set a time limit\nfor each round (as seconds)\n" +
                            "(Input 0 means there is no time limit)");
                    try {
                        this.timelimit=Integer.parseInt(timelimit);
                        if (this.timelimit<0){
                            JOptionPane.showMessageDialog(this,"Please input a positive number");
                        }
                        if (this.timelimit==0){
                            int result=JOptionPane.showConfirmDialog(null,"Would you like to reset the game?");
                            if (result==0){
                                chessboard.resetChesses();
                            }
//                            JLabel statusLabel = new JLabel("No time limit");
//                            statusLabel.setLocation(HEIGTH, HEIGTH / 10);
//                            statusLabel.setSize(200, 60);
//                            statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
//                            add(statusLabel);
                        }
                        if (this.timelimit>0){
                            int result=JOptionPane.showConfirmDialog(null,"Would you like to reset the game?");
                            if (result==0){
                                chessboard.resetChesses();
                            }
//                                JLabel statusLabel = new JLabel(time=this.timelimit/60+":"+(this.timelimit-this.timelimit/60*60));
//                                statusLabel.setLocation(HEIGTH, HEIGTH / 10);
//                                statusLabel.setSize(200, 60);
//                                statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
//                                this.add(statusLabel);
                            }
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(this,"Please input a number");
                    }
                }
                );
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 14));
        add(button);
    }


    private void addLoadButton() {
        JButton button = new JButton("Load a game");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener(e -> chessboard.loadChess()
        );
        button.setLocation(HEIGTH, HEIGTH / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);


//        JButton button = new JButton("Load");
//        button.setLocation(HEIGTH, HEIGTH / 10 + 180);
//        button.setSize(200, 60);
//        button.setFont(new Font("Rockwell", Font.BOLD, 20));
//        add(button);
//        button.addActionListener(e -> {
//            System.out.println("Click load");
//            String path = JOptionPane.showInputDialog(this,"Input Path here");
//            gameController.loadGameFromFile(path);
//        });
    }

    private void addResetButton() {
        JButton button = new JButton("Reset Chessboard");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener(e ->{
//            chessboard.removeAllChess();
            chessboard.resetChesses();
            gameController.setChessboard(chessboard);
        }
        );
        button.setLocation(HEIGTH, HEIGTH / 10 + 270);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);
    }
    private void addSaveButton() {
        JButton button = new JButton("Save this game");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener(e -> chessboard.saveChess()
        );
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);
    }


    @Override
    public void run() {
        Chessboard chessboard=gameController.getChessboard();
        if (timelimit>0){
            while (true){
                timelimit--;
                if (this.timelimit>0){
                    JLabel statusLabel = new JLabel(time=this.timelimit/60+":"+(this.timelimit-this.timelimit/60*60));
                    statusLabel.setLocation(HEIGTH, HEIGTH / 10);
                    statusLabel.setSize(200, 60);
                    statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
                    add(statusLabel);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
