package org.pennyledger.entitydb.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.IReportPager;
import org.gyfor.report.PDFReportPager;
import org.gyfor.report.PaperSize;
import org.gyfor.report.ReportingEngine;

public class SQLReport<T> {

  private static class WorkingLevel<T,A> {
    
    private enum Kind {
      DETAIL,
      COMPOUND;
    };
    
    private final Kind kind;
    private Function<T,Object> grouper;
    private IReportBlock logicalHeader;
    private IReportBlock logicalFooter;
    private IReportBlock physicalHeader;
    private IReportBlock physicalFooter;
    private IReportBlock firstFooter;
    private IReportBlock detail;
    
    private WorkingLevel (Function<T,Object> grouper, IReportGrouping level) {
      this.kind = Kind.COMPOUND;
      this.grouper = grouper;
      this.logicalHeader = level.getLogicalHeader();
      this.logicalFooter = level.getLogicalFooter();
      this.physicalHeader = level.getPhysicalHeader();
      this.physicalFooter = level.getPhysicalFooter();
      this.firstFooter = level.getFirstFooter();
    }
    
   
    private WorkingLevel (Function<T,Object> grouper, IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
      this.kind = Kind.COMPOUND;
      this.grouper = grouper;
      this.logicalHeader = logicalHeader;
      this.logicalFooter = logicalFooter;
      this.physicalHeader = physicalHeader;
      this.physicalFooter = physicalFooter;
      this.firstFooter = firstFooter;
    }
    
    private WorkingLevel (IReportBlock detail) {
      this.kind = Kind.DETAIL;
      this.detail = detail;
    }
    
    private void setData (Object data) {
      logicalHeader.setData(data);
      if (logicalFooter != null) {
        logicalFooter.setData(data);
      }
      if (physicalHeader != null) {
        physicalHeader.setData(data);
      }
      if (physicalFooter != null) {
        physicalFooter.setData(data);
      }
      if (firstFooter != null) {
        firstFooter.setData(data);
      }
    }
  }
  
  private final Supplier<T> supplier;
  
  private int depth = 0;
  
  @SuppressWarnings("unchecked")
  private WorkingLevel<T,?>[] workingLevels = new WorkingLevel[20];
  
  public SQLReport (Supplier<T> supplier) {
    this.supplier = supplier;
  }
  
  
  public SQLReport<T> title (String title) {
    return title(title, null);
  }

  
  public SQLReport<T> title (String title, String subTitle) {
    TitleLevel level = new TitleLevel(title, subTitle);
    WorkingLevel<T,?> workingLevel = new WorkingLevel<>(p -> "", level);
    workingLevels[depth] = workingLevel;
    depth++;
    return this;
  }
  
  
  public SQLReport<T> level(IReportGrouping level) {
    WorkingLevel<T,?> workingLevel = new WorkingLevel<>(p -> "", level);
    workingLevels[depth] = workingLevel;
    depth++;
    return this;
  }

  
  public SQLReport<T> level(Function<T,Object> grouper, IReportGrouping level) {
    WorkingLevel<T,?> workingLevel = new WorkingLevel<>(grouper, level);
    workingLevels[depth] = workingLevel;
    depth++;
    return this;
  }

  
  public <A> SQLReport<T> level(Function<T,Object> grouper, IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
    WorkingLevel<T,A> workingLevel = new WorkingLevel<T,A>(grouper, logicalHeader, logicalFooter, physicalHeader, physicalFooter, firstFooter);
    workingLevels[depth] = workingLevel;
    depth++;
    return this;
  }

  
  public SQLReport<T> detail(IReportBlock detail) {
    workingLevels[depth] = new WorkingLevel<T,Object>(detail);
    depth++;
    return this;
  }
  
  private ReportingEngine reportEngine;
  
  
  public void generate(String pathName) {
    Path path = Paths.get(pathName);
    generate(path, PaperSize.A4);
  }
  
  
  public void generate(Path path) {
    generate(path, PaperSize.A4);
  }
  
  
  public void generate(String pathName, PaperSize paperSize) {
    Path path = Paths.get(pathName);
    generate(path, paperSize);
  }
  
  
  public void generate(Path path, PaperSize paperSize) {
    IReportPager pager = new PDFReportPager(paperSize);
    reportEngine = new ReportingEngine(pager);
    T record = supplier.get();
    prepareLevel(0, record);
    pager.close(path);
  }
  
  
  public T prepareLevel(int d, T record) {
    WorkingLevel<T,?> level = workingLevels[d];
    switch (level.kind) {
    case COMPOUND :
      if (record != null) {
        level.setData(record);
        reportEngine.printHeader(level.logicalHeader, level.physicalHeader, level.physicalFooter, level.firstFooter);

        Object thisGroup = level.grouper.apply(record);
        Object group = thisGroup;
        while (thisGroup != null && thisGroup.equals(group)) {
          record = prepareLevel(d + 1, record);
          if (record == null) {
            break;
          }
          group = level.grouper.apply(record);
        }

        reportEngine.printFooter(level.logicalFooter);
      }
      return record;
    case DETAIL :
      level.detail.setData(record);
      reportEngine.printDetail(level.detail);
      return supplier.get();
    default :
      throw new RuntimeException("Invalid level Kind: " + level.kind);
    }
  }

}

