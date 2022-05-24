package controller;


import model.ChessComponent;
import model.KingChessComponent;
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
        if (first == null) {
            if (handleFirst(chessComponent)) {
                chessComponent.setSelected(true);
                List<ChessComponent> canmoveto=chessComponent.getAllCanMoveTo(chessboard.getChessComponents(),chessComponent);
                recordcanmoveto=canmoveto;
                for (int i = 0; i < canmoveto.size(); i++) {
                    canmoveto.get(i).repaint();
                }
//                chessComponent.setChessComponents(chessboard.getChessComponents());
                first = chessComponent;
                first.repaint();
            }
        } else {
            if (first == chessComponent) { // 再次点击取消选取
                chessComponent.setSelected(false);
                ChessComponent recordFirst = first;
                for (int i = 0; i < recordcanmoveto.size(); i++) {
                    recordcanmoveto.get(i).setSelectDraw(false);
                    recordcanmoveto.get(i).repaint();
                }
                first = null;
                recordFirst.repaint();
            } else if (handleSecond(chessComponent)) {
                //repaint in swap chess method.
                if (chessComponent instanceof KingChessComponent &&first.getChessColor()!=chessComponent.getChessColor()){
                    chessboard.swapChessComponents(first, chessComponent);
                }else {
                    chessboard.swapChessComponents(first, chessComponent);
                    chessboard.swapColor();
                }
                for (int i = 0; i < recordcanmoveto.size(); i++) {
                    recordcanmoveto.get(i).setSelectDraw(false);
                    recordcanmoveto.get(i).repaint();
                }
                first.setSelected(false);
                chessboard.levelUp(first);
//                int t=chessboard.getChessGameFrame().getOritimelimit();
//                chessboard.getChessGameFrame().setTimelimit(t);
//                Thread thread=chessboard.getChessGameFrame().getT();
//                thread.resume();
//                chessboard.getChessGameFrame().setT(thread);
                first = null;
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

    public ChessComponent getFirst() {
        return first;
    }

    public void setFirst(ChessComponent first) {
        this.first = first;
    }


    public ChessGameFrame getChessGameFrame() {
        return chessGameFrame;
    }

    public void setChessGameFrame(ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
    }
}
