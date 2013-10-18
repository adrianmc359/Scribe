package emailclient.gui;

import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import emailclient.*;

import org.apache.lucene.document.Document;
import ir.TextGrabber;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

public class NewMess extends JFrame
{
        private MailClient1 mailman;
        MimeMessage remail;
        int status = 0;
        JPanel contentPane;
        JPanel Scr = new JPanel();
        JPanel mess = new JPanel();
        JTextPane Contents = new JTextPane();
        JTabbedPane jTabbedPane1 = new JTabbedPane();
        JTable jTable3;
        Timer time;
        Document[] doc;
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JTextField jSubjectField = new JTextField();
        JLabel jLabel3 = new JLabel();
        JTextField jAddressField = new JTextField();
        JScrollPane jScrollPane1 = new JScrollPane();
        JTextPane jContents = new JTextPane();
        TableColumn tc;
        TextGrabber tg;
        JButton jSend = new JButton();
        JSplitPane jSplitPane1 = new JSplitPane();
        JScrollPane js0 = new JScrollPane();
        JScrollPane jt0 = new JScrollPane();
        Object[][] rows3 = new Object[0][4];
        Object[] names ={"Sentby", "Subject", "Date", "rank"};

        ErrorDialog errorpane;
        BorderLayout borderLayout2 = new BorderLayout();
        BorderLayout borderLayout1 = new BorderLayout();

        public NewMess(MailClient1 mailman, ErrorDialog errorpane)
        {
                enableEvents(AWTEvent.WINDOW_EVENT_MASK);
                try
                {
                        this.mailman = mailman;
                        this.errorpane = errorpane;
                        jLabel1.setText("         New Message");
                        status = 1;
                        tg = new TextGrabber(jContents, mailman.getProperties());
                        jbInit();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }

        }

        public NewMess(MailClient1 mailman, MimeMessage em, ErrorDialog errorpane)
        {
                enableEvents(AWTEvent.WINDOW_EVENT_MASK);
                try
                {
                        this.mailman = mailman;
                        this.remail = em;
                        this.errorpane = errorpane;
                        jLabel1.setText("         Reply to " + em.getFrom()[0]);
                        status = 2;
                        tg = new TextGrabber(jContents, mailman.getProperties());
                        jbInit();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }

        }

        protected void processWindowEvent(WindowEvent e)
        {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING)
                {
                        time.stop();
                        jMenuFileExit_actionPerformed(null);

                }
        }

        private void jbInit() throws Exception
        {

                contentPane = (JPanel)this.getContentPane();
                contentPane.setLayout(borderLayout1);
                jLabel1.setBounds(new Rectangle(6, 0, 795, 17));
                jTable3 = createJTable(rows3, names);
                jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
                jLabel2.setText("To");
                jLabel2.setBounds(new Rectangle(35, 20, 53, 17));
                jLabel3.setText("Subject");
                jLabel3.setBounds(new Rectangle(23, 46, 53, 17));
                this.setResizable(false);
                this.setTitle("New Email Message");
                jSend.setBounds(new Rectangle(646, 20, 99, 37));
                jSend.setToolTipText("");
                jSend.setText("Send");
                Scr.setLayout(borderLayout2);
                mess.setLayout(null);
                jSplitPane1.setDividerLocation(200);
                jAddressField.setBorder(BorderFactory.createLoweredBevelBorder());
                jAddressField.setBounds(new Rectangle(100, 18, 182, 21));
                jSubjectField.setBorder(BorderFactory.createLoweredBevelBorder());
                jSubjectField.setBounds(new Rectangle(100, 43, 183, 26));
                jScrollPane1.setOpaque(false);
                jScrollPane1.setBounds(new Rectangle( -3, 74, 800, 388));
                jt0.setOpaque(false);
                mess.add(jLabel1, null);
                mess.add(jLabel2, null);
                mess.add(jSubjectField, null);
                mess.add(jLabel3, null);
                mess.add(jSend, null);
                mess.add(jAddressField, null);
                mess.add(jScrollPane1, null);
                jScrollPane1.getViewport().add(jContents);
                jt0.getViewport().add(Contents);
                js0.getViewport().add(jTable3);
                jSplitPane1.add(jt0, JSplitPane.BOTTOM);
                jSplitPane1.add(js0, JSplitPane.TOP);
                jTabbedPane1.add(mess, "New Message");
                Scr.add(jSplitPane1, BorderLayout.NORTH);
                jTabbedPane1.add(Scr, "Scribe");
                contentPane.add(jTabbedPane1, BorderLayout.NORTH);
                tc =jTable3.getColumnModel().getColumn(3);
                tc.setCellRenderer(new MyRenderer());
                this.setSize(800, 600);
                time = new Timer(5000, new java.awt.event.ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                RunGrabber(e);
                        }
                });

                jSend.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(MouseEvent e)
                        {
                                try
                                {
                                        jSend_mouseClicked(e);
                                }
                                catch(javax.mail.MessagingException error)
                                {
                                        errorpane.setError(error.getMessage());
                                        errorpane.setError("Please check configuartion");


                                }

                                catch (Exception error)
                                {
                                        error.printStackTrace();
                                }
                        }
                });
                if (status == 2)
                {
                        this.jAddressField.setText(remail.getFrom()[0].toString());
                        this.jSubjectField.setText("re:" + remail.getSubject());
                }
                time.start();

        }

        private void jMenuFileExit_actionPerformed(ActionEvent e)
        {
                this.dispose();
        }

        void RunGrabber(ActionEvent e)
        {
                tg.start();
                doc = tg.getDocuments();
                updateScribe();
        }
        private JTable createJTable(Object[][] rows, Object[] names)
        {
                JTable myTable = new JTable(rows,names);
                myTable.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(MouseEvent e)
                        {
                                try
                                {
                                        jTable3_mouseClicked(e);
                                }
                                catch (Exception error)
                                {
                                        error.printStackTrace();
                                }
                        }
                });

                tc =myTable.getColumnModel().getColumn(3);
                tc.setCellRenderer(new MyRenderer());
                js0.getViewport().add(myTable);
                clearTable(myTable);
                return myTable;

        }


        /**
         * <p>Updates the Table</p>
         */
        private synchronized void updateScribe()
        {
                if (doc != null)
                {
                        int i = 0;
                        if(doc.length<10)
                       {
                               rows3=new Object[doc.length][6];
                       }
                       else
                       {
                               rows3=new Object[10][6];
                       }
                       jTable3 = createJTable(rows3, names);

                        while (i < doc.length && doc[i] != null && i <10)
                        {
                                jTable3.clearSelection();
                                jTable3.setValueAt(doc[i].get("subject"), i, 0);
                                jTable3.setValueAt(doc[i].get("sentby"), i, 1);
                                jTable3.setValueAt(doc[i].get("sentdate"), i, 2);
                                double rank=Double.parseDouble(doc[i].get("rank"));
                                rank=rank*100;
                                jTable3.setValueAt(String.valueOf((int)rank), i, 3);

                                i++;
                        }
                }
        }
        private void clearTable(JTable table)
        {
                int length = table.getRowCount();
                int i=0;
                while(i<length)
                {
                        table.setValueAt(null,i,0);
                        table.setValueAt(null,i,1);
                        table.setValueAt(null,i,2);
                        table.setValueAt(String.valueOf(0),i,3);
                        i++;
                }
        }




        void jTable3_mouseClicked(MouseEvent e) throws Exception
        {
                Contents.setText(doc[jTable3.getSelectedRow()].get("contents"));
                Contents.setCaretPosition(0);
        }

        void jSend_mouseClicked(MouseEvent e) throws MessagingException
        {
                sendEmail();
        }

        private void sendEmail() throws MessagingException
        {
                try
                {
                String subject = this.jSubjectField.getText();
                String emailaddress = this.jAddressField.getText();

                Date date = new Date();
                String contents = jContents.getText();

                MimeMessage em = new MimeMessage(mailman.getSession());
                em.setContent( (Object) contents, "text/plain");
                em.setSubject(subject);
                InternetAddress[] replyTO =
                        {
                        new InternetAddress(mailman.getEmailAddress())};
                em.setFrom(new InternetAddress(mailman.getEmailAddress()));
                em.setSentDate(date);
                em.setReplyTo(replyTO);
                em.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailaddress));
                if (status == 2)
                {
                        em.addHeader("References", remail.getHeader("Message-ID")[0]);
                }

                MimeMessage[] mail =
                        {
                        new MimeMessage(em)};

                        mailman.saveEmails("OUTBOX", mail);
                        mailman.sendEmail(mail);
                        time.stop();
                        if(tg.isAlive())
                        {
                                tg.destroy();
                        }
                        this.dispose();

                }
                catch(javax.mail.internet.AddressException error)
                {
                        errorpane.setError("Please enter Destination Address");
                        errorpane.show();
                }

                catch (Exception error)
                {
                        errorpane.setError(error.getMessage());
                        error.printStackTrace();
                        errorpane.show();
                }
        }

        public class MyRenderer extends JProgressBar implements TableCellRenderer
       {
               public MyRenderer()
               {
                       super(0,10);
               }
               public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column)
               {
                       setValue(value);
                       return this;
               }
               protected void setValue(Object value)
               {


                       if(!(value instanceof JProgressBar))
                       {
                               this.setValue(Integer.parseInt(value.toString()));
                       }
                       else
                       {
                               this.setValue(1);
                       }

               }


       }


}
