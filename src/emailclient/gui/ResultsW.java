package emailclient.gui;
/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

import java.io.IOException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import emailclient.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.lucene.document.Document;
import ir.QueryFormer2;
import ir.SimilarThread;

public class ResultsW extends JPanel
{
        MimeMessage email;
        JTable jTable3;
        JTextPane Contents = new JTextPane();
        JSplitPane jSplitPane1 = new JSplitPane();
        JScrollPane js0 = new JScrollPane();
        JScrollPane jt0 = new JScrollPane();
        BorderLayout borderLayout2 = new BorderLayout();
        TableColumn tc;
        Object[] names ={"Subject","Sentby", "Date", "rank"};
        Object[][] rows3 = new Object[0][4];
        Document[] doc = new Document[10];
        QueryFormer2 qf;
        Properties mailprop;
        SimilarThread st;

        public ResultsW()
        {
                try
                {
                        jbInit();

                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }
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


        private void jbInit() throws Exception
        {
                this.setLayout(borderLayout2);
                jTable3 = createJTable(rows3, names);
                this.setLayout(borderLayout2);
                jSplitPane1.setDividerLocation(200);
                jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
                jt0.getViewport().add(Contents);
                js0.getViewport().add(jTable3);
                jSplitPane1.add(jt0, JSplitPane.BOTTOM);
                jSplitPane1.add(js0, JSplitPane.TOP);
                this.add(jSplitPane1);
        }
        public void Evaluate(MimeMessage email,Properties mailprop)
        {
                try
                {

                        st = new SimilarThread();
                        this.email = email;
                        this.mailprop = mailprop;
                        if (!st.isAlive())
                        {
                                st.setValues(email.getContent().toString(), mailprop);
                                st.start();
                                doc=st.getDocuments();
                                updateScribe();
                        }

                }
                catch (IOException ex)
                {
                        ex.printStackTrace();
                }
                catch (MessagingException ex)
                {
                        ex.printStackTrace();
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



        private void updateScribe()
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
                       while ((i < 10 && i<doc.length) && doc[i] != null )
                       {
                               jTable3.clearSelection();
                               jTable3.setValueAt(doc[i].get("subject"), i, 0);
                               jTable3.setValueAt(doc[i].get("sentby"), i, 1);
                               jTable3.setValueAt(doc[i].get("sentdate"), i, 2);
                               double rank=Double.parseDouble(doc[i].get("rank"));
                               rank=rank*10;
                               jTable3.setValueAt(String.valueOf((int)rank), i, 3);
                               i++;
                       }
               }
       }



        private void jTable3_mouseClicked(MouseEvent e) throws Exception
        {
                if(jTable3.getSelectedRow()!=-1)
                {
                        Contents.setText(doc[jTable3.getSelectedRow()].get("contents"));
                        Contents.setCaretPosition(0);
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