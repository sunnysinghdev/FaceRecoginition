/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import pcnn.*;


/**
 *
 * @author Vivek
 */
public class F1 {
     String s=null;
    static public String InputFile;
  //  Main mainProgram=null;
    JFrame jframe=null;
    JLabel blank,blank2;
    static public JLabel status=new JLabel(" Status of Program ");
    ANN a=new ANN();
    public F1()
    {
        initComponent();
       // mainProgram=new Main();
    }
    public void initComponent(){
        ImageIcon img=new ImageIcon("icon/matrix.jpg");
        ImageIcon img1=new ImageIcon("icon/blankface2.jpg");
        ImageIcon img2=new ImageIcon("icon/blankface3.jpg");
        jframe=new JFrame("Face Recognition");
       // jframe.setPreferredSize(new Dimension(600, 400));
        jframe.setBounds(350, 100, img.getIconWidth()+15,img.getIconHeight()+55);
        //jframe.setUndecorated(true);
       // jframe.setBackground(new Color(200,200,20,20));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // jframe.setResizable(false);


        
        jframe.setIconImage(img.getImage());

       // jframe.setContentPane(new setBackgroundImage("icon/matrix.jpg"));

        JLayeredPane layer=new JLayeredPane();
        layer.setBounds(0, 0, 500, 300);
       

        
        JPanel panel1=new JPanel();

        panel1.setLayout(new BorderLayout(5,5));
        panel1.setBounds(40, 0, 400, 300);
        panel1.setOpaque(false);
        
       
        JButton jb1=new JButton("Get Image");
        jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                try {
                    JFileChooser fc = new JFileChooser("test");
                    fc.showOpenDialog(new JFrame("Input Image"));

                    new InputImage().Normalize(fc.getSelectedFile());
                    setInputImage(fc.getSelectedFile());
                    
                } catch (Exception ex) {
                   JOptionPane.showMessageDialog(jframe,ex);
                }
			}
		});
        JButton jb2=new JButton("Find Person");
        jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                try {
                    if(InputFile==null)
                      JOptionPane.showMessageDialog(jframe,"Please select an input Image file!");
                    else
                    {
                        File f=new File("gallery");
                        File[] flist=f.listFiles();
                        int index=a.Perform();
                       // JOptionPane.showMessageDialog(jframe,"Image Matched With Img no-> "+);
                        setFoundImage(flist[index-1]);
                    }
                } catch (Exception ex) {
                   JOptionPane.showMessageDialog(jframe,ex);
                }
			}
		});
        
        //jb2.setBounds(11, 62, 100,20);
        TrainingChart tc= new TrainingChart(100,0);
        tc.setBounds(10, 40, 300, 200);
        tc.setLayout(new BorderLayout(5,5));
       // panel1.add(tc);
       blank=new JLabel(img1);
       blank2=new JLabel(img2);
       blank.setBounds(10, 10, 125, 150);
       blank2.setBounds(10, 10, 125, 150);

       JLabel getImage=new JLabel();
       getImage.setBackground(Color.CYAN);
       getImage.setForeground(Color.BLACK);
       getImage.setOpaque(true);
       getImage.add(blank);

       JLabel foundImage=new JLabel();
       foundImage.setBackground(Color.WHITE);
       foundImage.setForeground(Color.BLACK);
       foundImage.setOpaque(true);
       foundImage.add(blank2);

       getImage.setBounds(20,70,145,175);
       jb1.setBounds(50,250,100,20);
       foundImage.setBounds(img.getIconWidth()-175, 70, 145, 175);
       jb2.setBounds(img.getIconWidth()-155,250,120,20);
       JLabel bg=new JLabel(img);
       //bg.setLayout(new FlowLayout());
       //bg.setVerticalAlignment(0);

      // bg.add(tc);
      // status.setBounds(10,img.getIconHeight()+5,300,30);
       status.setOpaque(true);
       status.setBackground(Color.BLACK);
       status.setForeground(Color.WHITE);
      // status.setHorizontalAlignment();
        bg.add(jb1);
        bg.add(getImage);
        bg.add(foundImage);
        bg.add(jb2);
        //bg.add(panel1);
        //bg.add(new TrainingChart(200, 0));
        JPanel panel = new JPanel();
        panel.setBounds(350, 100, img.getIconWidth(), img.getIconHeight()+40);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
       // panel.setLayout(new GridBagLayout());

        panel.setBackground(new Color(0,102,0));

        panel.add(bg);
        panel.add(status);//,BorderLayout.EAST);

       // panel.add(tc);//new setBackgroundImage("icon/matrix.jpg"));
       // panel.add(new TrainingChart(200, 0));
        jframe.getContentPane().add(panel);//,BorderLayout.CENTER);
       // jframe.pack();
    }
    public void setInputImage(File f) throws IOException{
        InputFile=f.toString();
        status.setText("Input Image File Loaded");
      
        BufferedImage img=ImageIO.read(f);
        Image image = img.getScaledInstance(125, 150, Image.SCALE_SMOOTH);
        ImageIcon img2=new ImageIcon(image);
        blank.setIcon(img2);
    }


    public void setFoundImage(File f){
    blank2.setIcon(new ImageIcon(f.toString()));
    status.setText("Image Name="+f.getName());
    }
    public static void main(String[] args) throws Exception{
    new F1().jframe.setVisible(true);
    //new TrainNetwork().tframe.setVisible(true);//new Main().getFoundPerson();
    }

}
