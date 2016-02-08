package org.pennyledger.docimport.thunderbird;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent.Kind;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.docstore.IDocumentStore;
import org.pennyledger.nio.DirectoryWatcher;
import org.pennyledger.nio.DirectoryWatcher.IProcessor;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = AccountsMboxFinder.class, configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true)
public class AccountsMboxFinder implements IProcessor {

  private static final Logger logger = LoggerFactory.getLogger(AccountsMboxFinder.class);

  @Configurable
  private Path thunderbirdProfilesDir = Paths.get(System.getProperty("user.home"),
      "AppData/Roaming/Thunderbird/Profiles");

  private Pattern pattern = Pattern.compile("^Accounts$");

  private DirectoryWatcher watchService;
  private IDocumentStore docStore;

  @Reference
  protected void setDocumentStore(IDocumentStore docStore) {
    this.docStore = docStore;
  }

  protected void unsetDocumentStore(IDocumentStore docStore) {
    this.docStore = null;
  }

  @Activate
  protected void activate(ComponentContext context) {
    ComponentConfiguration.load(this, context);

    watchService = new DirectoryWatcher(thunderbirdProfilesDir, pattern, this);
    findWithinThunderbirdProfile(thunderbirdProfilesDir);

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
  protected void deactivate() {
    watchService.close();
  }

  private void findWithinThunderbirdProfile(Path parent) {
    String[] childNames = parent.toFile().list();
    for (String childName : childNames) {
      Path path = parent.resolve(childName);
      if (Files.isDirectory(path) && Files.isReadable(path)) {
        if (childName.equals("Mail") || childName.equals("ImapMail")) {
          // Setup directory watchers for these directories
          findWithinMailAndImap(path);
        } else {
          findWithinThunderbirdProfile(path);
        }
      }
    }
  }

  private void findWithinMailAndImap(Path path) {
    // We register the path to the service. We watch for creation and modify
    // events
    String[] childNames = path.toFile().list();
    for (String childName : childNames) {
      Matcher matcher = pattern.matcher(childName);
      if (matcher.find()) {
        logger.info("Watching {} for changed mbox files (for importing into the document store)", path);
        watchService.registerDirectory(path);
      } else {
        Path childPath = path.resolve(childName);
        if (Files.isDirectory(childPath) && Files.isReadable(childPath)) {
          findWithinMailAndImap(childPath);
        }
      }
    }
  }

  @Override
  public void process(Path path, Kind<?> kind) {
    String fileName = path.getFileName().toString();
    if (fileName.equalsIgnoreCase("accounts")) {
      MboxProcessor mboxProcessor = new MboxProcessor(docStore);
      mboxProcessor.run(path);
    }
  }

  // public static void main (String[] args) {
  // AccountsMboxFinder finder = new AccountsMboxFinder();
  // finder.activate();
  // }
}
