package emailclient.gui;

import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import emailclient.*;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */


public class GMailbox extends JPanel
{
        int index = 0;
        BorderLayout borderLayout1 = new BorderLayout();
        JSplitPane jSplitPane1 = new JSplitPane();
        JScrollPane js0 = new JScrollPane();
        JScrollPane js1 = new JScrollPane();
        JTextPane Contents = new JTextPane();
        JTable jTable1;
        String name;
        MailClient1 mailman;
        ErrorDialog errorpane;
        MimeMessage[] emails;
        JMenuItem jMenuItem1 = new JMenuItem();
        JMenuItem jMenuItem2 = new JMenuItem();
        JMenuItem jMenuItem3 = new JMenuItem();
        JMenuItem jMenuItem4 = new JMenuItem();
        Object[] names ={"Sentby", "SentTo", "Subject", "Date","Read","Content"};
        Object[][] rows1 ;


        public GMailbox(String name, MailClient1 mailman, ErrorDialog errorpane)
        {
                this.name = name;
                this.mailman = mailman;
                this.errorpane = errorpane;
                try
                {
                        jbInit();
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }
        }
        public MimeMessage[] sort(MimeMessage[] mess)throws Exception
        {

                        int head=0;
                        int tail;
                        while(!(head>mess.length+1))
                        {
                                tail=head+1;
                                while(tail<mess.length)
                                {
                                        if(mess[head].getSentDate().after(mess[tail].getSentDate()))
                                        {
                                                MimeMessage dtemp=mess[head];
                                                mess[head]=mess[tail];
                                                mess[tail]=dtemp;
                                        }
                                        tail++;
                                }
                                head++;

                        }

                        return mess;

        }

        void jbInit() throws Exception
        {
                this.setLayout(borderLayout1);
                rows1 = new Object[0][6];
                jTable1 = createJTable(rows1, names);
                 jTable1.getModel().addTableModelListener(new TableModelListener()
                        {
                                public void tableChanged(TableModelEvent e)
                                {

                                        if( jTable1.getSelectedColumn()==5)
                                        {
                                                int row= jTable1.getSelectedRow();
                                                int col= jTable1.getSelectedColumn();
                                                Boolean value=Boolean.valueOf(String.valueOf( jTable1.getValueAt(row,col)));
                                                if(value.booleanValue()==true)
                                                {
                                                         jTable1.setValueAt(new Boolean(false),row,col);
                                                }
                                                else
                                                {
                                                         jTable1.setValueAt(new Boolean(true),row,col);
                                                }
                                        }
                                }
                        });

                jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
                jSplitPane1.setOpaque(false);
                js0.getViewport().add(jTable1);
                js1.getViewport().add(Contents);
                jSplitPane1.add(js0, JSplitPane.TOP);
                jSplitPane1.add(js1, JSplitPane.BOTTOM);
                this.add(jSplitPane1);
                jSplitPane1.setDividerLocation(160);


        }
        private JTable createJTable(Object[][] rows, Object[] names)
        {
                final JTable myTable = new JTable(new EmailTable(rows,names));
                TableColumn tc=myTable.getColumnModel().getColumn(4);
                tc.setCellEditor(new DefaultCellEditor(new JCheckBox()));
                myTable.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(MouseEvent e)
                        {
                                try
                                {
                                        if (e.getButton() == 1)
                                        {
                                                jTable1_mouseClicked(e);
                                        }

                                }
                                catch (Exception error)
                                {
                                        error.printStackTrace();
                                        errorpane.setError(error.getMessage());
                                        errorpane.show();
                                }
                        }
                });

                js0.getViewport().add(myTable);
                return myTable;

        }
        private void updateTable(MimeMessage[] emails,int position)throws Exception
        {
                updateTable(emails,false);
                jTable1.changeSelection(position,0,false,false);
        }
        public void updateTable(MimeMessage[] emails,boolean sort)throws Exception
        {


                if(sort)
                {
                        this.emails = sort(emails);
                }
                else
                {
                         this.emails = emails;
                }
                try
                {

                        if (emails != null)
                        {

                                rows1=new Object[emails.length][6];
                                MouseListener[] as=null;
                                if(jTable1!=null)
                                {
                                        as=jTable1.getMouseListeners();
                                }
                                jTable1 = createJTable(rows1, names);

                                int i = 0;
                                String[] ltext1 = new String[emails.length];
                                String[] ltext2 = new String[emails.length];
                                String[] ltext3 = new String[emails.length];
                                String[] ltext4 = new String[emails.length];
                                boolean[] ltext5 =new boolean[emails.length];
                                String[] ltext6 =new String[emails.length];
                                while (i < emails.length)
                                {
                                        ltext1[i] = String.valueOf(emails[i].getFrom()[0]);
                                        ltext2[i] = emails[i].getSubject();
                                        ltext3[i] = emails[i].getSentDate().toString();
                                        if (emails[i].getRecipients(MimeMessage.RecipientType.TO) != null)
                                        {
                                                ltext4[i] = String.valueOf(emails[i].getRecipients(MimeMessage.RecipientType.TO)[0]);
                                        }
                                        ltext5[i]=emails[i].getFlags().contains(Flags.Flag.SEEN);
                                        ltext6[i]=emails[i].getContentType();
                                        //ltext6[i]=emails[i].getMessageID().toString();
                                        i++;
                                }
                                for (int j = 0; j < emails.length; j++)
                                {
                                        jTable1.setValueAt(ltext1[j], j, 0);
                                        jTable1.setValueAt(ltext2[j], j, 2);
                                        jTable1.setValueAt(ltext3[j], j, 3);
                                        if (emails[j].getRecipients(MimeMessage.RecipientType.TO) != null)
                                        {
                                                jTable1.setValueAt(ltext4[j], j, 1);
                                        }
                                        jTable1.setValueAt(new Boolean(ltext5[j]), j, 4);
                                        jTable1.setValueAt(ltext6[j], j, 5);

                                }
                                if(as!=null)
                                {
                                        int j=as.length-1;
                                        while(j>=0&&j>as.length-10)
                                        {
                                                jTable1.addMouseListener(as[j]);
                                                j--;
                                        }
                                }

                        }






                }
                catch (MessagingException error)
                {
                        System.err.println("Message Exception has occurred");
                        error.printStackTrace();
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                }
                catch (Exception error)
                {
                        System.err.println("Exception has occurred");
                        error.printStackTrace();
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                }

        }

        public String getName()
        {
                return name;
        }
        public MimeMessage getEmail()
        {
                if(jTable1.getSelectedRowCount()==1)
                {
                        return this.emails[jTable1.getSelectedRow()];
                }
                return null;
        }


        private void SetContentPane(String text)
        {
                Contents.setText(text);
        }

        private void jTable1_mouseClicked(MouseEvent e) throws Exception
        {
                int row=jTable1.getSelectedRow();
                if (emails != null && row < emails.length)
                {
                        this.emails[row].setFlag(Flags.Flag.SEEN,true);
                        Contents.setText(emails[row].getContent().toString());
                        Contents.setCaretPosition(0);
                        updateTable(this.emails,row);
                }
        }
        public class EmailTable extends DefaultTableModel
        {

                public EmailTable(Object[][] data, Object[] columnNames)
                {
                        super(data,columnNames);

                }
                public Class getColumnClass(int c)
                {
                        return getValueAt(0, c).getClass();
                }
                public boolean isCellEditable(int row, int col)
                {
                        if(col>4)
                        {
                                return true;
                        }
                        return false;
                }



        }


}