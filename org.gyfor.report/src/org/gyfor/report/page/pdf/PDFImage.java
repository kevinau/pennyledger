package org.gyfor.report.page.pdf;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.gyfor.report.page.IPageImage;


public class PDFImage extends PDFContentReference implements IPageImage {

  private final int index;
  private final int pixelsWide;
  private final int pixelsHigh;
  
  
//  private void toHex(byte[] dest, byte[] source) {
//    int j = 0;
//    for (int i = 0; i < source.length; i++) {
//      int x = source[i];
//      int x0 = (x >> 4) & 0xF;
//      if (x0 < 10) {
//        dest[j++] = (byte)(x0 + '0');
//      } else {
//        dest[j++] = (byte)(x0 + 'a' - 10);
//      }
//      int x1 = (x & 0xF);
//      if (x1 < 10) {
//        dest[j++] = (byte)(x1 + '0');
//      } else {
//        dest[j++] = (byte)(x1 + 'a' - 10);
//      }
//    }
//  }
  
  

  public PDFImage (PDFDocument document, int index, InputStream imageInputStream) {
    super (document, "XObject");
    this.index = index;
    put ("Subtype", new PDFName("Image"));
    try {
      BufferedImage img = ImageIO.read(imageInputStream);
      pixelsWide = img.getWidth();
      pixelsHigh = img.getHeight();
      put ("Width", pixelsWide);
      put ("Height", pixelsHigh);
      
      IndexColorModel colorModel = (IndexColorModel)img.getColorModel();
      int colorMapSize = colorModel.getMapSize();
      int[] rgbArray = new int[colorMapSize];
      colorModel.getRGBs(rgbArray);
      byte[] deviceArray = new byte[colorMapSize * 3];
      for (int i = 0; i < rgbArray.length; i++) {
        deviceArray[i * 3 + 2] = (byte)(rgbArray[i] & 0xff);
        deviceArray[i * 3 + 1] = (byte)((rgbArray[i] >> 8) & 0xff);
        deviceArray[i * 3] = (byte)((rgbArray[i] >> 16) & 0xff);
      }
      PDFObject[] colorSpace = {
          new PDFName("Indexed"),
          new PDFName("DeviceRGB"),
          new PDFInteger(255),
          new PDFHexString(deviceArray),
      };
      put ("ColorSpace", new PDFArray(colorSpace));
      
//      Raster raster = img.getData();
//      DataBuffer data = raster.getDataBuffer();
//      int k = 0;
//      for (int i = 0; i < raster.getHeight(); i++) {
//        for (int j = 0; j < raster.getWidth(); j++) {
//          int x = data.getElem(k++);
//        }
//      }

      put ("BitsPerComponent", 8);
      int encodedLength = pixelsWide * pixelsHigh * 2 + pixelsHigh * 2;
      put ("Length", encodedLength);
      //put ("Filter", new PDFName("ASCIIHexDecode"));
      
      ByteArrayBuilder content = getByteArray();
      byte[] source = new byte[pixelsWide];
      //byte[] dest = new byte[pixelsWide * 2];
      for (int y = 0; y < pixelsHigh; y++) {
        int i = 0;
        for (int x = 0; x < pixelsWide; x++) {
          int rgb = img.getRGB(x, y);
          for (int c = 0; c < rgbArray.length; c++) {
            if (rgbArray[c] == rgb) {
              source[i++] = (byte)c;
              break;
            }
          }
        }
        //toHex(dest, source);
        //content.append(dest);
        //content.append('\r');
        //content.append('\n');
        content.append(source);
      }
      close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
  }


  public PDFName getId () {
    return new PDFName("Im" + index);
  }
  
  
  @Override
  public int getPixelsWide() {
    return pixelsWide;
  }
  
  
  @Override
  public int getPixelsHigh() {
    return pixelsHigh;
  }
  
}
