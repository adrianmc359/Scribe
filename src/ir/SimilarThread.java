package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An Email Client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 1.0
 */
import java.util.Date;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import ir.QueryFormer;
import java.util.Properties;
import org.apache.lucene.document.Document;
import ir.TextGrabber;
import java.io.*;
import javax.mail.*;

public class SimilarThread extends Thread
{
        String query;
        Document[] doc = new Document[10];
        QueryFormer qf;
        Properties mailprop;

        public SimilarThread()
        {
        }
        public void setValues(String query,Properties mailprop)
        {
                this.query=query;
                this.mailprop=mailprop;
        }

        public synchronized void start()
        {
                qf = new QueryFormer(mailprop);
                getText();
        }
        public synchronized void getText()
       {
                       doc = qf.Start(query);
       }
       public Document[] getDocuments()
        {
                return doc;
        }




}