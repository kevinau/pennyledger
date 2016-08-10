package org.pennyledger.reportdb;

import java.util.Arrays;
import java.util.List;

import org.pennyledger.object.EntityPlanFactory;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.report.IReportDetail;
import org.pennyledger.report.IReportGrouping;
import org.pennyledger.report.PaperSize;
import org.pennyledger.report.Report;
import org.pennyledger.reportdb.ResultSetSupplier;
import org.pennyledger.reportdb.TitleLevel;
import org.pennyledger.reportdb.TestStream3.Selection;

public class TestStream2 {

  private static IGLAccount[] accountx = {
      new GLAccount("income", "Income", GLAccountType.INCOME),
      new GLAccount("expense", "Expense", GLAccountType.EXPENSE),
      new GLAccount("asset", "Asset", GLAccountType.ASSET),
      new GLAccount("liability", "Liability", GLAccountType.LIABILITY),
      new GLAccount("equity", "Owners equity", GLAccountType.EQUITY),
      new GLAccount("gains", "Gains", GLAccountType.GAINS),
      new GLAccount("losses", "Losses", GLAccountType.LOSSES),
  };
  
  private static String fromSql = " FROM DividendVoucher "
      + "ORDER BY shareholderCode, eventDate";

  public static void main (String[] args) throws Exception {
    IEntityPlan<DividendVoucher> entityPlan = EntityPlanFactory.getEntityPlan(DividendVoucher.class);
    
    List<IGLAccount> accounts = Arrays.asList(accountx);
    
    ResultSetSupplier<Selection> supplier = new ResultSetSupplier<>(conn, Selection.class, entityPlan, fromSql);

//    IReportLevel headingLevel1 = supplier.getPageHeader("shareholderCode", (t, p) -> {
//      t.totalDividendPayment += p.getDividendPayment();
//      t.totalTaxCredit += p.getTaxCredit();
//    });    
    
    IReportGrouping<Selection> title = new TitleLevel<>("Shareholder dividends");
    
    IReportGrouping<Selection> group1 = supplier.simpleGroup(p -> p.shareholderCode, "shareholderCode", "name", null, "dividendPayment", "taxCredit");
    
    IReportGrouping<Selection> headings1 = supplier.simpleColumnHeadings();
    
    IReportDetail detail1 = supplier.simpleDetail("eventDate", "distributionNo", "companyYear",
        "dividendType", "dividendPerShare", "shares", "dividendPayment", "taxCredit");
    
    Report<Selection> report = new Report<Selection>(supplier);
    report.group(title);
    report.group(group1);
    report.group(headings1);
    report.detail(detail1);
    report.generate("test3.pdf", PaperSize.A4);
    
    supplier.close();
  }
  
  
}
