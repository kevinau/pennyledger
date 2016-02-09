package org.pennyledger.docstore.impl;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tika.mime.MimeType;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.pennyledger.docstore.IDocumentContents;
import org.pennyledger.docstore.IDocumentStore;
import org.pennyledger.nio.SafeOutputStream;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;
import org.pennyledger.util.MD5HashFactory;

import com.objectplanet.image.PngEncoder;

@Component(configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FileSystemDocumentStore implements IDocumentStore {

  @Configurable
  private Path baseDir = Paths.get(System.getProperty("user.home"), "PennyLedger");

  private Path sourceDir;
  private Path imagesDir;
  private Path contentsDir;
  
  @Activate
  public void activate(ComponentContext context) {
    ComponentConfiguration.load(this, context);

    try {
      sourceDir = baseDir.resolve("source");
      Files.createDirectories(sourceDir);
      imagesDir = baseDir.resolve("images");
      Files.createDirectories(imagesDir);
      contentsDir = baseDir.resolve("contents");
      Files.createDirectories(contentsDir);
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
        throw new IllegalArgumentException("Duplicate source files with id: " + id + " in " + sourceDir);
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


  @Override 
  public Path getContentsPath (String id) {
    return contentsDir.resolve(id + ".contents");
  }
  

  @Override 
  public Path getViewImagePath (String id) {
    return imagesDir.resolve(id + ".png");
  }
  

  @Override
  public void saveViewImage (String id, BufferedImage image) {
    Path imageFile = getViewImagePath(id);
    PngEncoder pngEncoder = new PngEncoder(PngEncoder.COLOR_TRUECOLOR);
    try (FileOutputStream imageOut = new FileOutputStream(imageFile.toFile())) {
      pngEncoder.encode(image, imageOut);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public IDocumentContents getContents(String id) {
    Path contentsPath = getContentsPath(id);
    return IDocumentContents.load(contentsPath);
  }


  @Override
  public Path getViewHTMLPath(String id) {
    Path htmlDir = baseDir.resolve("html");
    try {
      Files.createDirectories(htmlDir);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
    return htmlDir.resolve(id + ".html");
  }  

}
