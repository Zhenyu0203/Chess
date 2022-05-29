package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class KnightChessComponent extends ChessComponent{
    private static Image KNIGHT_WHITE;
    private static Image KNIGHT_BLACK;
    private Image knightImage;
    private char chessname=setChessname();

    public char setChessname() {
        char name;
        if (this.getChessColor()==ChessColor.BLACK){
            name='N';
        }else{
            name='n';
        }
        return name;
    }

    public char getChessname() {
        return chessname;
    }
    public KnightChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateKnightImage(chessColor);
    }

    private void initiateKnightImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (color == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source=getChessboardPoint();
        int x=Math.abs(source.getX()-destination.getX());// x坐标变换的值
        int y=Math.abs(source.getY()-destination.getY()); //y坐标变换的值
        if (x==1){
            if (y!=2){
                return false;
            }
        }else if (x==2){
            if (y!=1){
                return false;
            }
        }else {
            return false;
        }return true;
    }

    @Override
    public void loadResource() throws IOException {
        if (KNIGHT_WHITE == null) {
            KNIGHT_WHITE = ImageIO.read(new File("./images/white white knight.png"));
        }

        if (KNIGHT_BLACK == null) {
            KNIGHT_BLACK = ImageIO.read(new File("./images/black-knight.png"));
        }
    }
    public void changeTheme(int time){
        if (time==0){
            try {
                KNIGHT_WHITE = ImageIO.read(new File("./images/white white knight.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                KNIGHT_BLACK = ImageIO.read(new File("./images/black-knight.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (this.getChessColor() == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (this.getChessColor() == ChessColor.BLACK) {
                knightImage= KNIGHT_BLACK;
            }
        }else {
            try {
                KNIGHT_WHITE = ImageIO.read(new File("./images/knight-white.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                KNIGHT_BLACK = ImageIO.read(new File("./images/knight-black.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (this.getChessColor() == ChessColor.WHITE) {
                knightImage = KNIGHT_WHITE;
            } else if (this.getChessColor() == ChessColor.BLACK) {
                knightImage = KNIGHT_BLACK;
            }
        }
        super.changeTheme(time);
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(new Color(234, 177, 54, 228));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(knightImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
//        if (isSelected()) { // Highlights the model if selected.
//            g.setColor(Color.RED);
//            g.drawOval(0, 0, getWidth() , getHeight());
//        }
    }
}
