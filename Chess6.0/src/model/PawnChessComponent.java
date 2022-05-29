package model;

import com.sun.source.tree.WhileLoopTree;
import controller.ClickController;
import view.Chessboard;
import view.ChessboardPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PawnChessComponent extends ChessComponent {
    private static Image PAWN_WHITE;
    private static Image PAWN_BLACK;
    private Image pawnImage;
    private char chessname = setChessname();

    private Chessboard chessboard;

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void setChessboard(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public char setChessname() {
        char name;
        if (this.getChessColor() == ChessColor.BLACK) {
            name = 'P';
        } else {
            name = 'p';
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
        int x0 = source.getX();
        int xf = destination.getX();
        int y0 = source.getY();
        int yf = destination.getY();
        //吃过路兵
        if ((x0==3&&this.getChessColor()==ChessColor.WHITE)||(x0==4&&this.getChessColor()==ChessColor.BLACK)){
            if (checkcglb(chessComponents,destination)) {
                return true;
            }
        }

        if (yf != y0) {
            if (Math.abs(yf - y0) == 1 && !(chessComponents[xf][yf] instanceof EmptySlotComponent)) {
                if (this.getChessColor() == ChessColor.BLACK && xf - x0 != 1) {
                    return false;
                } else if (this.getChessColor() == ChessColor.WHITE && x0 - xf != 1) {
                    return false;
                }
                } else return false;
        } else if (this.getChessColor() == ChessColor.BLACK && xf - x0 != 1) {
            if (!(chessComponents[xf][yf] instanceof EmptySlotComponent)) { //如果直走的地方不是空格，不能走
                return false;
            } else if (xf - x0 == 2) {
                if (x0 != 1) {
                    return false;
                }
            } else return false;
        } else if (this.getChessColor() == ChessColor.WHITE && x0 - xf != 1) {
            if (!(chessComponents[xf][yf] instanceof EmptySlotComponent)) {
                return false;
            } else if (x0 - xf == 2) {
                if (x0 != 6) {
                    return false;
                }
            } else return false;
            if (!(chessComponents[xf][yf] instanceof EmptySlotComponent)) {
                return false;
            }
        } else if (this.getChessColor() == ChessColor.BLACK && xf - x0 == 1) {
            if (!(chessComponents[xf][yf] instanceof EmptySlotComponent)) {
                return false;
            }
        } else if (this.getChessColor() == ChessColor.WHITE && x0 - xf == 1) {
            if (!(chessComponents[xf][yf] instanceof EmptySlotComponent)) {
                return false;
            }
        }

        return true;


//        ChessboardPoint source = getChessboardPoint();
//        int x0=source.getX();
//        int xf=destination.getX();
//        int y0=source.getY();
//        int yf=destination.getY();
//        if (yf!=y0){
//            if (Math.abs(yf-y0)==1&& !(chessComponents[xf][yf]instanceof EmptySlotComponent)){
//                if (this.getChessColor()==ChessColor.BLACK&&xf-x0!=1){
//                    return false;
//                }else if (this.getChessColor()==ChessColor.WHITE&&x0-xf!=1){
//                    return false;
//                }else return false;
//
////            }else if (chessComponents[x0][y0+1] instanceof PawnChessComponent &&chessComponents[x0][y0] instanceof PawnChessComponent){
////                if (chessComponents[x0][y0+1].getChessColor()==ChessColor.BLACK&&currentPlayer==ChessColor.WHITE&& chessComponents[x0][y0].getChessColor()==currentPlayer){
////                    if (yf!=y0+1||xf!=x0-1||x0!=3){
////                        return false;
////                    }
////                }else if (chessComponents[x0][y0+1].getChessColor()==ChessColor.WHITE&&currentPlayer==ChessColor.BLACK&&chessComponents[x0][y0].getChessColor()==currentPlayer){
////                    if (x0!=4||yf!=y0+1||xf!=x0+1){
////                        return false;
////                    }
////                }else return false;
////            }else if (chessComponents[x0][y0-1] instanceof PawnChessComponent &&chessComponents[x0][y0] instanceof PawnChessComponent){
////                if (chessComponents[x0][y0-1].getChessColor()==ChessColor.BLACK&&currentPlayer==ChessColor.WHITE&& chessComponents[x0][y0].getChessColor()==currentPlayer){
////                    if (yf!=y0-1||xf!=x0-1||x0!=3){
////                        return false;
////                    }
////                }else if (chessComponents[x0][y0-1].getChessColor()==ChessColor.WHITE&&currentPlayer==ChessColor.BLACK&&chessComponents[x0][y0].getChessColor()==currentPlayer){
////                    if (x0!=4||yf!=y0-1||xf!=x0+1){
////                        return false;
////                    }
////                }else return false;
//            }
//            /*else if (chessComponents[x0][y0+1] instanceof PawnChessComponent&& chessComponents[x0][y0] instanceof PawnChessComponent&&
//                    chessComponents[x0][y0+1].getChessColor()!=currentPlayer){ //并列的是不同色的pawn
//                if (this.getChessColor()==ChessColor.BLACK&&(xf-x0!=1||yf-y0!=1)){
//                    return false;
//                }else if (this.getChessColor()==ChessColor.WHITE&&(x0-xf!=1||yf-y0!=1)){
//                    return false;
//                }
//            } else if (chessComponents[x0][y0-1] instanceof PawnChessComponent&& chessComponents[x0][y0] instanceof PawnChessComponent&&
//                    chessComponents[x0][y0-1].getChessColor()!=currentPlayer){ //并列的是不同色的pawn
//                if (this.getChessColor()==ChessColor.BLACK&&(xf-x0!=1||y0-yf!=1)){
//                    return false;
//                }else if (this.getChessColor()==ChessColor.WHITE&&(x0-xf!=1||y0-yf!=1)){
//                    return false;
//                }
//            }*/else return false;
//        }
//        /*else if ((xf-x0!=1&&this.getChessColor()==ChessColor.BLACK)
//                ||x0-xf!=1&&this.getChessColor()==ChessColor.WHITE)){
//            if (Math.abs(xf-x0)==2){ //记录该棋子行棋数
//                if ((this.getChessColor()==ChessColor.WHITE&&source.getX()!=6)||
//                        (this.getChessColor()==ChessColor.BLACK&&source.getX()!=1)){
//                    return false;
//                }
//            }else return false;
//        }return true;*/
//        else if (this.getChessColor()==ChessColor.BLACK&&xf-x0!=1){
//            if (!(chessComponents[xf][yf] instanceof EmptySlotComponent)){ //如果直走的地方不是空格，不能走
//                return false;
//            }else if (xf-x0==2){
//                if (x0!=1){
//                    return false;
//                }
//            }else return false;
//        }else if (this.getChessColor()==ChessColor.WHITE&&x0-xf!=1){
//            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
//                return false;
//            }else if (x0-xf==2){
//                if (x0!=6){
//                    return false;
//                }
//            }else return false;
//            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
//                return false;
//            }
//        }else if (this.getChessColor()==ChessColor.BLACK&&xf-x0==1){
//            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
//                return false;
//            }
//        }else if (this.getChessColor()==ChessColor.WHITE&&x0-xf==1){
//            if (!(chessComponents[xf][yf]instanceof EmptySlotComponent)){
//                return false;
//            }
//        }
//
//        return true;
    }

    public boolean checkcglb(ChessComponent[][] chessComponents, ChessboardPoint destination){
        ChessboardPoint source = getChessboardPoint();
        int x0 = source.getX();
        int xf = destination.getX();
        int y0 = source.getY();
        int yf = destination.getY();
        List<String> lasttwoboard = this.getChessboard().previousGraph.get(this.getChessboard().previousGraph.size() - 2);
        if ((Math.abs(x0-xf)==1&&Math.abs(y0-yf)==1)&& chessComponents[xf][yf]instanceof EmptySlotComponent
                &&((this.getChessColor()==ChessColor.WHITE&&x0==3&&xf-x0==-1&&chessboard.getChessboardGraph().get(x0).charAt(y0)=='p')
                ||(this.getChessColor()==ChessColor.BLACK&&x0==4&&xf-x0==1&&chessboard.getChessboardGraph().get(x0).charAt(y0)=='P'))) {
            if (chessComponents[xf][yf] instanceof EmptySlotComponent&&(chessComponents[x0][yf] instanceof PawnChessComponent)) {
                if (lasttwoboard.get(x0).charAt(yf)=='_'){
                    return true;
                }
            }
        }
       return false;
    }
    public void changeTheme(int time){
        if (time==0){
            try {
                PAWN_WHITE = ImageIO.read(new File("./images/white-pawn.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                PAWN_BLACK = ImageIO.read(new File("./images/black-pawn.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (this.getChessColor() == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (this.getChessColor() == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        }else {
            try {
                PAWN_WHITE = ImageIO.read(new File("./images/pawn-white.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                PAWN_BLACK = ImageIO.read(new File("./images/pawn-black.png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (this.getChessColor() == ChessColor.WHITE) {
                pawnImage = PAWN_WHITE;
            } else if (this.getChessColor() == ChessColor.BLACK) {
                pawnImage = PAWN_BLACK;
            }
        }
        super.changeTheme(time);
    }
    @Override
    public void loadResource() throws IOException {
        if (PAWN_WHITE == null) {
            PAWN_WHITE = ImageIO.read(new File("./images/white-pawn.png"));
        }

        if (PAWN_BLACK == null) {
            PAWN_BLACK = ImageIO.read(new File("./images/black-pawn.png"));
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected()) { // Highlights the model if selected.

            g.setColor(new Color(234, 177, 54, 228));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
//        g.drawImage(rookImage, 0, 0, getWidth() - 13, getHeight() - 20, this);
        g.drawImage(pawnImage, 0, 0, getWidth(), getHeight(), this);
        g.setColor(Color.BLACK);

    }
}
