package org.pennyledger.form.test.xpath;

import java.util.List;

import javax.persistence.Embeddable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.FormXPath;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

@SuppressWarnings("unused")
public class XpathArrayTest {
  
  @Embeddable
  private static class Book {
    private String[] authors = {
        "alfred",
        "bob",
        "charles",
        "doug",
    };
  }

  
  private IFormModel<Book> formModel;
  private IObjectModel rootModel;
  
  @Before
  public void setup () {
    Book instance = new Book();
    formModel = new FormModel<Book>(instance);
    rootModel = (IGroupModel)formModel.getRootModel();
  }

  
  @Test
  @SuppressWarnings("unchecked")
  public void elementsTest1 () throws Exception {
    FormXPath path = new FormXPath("authors");
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(rootModel);
    Assert.assertEquals(1, list.size());
    Assert.assertEquals("authors", list.get(0).getPathName());
  }
  
  
  @Test
  @SuppressWarnings("unchecked")
  public void elementsTest2 () throws Exception {
    FormXPath path = new FormXPath("authors/*");
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(rootModel);
    Assert.assertEquals(4, list.size());
    Assert.assertEquals("0", list.get(0).getPathName());
    Assert.assertEquals("authors", list.get(0).getParent().getPathName());
  }
  
  
  @Test
  @SuppressWarnings("unchecked")
  public void elementsTest3 () throws Exception {
    FormXPath path = new FormXPath("authors/*[3]");
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(rootModel);
    Assert.assertEquals(1, list.size());
    Assert.assertEquals("2", list.get(0).getPathName());
    Assert.assertEquals("authors", list.get(0).getParent().getPathName());
  }
  
  
}
