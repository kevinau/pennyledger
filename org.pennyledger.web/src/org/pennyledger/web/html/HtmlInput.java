package org.pennyledger.web.html;



public class HtmlInput extends HtmlElement {

  private final String name;
  private final String type;
  
  private int size;
  private int maxLength;
  private boolean readOnly;
  private Boolean spellCheck;
  private String value;
  
  
  public HtmlInput (HtmlElement parentNode, String type, String name) {
    super (parentNode, "input", false);
    this.type = type;
    this.name = name;
    setId(name);
  }

  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "type", type);
    appendAttribute (builder, "name", name);
    appendAttribute (builder, "size", size);
    if (maxLength != 0) {
      appendAttribute (builder, "maxlength", maxLength);
    }
    appendAttribute (builder, "readonly", readOnly);
    appendAttribute (builder, "value", value);
    if (spellCheck != null) {
      appendAttribute (builder, "spellcheck", spellCheck.toString());
    }
  }


  public void setSize(int size) {
    this.size = size;
  }
  
  
  public void setMaxLength (int maxLength) {
    this.maxLength = maxLength;
  }


  public void setReadOnly (boolean readOnly) {
    this.readOnly = readOnly;
  }

  
  public void setValue (String value) {
    this.value = value;
  }
  
  
  public void setSpellCheck (boolean spellCheck) {
    this.spellCheck = spellCheck;
  }
  
}
