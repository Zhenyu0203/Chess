package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BishopChessComponent extends ChessComponent{
    private static Image BISHOP_WHITE;
    private static Image BISHOP_BLACK;
    private Image queenImage;
    private char chessname=setChessname();

    public char setChessname() {
        char name;
        if (this.getChessColor()==ChessColor.BLACK){
            name='B';
        }else{
            name='b';
        }
        return name;
    }

    public char getChessname() {
        return chessname;
    }
    public BishopChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateBishopImage(chessColor);
    }

    private void initiateBishopImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = BISHOP_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = BISHOP_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        int x0=source.getX();
        int xf=destination.getX();
        int y0=source.getY();
        int yf=destination.getY();//先定义一下，简化后面的工作量。x0是初始x坐标，xf是想去的地方的x坐标。
        /*if (Math.abs(source.getX()-destination.getX())==Math.abs(source.getY()-destination.getY())){
            for (int row = 1+Math.min(source.getX(),destination.getX()),col=1+Math.min(source.getY(),destination.getY());
                 row <Math.max(source.getX(),destination.getX())&&col<Math.max(source.getY(),destination.getY()) ; row++,col++) {
                if (!(chessboard[row][col] instanceof EmptySlotComponent)){
                    return false;
                }
            }
        }else return false;
        return true;*/
        if (Math.abs(x0-xf)==Math.abs(y0-yf)){
            if ((x0<xf&&y0<yf)||(x0>xf&&y0>yf)){ //横纵坐标变化可以同增同减的情况
                for (int row = 1+Math.min(x0,xf),col=1+Math.min(y0,yf); row <Math.max(x0,xf)&&col<Math.max(y0,yf) ; row++,col++) {
                    if (!(chessboard[row][col] instanceof EmptySlotComponent)){
                        return false;
                    }
                }
            }else if ((x0>xf&&y0<yf)||(x0<xf&&y0>yf)){
                for (int row = 1+Math.min(x0,xf),col=Math.max(y0,yf)-1; row <Math.max(x0,xf)&&col>Math.min(y0,yf) ; row++,col--) {
                    if (!(chessboard[row][col] instanceof EmptySlotComponent)){
                        return false;
                    }
                }
            }
        }else return false;
        return true;
    }

    @Override
    public void loadResource() throws IOException {
        if (BISHOP_WHITE == null) {
            BISHOP_WHITE = ImageIO.read(new File("./images/bishop-white.png"));
        }

        if (BISHOP_BLACK == null) {
            BISHOP_BLACK = ImageIO.read(new File("./images/bishop-black.png"));
        }
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(queenImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
