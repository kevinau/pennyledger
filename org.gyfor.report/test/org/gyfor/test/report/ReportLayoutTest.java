package org.gyfor.test.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.ReportingEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.pennyledger.nio.CleanTextReader;

@RunWith(Parameterized.class)
public class ReportLayoutTest {

  @SuppressWarnings("unused")
  private final String name;
  private final File file;
  
  public ReportLayoutTest (String name, File file) {
    this.name = name;
    this.file = file;
  }
  
  
  @Test
  public void test() {
    TestReportPager pager = null;
    try (CleanTextReader reader = new CleanTextReader(file)) {
      String line = reader.readLine();
      while (line != null && !line.startsWith("---")) {
        line = reader.readLine();
      }
      line = reader.readLine();
      
      Queue<TestReportPager.Expected> expecteds = new LinkedList<>();
      while (line != null) {
        String[] elems = line.split("  *");
        int expectedPage = Integer.parseInt(elems[0]) - 1;
        int expectedOffset = Integer.parseInt(elems[1]);
        TestReportPager.Expected eblock = new TestReportPager.Expected(expectedPage, expectedOffset);
        expecteds.add(eblock);
        line = reader.readLine();
      }
      pager = new TestReportPager(expecteds);
      reader.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    
    ReportingEngine reportEngine = new ReportingEngine(pager);
    try (CleanTextReader reader = new CleanTextReader(file)) {
      String line = reader.readLine();
      while (line != null && !line.startsWith("---")) {
        // Build the report blocks and add them to the report engine
        IReportBlock block = null;
        
        String[] elems = line.split("  *");
        switch (elems[0]) {
        case "h" :
          block = new TestReportBlock(pager, elems[1]);
          if (elems.length == 2) {
            reportEngine.printHeader(block, null, null, null);
          } else {
            IReportBlock block2 = new TestReportBlock(pager, elems[2]);
            if (elems.length == 3) {
              reportEngine.printHeader(block, block2, null, null);
            } else {
              IReportBlock block3 = new TestReportBlock(pager, elems[3]);
              if (elems.length == 4) {
                reportEngine.printHeader(block, block2, block3, null);
              } else {
                IReportBlock block4 = new TestReportBlock(pager, elems[4]);
                reportEngine.printHeader(block, block2, block3, block4);
              }
            }
          }
          break;
        case "d" :
          block = new TestReportBlock(pager, elems[1]);
          reportEngine.printDetail(block);
          break;
        case "f" :
          block = new TestReportBlock(pager, elems[1]);
          reportEngine.printFooter(block);
          break;
        default :
          throw new RuntimeException("Unknown line: " + line);
        }
        line = reader.readLine();
      }
      reader.close();      
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }    
  }

  
  @Parameters(name = "{index}: {0}")
  public static Collection<Object[]> data() {
    List<Object[]> list = new ArrayList<>();
    
    File dir = new File(".");
    String[] fileNames = dir.list();
    if (fileNames != null) {
      for (String fileName : fileNames) {
        if (fileName.endsWith(".txt")) {
          String name = fileName.substring(0, fileName.length() - 4);
          list.add(new Object[] {name, new File(dir, fileName)});
        }
      }        
    }
    return list;
  }

}
