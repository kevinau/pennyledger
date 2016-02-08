package org.pennyledger.docimport.thunderbird;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.pennyledger.nio.SafeWriter;

class MboxChangeDetector {

  private final Path mboxPath;
  private final Path markFile;
  
  long eofOffset = 0;
  long idOffset = 0;
  String idText = null;
  
  public MboxChangeDetector (Path mboxPath) {
    this.mboxPath = mboxPath;
    String fileName = mboxPath.getFileName().toString();
    markFile = mboxPath.resolveSibling(fileName + ".mark");
  }

  
  public long getNextStart() {
    long nextStart = 0;
    
    try {
      if (Files.exists(markFile)) {
        // Read the one line file that contains:
        //   - Length of the mbox file when last read.
        //   - Offset of the last Message-ID line.
        //   - The Message-ID string
        List<String> lines = Files.readAllLines(markFile);
        if (lines.size() != 1) {
          throw new RuntimeException("Mark file contains the wrong number of lines: " + markFile);
        }
        String[] segments = lines.get(0).split(" ");
        if (segments.length != 4) {
          throw new RuntimeException("Mark file contains the wrong details: " + lines.get(0));
        }
        nextStart = Long.parseLong(segments[0]);
        long idOffset = Long.parseLong(segments[1]);
        String idText = segments[2] + " " + segments[3];
        
        // Read the current Message-ID, starting at the offset.  If the current Message-ID and the prior
        // Message-ID are the same, the mbox file has not been compacted since last read.  Seek to the 
        // length of the mbox file when last read, and read from there. 
        InputStream is = new FileInputStream(mboxPath.toFile());
        is.skip(idOffset);
        byte[] buffer = new byte[idText.length()];
        int n = is.read(buffer);
        if (n != buffer.length || !Arrays.equals(idText.getBytes(), buffer)) {
          // If the Message-ID is not the same (or if eof is found or whatever), start reading the
          // mbox file from the begining.
          nextStart = 0;
        }
        is.close();
      }
    } catch (NumberFormatException ex) {
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
    return nextStart;
  }
  
  
  public void setNextStart () {
    try (FileScanner scanner = new FileScanner(mboxPath)) {
      ILineProcessor lineProcessor = new ILineProcessor() {
        @Override
        public void processLine(long lineOffset, String line) {
          if (line.startsWith("Message-ID: ")) {
            idOffset = lineOffset;
            idText = line;
          }
        }
      };
      
      scanner.walk(lineProcessor);
      eofOffset = scanner.getFileOffset();
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
    
    if (idText != null) {
      try (SafeWriter writer = new SafeWriter(markFile)) {
        writer.write(eofOffset + " " + idOffset + " " + idText);
        writer.newLine();
        writer.commit();
      } catch (IOException ex) {
        throw new UncheckedIOException(ex);
      }
    }
  }
}
