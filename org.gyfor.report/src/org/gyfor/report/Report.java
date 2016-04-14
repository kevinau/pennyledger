package org.gyfor.report;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;

public class Report<T> {

  private static class ReportLevel<T> {
    
    private enum Kind {
      DETAIL,
      COMPOUND;
    };
    
    private final Kind kind;
    private List<Object> filters = new ArrayList<>();
    private Function<T,?> grouper;
    private IReportBlock logicalHeader;
    private IReportBlock logicalFooter;
    private IReportBlock physicalHeader;
    private IReportBlock physicalFooter;
    private IReportBlock firstFooter;
    private IReportBlock detail;
    
    private ReportLevel (Function<T,?> grouper, IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
      this.kind = Kind.COMPOUND;
      this.grouper = grouper;
      this.logicalHeader = logicalHeader;
      this.logicalFooter = logicalFooter;
      this.physicalHeader = physicalHeader;
      this.physicalFooter = physicalFooter;
      this.firstFooter = firstFooter;
    }
    
    private ReportLevel (IReportBlock detail) {
      this.kind = Kind.DETAIL;
      this.detail = detail;
    }
    
    private void addFilter(Predicate<T> p) {
      filters.add(p);
    }
    
    private void addAccumulator(Consumer<T> p) {
      filters.add(p);
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
  private ReportLevel<T>[] levels = new ReportLevel[20];
  
  public Report (Supplier<T> supplier) {
    this.supplier = supplier;
  }
  
  public Report<T> filter(Predicate<T> p) {
    levels[depth].addFilter(p);  
    return this;
  }
  
  
  public Report<T> level(Function<T,?> grouper, IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
    ReportLevel<T> level = new ReportLevel<T>(grouper, logicalHeader, logicalFooter, physicalHeader, physicalFooter, firstFooter);
    levels[depth] = level;
    depth++;
    return this;
  }

  
  public Report<T> accumulate(Consumer<T> p) {
    levels[depth].addAccumulator(p);
    return this;
  }
  
  
  public void detail(@NonNull IReportBlock detail) {
    levels[depth] = new ReportLevel<T>(detail);
    depth++;
  }
  
  private ReportEngine reportEngine;
  
  public void prepare() {
    T record = supplier.get();
    prepareLevel(0, record);
  }
  
  
  @SuppressWarnings("unchecked")
  public T prepareLevel(int d, T record) {
    ReportLevel<T> level = levels[d];
    for (Object filter : level.filters) {
      if (filter instanceof Predicate) {
        Predicate<T> p = (Predicate<T>)filter;
        if (!p.test(record)) {
          return supplier.get();
        }
      } else if (filter instanceof Consumer) {
        Consumer<T> p = (Consumer<T>)filter;
        p.accept(record);
      } else {
        throw new RuntimeException("Unexpected filter: " + filter.getClass());
      }
    }
    
    switch (level.kind) {
    case COMPOUND :
      Object thisGroup = level.grouper.apply(record);
      Object group = thisGroup;
      while (thisGroup != null && thisGroup.equals(group)) {
        level.setData(record);
        reportEngine.printHeader(level.logicalHeader, level.physicalHeader, level.physicalFooter, level.firstFooter);
        T nextRecord = prepareLevel(d + 1, record);
        reportEngine.printFooter(level.logicalFooter);
        record = nextRecord;
        group = level.grouper.apply(record);
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

