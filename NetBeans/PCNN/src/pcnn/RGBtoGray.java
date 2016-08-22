
package pcnn;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import javax.imageio.ImageIO;

public class RGBtoGray {

    public void process(BufferedImage img) throws Exception{
        int w = img.getWidth();
        int h = img.getHeight();
        int[] pixels = new int[w*h];
        //double[] pixels=new double[w*h];
            Raster in=img.getData();
            int c=0;
                for(int i=0;i<w;i++)
                    for(int j=0;j<h;j++)
                        pixels[c++]=img.getRGB(i, j);
    System.out.print(w+"*"+h+"="+pixels.length);
            int flag=1;
        if(img.getColorModel().getColorSpace().getNumComponents()==3)
        {
            pixels = RGB2Gray(pixels);flag=0;
        }
   
        saveImage(pixels,w,h);
    }

    public int[] RGB2Gray(int[] RGBPixels) {
        int[] gray = new int[RGBPixels.length];
        int j = -1;
        for (int i = 0; i < gray.length;i++) {
            gray[i] = (int) (0.299 * (RGBPixels[i] >> 16 & 0xff) + 0.587 * (RGBPixels[i] >> 8 & 0xff) + 0.114 * (RGBPixels[i] & 0xff));
        }
        return gray;
    }

    public void saveImage(int[] pixels,int w,int h)throws Exception{

     BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
     WritableRaster raster=image.getRaster();//getData();
     int c=0;
     for(int i=0;i<w;i++)
     {
         for(int j=0;j<h;j++)
         {
             raster.setSample(i,j,0,pixels[c++]);
         }
     }

File output=new File("icon/check.png");
    ImageIO.write(image,"PNG",output);
}
}