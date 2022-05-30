package controller;


import model.*;
import view.ChessGameFrame;
import view.Chessboard;
import view.ChessboardPoint;

import java.io.Serializable;
import java.util.List;

public class ClickController implements Serializable {
    private final Chessboard chessboard;
    private ChessComponent first;
    public ChessGameFrame chessGameFrame;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }
    List<ChessComponent>recordcanmoveto=null;

    public void onClick(ChessComponent chessComponent) {
        if (first == null) {//第一次选棋子
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                //导入当前的棋盘（chessboard类型，不能只是当前棋盘的List类型），以便判断吃过路兵
                if (chessComponent instanceof PawnChessComponent){
                    if (chessComponent.getChessColor()== ChessColor.BLACK&&chessComponent.getChessboardPoint().getX()==4){
                        ((PawnChessComponent) chessComponent).setChessboard(chessboard);
                    }
                    if (chessComponent.getChessColor()== ChessColor.WHITE&&chessComponent.getChessboardPoint().getX()==3){
                        ((PawnChessComponent) chessComponent).setChessboard(chessboard);
                    }
                }

                //标记出所有合法移动格
                List<ChessComponent> canmoveto=chessComponent.getAllCanMoveTo(chessboard.getChessComponents(),chessComponent);
                recordcanmoveto=canmoveto;
                for (int i = 0; i < canmoveto.size(); i++) {
                    canmoveto.get(i).repaint();
                }

                first = chessComponent;
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                //取消标记所有合法移动格
                for (int i = 0; i < recordcanmoveto.size(); i++) {
                    recordcanmoveto.get(i).setSelectDraw(false);
                    recordcanmoveto.get(i).repaint();
                }
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //吃过路兵，先判断后重写swapChessComponent
                boolean cglb=false;
                if (first instanceof PawnChessComponent&&chessboard.getPreviousGraph().size()>=4){
                    ((PawnChessComponent) first).setChessboard(chessboard);
                    if (((PawnChessComponent) first).checkcglb(chessboard.getChessComponents(), chessComponent.getChessboardPoint())){
                        int x0=first.getChessboardPoint().getX();
                        int y0=first.getChessboardPoint().getY();
                        int yf=chessComponent.getChessboardPoint().getY();
                        chessboard.swapChessComponents(first,chessComponent);
                        chessboard.swapChessComponents(chessboard.getChessComponents()[x0][y0],chessboard.getChessComponents()[x0][yf]);
                        chessboard.swapColor();
                        cglb=true;
                    }
                }
                if (!cglb){
                    //胜负判断，被吃棋子是王
                    if (chessComponent instanceof KingChessComponent &&first.getChessColor()!=chessComponent.getChessColor()){
                        chessboard.swapChessComponents(first, chessComponent);
                    }else {
                        //正常吃子
                        chessboard.swapChessComponents(first, chessComponent);
                        chessboard.swapColor();
                    }
                }
                //取消标记合法落子点
                for (int i = 0; i < recordcanmoveto.size(); i++) {
                    recordcanmoveto.get(i).setSelectDraw(false);
                    recordcanmoveto.get(i).repaint();
                }
                first.setSelected(false);
                //判断是否升变
                chessboard.levelUp(first);
//                int t=chessboard.getChessGameFrame().getOritimelimit();
//                chessboard.getChessGameFrame().setTimelimit(t);
//                Thread thread=chessboard.getChessGameFrame().getT();
//                thread.resume();
//                chessboard.getChessGameFrame().setT(thread);
                first = null;
                //将当前棋盘录入当前棋盘过往棋谱中
                chessboard.addToPreviousGraph(chessboard.getChessboardGraph());
                chessboard.loadChessGame(chessboard.getChessboardGraph());
            }
        }
    }

    /**
     * @param chessComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    private boolean handleFirst(ChessComponent chessComponent) {
        return chessComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param chessComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(ChessComponent chessComponent) {
        if (first.getChessColor()!=chessboard.getCurrentColor()){
            first.setSelected(false);
            first.repaint();
            first=null;
            return false;
        }
        return chessComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), chessComponent.getChessboardPoint());
    }


    public void setFirst(ChessComponent first) {
        this.first = first;
    }


}
