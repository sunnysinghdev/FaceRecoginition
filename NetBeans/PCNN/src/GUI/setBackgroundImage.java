/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 *
 * @author Vivek
 */
public class setBackgroundImage extends JPanel {
private BufferedImage img=null;
    setBackgroundImage(String image){
        try{
        img=ImageIO.read(new File(image));
        }
        catch(Exception ex){
        System.out.println(ex);}
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }
}
