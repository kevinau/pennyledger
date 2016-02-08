package org.pennyledger.docimport.thunderbird;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileScanner implements AutoCloseable {
  
  public static void main(String[] args) throws Exception {
    final File file = new File(
//      "C:/Users/Kevin/AppData/Roaming/Thunderbird/Profiles/3mfc3db7.default/Mail/Local Folders/Inbox");
//      "C:/Users/Kevin/AppData/Roaming/Thunderbird/Profiles/zqs1fach.default/Mail/mail.internode.on.net/Inbox");
      "C:/Users/Kevin/AppData/Roaming/Thunderbird/Profiles/zqs1fach.default/Mail/Local Folders/Accounts"); // new

    try (FileScanner scanner = new FileScanner(file.toPath())) {
      System.out.println("File length " + file.length());
      long start = System.nanoTime();
      
      ILineProcessor lineProcessor = new ILineProcessor() {
        @Override
        public void processLine(long lineOffset, String line) {
          if (line.startsWith("Message-ID: ")) {
            System.out.println(lineOffset + ": " + line);
          }
        }
      };
      
      scanner.walk(lineProcessor);
      System.out.println("File length " + file.length());
      long end = System.nanoTime();
      System.out.println("Time: " + (end - start) / 1e9);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  
  private static final int BUFFER_SIZE = 4096;
  private static final Charset CHARSET = StandardCharsets.ISO_8859_1;
  
  private final SeekableByteChannel byteChannel;

  private long fileOffset = 0;
  
  
  public FileScanner (Path path) {
    try {
      byteChannel = Files.newByteChannel(path);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  
  public void walk (ILineProcessor lineProcessor) throws IOException {
    ByteBuffer chunk = ByteBuffer.allocate(BUFFER_SIZE);
    boolean skipNL = false;
    int unprocessed = 0;
    String line = null;
    long lineOffset = 0;
    
    int n = byteChannel.read(chunk);
    while (n != -1) {
      chunk.flip();
      int start = chunk.position();
      while (chunk.hasRemaining()) {
        // Look for \r and \n in the chunk
        byte b = chunk.get();
        if (b == (byte)'\r') {
          int m = chunk.position() - start - 1;
          chunk.position(start);
          byte[] lineBytes = new byte[m];
          chunk.get(lineBytes, 0, m);
          if (line == null) {
            line = new String(lineBytes, CHARSET);
            lineOffset = fileOffset;
          } else {
            line += new String(lineBytes, CHARSET);
          }
          lineProcessor.processLine(lineOffset, line);
          fileOffset += m + 1;
          chunk.get();            // Step over the \r again
          start = chunk.position();
          line = null;
          unprocessed = 0;
          skipNL = true;
        } else if (b == (byte)'\n') {
          if (skipNL) {
            start = chunk.position();
            fileOffset++;
            skipNL = false;
          } else {
            int m = chunk.position() - start - 1;
            chunk.position(start);
            byte[] lineBytes = new byte[m];
            chunk.get(lineBytes, 0, m);
            if (line == null) {
              line = new String(lineBytes, CHARSET);
              lineOffset = fileOffset;
            } else {
              line += new String(lineBytes, CHARSET);
            }
            lineProcessor.processLine(lineOffset, line);
            fileOffset += m + 1;
            chunk.get();            // Step over the \n again
            start = chunk.position();
            line = null;
            unprocessed = 0;
          }
        } else {
          unprocessed++;
        }
      }
      chunk.position(start);
      byte[] lineBytes = new byte[unprocessed];
      chunk.get(lineBytes, 0, unprocessed);
      if (line == null) {
        line = new String(lineBytes, CHARSET);
        lineOffset = fileOffset;
      } else {
        line += new String(lineBytes, CHARSET);
      }
      fileOffset += unprocessed;
      unprocessed = 0;
      chunk.clear();
      n = byteChannel.read(chunk);
    }
    if (line != null) {
      // There is a final line without a terminating \r or \n.
      lineProcessor.processLine(lineOffset, line);
    }
  }

    
  @Override
  public void close() {
    try {
      byteChannel.close();
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
  
  
  public long getFileOffset () {
    return fileOffset;
  }
  
}
