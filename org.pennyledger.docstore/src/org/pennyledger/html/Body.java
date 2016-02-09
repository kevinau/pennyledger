package org.pennyledger.html;

public class Body extends NodeWithChildren {

  Body() {
    super("body");
  }


  public Body add(Node child) {
    super.addChild(child);
    return this;
  }


  public Body add(Node... children) {
    super.addChildren(children);
    return this;
  }

  
  public Img newImg (String fileName) {
    Img img = new Img();
    super.addChild(img);
    img.setAttribute("src", fileName);
    return img;
  }

  
  public Div newDiv() {
    Div div = new Div();
    super.addChild(div);
    return div;
  }
}
