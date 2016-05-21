package org.gyfor.report;

import java.util.LinkedList;
import java.util.Queue;

import org.gyfor.report.page.pdf.PDFContent;


public class ReportingEngine implements Engine {
  
  private class QueuedHeader {
    final int levelDepth;
    final IReportBlock header;
    final IReportBlock separator;
    
    private QueuedHeader (int levelDepth, IReportBlock header, IReportBlock separator) {
      this.levelDepth = levelDepth;
      this.header = header;
      this.separator = separator;
    }
    
    private void emit (PDFContent canvas, int offset) {
      if (separator != null) {
        int useCount = levels[levelDepth - 1].useCount;
        if (useCount > 0) {
          separator.emit(canvas, offset);
          offset += separator.getHeight();
        }
      }
      header.emit(canvas, offset);
    }
    
    private int getHeight () {
      int n = header.getHeight();
      if (separator != null) {
        int useCount = levels[levelDepth - 1].useCount;
        if (useCount > 0) {
          n += separator.getHeight();
        }
      }
      return n;
    }
  }
  
  private final IReportPager pager;
  private final int pHeight;
  private int pOffset = 0;
  private int levelDepth = 0;
  private Queue<QueuedHeader> queuedHeaders = new LinkedList<>();
  private EngineLevel[] levels = new EngineLevel[20];
  {
    levels[0] = new EngineLevel();
    levelDepth++;
  }

  private static class EngineLevel {
    private int useCount;
    private IReportBlock physicalHeader;
    private IReportBlock physicalFooter;
    private IReportBlock firstFooter;
    // detail and logicalFooter blocks are used when they are added.  They are not queued in a ReportEngineLevel

    public EngineLevel () {
      this.physicalHeader = null;
      this.physicalFooter = null;
      this.firstFooter = null;
    }
    
    
    public EngineLevel (IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
      this.physicalHeader = physicalHeader;
      this.physicalFooter = physicalFooter;
      this.firstFooter = firstFooter;
    }


    private IReportBlock getEffectivePhysicalFooter (int count) {
      if (firstFooter != null && count == 0) {
        return firstFooter;
      } else {
        return physicalFooter;
      }
    }

  
    private IReportBlock getEffectiveLogicalFooter (IReportBlock logicalFooter, int count) {
      if (firstFooter != null && count == 0) {
        return firstFooter;
      } else {
        return logicalFooter;
      }
    }
     
  }
 
  
  public ReportingEngine (IReportPager pager) {
    this.pager = pager;
    pHeight = pager.getPageHeight();
  }


  @Override
  public void processHeader (IReportGrouping<?> level) {
    processHeader (level.getLogicalHeader(), level.getPhysicalHeader(), level.getPhysicalFooter(), level.getFirstFooter(), level.getSeparator());
  }

  
  private void processHeader (IReportBlock logicalHeader, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter, IReportBlock separator) {
    if (logicalHeader.isMandatory()) {
      doEmittableBlock(logicalHeader, separator);
      levels[levelDepth - 1].useCount++;
    } else {
      queuedHeaders.add(new QueuedHeader(levelDepth, logicalHeader, separator));
    }
    
    EngineLevel level = new EngineLevel(physicalHeader, physicalFooter, firstFooter);
    levels[levelDepth++] = level;
  }

  
  @Override
  public void processDetail (IReportDetail detail) {
    processDetail(detail.getDetail(), detail.getSeparator());
  }
  
  
  private void processDetail (IReportBlock detail, IReportBlock separator) {
    doEmittableBlock(detail, separator);
    levels[levelDepth - 1].useCount++;
  }
  
  
  @Override
  public void processFooter (IReportGrouping<?> level) {
    printFooter(level.getLogicalFooter());
  }
  
  
  private void printFooter (IReportBlock logicalFooter) {
    EngineLevel level = levels[--levelDepth];
    IReportBlock footer = level.getEffectiveLogicalFooter(logicalFooter, 0);
    if (footer != null) {
      doEmittableBlock(footer, null);
    }
  }
  
  
  private void doEmittableBlock (IReportBlock block, IReportBlock separator) {
    int tryOffset = pOffset;
    for (QueuedHeader queuedHeader : queuedHeaders) {
      tryOffset += queuedHeader.getHeight();
    }
    if (separator != null && levels[levelDepth - 1].useCount > 0) {
      tryOffset += separator.getHeight();
    }
    tryOffset += block.getHeight();
    int tryHeight = pHeight;
    for (int i = 0; i < levelDepth; i++) {
      IReportBlock pf = levels[i].getEffectivePhysicalFooter(levels[levelDepth - 1].useCount);
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
        levels[i].useCount = 0;
      }
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
    for (QueuedHeader queuedHeader : queuedHeaders) {
      PDFContent canvas = pager.getContent();
      queuedHeader.emit(canvas, pOffset);
      pOffset += queuedHeader.getHeight();
      levels[queuedHeader.levelDepth - 1].useCount++;
    }
    queuedHeaders.clear();
    
    PDFContent canvas = pager.getContent();
    if (separator != null && levels[levelDepth - 1].useCount > 0) {
      separator.emit(canvas, pOffset);
      pOffset += separator.getHeight();
    }
    block.emit(canvas, pOffset);
    pOffset += block.getHeight();
  }
  
  
  public void close () {
    pager.close();
  }

}
