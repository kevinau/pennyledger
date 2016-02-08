package org.pennyledger.docimport.folder;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.regex.Pattern;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.nio.DirectoryWatcher;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;
import org.pennyledger.value.ExistingDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service=FolderWatchService.class, configurationPolicy=ConfigurationPolicy.OPTIONAL, immediate=true)
public class FolderWatchService {

  private static final Logger logger = LoggerFactory.getLogger(FolderWatchService.class);
  
  @Configurable
  private ExistingDirectory watchDir = new ExistingDirectory(System.getProperty("user.home"), "Download-accounts");

  @Configurable
  private Pattern pattern = Pattern.compile(".");
  
  private DirectoryWatcher watchService;
  
  @Activate
  protected void activate (ComponentContext context) {
    ComponentConfiguration.load(this, context);
    
    logger.info("Watching {} for new files (for importing into the document store)", watchDir);

    // Create a processor to import the documents found in the download directory
    DirectoryWatcher.IProcessor processor = new DirectoryWatcher.IProcessor() {
      @Override
      public void process(Path path, Kind<?> kind) {
        System.out.println("Found... " + kind + " " + path);
      }      
    };
    
    watchService = new DirectoryWatcher(watchDir, pattern, processor);
    watchService.registerDirectory(watchDir);
    
    // Start watching the directory in a separate thread.
    new Thread() {
      @Override
      public void run() {
        watchService.start();
      }
    }.start();
    
    // Fire off the processor for existing files
    watchService.queueExistingFiles();
  }
  
  
  @Deactivate
  protected void deactivate () {
    watchService.close();
    logger.info("No longer watching {} for new files", watchDir);
  }
  
}
