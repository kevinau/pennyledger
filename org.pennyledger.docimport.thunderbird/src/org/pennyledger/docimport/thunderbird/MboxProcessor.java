/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.pennyledger.docimport.thunderbird;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.ArrayList;
import java.util.List;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.dom.field.ContentDispositionField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.MboxIterator;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.stream.Field;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.pennyledger.docstore.IDocumentStore;
import org.pennyledger.nio.DirectoryWatcher.IProcessor;

/**
 * Simple example of how to use Apache Mime4j Mbox Iterator. We split one mbox
 * file file into individual email messages.
 */
public class MboxProcessor implements IProcessor {

  private final static CharsetEncoder ENCODER = Charset.forName("ISO-8859-1").newEncoder();
  
  private final IDocumentStore docStore;
  
  private final Tika tika;
  private final MimeTypes mimeTypes;
  private final MimeType applicationPDF;
  
//  private static class ShortMessage {
//    private final Date date;
//    private final String subject;
//    private final Mailbox sender;
//    private final AddressList to;
//    private final AddressList cc;
//    private final AddressList bcc;
//    private final List<String> attachments = new ArrayList<>(0);
//    
//    private boolean seen = false;
//    
//    private ShortMessage (Message message) {
//      this.date = message.getDate();
//      this.subject = message.getSubject();
//      this.sender = message.getSender();
//      this.to= message.getTo();
//      this.cc = message.getCc();
//      this.bcc = message.getBcc();
//    }
//    
//    private void addAttachmentName (String name) {
//      attachments.add(name);
//    }
//  }
  
  
  public MboxProcessor (IDocumentStore docStore) {
    this.docStore = docStore;
    
    tika = new Tika();
    mimeTypes = MimeTypes.getDefaultMimeTypes();
    try {
      applicationPDF = mimeTypes.forName("application/pdf");
    } catch (MimeTypeException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  // simple example of how to split an mbox into individual files
  public static void main(String[] args) throws Exception {
    final File mbox = new File(
//      "C:/Users/Kevin/AppData/Roaming/Thunderbird/Profiles/3mfc3db7.default/Mail/Local Folders/Inbox");
//    "C:/Users/Kevin/AppData/Roaming/Thunderbird/Profiles/zqs1fach.default/Mail/mail.internode.on.net/Inbox");
//      "C:/Users/Kevin/AppData/Roaming/Thunderbird/Profiles/zqs1fach.default/Mail/Local Folders/Accounts"); // new
      "C:/Users/Kevin/AppData/Roaming/Thunderbird/Profiles/3mfc3db7.default/Mail/Local Folders/Accounts");
    Path mboxPath = mbox.toPath();
    
    MboxProcessor mboxIterator = new MboxProcessor(null);
    mboxIterator.run(mboxPath);
  }
  
  
  void run (Path mboxPath) {
    if (!(Files.exists(mboxPath) || Files.isRegularFile(mboxPath) || Files.isReadable(mboxPath))) {
      throw new IllegalArgumentException("File not found or is not readable: " + mboxPath);
    }
    
    MboxChangeDetector mboxChange = new MboxChangeDetector(mboxPath);
    long startOffset = mboxChange.getNextStart();

    int count;
    try {
      count = 0;
      MboxIterator mboxIterator = MboxIterator.fromFile(mboxPath, startOffset).charset(ENCODER.charset()).build();
      for (CharBufferWrapper rawMessage : mboxIterator) {
        int[] pdfCount = new int[1];
        // saveMessageToFile(count, buf);
        System.out.println("Message-" + (count + 1));
        messageSummary("message-" + (count + 1), rawMessage.asInputStream(ENCODER.charset()), pdfCount);
        count++;
      }
      
      // Update mark file for this mbox
      mboxChange.setNextStart();
    } catch (MimeTypeException | IOException | MimeException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  // private static void saveMessageToFile(int count, CharBuffer buf) throws
  // IOException {
  // FileOutputStream fout = new FileOutputStream(new
  // File("target/messages/msg-" + count));
  // FileChannel fileChannel = fout.getChannel();
  // ByteBuffer buf2 = ENCODER.encode(buf);
  // fileChannel.write(buf2);
  // fileChannel.close();
  // fout.close();
  // }

  /**
   * Parse a message and return a simple {@link String} representation of some
   * important fields.
   *
   * @param messageBytes
   *          the message as {@link java.io.InputStream}
   * @return String
   * @throws IOException
   * @throws MimeException
   * @throws MimeTypeException 
   */
  private void messageSummary(String messageName, InputStream messageBytes, int[] pdfCount) throws IOException, MimeException, MimeTypeException {
    MessageBuilder builder = new DefaultMessageBuilder();
    Message message = builder.parseMessage(messageBytes);
    
//    return String.format("\nMessage %s \n" + "ID:\t%s\n" + "Multi-part:\t%s\n" + 
//                         "Body class:\t%s\n" + 
//                         "Sent:\t%s\n" + "From:\t%s\n" + "To:\t%s\n", message.getSubject(), 
//        message.getMessageId(),
//        message.isMultipart() ? "true" : "false",
//        (message.getBody().toString()),
//        message.getDate().toString(), 
//        message.getFrom(),
//        message.getTo());
    Field statusField = message.getHeader().getField("X-Mozilla-Status");
    if (statusField == null) {
      throw new RuntimeException("No X-Mozilla-Status field in " + message.getMessageId());
    }
    String status = statusField.getBody();
    if (!status.equals("0009")) {
      // Exclude deleted messages
      List<String> attachments = new ArrayList<>(0);
      findAttachments(message, attachments);
      dumpMessage(messageName, message, pdfCount);
    }
  }
  
  
  private void findAttachments (Entity entity, List<String> attachments) throws IOException, MimeTypeException {
    if (entity.isMultipart()) {
      Multipart multipart = (Multipart)entity.getBody();
      for (Entity e : multipart.getBodyParts()) {
        findAttachments (e, attachments);
      }
    } else {
      String fileName = null;
      String contentTypeName = null;
      boolean isAttachment = false;
      
      Header header = entity.getHeader();
      for (Field field : header.getFields()) {
        if (field instanceof ContentTypeField) {
          ContentTypeField ctf = (ContentTypeField)field;
          String fn = ctf.getParameter("name");
          if (fn != null) {
            contentTypeName = fn;
          }
        } else if (field instanceof ContentDispositionField) {
          ContentDispositionField cdf = (ContentDispositionField)field;
          String fn = cdf.getParameter(ContentDispositionField.PARAM_FILENAME);
          if (fn != null) {
            fileName = fn;
          }
          isAttachment = cdf.isAttachment();
        }
      }
      if (fileName == null) {
        fileName = contentTypeName;
      }
      if (isAttachment && fileName != null) {
        attachments.add(fileName);
      }
    }
  }

  
  private void dumpMessage (String label, Message message, int[] pdfCount) throws IOException, MimeTypeException {
    System.out.println(label + ": " + message.getMimeType() + " " + message.getClass().getSimpleName());
    if (message.isMultipart()) {
      Multipart multipart = (Multipart)message.getBody();
      int n = 1;
      for (Entity e : multipart.getBodyParts()) {
        dumpEntity (label + "-" + n, e, pdfCount);
        n++;
      }
    } else if (message instanceof BodyPart) {
      BodyPart bodyPart = (BodyPart)message;
      dumpBody (label, bodyPart.getBody(), pdfCount);
    } else {
      dumpBody (label, message.getBody(), pdfCount);
//    } else {
//      throw new IllegalArgumentException("Unknown message type: " + message.getClass().getSimpleName());
    }
  }
  
  
  private void dumpEntity (String label, Entity entity, int[] pdfCount) throws IOException, MimeTypeException {
    System.out.println(label + ": " + entity.getMimeType() + " " + entity.getClass().getSimpleName());
    if (entity.isMultipart()) {
      Multipart multipart = (Multipart)entity.getBody();
      int n = 1;
      for (Entity e : multipart.getBodyParts()) {
        dumpEntity (label + "-" + n, e, pdfCount);
        n++;
      }
    } else if (entity instanceof BodyPart) {
      BodyPart bodyPart = (BodyPart)entity;
      dumpBody (label, bodyPart.getBody(), pdfCount);
    }
  }

  
  public void dumpBody (String label, Body body, int[] pdfCount) throws IOException, MimeTypeException {
    InputStream is = null;
    if (body instanceof TextBody) {
      TextBody textBody = (TextBody)body;
      is = textBody.getInputStream();
    } else if (body instanceof BinaryBody) {
      BinaryBody binaryBody = (BinaryBody)body;
      is = binaryBody.getInputStream();
    } else if (body instanceof Message) {
      dumpMessage (label, (Message)body, pdfCount);
    } else {
      throw new IllegalStateException(body.getClass().getSimpleName() + " is not TextBody, BinaryBody or nested Message");
    }
    String mimeTypeName = tika.detect(is);
    MimeType mimeType = mimeTypes.getRegisteredMimeType(mimeTypeName);
    System.out.println(mimeType + "=====================" + mimeType.getType() + " " + mimeType.getName());
    if (mimeType.equals(applicationPDF)) {
      if (pdfCount[0] == 0) {
        // Ship the PDF attachment off to the document store.  Only the first PDF attachment is sent to the 
        // document store.  We probably send all attachments.
        if (docStore != null) {
          String docId = docStore.importDocument(is, mimeType);
          System.out.println("Sent PDF to document store: " + docId);
        } else {
          System.out.println("Import into document store");
        }
      }
      pdfCount[0]++;
    }
  }


  @Override
  public void process(Path path, Kind<?> kind) {
    run (path);
  }

}
