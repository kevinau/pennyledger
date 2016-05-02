package org.pennyledger.entitydb.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

import javax.persistence.Id;
import javax.persistence.Version;

import org.pennyledger.db.IConnection;
import org.pennyledger.db.IPreparedStatement;
import org.pennyledger.db.SpecifiedDatabase;
import org.pennyledger.math.Decimal;
import org.pennyledger.object.Entity;
import org.pennyledger.object.FormField;
import org.pennyledger.object.value.TimestampValue;
import org.pennyledger.sql.dialect.IDialect;
import org.pennyledger.sql.dialect.derby.DerbyEmbeddedDialect;

@Entity
public class DividendVoucher {

  @Id
  private int id;
  
  @Version
  private TimestampValue version;
  
  private LocalDate eventDate;
  private int distributionNo;
  private int companyYear;
  private LocalDate accountingYearEnd;
  
  @FormField(length=10)
  private String dividendType;
  private Decimal dividendPerShare;
  private int shares;
  private Decimal dividendPayment;
  private Decimal taxCredit;
  private Decimal totalDividend;

  @FormField(length=30)
  private String name;
  @FormField(length=30)
  private String address;
  @FormField(length=30)
  private String town;
  private String postcode;

  @FormField(length=4)
  private String shareholderCode;
  
  public DividendVoucher () {
  }

  public DividendVoucher(LocalDate eventDate, int distributionNo, int companyYear, LocalDate accountingYearEnd,
      String dividendType, Decimal dividendPerShare, int shares, Decimal dividendPayment, Decimal taxCredit,
      Decimal totalDividend, String name, String address, String town, String postcode, String shareholderCode) {
    this.eventDate = eventDate;
    this.distributionNo = distributionNo;
    this.companyYear = companyYear;
    this.accountingYearEnd = accountingYearEnd;
    this.dividendType = dividendType;
    this.dividendPerShare = dividendPerShare;
    this.shares = shares;
    this.dividendPayment = dividendPayment;
    this.taxCredit = taxCredit;
    this.totalDividend = totalDividend;
    this.name = name;
    this.address = address;
    this.town = town;
    this.postcode = postcode;
    this.shareholderCode = shareholderCode;
  }
  

  @Override
  public String toString() {
    return "DividendVoucher [id=" + id + ", eventDate=" + eventDate + ", distributionNo=" + distributionNo + ", companyYear="
        + companyYear + ", accountingYearEnd=" + accountingYearEnd + ", dividendType=" + dividendType
        + ", dividendPerShare=" + dividendPerShare + ", shares=" + shares + ", dividendPayment=" + dividendPayment
        + ", taxCredit=" + taxCredit + ", totalDividend=" + totalDividend + ", name=" + name + ", address=" + address
        + ", town=" + town + ", postcode=" + postcode + ", shareholderCode=" + shareholderCode + "]";
  }

  public LocalDate getEventDate() {
    return eventDate;
  }

  public int getDistributionNo() {
    return distributionNo;
  }

  public int getCompanyYear() {
    return companyYear;
  }

  public LocalDate getAccountingYearEnd() {
    return accountingYearEnd;
  }

  public String getDividendType() {
    return dividendType;
  }

  public Decimal getDividendPerShare() {
    return dividendPerShare;
  }

  public int getShares() {
    return shares;
  }

  public Decimal getDividendPayment() {
    return dividendPayment;
  }

  public Decimal getTaxCredit() {
    return taxCredit;
  }

  public Decimal getTotalDividend() {
    return totalDividend;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getTown() {
    return town;
  }

  public String getPostcode() {
    return postcode;
  }

  public String getShareholderCode() {
    return shareholderCode;
  }

  public static void main (String[] args) throws Exception {
    IDialect dialect = new DerbyEmbeddedDialect();
    SpecifiedDatabase connFactory = new SpecifiedDatabase(dialect, "localhost", "myDB", "kevin", "kevin");
    
    IConnection conn = connFactory.getIConnection();
    conn.setAutoCommit(true);
    
    try (BufferedReader reader = new BufferedReader(new FileReader("Declared-dividends.csv"))) {
      // Skip header line
      reader.readLine();
      String line = reader.readLine();
      while (line != null) {
        String sql = "INSERT INTO DividendVoucher (eventDate,distributionNo,companyYear,accountingYearEnd,dividendType,dividendPerShare,shares,dividendPayment,taxCredit,totalDividend,name,address,town,postcode,shareholderCode) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        IPreparedStatement ps = conn.prepareStatement(sql);
        
        String[] field = line.split(",");
        System.out.println(field[0] + "...." + LocalDate.parse(field[0]));
        ps.setLocalDate(LocalDate.parse(field[0]));
        ps.setInt(Integer.parseInt(field[1]));
        ps.setInt(Integer.parseInt(field[2]));
        ps.setLocalDate(LocalDate.parse(field[3]));
        ps.setString(field[4]);
        ps.setDecimal(new Decimal(field[5]));
        ps.setInt(Integer.parseInt(field[6]));
        ps.setDecimal(new Decimal(field[7]));
        ps.setDecimal(new Decimal(field[8]));
        ps.setDecimal(new Decimal(field[9]));
        ps.setString(field[10]);
        ps.setString(field[11]);
        ps.setString(field[12]);
        ps.setString(field[13]);
        ps.setString(field[14]);
        ps.executeUpdate();

        line = reader.readLine();
      }
    }
    conn.close();
  }
  
//    
////    DividendVoucher dv = new DividendVoucher();
////    ICouchDatabase couchDB = couch.getDatabase("accounts");
////    
////    GLEntry entry1 = new GLEntry("1001", new Date(), 1000.10, "Rental income 48 Fairclough");
////    
////    CouchResponse save1 = couchDB.save(entry1);
////    GLEntry entry2 = couchDB.fetch(save1.getId());
////    System.out.println("------------> " + entry2);
////    //couch.execute("/accountsxxx");
//  }
//
//  @Override
//  public void map(DividendVoucher doc) {
//    GLEntry entry = new GLEntry(Integer.toString(doc.getDistributionNo()), doc.date, doc.getCompanyYear() + " " + doc.getDividendType());
//    entry.addDetail("Dividend paid", doc.getDividendPayment());
//    if (!doc.taxCredit.isZero()) {
//      entry.addDetail("Tax credit", doc.getTaxCredit());
//    }
//    entry.addBalancingDetail("Dividend owing");
//    emit (entry);
//  }
//
}
