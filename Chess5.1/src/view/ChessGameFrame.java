package view;

import controller.GameController;

import javax.swing.*;
import javax.swing.border.Border;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
@SuppressWarnings("ALL")
public class ChessGameFrame extends JFrame implements Serializable,Runnable{
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;
    public final int CHESSBOARD_SIZE;
    private GameController gameController;

    Thread t=new Thread(this);
    private int timelimit =0;//as seconds
    private int oritimelimit =0;//as seconds
    String time="No time limit";

    JLabel timer;

    JLabel currentPlayer;
    JLabel currentPlayerCube;
    int changeBackgroundTime;


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
        initCurrentPlayerLabel();
        addBackgroundButton();
        playBackgroundMusic();

        changeBackgroundTime=0;

        t.start();
        t.suspend();

        gameController.getChessboard().setChessGameFrame(this);
    }


    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {

        Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE, CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGTH / 10+20, HEIGTH / 10);
        chessboard.setChessGameFrame(this);
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
        this.timer = new JLabel("No time limit");
        timer.setLocation(HEIGTH-530, HEIGTH / 10 -65);
        timer.setSize(200, 60);
        timer.setFont(new Font("Rockwell", Font.BOLD, 22));
        add(timer);
    }
    public void resetTimerLabel(){
        t.suspend();
        this.setTimelimit(0);
        this.setOritimelimit(0);
        remove(timer);
        this.timer=new JLabel("No time limit");
        timer.setLocation(HEIGTH-530, HEIGTH / 10 -65);
        timer.setSize(200, 60);
        timer.setFont(new Font("Rockwell", Font.BOLD, 22));
        add(this.timer);
        this.repaint();
    }
    private void initCurrentPlayerLabel() {
        this.currentPlayer = new JLabel("Player: White");
        currentPlayer.setLocation(HEIGTH-285, HEIGTH / 10 -65);
        currentPlayer.setSize(200, 60);
        currentPlayer.setFont(new Font("Rockwell", Font.BOLD, 22));
        add(currentPlayer);

        Border border=BorderFactory.createLineBorder(Color.black);
        this.currentPlayerCube=new JLabel("");
        currentPlayerCube.setLocation(HEIGTH-125, HEIGTH / 10 -55);
        currentPlayerCube.setSize(40, 40);
        currentPlayerCube.setFont(new Font("Rockwell", Font.BOLD, 22));
        currentPlayerCube.setBorder(border);
        currentPlayerCube.setOpaque(true);
        currentPlayerCube.setBackground(Color.ORANGE);
        add(currentPlayerCube);
    }
    public void setBlackPlayerLabel() {
        remove(currentPlayer);
        this.currentPlayer = new JLabel("Player: Black");
        currentPlayer.setLocation(HEIGTH-285, HEIGTH / 10 -65);
        currentPlayer.setSize(200, 60);
        currentPlayer.setFont(new Font("Rockwell", Font.BOLD, 22));
        add(currentPlayer);

        currentPlayerCube.setOpaque(true);
        currentPlayerCube.setBackground(Color.BLUE);
        add(currentPlayerCube);
        this.repaint();
    }
    public void setWhitePlayerLabel() {
        remove(currentPlayer);
        this.currentPlayer = new JLabel("Player: White");
        currentPlayer.setLocation(HEIGTH-285, HEIGTH / 10 -65);
        currentPlayer.setSize(200, 60);
        currentPlayer.setFont(new Font("Rockwell", Font.BOLD, 22));
        add(currentPlayer);

        currentPlayerCube.setOpaque(true);
        currentPlayerCube.setBackground(Color.ORANGE);
        add(currentPlayerCube);
        this.repaint();
    }



    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addUndoButton() {
        JButton button = new JButton("Undo move");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.undoMove());
        button.setLocation(HEIGTH, HEIGTH / 10 );
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }
    private void addResetButton() {
        JButton button = new JButton("Reset Chessboard");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener(e ->{
                    chessboard.resetChesses();
                    gameController.setChessboard(chessboard);
                    gameController.getChessboard().getChessGameFrame().resetTimerLabel();
                    gameController.getChessboard().getChessGameFrame().setWhitePlayerLabel();

                }
        );
        button.setLocation(HEIGTH, HEIGTH / 10 + 90);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }
    private void addPreviousMoveButton() {
        JButton button = new JButton("Check last move");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.checkPreviousBoard());
        button.setLocation(HEIGTH+250, HEIGTH / 10 );
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }
    private void addNextMoveButton() {
        JButton button = new JButton("Check next move");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.checkNextBoard());
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 90);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }
    private void addReturnButton() {
        JButton button = new JButton("Current board");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener((e) -> chessboard.returnToCurrentBoard());
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }
    private void addTLButton() {
        JButton button = new JButton("Set time limit");
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
                            t.suspend();
                            int result=JOptionPane.showConfirmDialog(null,"Would you like to reset the game?");
                            if (result==0){
                                chessboard.resetChesses();
                            }
                            remove(timer);
                            this.timer=new JLabel("No time limit");
                            timer.setLocation(HEIGTH-530, HEIGTH / 10 -65);
                            timer.setSize(200, 60);
                            timer.setFont(new Font("Rockwell", Font.BOLD, 22));
                            add(this.timer);
                            this.repaint();
                        }
                        if (this.timelimit>0){
                            oritimelimit=this.timelimit;
                            int result=JOptionPane.showConfirmDialog(null,"Would you like to reset the game?");
                            if (result==0){
                                chessboard.resetChesses();
                            }
                            remove(timer);
                            this.timer=new JLabel(time=this.timelimit/60+":"+(this.timelimit-this.timelimit/60*60));
                            timer.setLocation(HEIGTH-480, HEIGTH / 10 -65);
                            timer.setSize(200, 60);
                            timer.setFont(new Font("Rockwell", Font.BOLD, 24));
                            add(this.timer);
                            this.repaint();
                            t.resume();
                            }
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(this,"Please input a number");
                    }
                }
                );
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 270);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);
    }


    private void addLoadButton() {
        JButton button = new JButton("Load a game");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener(e -> {chessboard.loadChess();
            chessboard.getChessGameFrame().resetTimerLabel();
        }
        );
        button.setLocation(HEIGTH, HEIGTH / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        add(button);

    }
    private void addSaveButton() {
        JButton button = new JButton("Save this game");
        Chessboard chessboard=gameController.getChessboard();
        button.addActionListener(e -> {
                    chessboard.saveChess();
                }
        );
        button.setLocation(HEIGTH, HEIGTH / 10 + 270);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 19));
        add(button);
    }
    ImageIcon img =new ImageIcon("./images/333(1).jpg");// 把背景图片显示在一个标签里面
    JLabel label = new JLabel(img);
    private void addBackgroundButton() {
        JButton button = new JButton("Change background");
//        Chessboard chessboard=gameController.getChessboard();

        button.addActionListener((e) ->
        {
            // 设置标签大小
            label.setBounds(-200,-100,1620,800);
            // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
            JPanel imagePanel = (JPanel) this.getContentPane();
            imagePanel.setOpaque(false);
            this.getLayeredPane().setLayout(null);
                // 把背景图片添加到分层窗格的最底层作为背景
            if (changeBackgroundTime==0){
                this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
                changeBackgroundTime=1;
                this.repaint();
            }else {
                this.getLayeredPane().remove(label);
                changeBackgroundTime=0;
                this.repaint();
            }


        });
        button.setLocation(HEIGTH+250, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 16));
        add(button);
    }


    public static void playEndMusic(){
        try {
            URL ca;
            File f=new File("./music/laugh.wav");
            ca =f.toURL();
            AudioClip aau=null;
            aau= Applet.newAudioClip(ca);

                aau.play();
//                aau.loop();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public static void playBackgroundMusic(){
        try {
            File f=new File("./music/Bgm.wav");
            URL cb;
            cb=f.toURL();
            AudioClip aau=null;
            aau= Applet.newAudioClip(cb);

            aau.play();
            aau.loop();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public static void playXiaqiMusic(){
        try {
            URL cb;
            File f=new File("./music/xiaqi.wav");
            cb=f.toURL();
            AudioClip aau;
            aau= Applet.newAudioClip(cb);


                aau.play();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Thread getT() {
        return t;
    }

    public void setT(Thread t) {
        this.t = t;
    }

    public int getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(int timelimit) {
        this.timelimit = timelimit;
    }

    public int getOritimelimit() {
        return oritimelimit;
    }

    public void setOritimelimit(int oritimelimit) {
        this.oritimelimit = oritimelimit;
    }

    @Override
    public void run() {
        Chessboard chessboard=gameController.getChessboard();
        if (timelimit>0) {
            while (true) {
                timelimit--;
                if (timelimit == -1) {
                    gameController.getChessboard().swapColor();
                    timelimit = oritimelimit;
                }
                gameController.getChessboard().setChessGameFrame(this);
                remove(timer);
                this.timer = new JLabel(time = this.timelimit / 60 + ":" + (this.timelimit - this.timelimit / 60 * 60));
                timer.setLocation(HEIGTH-480, HEIGTH / 10 -65);
                timer.setSize(200, 60);
                timer.setFont(new Font("Rockwell", Font.BOLD, 24));
                add(this.timer);
                this.repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        else {
//            remove(timer);
//            this.timer=new JLabel("No time Limit");
//            timer.setLocation(HEIGTH, HEIGTH / 10);
//            timer.setSize(200, 60);
//            timer.setFont(new Font("Rockwell", Font.BOLD, 20));
//            add(this.timer);
//            this.repaint();
//            t.suspend();
//        }
    }
}
