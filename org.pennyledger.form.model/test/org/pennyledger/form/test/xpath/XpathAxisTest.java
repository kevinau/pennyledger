package org.pennyledger.form.test.xpath;

import java.util.List;

import javax.persistence.Embeddable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.FormXPath;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

@SuppressWarnings("unused")
public class XpathAxisTest {
  
  @Embeddable
  private static class Author {
    private String last;
    private String first;
  }
  
  @Embeddable
  private static class Article {
    private Author author = new Author();
  }
  
  @Embeddable
  private static class Journal {
    private Article article1 = new Article();
    private Article article2 = new Article();
    private Article article3 = new Article();
    
  }
  private static class Library {
    private Journal journal1 = new Journal();
    private Journal journal2 = new Journal();
    private Journal journal3 = new Journal();
  }
  
  
  private IFormModel<Library> formModel;
  private IObjectModel context;
  
  @Before
  public void setup () {
    Library instance = new Library();
    formModel = new FormModel<Library>(instance);
    IGroupModel groupModel = (IGroupModel)formModel.getRootModel();
    IGroupModel journalModel = (IGroupModel)groupModel.getMember("journal2");
    context = journalModel.getMember("article2");
  }

  
  @Test
  public void testSetup () throws Exception {
    Assert.assertNotNull(context);
    Assert.assertEquals("article2", context.getPathName());
    
    FormXPath path = new FormXPath(".");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(1, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("article2", m.getPathName());
  }

  
  @Test
  @SuppressWarnings("unchecked")
  public void branchTest1 () throws Exception {
    FormXPath path = new FormXPath("descendant-or-self::journal1");
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(formModel);
    Assert.assertEquals(1, list.size());
    Assert.assertEquals("journal1", list.get(0).getPathName());

    path = new FormXPath("descendant-or-self::article1");
    list = (List<? extends IObjectModel>)path.evaluate(formModel);
    Assert.assertEquals(3, list.size());
    Assert.assertEquals("article1", list.get(0).getPathName());
    Assert.assertEquals("journal1", list.get(0).getParent().getPathName());
  }
  
  
  @Test
  @SuppressWarnings("unchecked")
  public void branchTest2 () throws Exception {
    FormXPath path = new FormXPath("//journal1");
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(formModel);
    Assert.assertEquals(1, list.size());
    Assert.assertEquals("journal1", list.get(0).getPathName());

    path = new FormXPath("//article1");
    list = (List<? extends IObjectModel>)path.evaluate(formModel);
    Assert.assertEquals(3, list.size());
    Assert.assertEquals("article1", list.get(0).getPathName());
    Assert.assertEquals("journal1", list.get(0).getParent().getPathName());
  }
  
  
  @Test
  public void branchTest11 () throws Exception {
    FormXPath path = new FormXPath("descendant-or-self::first");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(formModel);
    Assert.assertEquals(9, list.size());
    Assert.assertEquals("first", list.get(0).getPathName());
  }
  
  
  @Test
  public void branchTest21 () throws Exception {
    FormXPath path = new FormXPath("//first");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(formModel);
    Assert.assertEquals(9, list.size());
    Assert.assertEquals("first", list.get(0).getPathName());
  }
  
  
  @Test
  public void testSelfAxis () throws Exception {
    FormXPath path = new FormXPath("self::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(1, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("article2", m.getPathName());
  }
  
  
  @Test
  public void testChildAxis () throws Exception {
    FormXPath path = new FormXPath("child::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(1, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("author", m.getPathName());
  }
  
  
  @Test
  public void testDescendentAxis () throws Exception {
    FormXPath path = new FormXPath("descendant::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(3, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("author", m.getPathName());
    m = list.get(1);
    Assert.assertEquals("last", m.getPathName());
  }
  
  
  @Test
  public void testParentAxis () throws Exception {
    FormXPath path = new FormXPath("parent::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(1, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("journal2", m.getPathName());
  }
  
  
  @Test
  public void testAncestorAxis () throws Exception {
    FormXPath path = new FormXPath("ancestor::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(2, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("form", m.getPathName());
    m = list.get(1);
    Assert.assertEquals("journal2", m.getPathName());
  }
  
  
  @Test
  public void testFollowingSiblingAxis () throws Exception {
    FormXPath path = new FormXPath("following-sibling::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(1, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("article3", m.getPathName());
  }
  
  
  @Test
  public void testPrecedingSiblingAxis () throws Exception {
    FormXPath path = new FormXPath("preceding-sibling::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(1, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("article1", m.getPathName());
  }
  
  
  @Test
  public void testFollowingAxis () throws Exception {
    FormXPath path = new FormXPath("following::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(17, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("article3", m.getPathName());
    m = list.get(5);
    Assert.assertEquals("article1", m.getPathName());
    m = list.get(8);
    Assert.assertEquals("first", m.getPathName());
  }
  
  
  @Test
  public void testPrecedingAxis () throws Exception {
    FormXPath path = new FormXPath("preceding::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(17, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("journal1", m.getPathName());
    m = list.get(5);
    Assert.assertEquals("article2", m.getPathName());
    m = list.get(8);
    Assert.assertEquals("first", m.getPathName());
  }
  
  
  @Test
  public void testDescendentOrSelfAxis () throws Exception {
    FormXPath path = new FormXPath("descendant-or-self::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(4, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("article2", m.getPathName());
    m = list.get(1);
    Assert.assertEquals("author", m.getPathName());
    m = list.get(2);
    Assert.assertEquals("last", m.getPathName());
  }
  
  @Test
  public void testAncestorOrSelfAxis () throws Exception {
    FormXPath path = new FormXPath("ancestor-or-self::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(3, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("form", m.getPathName());
    m = list.get(1);
    Assert.assertEquals("journal2", m.getPathName());
    m = list.get(2);
    Assert.assertEquals("article2", m.getPathName());
  }
  
  
  @Test
  public void testAttributeAxis () throws Exception {
    FormXPath path = new FormXPath("attribute::*");
    @SuppressWarnings("unchecked")
    List<? extends IObjectModel> list = (List<? extends IObjectModel>)path.evaluate(context);
    Assert.assertEquals(0, list.size());
  }

  
  @Test
  public void testFormGetModels () {
    List<IObjectModel> list = formModel.selectObjectModels("/form/journal2/article2//*");
    Assert.assertEquals(3, list.size());
    IObjectModel m = list.get(0);
    Assert.assertEquals("author", m.getPathName());
    m = list.get(1);
    Assert.assertEquals("last", m.getPathName());
  }
  
  @Test
  public void testFormGetFieldModel () {
    IFieldModel fieldModel = formModel.selectFieldModel("/form/journal2/article2/author/first");
    Assert.assertEquals("first", fieldModel.getPathName());
  }
  
  @Test(expected=RuntimeException.class)
  public void testFormGetFieldModel2 () {
    IFieldModel fieldModel = formModel.selectFieldModel("/form/journal2/article2/author/*");
    Assert.fail();
  }
  
  @Test(expected=RuntimeException.class)
  public void testFormGetFieldModel3 () {
    IFieldModel fieldModel = formModel.selectFieldModel("/form/journal2/article2/author");
    Assert.fail();
  }
  
}
