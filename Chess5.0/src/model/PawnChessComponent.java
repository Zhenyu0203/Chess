package model;

import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PawnChessComponent extends ChessComponent{
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;
    private Image pawnImage;
    private char chessname=setChessname();

    public char setChessname() {
        char name;
        if (this.getChessColor()==ChessColor.BLACK){
            name='P';
        }else{
            name='p';
        }
        return name;
    }

    public char getChessname() {
        return chessname;
    }
    public PawnChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiatePawnImage(chessColor);
    }

    private void initiatePawnImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (color == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();
        int x0=source.getX();
        int xf=destination.getX();
        int y0=source.getY();
        int yf=destination.getY();
        if (yf!=y0){
            if (Math.abs(yf-y0)==1&& !(chessComponents[xf][yf]instanceof EmptySlotComponent)){
                if (this.getChessColor()==ChessColor.BLACK&&xf-x0!=1){
                    return false;
                }else if (this.getChessColor()==ChessColor.WHITE&&x0-xf!=1){
                    return false;
                }
            }else return false;
        }
        /*else if ((xf-x0!=1&&this.getChessColor()==ChessColor.BLACK)
                ||x0-xf!=1&&this.getChessColor()==ChessColor.WHITE)){
            if (Math.abs(xf-x0)==2){ //记录该棋子行棋数
                if ((this.getChessColor()==ChessColor.WHITE&&source.getX()!=6)||
                        (this.getChessColor()==ChessColor.BLACK&&source.getX()!=1)){
                    return false;
                }
            }else return false;
        }return true;*/
        else if (this.getChessColor()==ChessColor.BLACK&&xf-x0!=1){
            if (!(chessComponents[xf][yf] instanceof EmptySlotComponent)){ //如果直走的地方不是空格，不能走
                return false;
            }else if (xf-x0==2){
                if (x0!=1){
                    return false;
                }
            }else return false;
        }else if (this.getChessColor()==ChessColor.WHITE&&x0-xf!=1){
            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
                return false;
            }else if (x0-xf==2){
                if (x0!=6){
                    return false;
                }
            }else return false;
            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
                return false;
            }
        }else if (this.getChessColor()==ChessColor.BLACK&&xf-x0==1){
            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
                return false;
            }
        }else if (this.getChessColor()==ChessColor.WHITE&&x0-xf==1){
            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
                return false;
            }
        }

        return true;
    }

    @Override
    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
        }
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(pawnImage, 0, 0, getWidth() , getHeight(), this);
        g.setColor(Color.BLACK);
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}
