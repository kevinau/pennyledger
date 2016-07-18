package org.gyfor.report;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.pennyledger.util.ResettableSupplier;

public class Report<T> {

  private static class Trail<T> {
    private final Trail<T> parent;
    private final Object value;
    private final IReportGrouping<T> grouping;
    
    private Trail (Trail<T> parent, IReportGrouping<T> grouping, T source) {
      this.parent = parent;
      this.value = grouping.getGroup(source);
      this.grouping = grouping;
    }
    
    private boolean isSame(T source) {
      if (source == null) {
        return false;
      } else {
        Object value2 = grouping.getGroup(source);
        boolean same = value.equals(value2);
        if (same) {
          if (parent == null) {
            return true;
          } else {
            return parent.isSame(source);
          }
        } else {
          return false;
        }
      }
    }
  }
  
  private final ResettableSupplier<T> supplier;
  
  private List<IReportLevel> workingLevels = new ArrayList<>();
  
  private Engine engine;
  
    
  public Report (ResettableSupplier<T> supplier) {
    this.supplier = supplier;
  }
  
  
//  public Report<T> title (String title) {
//    return title(title, null);
//  }
//  
//  
//  public Report<T> title (String title, String subTitle) {
//    TitleLevel level = new TitleLevel(title, subTitle);
//    workingLevels.add(level);
//    return this;
//  }
//  
//  
//  public Report<T> columnHeadings () {
//    ColumnHeadingsLevel<T> colHeadings = new ColumnHeadingsLevel<>();
//    workingLevels.add(colHeadings);
//    return this;
//  }
  
  
  public Report<T> group(IReportGrouping<T> grouping) {
    workingLevels.add(grouping);
    return this;
  }

  
//  public FlatDataReport<T> level(Function<T,?> grouper, IReportGrouping level) {
//    WorkingLevel<T> workingLevel = new WorkingLevel<T>(grouper, level);
//    workingLevels[depth] = workingLevel;
//    depth++;
//    return this;
//  }

  
  public Report<T> detail(IReportBlock detail) {
    return detail(new ReportDetail(detail));
  }
  
  public Report<T> detail(IReportDetail detail) {
    workingLevels.add(detail);
    return this;
  }
  
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
    engine = new CalcWidthsEngine();
    T record = supplier.get();
    prepareLevel(0, record, null);
    
    finalizeWidths();
    
    IReportPager pager = new PDFReportPager(paperSize);
    engine = new ReportingEngine(pager);
    record = supplier.reset().get();
    prepareLevel(0, record, null);
    pager.close(path);
  }
  
  @SuppressWarnings("unchecked")
  public T prepareLevel(int d, T record, Trail<T> trail) {
    IReportLevel level = workingLevels.get(d);    
    if (level.isDetail()) {
      IReportDetail detail = (IReportDetail)level;
      detail.setData(record);
      engine.processDetail(detail);

      // Allow all upper level groupings to accumulate the detail that has been printed.
      for (IReportLevel g : workingLevels) {
        if (g.equals(level)) {
          break;
        }
        ((IReportGrouping<T>)g).accumulate(record);
      }
      return supplier.get();
    } else {
      IReportGrouping<T> grouping = (IReportGrouping<T>)level;
      grouping.reset();
      if (record != null) {
        grouping.setData(record);
        engine.processHeader(grouping);

        Trail<T> trail2 = new Trail<>(trail, grouping, record);
//        Object thisGroup = grouping.getGroup(record);
//        Object group = thisGroup;
        while (trail2.isSame(record)) {
          record = prepareLevel(d + 1, record, trail2);
          if (record == null) {
            break;
          }
//          group = grouping.getGroup(record);
        }

        engine.processFooter(grouping);
      }
      return record;
    }
  }

  
  private void finalizeWidths () {
    for (IReportLevel level : workingLevels) {
      level.finalizeWidths();
    }
  }
  
}

