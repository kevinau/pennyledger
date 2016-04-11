package org.gyfor.report;

import java.util.LinkedList;
import java.util.Queue;


public class ReportEngine {
  
  private final IReportPager pager;
  private final int pHeight;
  private int pOffset = 0;
  private int levelDepth = 0;
  private Queue<IReportBlock> queuedHeaders = new LinkedList<>();
  private ReportLevel[] levels = new ReportLevel[20];

  private static class ReportLevel {
    private int useCount;
    private IReportBlock physicalHeader;
    private IReportBlock physicalFooter;
    private IReportBlock firstFooter;
    // detail and logicalFooter blocks are used when they are added.  They are not queued in a ReportLevel

    public ReportLevel (IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
      this.physicalHeader = physicalHeader;
      this.physicalFooter = physicalFooter;
      this.firstFooter = firstFooter;
    }


    private IReportBlock getEffectivePhysicalFooter () {
      if (firstFooter != null && useCount == 0) {
        return firstFooter;
      } else {
        return physicalFooter;
      }
    }

  
    private IReportBlock getEffectiveLogicalFooter (IReportBlock logicalFooter) {
      if (firstFooter != null && useCount == 0) {
        return firstFooter;
      } else {
        return logicalFooter;
      }
    }

  }
 
  
  public ReportEngine (IReportPager pager) {
    this.pager = pager;
    pHeight = pager.getPageHeight();
  }


  public void addHeader (IReportLevel level) {
    addHeader(level.getLogicalHeader(), level.getPhysicalHeader(), level.getPhysicalFooter(), level.getFirstFooter());
  }
  
  
  public void addHeader (IReportBlock logicalHeader, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
    if (logicalHeader.isMandatory()) {
      doEmittableBlock(logicalHeader);
    } else {
      queuedHeaders.add(logicalHeader);
    }
    ReportLevel level = new ReportLevel(physicalHeader, physicalFooter, firstFooter);
    levels[levelDepth++] = level;
  }

  
  public void addDetail (IReportBlock detail) {
    doEmittableBlock(detail);
  }
  
  
  public void addFooter (IReportBlock logicalFooter) {
    ReportLevel level = levels[--levelDepth];
    IReportBlock footer = level.getEffectiveLogicalFooter(logicalFooter);
    doEmittableBlock(footer);
  }
  
  
  public void addFooter (IReportLevel level) {
    addFooter(level.getLogicalFooter());
  }
  
  
  private void doEmittableBlock (IReportBlock block) {
    int useCount = 0;
    if (levelDepth > 0) {
      useCount = levels[levelDepth - 1].useCount;
    }

    int tryOffset = pOffset;
    for (IReportBlock queuedHeader : queuedHeaders) {
      tryOffset += queuedHeader.getHeight();
    }
    tryOffset += block.getHeight();
    int tryHeight = pHeight;
    for (int i = 0; i < levelDepth; i++) {
      IReportBlock pf = levels[i].getEffectivePhysicalFooter();
      tryHeight -= pf.getHeight();
    }
    
    if (tryOffset > tryHeight) {
      // There is not enough space on the page for this block
      int breakDepth = levelDepth - queuedHeaders.size();
      for (int i = breakDepth - 1; i >= 0; i--) {
        System.out.println("Pysucal footer: " + levels[i].physicalFooter);
        levels[i].physicalFooter.emit(pOffset, useCount);
        pOffset += levels[i].physicalFooter.getHeight();
      }
      pager.newPage();
      System.out.println("----------------------");
      pOffset = 0;
      for (int i = 0; i < breakDepth; i++) {
        System.out.println("Physical header: " + levels[i].physicalHeader);
        levels[i].physicalHeader.emit(pOffset, useCount);
        pOffset += levels[i].physicalHeader.getHeight();
      }
    }
    // There is now enough space for this block
    for (IReportBlock queuedHeader : queuedHeaders) {
      System.out.println("Queued logical header: " + queuedHeader);
      queuedHeader.emit(pOffset, useCount);
      pOffset += queuedHeader.getHeight();
    }
    queuedHeaders.clear();
    System.out.println("Detail or logical footer: " + block);
    block.emit(pOffset, useCount);
    pOffset += block.getHeight();
    if (levelDepth > 0) {
      levels[levelDepth - 1].useCount++;
    }
  }
  
  
  public void close () {
    pager.close();
  }

}
