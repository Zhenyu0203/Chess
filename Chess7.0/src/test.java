import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        LinkedList<List<String>> test1=new LinkedList<>();
        List<String> test2=new ArrayList<>();

        test2.add("RRRRRRRR");
        test2.add("RRRRRRRR");
        test2.add("RRRRRRRR");
        test2.add("RRRRRRRR");
        test2.add("RRRRRRRR");
        test2.add("RRRRRRRR");
        test2.add("RRRRRRRR");
        test2.add("RRRRRRRR");
        test2.add("yellow");


        test1.add(test2);


        String filename= JOptionPane.showInputDialog(null,"Please enter the file name:","");
        if (filename!=null){
            if (filename=="") filename = String.valueOf(System.currentTimeMillis());
            String path = "./saves" + File.separator + filename + ".txt";
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {//创建文件输出流对象
                fos = new FileOutputStream(file);
                //创建文件对象输出流对象
                oos = new ObjectOutputStream(fos);
                oos.writeObject(test1);
                JOptionPane.showMessageDialog(null, "Saved");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
