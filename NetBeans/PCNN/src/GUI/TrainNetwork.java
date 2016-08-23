/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import pcnn.ANN;

/**
 *
 * @author Vivek
 */
public class TrainNetwork {

    static public String InputFile;
    
    JFrame tframe=null;
    JButton jb1;
    JLabel blank,blank2;
    JLabel jlabel1=new JLabel("0.70");
    JLabel jlabel4=null;
    JTextField textfield=null;

    int momentum=30,learningRate=70;
    int noOfImages;
    static public JLabel status=new JLabel(" Status of Program ");

    public TrainNetwork()
    {
        initComponent();    
    }

    public void initComponent(){
        ImageIcon img=new ImageIcon("icon/matrix.jpg");
        ImageIcon img1=new ImageIcon("icon/NN.png");
        ImageIcon img2=new ImageIcon("icon/NNimage.png");
        tframe=new JFrame("Face Recognition/Train NetWork");
       // jframe.setPreferredSize(new Dimension(600, 400));
        tframe.setBounds(350, 100, img.getIconWidth()+5,img.getIconHeight()+50);
        //jframe.setUndecorated(true);
       // tframe.setBackground(new Color(200,200,20,20));
        tframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tframe.setResizable(false);



        tframe.setIconImage(img.getImage());

        textfield=new JTextField("");
        JButton inputButton=new JButton("Open");
         inputButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                        JFileChooser fc=new JFileChooser("gallery");
                        fc.showOpenDialog(new JFrame("Select DataSet"));
                        File f=fc.getCurrentDirectory();
                        File[] flist=f.listFiles();
                        noOfImages=flist.length;
                        InputFile=f.toString();
                        textfield.setText(InputFile);
                        //JOptionPane.showMessageDialog(tframe,InputFile+"--NO="+noOfImages);
			}
		});
        textfield.setBounds(10, 20, 250, 20);
         inputButton.setBounds(265, 20, 90, 20);


        JLayeredPane layer=new JLayeredPane();
        layer.setBounds(0, 0, 500, 300);



        JPanel panel1=new JPanel();

        panel1.setLayout(new BorderLayout(5,5));
        panel1.setBounds(40, 0, 400, 300);
        panel1.setOpaque(false);


//==============================================================================
        //======================Bottom Pane=====================================
        jb1=new JButton("Train Network");
        
        jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                try {
                        File f=new File("train.ann");
                        if(f.exists())
                        {
                            int op=JOptionPane.showConfirmDialog(tframe, "System has been already Trained!\nDo you want to re-Train?");
                            if(op==0)
                            {
                                    int y=ANN.TrainNetwork(InputFile, noOfImages);
                                    if(y==1)
                                    JOptionPane.showMessageDialog(tframe, "Sytstem Trained Successfully!");
                                    jb1.setText("Test Network");
                            }
                            if(op==1)
                                new F1().jframe.setVisible(true);
                        }
                        else
                        {
                            ANN.TrainNetwork(InputFile, noOfImages);
                            JOptionPane.showMessageDialog(tframe, "Sytstem Trained Successfully!");
                            new F1().jframe.setVisible(true);
                        
                        }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(tframe, ex);
                     JOptionPane.showMessageDialog(tframe, "Sorry Training failed!");
                }
			}
		});

       JLabel jlabel2=new JLabel(" Learning Rate @");
       jlabel2.setOpaque(true);
       jlabel1.setOpaque(true);
                final JSlider learnSlider=new JSlider(JSlider.HORIZONTAL,0,100,70);
                learnSlider.addChangeListener(new javax.swing.event.ChangeListener() {

                                public void stateChanged(ChangeEvent e) {
                                    jlabel1.setText("0."+learnSlider.getValue());
                                    //learningRate=learnSlider.getValue();
                                }
            });
            
        jlabel2.setBounds(10, 285, 120, 30);
        jlabel1.setBounds(130, 285,50, 30);
        learnSlider.setBounds(10, 310, 170, 30);
          learnSlider.setOpaque(false);

         JLabel jlabel3=new JLabel(" Momentum ");
       jlabel3.setOpaque(true);
       jlabel4=new JLabel("0.30");
       jlabel4.setOpaque(true);
                final JSlider learnSlider1=new JSlider(JSlider.HORIZONTAL,0,100,30);
                learnSlider1.setOpaque(false);
                learnSlider1.addChangeListener(new javax.swing.event.ChangeListener() {

                                public void stateChanged(ChangeEvent e) {
                                    jlabel4.setText("0."+learnSlider1.getValue());
                                   // momentum=learnSlider1.getValue();
                                }
            });

        jlabel3.setBounds(200, 285, 120, 30);
        jlabel4.setBounds(320, 285,50, 30);
        learnSlider1.setBounds(200, 310, 170, 30);

        jb1.setBounds(375,285,120,40);
        
//==============================================================================
        //==================Chart Area==========================================
       
        JLabel getChart=new JLabel();
       getChart.setBounds(10,60,img.getIconWidth()-20,215);
       getChart.setIcon(img1);
       getChart.setBackground(Color.WHITE);
       getChart.setForeground(Color.BLACK);
       getChart.setOpaque(true);

       TrainingChart tc= new TrainingChart(300,0);
       //tc.setBounds(0, 0, 300, 200);
     //  getChart.setLayout(new GridLayout(1,1));
      // getChart.add(tc);

      
       
       
      
      
       JLabel bg=new JLabel(img);
       //bg.setLayout(new FlowLayout());
       //bg.setVerticalAlignment(0);

      // bg.add(tc);
      // status.setBounds(10,img.getIconHeight()+5,300,30);
       status.setOpaque(false);
       status.setForeground(Color.WHITE);
      // status.setHorizontalAlignment();
        bg.add(inputButton);
        bg.add(textfield);
        bg.add(jb1);
        bg.add(getChart);
        
        bg.add(jlabel1);
        bg.add(jlabel2);
        bg.add(learnSlider);

        bg.add(jlabel3);
        bg.add(jlabel4);
        bg.add(learnSlider1);


       
        JPanel panel = new JPanel();
        panel.setBounds(350, 100, img.getIconWidth(), img.getIconHeight()+40);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
       // panel.setLayout(new GridBagLayout());

        panel.setBackground(new Color(0,102,0));

        panel.add(bg);
        panel.add(status);//,BorderLayout.EAST);

       // panel.add(tc);//new setBackgroundImage("icon/matrix.jpg"));
       // panel.add(new TrainingChart(200, 0));
        tframe.getContentPane().add(panel);//,BorderLayout.CENTER);
       // jframe.pack();
    }
    public static void main(String[] st){
    new TrainNetwork().tframe.setVisible(true);}
    
}
