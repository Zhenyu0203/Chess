package view;


import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 这个类表示面板上的棋盘组件对象
 */
public class Chessboard extends JComponent implements Serializable {
    /**
     * CHESSBOARD_SIZE： 棋盘是8 * 8的
     * <br>
     * BACKGROUND_COLORS: 棋盘的两种背景颜色
     * <br>
     * chessListener：棋盘监听棋子的行动
     * <br>
     * chessboard: 表示8 * 8的棋盘
     * <br>
     * currentColor: 当前行棋方
     */
    private static final int CHESSBOARD_SIZE = 8;

    private final ChessComponent[][] chessComponents = new ChessComponent[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ChessColor currentColor = ChessColor.WHITE;
    //all chessComponents in this chessboard are shared only one model controller
    private final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;

    public int boardwidth;
    public int boardheight;
    public List<String> initGraph;
    public List<String> currentGraph;

    public List<List<String>> previousGraph;


    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        boardwidth=width;
        boardheight=height;
        CHESS_SIZE = width / 8;
        System.out.printf("chessboard size = %d, chess size = %d\n", width, CHESS_SIZE);

        initiateEmptyChessboard();

        initRookOnBoard(0, 0, ChessColor.BLACK);
        initRookOnBoard(0, CHESSBOARD_SIZE - 1, ChessColor.BLACK);
        initRookOnBoard(CHESSBOARD_SIZE - 1, 0, ChessColor.WHITE);
        initRookOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 1, ChessColor.WHITE);

        initKnightOnBoard(0, 1, ChessColor.BLACK);
        initKnightOnBoard(0, CHESSBOARD_SIZE - 2, ChessColor.BLACK);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, 1, ChessColor.WHITE);
        initKnightOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 2, ChessColor.WHITE);

        initBishopOnBoard(0,2,ChessColor.BLACK);
        initBishopOnBoard(0,CHESSBOARD_SIZE-3,ChessColor.BLACK);
        initBishopOnBoard(CHESSBOARD_SIZE-1,2,ChessColor.WHITE);
        initBishopOnBoard(CHESSBOARD_SIZE-1,CHESSBOARD_SIZE-3,ChessColor.WHITE);

        initQueenOnBoard(0, 3, ChessColor.BLACK);
        initQueenOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 5, ChessColor.WHITE);

        initKingOnBoard(0, 4, ChessColor.BLACK);
        initKingOnBoard(CHESSBOARD_SIZE - 1, CHESSBOARD_SIZE - 4, ChessColor.WHITE);

        for (int i = 0; i < CHESSBOARD_SIZE; i++) {
            initPawnOnBoard(1, i, ChessColor.BLACK);
            initPawnOnBoard(CHESSBOARD_SIZE - 2, CHESSBOARD_SIZE - i-1, ChessColor.WHITE);
        }
        initGraph =getChessboardGraph();
        previousGraph=new ArrayList<>();
        previousGraph.add(initGraph);
    }

    public List<String> getCurrentGraph() {
        return currentGraph;
    }

    public void setCurrentGraph(List<String> currentGraph) {
        this.currentGraph = currentGraph;
    }

    public ChessComponent[][] getChessComponents() {
        return chessComponents;
    }


    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void putChessOnBoard(ChessComponent chessComponent) {
        int row = chessComponent.getChessboardPoint().getX(), col = chessComponent.getChessboardPoint().getY();

        if (chessComponents[row][col] != null) {
            remove(chessComponents[row][col]);
        }
        add(chessComponents[row][col] = chessComponent);
    }


    public List<String> getChessboardGraph(){
        List<String> graph=new ArrayList<>(9);
            for (int row = 0; row < 8; row++) {
                StringBuilder graphBuilder = new StringBuilder();
                for (int col = 0; col < 8; col++) {
                    graphBuilder.append(chessComponents[row][col].getChessname());
                }
                graph.add(graphBuilder.toString());
            }
            if (getCurrentColor()==ChessColor.WHITE){
                graph.add(ChessColor.WHITE.getName());
            }else if(getCurrentColor()==ChessColor.BLACK){
                graph.add(ChessColor.BLACK.getName());
            }else {
                graph.add(ChessColor.NONE.getName());
            }
            return graph;
    }

    public void loadChessGame(List<String> chessboard){
        //检查size合法性1
        if (chessboard.size()!=9){
            JOptionPane.showMessageDialog(this, "Wrong ChessBoard Size");
        }
        //开始加载
        clickController.setFirst(null);
        if (Objects.equals(chessboard.get(8), ChessColor.BLACK.getName())) {
            this.currentColor = ChessColor.BLACK;
        } else if (Objects.equals(chessboard.get(8), ChessColor.WHITE.getName())){
            this.currentColor = ChessColor.WHITE;
        }else if (Objects.equals(chessboard.get(8), ChessColor.NONE.getName())){
            this.currentColor=ChessColor.NONE;
        }else {
            //检查size合法性2
                JOptionPane.showMessageDialog(this, "Wrong ChessBoard Size");
        }
        //检查color合法性
        if (this.currentColor==ChessColor.NONE){
            JOptionPane.showMessageDialog(this, "No CurrentPlayer");
        }

        this.removeAllChess();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (chessboard.get(row).charAt(col) == 'r') {
                    putChessOnBoard(new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.WHITE, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'R') {
                    putChessOnBoard(new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.BLACK, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'n') {
                    putChessOnBoard(new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.WHITE, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'N') {
                    putChessOnBoard(new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.BLACK, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'b') {
                    putChessOnBoard(new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.WHITE, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'B') {
                    putChessOnBoard(new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.BLACK, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'p') {
                    putChessOnBoard(new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.WHITE, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'P') {
                    putChessOnBoard(new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.BLACK, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'q') {
                    putChessOnBoard(new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.WHITE, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'Q') {
                    putChessOnBoard(new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.BLACK, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'k') {
                    putChessOnBoard(new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.WHITE, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == 'K') {
                   putChessOnBoard(new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), ChessColor.BLACK, clickController, CHESS_SIZE));
                } else if (chessboard.get(row).charAt(col) == '_') {
                    putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(row, col),calculatePoint(row, col),clickController,CHESS_SIZE));
                }else {
                    JOptionPane.showMessageDialog(null, "No Such Kind of Chess");
                }
                chessComponents[row][col].repaint();
            }
        }

    }
    public void swapChessComponents(ChessComponent chess1, ChessComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        chessComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        chessComponents[row2][col2] = chess2;

        chess1.repaint();
        chess2.repaint();
    }

    public void initiateEmptyChessboard() {
        for (int i = 0; i < chessComponents.length; i++) {
            for (int j = 0; j < chessComponents[i].length; j++) {
                putChessOnBoard(new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE));
            }
        }
    }

    public void swapColor() {
        currentColor = currentColor == ChessColor.BLACK ? ChessColor.WHITE : ChessColor.BLACK;
    }

    private void initRookOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new RookChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initKingOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new KingChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initQueenOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new QueenChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initKnightOnBoard(int row, int col, ChessColor color) {
    ChessComponent chessComponent = new KnightChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initPawnOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new PawnChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }
    private void initBishopOnBoard(int row, int col, ChessColor color) {
        ChessComponent chessComponent = new BishopChessComponent(new ChessboardPoint(row, col), calculatePoint(row, col), color, clickController, CHESS_SIZE);
        chessComponent.setVisible(true);
        putChessOnBoard(chessComponent);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE, row * CHESS_SIZE);
    }

    public void loadGame(List<String> chessData) {
        chessData.forEach(System.out::println);
    }

    public void resetChesses(){
        clickController.setFirst(null);
        this.loadChessGame(initGraph);
    }
    public void removeAllChess(){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                remove(chessComponents[row][col]);
                add(chessComponents[row][col]=new EmptySlotComponent(new ChessboardPoint(row,col),calculatePoint(row,col),clickController,CHESS_SIZE));
                chessComponents[row][col].repaint();
            }
        }
    }

    public void saveChess(){
//        JFileChooser chooser=new JFileChooser();
//        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int result=chooser.showOpenDialog(null);
//        File parent=chooser.getSelectedFile();
//        System.out.println("file parent path---->"+parent.getAbsolutePath());
//        String path= parent.getAbsolutePath()+File.separator+System.currentTimeMillis()+".txt";
        String path= "./saves"+File.separator+System.currentTimeMillis()+".txt";
        File file =new File(path);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try {//创建文件输出流对象
            fos=new FileOutputStream(file);
            //创建文件对象输出流对象
            oos=new ObjectOutputStream(fos);
            oos.writeObject(this.getChessboardGraph());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadChess(){
        JFileChooser chooser=new JFileChooser();
        int result=chooser.showOpenDialog(null);
        File parent=chooser.getSelectedFile();
        //判断文件类型合法性
        String txtcheck=parent.getName();
        if (!txtcheck.substring(txtcheck.length() - 4).equals(".txt")){
            JOptionPane.showMessageDialog(this, "Please input a txt file");
        }
        FileInputStream fis;
        ObjectInputStream ois=null;
        try{
            fis=new FileInputStream(parent);
            ois=new ObjectInputStream(fis);
            List<String> loadgraph=(List<String>) ois.readObject();
            this.currentGraph=loadgraph;
            this.loadChessGame(currentGraph);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (ois!=null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

