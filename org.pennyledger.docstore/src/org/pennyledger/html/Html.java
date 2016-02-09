package org.pennyledger.html;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import org.pennyledger.docstore.parser.impl.SafeOutputStream;

public class Html extends NodeWithChildren {

  public Html() {
    super("html");
  }


  public Head newHead() {
    Head head = new Head();
    super.addChild(head);
    return head;
  }


  public Body newBody() {
    Body body = new Body();
    super.addChild(body);
    return body;
  }


  public void write (Path path) {
    StringBuilder b = new StringBuilder();
    write (b);
    try (OutputStream outputStream = new SafeOutputStream(path)) {
      outputStream.write(b.toString().getBytes(), 0, b.length());
      outputStream.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
