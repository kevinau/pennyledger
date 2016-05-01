package org.gyfor.report;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.gyfor.report.level.TitleLevel;

public class Report<T> {

  private final Supplier<T> supplier;
  
  private List<IReportLevel> workingLevels = new ArrayList<>();
  
  private ReportEngine reportEngine;
  
    
  public Report (Supplier<T> supplier) {
    this.supplier = supplier;
  }
  
  
  public Report<T> title (String title, String subTitle) {
    TitleLevel level = new TitleLevel(title, subTitle);
    workingLevels.add(level);
    return this;
  }
  
  
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
    IReportPager pager = new PDFReportPager(paperSize);
    reportEngine = new ReportEngine(pager);
    T record = supplier.get();
    prepareLevel(0, record);
    pager.close(path);
  }
  
  
  @SuppressWarnings("unchecked")
  public T prepareLevel(int d, T record) {
    IReportLevel level = workingLevels.get(d);    
    if (level.isGrouping()) {
      IReportGrouping<T> grouping = (IReportGrouping<T>)level;
      if (record != null) {
        grouping.setData(record);
        reportEngine.printHeader(grouping);

        Object thisGroup = grouping.getGroup(record);
        Object group = thisGroup;
        while (thisGroup != null && thisGroup.equals(group)) {
          record = prepareLevel(d + 1, record);
          if (record == null) {
            break;
          }
          group = grouping.getGroup(record);
        }

        reportEngine.printFooter(grouping);
      }
      return record;
    } else {
      IReportDetail detail = (IReportDetail)level;
      detail.setData(record);
      reportEngine.printDetail(detail);

      // Allow all upper level groupings to accumulate the detail that has been printed.
      for (IReportLevel g : workingLevels) {
        if (g.equals(level)) {
          break;
        }
        ((IReportGrouping<T>)g).accumulate(record);
      }
      return supplier.get();
    }
  }

}

