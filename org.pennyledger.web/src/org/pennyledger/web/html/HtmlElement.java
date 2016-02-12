package org.pennyledger.web.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HtmlElement extends HtmlNode {

  private final String tag;
  private final boolean isEndTag;
  private final List<HtmlNode> nodes = new ArrayList<HtmlNode>();
  private String klass;
  private String id;
  private String style;
  private String title;
  private Map<String, String> eventHandlers = null;
  

  protected HtmlElement (HtmlElement parentNode, String tag) {
    this.tag = tag;
    this.isEndTag = true;
    if (parentNode != null) {
      parentNode.add(this);
    }
  }
  
  
  protected HtmlElement (HtmlElement parentNode, String tag, boolean isEndTag) {
    this.tag = tag;
    this.isEndTag = isEndTag;
    if (parentNode != null) {
      parentNode.add(this);
    }
  }
  
  
  public void setId (String id) {
    this.id = id;
  }
  
  
  public void setClass (String klass) {
    this.klass = klass;
  }
  
  
  public void setStyle (String style) {
    this.style = style;
  }
  
  
  public void setTitle (String title) {
    this.title = title;
  }
  
  
  public void setEventHandler (String name, String script) {
    if (eventHandlers == null) {
      eventHandlers = new HashMap<String, String>();
    }
    eventHandlers.put(name, script);
  }
  
  
  protected void add(HtmlNode node) {
    nodes.add(node);
  }
  
  
  public void addNbsp() {
    nodes.add(new HtmlText());
  }
  
  
  public void addLiteral(String text) {
    nodes.add(new HtmlText(text, true));
  }
  
  
  public void addText(String text) {
    nodes.add(new HtmlText(text));
  }
  
  
  public void addText(Object obj) {
    nodes.add(new HtmlText(obj.toString()));
  }
  
  
  protected static void appendAttribute (StringBuilder builder, String name, Object value) {
    if (value != null) {
      builder.append(' ');
      builder.append(name);
      builder.append('=');
      builder.append('\"');
      builder.append(value.toString());
      builder.append('\"');
    }
  }
  
  
  protected static void appendAttribute (StringBuilder builder, String name, Integer value) {
    if (value != null) {
      builder.append(' ');
      builder.append(name);
      builder.append('=');
      builder.append('\"');
      builder.append(value.toString());
      builder.append('\"');
    }
  }
  
  
  protected static void appendAttribute (StringBuilder builder, String name, boolean value) {
    if (value) {
      builder.append(' ');
      builder.append(name);
      builder.append('=');
      builder.append('\"');
      builder.append(name);
      builder.append('\"');
    }
  }
  
  
  @Override
  public void buildString (StringBuilder builder, int level, boolean pack) {
    boolean content = false;
    for (HtmlNode node : nodes) {
      if (!(node instanceof HtmlText)) {
        content = true;
        break;
      }
    }

    if (!pack) {
      for (int i = 0; i < level; i++) {
        builder.append("  ");
      }
    }
    builder.append('<');
    builder.append(tag);
    appendAttributes (builder);
    if (eventHandlers != null) {
      for (Map.Entry<String, String> entry : eventHandlers.entrySet()) {
        appendAttribute(builder, entry.getKey(), entry.getValue());
      }
    }
    builder.append('>');
    boolean indentEndTag = true;

    if (content == false) {
      indentEndTag = false;
      for (HtmlNode node : nodes) {
        node.buildString(builder, level + 1, false);
      }
    } else {
      if (!pack) {
        builder.append("\r\n");
      }
      for (HtmlNode node : nodes) {
        node.buildString(builder, level + 1, pack);
      }
    }
//    if (!pack) {
//      builder.append("\r\n");
//    }
//    for (HtmlNode node : nodes) {
//      node.buildString(builder, level + 1, pack);
//    }

    if (isEndTag) {
      if (indentEndTag && !pack) {
        for (int i = 0; i < level; i++) {
          builder.append("  ");
        }
      }
      builder.append('<');
      builder.append('/');
      builder.append(tag);
      builder.append('>');
    }
    if (!pack) {
      builder.append("\r\n");
    }
  }
  
  
  @Override 
  public void buildString (StringBuilder builder) {
    buildString (builder, 0, false);
  }
  
  
  protected void buildTopLevelString (StringBuilder builder) {
    for (HtmlNode node : nodes) {
      node.buildString(builder, 0, false);
    }
  }
  
  
  protected void appendAttributes (StringBuilder builder) {
    appendAttribute (builder, "id", id);
    appendAttribute (builder, "class", klass);
    appendAttribute (builder, "style", style);
    appendAttribute (builder, "title", title);
  }
  
}
