package org.pennyledger.reportdb;

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
public class FinancialYear {

  @Id
  private int id;
  
  @Version
  private TimestampValue version;
  
  @FormField(length=6)
  private String name;
  private LocalDate endDate;
  
  public FinancialYear () {
  }

  public FinancialYear(String name, LocalDate endDate) {
    this.name = name;
    this.endDate = endDate;
  }
  

  @Override
  public String toString() {
    return "FinancialYear [id=" + id + ", namee=" + name + ", endDate=" + endDate + "]";
  }

  public String geName() {
    return name;
  }

  public LocalDate geEndDate() {
    return endDate;
  }

  public static void main (String[] args) throws Exception {
    IDialect dialect = new DerbyEmbeddedDialect();
    SpecifiedDatabase connFactory = new SpecifiedDatabase(dialect, "localhost", "myDB", "kevin", "kevin");
    
    IConnection conn = connFactory.getIConnection();
    conn.setAutoCommit(true);
    
    try (BufferedReader reader = new BufferedReader(new FileReader("CompanyYear.csv"))) {
      // Skip header line
      reader.readLine();
      String line = reader.readLine();
      while (line != null) {
        String sql = "INSERT INTO FinanacialYear x (name, startDate, endDate) VALUES (?, SELECT MAX(y.endDate) + 1 FROM FinancialYear y WHERE y.endDate < x.endDate), ?)";
        IPreparedStatement ps = conn.prepareStatement(sql);
        
        String[] field = line.split(",");
        System.out.println(field[0] + "...." + field[1]);
        ps.setString(field[0]);
        LocalDate endDate = LocalDate.parse(field[1]);
        ps.executeUpdate();

        line = reader.readLine();
      }
    }
    conn.close();
  }
  
}
