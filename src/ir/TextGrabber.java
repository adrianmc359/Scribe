package ir;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import org.apache.lucene.document.*;
import java.util.Properties;
/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

public class TextGrabber extends Thread
{
      JTextPane pane;
      Document[] doc=new Document[10];
      QueryFormer2 qf;
      Properties mailprop;
      public TextGrabber(JTextPane pane,Properties mailprop)
      {
            this.pane=pane;
            this.mailprop=mailprop;
            qf =new QueryFormer2(mailprop);
      }
      public void start()
      {
                        getText();
      }
      public synchronized void getText()
      {
            doc=qf.Start(pane.getText());
      }
      public  Document[] getDocuments()
      {
            return doc;
      }


}