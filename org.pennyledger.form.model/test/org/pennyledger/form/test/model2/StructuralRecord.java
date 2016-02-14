package org.pennyledger.form.test.model2;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.ManyToOne;


/** 
 * A comprehensive class to test all the different model types.
 *
 */
public class StructuralRecord extends BaseRecord {

  private Integer field1;
  
  private String[] field2;
  
  @ManyToOne
  private ReferencedRecord referenced;

  @Embedded
  private EmbeddedRecord embedded;

  private ArrayElemRecord[] array;
  
  private List<ListElemRecord> list;
  
  public Integer getField1() {
    return field1;
  }

  public String[] getField2() {
    return field2;
  }

  public ReferencedRecord getReferenced() {
    return referenced;
  }

  public EmbeddedRecord getEmbedded() {
    return embedded;
  }
  
  public ArrayElemRecord[] getArray() {
    return array;
  }

  public List<ListElemRecord> getList() {
    return list;
  }

  public void setList(List<ListElemRecord> list) {
    this.list = list;
  }
  
}
