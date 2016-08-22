/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.*;//JPanel;

/**
 *
 * @author Vivek
 */
public class TrainingChart extends JPanel{

    int epoch=0;
    int y=0;
    TrainingChart(int x,int y){
        this.epoch=x;
        this.y=y;
        super.setOpaque(true);
        super.setLayout(new FlowLayout());
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
         g.setColor(Color.red);
        for(int x=0;x<2*epoch;x+=2){

            g.drawLine(10+x, (y++), 10+x, 300);

        }
}
}
