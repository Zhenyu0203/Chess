package model;

import controller.ClickController;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class QueenChessComponent extends ChessComponent{
    private static Image QUEEN_WHITE;
    private static Image QUEEN_BLACK;
    private Image queenImage;

    private char chessname=setChessname();

    public char setChessname() {
        char name;
        if (this.getChessColor()==ChessColor.BLACK){
            name='Q';
        }else{
            name='q';
        }
        return name;
    }

    public char getChessname() {
        return chessname;
    }
    public QueenChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        initiateQueenImage(chessColor);
    }

    private void initiateQueenImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                queenImage = QUEEN_WHITE;
            } else if (color == ChessColor.BLACK) {
                queenImage = QUEEN_BLACK;
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
        if (x0==xf) { //竖着走
            int row = x0;
            for (int col = Math.min(y0,yf) + 1; col < Math.max(y0,yf); col++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;//路上有子，不能越子
                }
            }
        } else if (y0==yf) { //横着走
            int col = y0;
            for (int row = Math.min(x0,xf) + 1; row < Math.max(x0,xf); row++) {
                if (!(chessComponents[row][col] instanceof EmptySlotComponent)) {
                    return false;
                }
            }
        }else if (Math.abs(x0-xf)==Math.abs(y0-yf)){
            if ((x0<xf&&y0<yf)||(x0>xf&&y0>yf)){ //横纵坐标变化可以同增同减的情况
                for (int row = 1+Math.min(x0,xf),col=1+Math.min(y0,yf); row <Math.max(x0,xf)&&col<Math.max(y0,yf) ; row++,col++) {
                    if (!(chessComponents[row][col] instanceof EmptySlotComponent)){
                        return false;
                    }
                }
            }else if ((x0>xf&&y0<yf)||(x0<xf&&y0>yf)){
                for (int row = 1+Math.min(x0,xf),col=Math.max(y0,yf)-1; row <Math.max(x0,xf)&&col>Math.min(y0,yf) ; row++,col--) {
                    if (!(chessComponents[row][col] instanceof EmptySlotComponent)){
                        return false;
                    }
                }
            }
        }else return false;
        return true;
    }

    @Override
    public void loadResource() throws IOException {
        if (QUEEN_WHITE == null) {
            QUEEN_WHITE = ImageIO.read(new File("./images/queen-white.png"));
        }

        if (QUEEN_BLACK == null) {
            QUEEN_BLACK = ImageIO.read(new File("./images/queen-black.png"));
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
