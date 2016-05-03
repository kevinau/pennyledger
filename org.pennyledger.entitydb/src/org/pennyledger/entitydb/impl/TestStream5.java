package org.pennyledger.entitydb.impl;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.object.type.Alignment;



public class TestStream5 {

  public static String[] test2 = {"Long heading1", "Data1", "D2"};
  public static String[] test3 = {"Head", "Long data1", "Long data2"};
  public static String[] test4 = {"Head", "Long data1", "L2"};

  @Test
  public void uniformDataLongHeading () {
    String[] test1 = {"Data1", "Data2", "Data3"};
    align ("Long heading1", test1, Alignment.LEFT, 1300, 1300 / 2, 400, 0, 0);
  }
  
  
  @Test
  public void uniformDataShortHeading () {
    String[] test1 = {"Data1", "Data2", "Data3"};
    align ("Hd", test1, Alignment.LEFT, 500, 250, 0, 0, 0);
  }
  
  
  @Test
  public void skewedDataLongHeading () {
    String[] test1 = {"Long data1", "D2", "D3", "D4", "D5"};
    align ("Long heading1", test1, Alignment.LEFT, 1300, 650, 150, 0, 0);
  }
  
  
  @Test
  public void skewedDataMiddleHeading () {
    String[] test1 = {"V Long data1", "D2", "D3", "D4", "D5"};
    align ("Heading", test1, Alignment.LEFT, 1200, 350, 0, 0, 0);
  }
  
  
  @Test
  public void skewedDataShortHeading () {
    String[] test1 = {"V Long data1", "D2", "D3", "D4", "D5"};
    align ("Hd", test1, Alignment.LEFT, 1200, 600, 0, 0, 0);
  }
  
  
  @Test
  public void uniformDataLongHeading1 () {
    String[] test1 = {"Data1", "Data2", "Data3"};
    align ("Long heading1", test1, Alignment.CENTRE, 1300, 1300 / 2, 0, 650, 0);
  }
  
  
  @Test
  public void uniformDataShortHeading1 () {
    String[] test1 = {"Data1", "Data2", "Data3"};
    align ("Hd", test1, Alignment.CENTRE, 500, 250, 0, 250, 0);
  }
  
  
  @Test
  public void skewedDataLongHeading1 () {
    String[] test1 = {"Long data1", "D2", "D3", "D4", "D5"};
    align ("Long heading1", test1, Alignment.CENTRE, 1300, 650, 0, 650, 0);
  }
  
  
  @Test
  public void skewedDataMiddleHeading1 () {
    String[] test1 = {"V Long data1", "D2", "D3", "D4", "D5"};
    align ("Heading", test1, Alignment.CENTRE, 1200, 350, 0, 600, 0);
  }
  
  
  @Test
  public void skewedDataShortHeading1 () {
    String[] test1 = {"V Long data1", "D2", "D3", "D4", "D5"};
    align ("Hd", test1, Alignment.CENTRE, 1200, 600, 0, 600, 0);
  }
  
  
  @Test
  public void uniformDataLongHeading2 () {
    String[] test1 = {"Data1", "Data2", "Data3"};
    align ("Long heading1", test1, Alignment.RIGHT, 1300, 1300 / 2, 0, 0, 1300 - 400);
  }
  
  
  @Test
  public void uniformDataShortHeading2 () {
    String[] test1 = {"Data1", "Data2", "Data3"};
    align ("Hd", test1, Alignment.RIGHT, 500, 250, 0, 0, 500);
  }
  
  
  @Test
  public void skewedDataLongHeading2 () {
    String[] test1 = {"Long data1", "D2", "D3", "D4", "D5"};
    align ("Long heading1", test1, Alignment.RIGHT, 1300, 650, 0, 0, 1300 - 150);
  }
  
  
  @Test
  public void skewedDataMiddleHeading2 () {
    String[] test1 = {"V Long data1", "D2", "D3", "D4", "D5"};
    align ("Heading", test1, Alignment.RIGHT, 1200, 350, 0, 0, 1200);
  }
  
  
  @Test
  public void skewedDataShortHeading2 () {
    String[] test1 = {"V Long data1", "D2", "D3", "D4", "D5"};
    align ("Hd", test1, Alignment.RIGHT, 1200, 600, 0, 0, 1200);
  }
  
  
  @Test
  public void skewedDataShortHeading3 () {
    String[] test1 = {"123456789012", "12", "23", "34", "45"};
    align ("Hd", test1, Alignment.NUMERIC, 1200, 200, 0, 0, 1200);
  }
  
  
  private void align (String heading, String[] data, Alignment alignment, int widthx, int headingCentrex, int dataLeftx, int dataCentrex, int dataRightx) {
    int maxData = 0;
    int sumData = 0;
    int k = data.length;
    for (int i = 0; i < data.length; i++) {
      int n = data[i].length() * 100;
      maxData = Math.max(maxData, n);
      sumData += n;
    }
    int aveData = sumData / k;
    int headingWidth = heading.length() * 100;
    int width = Math.max(maxData, headingWidth);
    Assert.assertEquals(widthx, width);
    
    int headingCentre;
    if (headingWidth > maxData) {
      // Heading size is greater than data width
      headingCentre = headingWidth / 2;
      Assert.assertEquals(headingCentrex, headingCentre);
    } else {
      // Heading size is less than data width
      if (headingWidth > aveData) {
        // ... but greater than the average data width
        headingCentre = headingWidth / 2;
        Assert.assertEquals(headingCentrex, headingCentre);
      } else {
        // ... and less than the average data width
        headingCentre = width / 2;
        Assert.assertEquals(headingCentrex, headingCentre);
      }
    }
    
    switch (alignment) {
    case LEFT :
      int dataLeft;

      if (headingWidth > maxData) {
        // Heading size is greater than data width
        dataLeft = (headingWidth - maxData) / 2;
        Assert.assertEquals(dataLeftx, dataLeft);
      } else {
        // Heading size is less than data width
        dataLeft = 0;
        Assert.assertEquals(dataLeftx, dataLeft);
      }
      break;
    case CENTRE :
      int dataCentre;

      if (headingWidth > maxData) {
        // Heading size is greater than data width
        dataCentre = headingWidth / 2;
        Assert.assertEquals(dataCentrex, dataCentre);
      } else {
        // Heading size is less than data width
        dataCentre = maxData / 2;
        Assert.assertEquals(dataCentrex, dataCentre);
      }
      break;
    case RIGHT :
      int dataRight;

      if (headingWidth > maxData) {
        // Heading size is greater than data width
        dataRight = headingWidth - (headingWidth- maxData) / 2;
        Assert.assertEquals(dataRightx, dataRight);
      } else {
        // Heading size is less than data width
        dataRight = maxData;
        Assert.assertEquals(dataRightx, dataRight);
      }
      break;
    case NUMERIC :
      break;
    }
  }
}
