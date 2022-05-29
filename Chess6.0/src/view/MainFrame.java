package view;

import controller.GameController;

import javax.crypto.Mac;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MainFrame extends JFrame {
    private final int WIDTH;
    private final Dimension displaySize=Toolkit.getDefaultToolkit().getScreenSize();
    private final int HEIGTH;


    private Border emptyBorder=BorderFactory.createEmptyBorder(0,0,0,0);

    ImageIcon img =new ImageIcon("./images/mainback.jpg");// 把背景图片显示在一个标签里面
    JLabel label = new JLabel(img);
    Image image=img.getImage();


    public MainFrame(int width, int height)  {
        setTitle("2022 CS102A Project"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;

        setSize(WIDTH/3, HEIGTH/3);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        this.getContentPane().setBackground(new Color(179, 208, 222, 185));
//        this.getContentPane().setVisible(false);
//        setBackground(Color.blue);
        this.setVisible(true);



        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }


        addLoadGameButton();
        addStartNewGameButton();
        addTitle();
    }

//    private static final long serialVersionUID=1L;
//    public void paint(Graphics g){
//        g.drawImage(image,0,0,this.getWidth(),this.getHeight(),null);
//    }
    private void addStartNewGameButton()  {
        JButton button = new JButton("New Game");
        button.addActionListener((e) -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1200, 675);//800*608
            mainFrame.setVisible(true);
            this.setVisible(false);
            }
        );
        button.setLocation(this.getSize().width/2-60, this.getSize().height/2-36);
        button.setSize(120, 36);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        add(button);
    }
    private void addLoadGameButton()  {
        this.repaint();
        JButton button = new JButton("Load");
        button.addActionListener((e) -> {
            ChessGameFrame mainFrame = new ChessGameFrame(1200, 675);//800*608
            mainFrame.setVisible(true);
            this.setVisible(false);
            mainFrame.loadGame();
            }
        );
        button.setLocation(this.getSize().width/2-60, this.getSize().height/2+18);
        button.setSize(120, 36);
        button.setFont(new Font("Rockwell", Font.BOLD, 18));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
//        button.setVisible(true);
        add(button);
    }
    private void addTitle(){
        JLabel title=new JLabel("CHESS");
        title.setLocation(this.getSize().width/2-65,this.getSize().height/2-90);
        title.setSize(150,36);
        title.setFont(new Font("Cooper Black",Font.BOLD,36));
        add(title);

    }

}
