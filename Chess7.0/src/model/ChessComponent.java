package model;

import view.ChessGameFrame;
import view.ChessboardPoint;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是一个抽象类，主要表示8*8棋盘上每个格子的棋子情况，当前有两个子类继承它，分别是EmptySlotComponent(空棋子)和RookChessComponent(车)。
 */

public abstract class ChessComponent extends JComponent {

    /**
     * CHESSGRID_SIZE: 主要用于确定每个棋子在页面中显示的大小。
     * <br>
     * 在这个设计中，每个棋子的图案是用图片画出来的（由于国际象棋那个棋子手动画太难了）
     * <br>
     * 因此每个棋子占用的形状是一个正方形，大小是50*50
     */

//    private static final Dimension CHESSGRID_SIZE = new Dimension(1080 / 4 * 3 / 8, 1080 / 4 * 3 / 8);
    private static Color[] BACKGROUND_COLORS = new Color[]{new Color(245, 245, 223, 255), new Color(3, 59, 98)};

    public static Color[] getBackgroundColors() {
        return BACKGROUND_COLORS;
    }

    public static void setBackgroundColors(Color[] backgroundColors) {
        BACKGROUND_COLORS = backgroundColors;
    }

    /**
     * handle click event
     */
    private ClickController clickController;

    /**
     * chessboardPoint: 表示8*8棋盘中，当前棋子在棋格对应的位置，如(0, 0), (1, 0), (0, 7),(7, 7)等等
     * <br>
     * chessColor: 表示这个棋子的颜色，有白色，黑色，无色三种
     * <br>
     * selected: 表示这个棋子是否被选中
     */
    private ChessboardPoint chessboardPoint;
    protected final ChessColor chessColor;
    private boolean selected;//当前棋子是否被选中
    private boolean selectDraw;//当前棋子是否被选中标记，在paintComponent中，如果被选中标记，则被标黄

    private char chessname;

    private ChessGameFrame chessGameFrame;
    private ChessComponent[][] chessComponents;
    public ChessColor currentPlayer;
    private boolean isEnter=false;//判断鼠标是否进入当前格
    public abstract char getChessname();
    public abstract char setChessname();

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);
        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
        this.selected = false;
        this.clickController = clickController;
    }


    public ChessboardPoint getChessboardPoint() {
        return chessboardPoint;
    }

    public void setChessboardPoint(ChessboardPoint chessboardPoint) {
        this.chessboardPoint = chessboardPoint;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @param another 主要用于和另外一个棋子交换位置
     *                <br>
     *                调用时机是在移动棋子的时候，将操控的棋子和对应的空位置棋子(EmptySlotComponent)做交换
     */
    public void swapLocation(ChessComponent another) {
        ChessboardPoint chessboardPoint1 = getChessboardPoint(), chessboardPoint2 = another.getChessboardPoint();
        Point point1 = getLocation(), point2 = another.getLocation();
        setChessboardPoint(chessboardPoint2);
        setLocation(point2);
        another.setChessboardPoint(chessboardPoint1);
        another.setLocation(point1);
    }

    /**
    *改主题，以下为改变棋盘格颜色，棋子样式在各个子类中重写
     **/
    public void changeTheme(int time){
        if (time==0){
            BACKGROUND_COLORS=new Color[]{
                    new Color(245, 245, 223, 255), new Color(3, 59, 98)
            };
        }else {
            BACKGROUND_COLORS=new Color[]{
                    new Color(255, 234, 176, 219), new Color(91, 63, 12)
            };
        }
    }


    /**
     * @param e 响应鼠标监听事件
     *          <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用所有监听者的onClick方法，处理棋子的选中，移动等等行为。
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);

        //点击方法
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {

            System.out.printf("Click [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
            clickController.onClick(this);
        }
        //鼠标检测方法
        if (e.getID()==MouseEvent.MOUSE_ENTERED){
            isEnter=true;
            this.repaint();
        }
        if (e.getID()==MouseEvent.MOUSE_EXITED){
            isEnter=false;
            this.repaint();
        }
    }

    public void setChessGameFrame(ChessGameFrame chessGameFrame) {
        this.chessGameFrame = chessGameFrame;
    }

    /**
     * @param chessboard  棋盘
     * @param destination 目标位置，如(0, 0), (0, 7)等等
     * @return this棋子对象的移动规则和当前位置(chessboardPoint)能否到达目标位置
     * <br>
     * 这个方法主要是检查移动的合法性，如果合法就返回true，反之是false
     */
    public abstract boolean canMoveTo(ChessComponent[][] chessboard, ChessboardPoint destination);

    public boolean isSelectDraw() {
        return selectDraw;
    }

    public void setSelectDraw(boolean selectDraw) {
        this.selectDraw = selectDraw;
    }

    /**
    *获得当前棋子的所有合法落子点
     **/
    public List<ChessComponent> getAllCanMoveTo(ChessComponent[][]chessboard, ChessComponent chessComponent){
        List<ChessComponent> canmoveto=new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessComponent.canMoveTo(chessboard,new ChessboardPoint(i,j))){
                    if (chessboard[i][j].getChessColor()!=chessComponent.getChessColor()){
                        canmoveto.add(chessboard[i][j]);
                        chessboard[i][j].setSelectDraw(true);
                    }

                }
            }
        }
        return canmoveto;
    }

    /**
     * 这个方法主要用于加载一些特定资源，如棋子图片等等。
     *
     * @throws IOException 如果一些资源找不到(如棋子图片路径错误)，就会抛出异常
     */
    public abstract void loadResource() throws IOException;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        if (this.isSelectDraw()){//画合法落子点
            g.setColor(new Color(234, 177, 54, 228));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }else {//画棋盘格
            System.out.printf("repaint chess [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
        Color squareColor = BACKGROUND_COLORS[(chessboardPoint.getX() + chessboardPoint.getY()) % 2];
        g.setColor(squareColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        if (isEnter){//画鼠标当前棋子
            if (this.getChessColor()==currentPlayer||this.isSelectDraw()){//若当前棋子可操作，标记为绿色，否则为黑色
                g.setColor(new Color(23, 168, 96, 200));
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }else {
                g.setColor(new Color(62, 62, 63, 202));
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
            }

        }
    }

}
