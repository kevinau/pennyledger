package org.pennyledger.docstore.test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.docstore.IDocumentStore;

@Component(configurationPolicy=ConfigurationPolicy.IGNORE)
public class DocumentStoreTest {

  private IDocumentStore docStore;
  
  @Reference
  public void setDocumentStore (IDocumentStore docStore) {
    this.docStore = docStore;
  }
  
  public void unsetDocumentStore (IDocumentStore docStore) {
    this.docStore = null;
  }
  
  @Activate 
  public void activate () {
    List<Path> originFiles = new ArrayList<>();

    Path originDir = Paths.get("C:/Users/Kevin/Accounts/JH Shares/Telstra");
    String pattern = "*.pdf";
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(originDir, pattern)) {
       for (Path fileEntry : stream) {
        originFiles.add(fileEntry);
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    for (Path file : originFiles) {
      System.out.println(file);
      docStore.importDocument(file);
    }
  }
}
