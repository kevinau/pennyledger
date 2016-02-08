package org.pennyledger.docstore.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.mime.MimeType;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.pennyledger.docstore.IDocumentStore;
import org.pennyledger.nio.SafeOutputStream;
import org.pennyledger.util.MD5HashFactory;

@Component(configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FileSystemDocumentStore implements IDocumentStore {

  private Path sourceDir;

  @Activate
  public void activate(BundleContext context) {
    Path basePath;
    
    String baseName = context.getProperty("BaseDir");
    if (baseName == null) {
      baseName = System.getProperty("user.home");
      basePath = Paths.get(baseName, "PennyLedger");
    } else {
      basePath = Paths.get(baseName);
    }
    try {
      sourceDir = basePath.resolve("source");
      Files.createDirectories(sourceDir);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  @Override 
  public Path getSourcePath (String id) {
    String pattern = id + ".*";
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, pattern)) {
      Path sourcePath = null;
      int n = 0;
      for (Path fileEntry : stream) {
        sourcePath = fileEntry;
        n++;
      }
      if (n == 0) {
        throw new IllegalArgumentException("No source file with id: " + id);
      } else if (n > 1) {
        throw new IllegalArgumentException("Duplicate source files with id: " + id);
      }
      return sourcePath;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

  }

  @Override
  public String importDocument(Path path) {
    String pathName = path.toString();
    int n = pathName.lastIndexOf('.');
    if (n == -1) {
      throw new IllegalArgumentException("Source path with no extension (ie no type)");
    }
    String extn = pathName.substring(n);
    
    String id = new MD5HashFactory().getFileDigest(path).toString();
    Path newSourcePath = sourceDir.resolve(id + extn);
    
    if (Files.exists(newSourcePath)) {
      // No need to copy.  The file already exists.  The file is uniquely named by it's MD5 hash, 
      // so if it exists under that name, it exists and is current.  Size and timestamp do not 
      // need to be checked.
    } else {
      // Copy the file
      try (SafeOutputStream targetOutputStream = new SafeOutputStream(newSourcePath)) {
        Files.copy(path, targetOutputStream);
        targetOutputStream.commit();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    return id;
  }

  @Override
  public String importDocument(InputStream is, MimeType mimeType) {
    String id = new MD5HashFactory().getInputStreamDigest(is).toString();
    
    String extn = mimeType.getExtension();
    Path newSourcePath = sourceDir.resolve(id + extn);
    
    if (Files.exists(newSourcePath)) {
      // No need to copy.  The file already exists.  The file is uniquely named by it's MD5 hash, 
      // so if it exists under that name, it exists and is current.  Size and timestamp do not 
      // need to be checked.
    } else {
      // Copy the file
      try (SafeOutputStream targetOutputStream = new SafeOutputStream(newSourcePath)) {
        byte[] buffer = new byte[4096];
        int n = is.read(buffer);
        while (n > 0) {
          targetOutputStream.write(buffer, 0, n);
          n = is.read(buffer);
        }
        targetOutputStream.commit();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    return id;
  }
  
}
