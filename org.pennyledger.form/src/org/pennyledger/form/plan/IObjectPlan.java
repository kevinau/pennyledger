package org.pennyledger.form.plan;

import org.pennyledger.form.EntryMode;

/**
 * The detail of a class field. The plan contains sufficient detail about a
 * class so it can be used as the basis of a data entry form or a persistent
 * entity.
 * <p>
 * The detail here is determined solely from the class field or annotations on
 * the field. It contains no field values or other runtime information. Object
 * plans can be cached.
 * 
 * @author Kevin Holloway
 * 
 */
public interface IObjectPlan {

  public default void dump () {
    dump (0);
  }

  public void dump (int level);

  /**
   * Get the label of this field. The label is a short, human readable
   * description of this field, suitable as a label on a data entry form.
   * Typically it is the sentence form of the field name (that is, the camel
   * case field name converted to a sentence), but it can be anything else.
   * <p>
   * The label is static in the sense that it is determined by the entity class
   * and does not change with different entity values.
   */
  public String getDeclaredLabel();

  public EntryMode getDeclaredMode();

  /**
   * Get the name of this field. This is the unqualified name of this field
   * within the class.
   */
  public String getName();

  public IObjectPlan getParent();
  
  public default void indent (int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
  }

  /**
   * Can this field take a <code>null</code> value and still be valid.
   * <p>
   * Note that is attribute is similar to the @Nullable/@NotNull annotations,
   * except that:
   * <ul>
   * <li>While an instance of the field is being constructed the field can be
   * <code>null</code></li>
   * <li>If a field is not nullable, and there is no entered value, the field is
   * shown as an error.</li>
   * </ul>
   */
  public boolean isOptional();

  public PlanKind kind();
  
}
