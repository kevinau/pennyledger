package org.gyfor.report;

import java.util.LinkedList;
import java.util.Queue;

import org.gyfor.report.page.pdf.PDFContent;


public class ReportEngine {
  
  private final IReportPager pager;
  private final int pHeight;
  private int pOffset = 0;
  private int levelDepth = 0;
  private Queue<IReportBlock> queuedHeaders = new LinkedList<>();
  private EngineLevel[] levels = new EngineLevel[20];

  private static class EngineLevel {
    private int useCount;
    private IReportBlock physicalHeader;
    private IReportBlock physicalFooter;
    private IReportBlock firstFooter;
    // detail and logicalFooter blocks are used when they are added.  They are not queued in a ReportEngineLevel

    public EngineLevel (IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
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


  public void printHeader (IReportGrouping level) {
    printHeader (level.getLogicalHeader(), level.getPhysicalHeader(), level.getPhysicalFooter(), level.getFirstFooter());
  }

  
  public void printHeader (IReportBlock logicalHeader, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
    if (logicalHeader.isMandatory()) {
      doEmittableBlock(logicalHeader);
    } else {
      queuedHeaders.add(logicalHeader);
    }
    
    EngineLevel level = new EngineLevel(physicalHeader, physicalFooter, firstFooter);
    levels[levelDepth++] = level;
  }

  
  public void printDetail (IReportBlock detail) {
    doEmittableBlock(detail);
  }
  
  
  public void printDetail (IReportDetail detail) {
    printDetail(detail.getDetail());
  }
  
  
  public void printFooter (IReportBlock logicalFooter) {
    EngineLevel level = levels[--levelDepth];
    IReportBlock footer = level.getEffectiveLogicalFooter(logicalFooter);
    if (footer != null) {
      doEmittableBlock(footer);
    }
  }
  
  
  public void printFooter (IReportGrouping<?> level) {
    printFooter(level.getLogicalFooter());
  }
  
  
  private void doEmittableBlock (IReportBlock block) {
    int tryOffset = pOffset;
    for (IReportBlock queuedHeader : queuedHeaders) {
      tryOffset += queuedHeader.getHeight();
    }
    tryOffset += block.getHeight();
    int tryHeight = pHeight;
    for (int i = 0; i < levelDepth; i++) {
      IReportBlock pf = levels[i].getEffectivePhysicalFooter();
      if (pf != null) {
        tryHeight -= pf.getHeight();
      }
    }
    
    if (tryOffset > tryHeight) {
      // There is not enough space on the page for this block
      int breakDepth = levelDepth - queuedHeaders.size();
      for (int i = breakDepth - 1; i >= 0; i--) {
        IReportBlock physicalFooter = levels[i].physicalFooter;
        if (physicalFooter != null) {
          PDFContent canvas = pager.getContent();
          physicalFooter.emit(canvas, pOffset);
          pOffset += physicalFooter.getHeight();
        }
      }
      pager.newPage();
      pOffset = 0;
      for (int i = 0; i < breakDepth; i++) {
        IReportBlock physicalHeader = levels[i].physicalHeader;
        if (physicalHeader != null) {
          PDFContent canvas = pager.getContent();
          physicalHeader.emit(canvas, pOffset);
          pOffset += physicalHeader.getHeight();
        }
      }
    }
    // There is now enough space for this block
    for (IReportBlock queuedHeader : queuedHeaders) {
      PDFContent canvas = pager.getContent();
      queuedHeader.emit(canvas, pOffset);
      pOffset += queuedHeader.getHeight();
    }
    queuedHeaders.clear();
    PDFContent canvas = pager.getContent();
    block.emit(canvas, pOffset);
    pOffset += block.getHeight();
    if (levelDepth > 0) {
      levels[levelDepth - 1].useCount++;
    }
  }
  
  
  public void close () {
    pager.close();
  }

}
