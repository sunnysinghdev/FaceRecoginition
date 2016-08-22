
package pcnn;



import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class InputImage {

    public void Normalize(File f)throws Exception{

        BufferedImage img=ImageIO.read(f);
        
        Image image = img.getScaledInstance(125, 150, Image.SCALE_SMOOTH);

        BufferedImage normImage=new BufferedImage(125, 150, BufferedImage.TYPE_BYTE_GRAY);

        normImage.getGraphics().drawImage(image, 0, 0 , null);

        File dir=new File("normal");
        dir.mkdir();
        File f1=new File("normal/check.png");
        ImageIO.write(normImage, "PNG",f1);

        System.out.println(f1.toString());


    }

}

