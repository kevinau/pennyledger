package org.pennyledger.html;

import java.util.ArrayList;

public class NodeWithChildren extends Node {

  public ArrayList<Node> children;

  protected NodeWithChildren(String tag) {
    super(tag);
    this.children = new ArrayList<Node>();
  }


  protected void addChild (Node node) {
    children.add(node);
  }
  
  
  @SuppressWarnings("unchecked")
  public <X extends NodeWithChildren> X addText(String text) {
    addChild(new TextNode(text));
    return (X)this;
  }



  protected void addChildren (Node... nodes) {
    for (Node node : nodes) {
      children.add(node);
    }
  }
  
  
  @Override
  protected void write(StringBuilder b) {
    super.write(b);
    if (children != null && children.size() > 0) {
      for (Node child : children) {
        child.write(b);
      }
    }
    super.writeEndTag(b);
  }

}