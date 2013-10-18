package emailclient.gui;

import java.io.File;
import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import emailclient.*;

import mailthread.MailThread2;
import mailthread.ThreadsW;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */


public class Scribe2F extends JFrame
{
        NewMess nm;
        ResultsW results;
        private OpenFram splash = new OpenFram();
        private MailClient1 mailman;
        JPanel contentPane;
        GMailbox[] panels = new GMailbox[2];
        MimeMessage[] em; //Emails Storage
        Object[] emails = new Object[panels.length];
        JMenuBar jMenuBar1 = new JMenuBar();
        JMenu jMenuFile = new JMenu();
        JMenu jMenuHelp = new JMenu();
        JMenuItem jMenuHelpAbout = new JMenuItem();
        JPopupMenu pop = new JPopupMenu("Options");
        JMenuItem jMenuItemp1 = new JMenuItem();
        JMenuItem jMenuItemp4 = new JMenuItem();
        JToolBar jToolBar = new JToolBar();
        ImageIcon image1;
        ImageIcon image2;
        ImageIcon image3;
        JLabel statusBar = new JLabel();
        BorderLayout borderLayout1 = new BorderLayout();
        JTabbedPane jTabbedPane1 = new JTabbedPane();
        JButton jButton4 = new JButton();
        JMenuItem jMenuItem1 = new JMenuItem();
        JMenuItem jMenuItem2 = new JMenuItem();
        //Error menu
        ErrorDialog errorpane = new ErrorDialog();
        JMenu jMenu1 = new JMenu();
        JMenuItem jMenuItem3 = new JMenuItem();
        JMenuItem jMenuItem4 = new JMenuItem();
        //Construct the frame
        public Scribe2F()
        {
                enableEvents(AWTEvent.WINDOW_EVENT_MASK);
                try
                {
                        jbInit();
                }
                catch (Exception e)
                {
                        errorpane.setError(e.getMessage());
                        errorpane.show();
                        e.printStackTrace();
                }
        }

        //Component initialization
        private void jbInit() throws Exception
        {
                splash.show();
                //panels[0] = new GMailbox("Server", mailman, errorpane);
                panels[0] = new GMailbox("INBOX", mailman, errorpane);
                panels[1] = new GMailbox("OUTBOX", mailman, errorpane);
                mailman = new MailClient1();
                File firstTime=new File(mailman.getProperties().getProperty("base")+"/firsttime");
                image1 = new ImageIcon(emailclient.gui.Scribe2F.class.getResource("openFile.gif"));
                image2 = new ImageIcon(emailclient.gui.Scribe2F.class.getResource("closeFile.gif"));
                image3 = new ImageIcon(emailclient.gui.Scribe2F.class.getResource("help.gif"));
                contentPane = (JPanel)this.getContentPane();
                contentPane.setLayout(borderLayout1);
                this.setSize(new Dimension(640, 480));
                this.setTitle("Scribe 1.0");
                statusBar.setText(" ");
                jMenuFile.setText("File");
                jMenuHelp.setText("Help");
                jMenuHelpAbout.setText("About");
                jMenuHelpAbout.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                jMenuHelpAbout_actionPerformed(e);
                        }
                });
                jButton4.setText("POP");
                jMenuItem1.setText("Exit");
                jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                jMenuItem1_actionPerformed(e);
                        }
                });
                jMenuItem2.setText("New");
                jMenuItem2.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                jMenuItem2_actionPerformed(e);
                        }
                });
                jMenu1.setText("Options");
                jMenuItem3.setText("Config");
                jMenuItem3.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                jMenuItem3_actionPerformed(e);
                        }
                });
                jMenuItemp1.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                jMenuItemp1_actionPerformed(e);
                        }
                });
                jMenuItem4.setText("Rebuid Index");
                jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                jMenuItem4_actionPerformed(e);
                        }
                });
                jToolBar.add(jButton4, null);
                jMenuHelp.add(jMenuHelpAbout);
                jMenuBar1.add(jMenuFile);
                jMenuBar1.add(jMenu1);
                jMenuBar1.add(jMenuHelp);
                this.setJMenuBar(jMenuBar1);
                contentPane.add(jToolBar, BorderLayout.NORTH);
                contentPane.add(statusBar, BorderLayout.SOUTH);
                for (int i = 0; i < panels.length; i++)
                {
                        jTabbedPane1.add(panels[i], panels[i].getName());
                }
                contentPane.add(jTabbedPane1, BorderLayout.CENTER);
                jMenuFile.add(jMenuItem2);
                jMenuFile.add(jMenuItem1);
                jButton4.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(MouseEvent e)
                        {
                                jButton4_mouseClicked(e);
                        }
                });

                jMenuItemp1.setText("delete");
                jMenuItemp4.setText("reply");
                pop.add(jMenuItemp1);
                pop.add(jMenuItemp4);
                pop.addSeparator();
                panels[0].jTable1.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(MouseEvent e)
                        {
                                try
                                {
                                        if (e.getButton() == 3)
                                        {
                                                rightClick_mouseClicked(e);
                                        }
                                        else
                                        {
                                                ProcessEmail(e);
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
                jMenuItemp4.addActionListener(new java.awt.event.ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                jMenuItemp4_actionPerformed(e);
                        }
                });

                jMenu1.add(jMenuItem3);
                jMenu1.add(jMenuItem4);
                //below non-swing objects
                splash.setText("Scanning Mail folders");
                this.updateEmaillist();
                if(!firstTime.exists())
                {
                        splash.setText("Building Index");
                        this.IndexCollection();
                        firstTime.createNewFile();
                }
                splash.setText("Detecting Mail threads");
                MailThread2 nw = new MailThread2(mailman.getProperties(), mailman.getSession());
                ThreadsW tw = new ThreadsW(nw, "MailThreads");
                this.jTabbedPane1.add(tw,  "Mail Threads");
                results=new ResultsW();
                this.jTabbedPane1.add("similar",results);
                splash.dispose();

        }

        //File | Exit action performed

        //Help | About action performed
        public void jMenuHelpAbout_actionPerformed(ActionEvent e)
        {
                Scribe2F_AboutBox dlg = new Scribe2F_AboutBox(this);
                Dimension dlgSize = dlg.getPreferredSize();
                Dimension frmSize = getSize();
                Point loc = getLocation();
                dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
                dlg.setModal(true);
                dlg.show();
        }

        /**
         * <p>passes the correct emails to the correct JPanel</p>
         */
        public void updateEmaillist()
        {
                try
                {
                        int curBox = 0;
                        //updating local emailist;
                        while (curBox < panels.length)
                        {
                                em = (MimeMessage[]) emails[curBox];
                                if (em == null)
                                {

                                }
                                else
                                {

                                        panels[curBox].updateTable(em,false);

                                }
                                if (!mailman.isEmpty("Mail/" + panels[curBox].getName()))
                                {
                                        MimeMessage[] tem = mailman.getEmails("Mail/" + panels[curBox].getName());

                                        if (tem != null)
                                        {

                                                panels[curBox].updateTable(tem,false);

                                        }

                                }
                                curBox++;
                        }




                }
                catch (MessagingException error)
                {
                        this.statusBar.setText("Message Exception has occurred");
                        errorpane.setError("Message Exception has occurred");
                        error.printStackTrace();
                }
                catch(BadEmailException error)
                {
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                }

                catch (Exception error)
                {
                        this.statusBar.setText("Exception has occurred");
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                        error.printStackTrace();
                }
                // This section is necessary to reattach mouse listener to GMailbox classes after they
                // reinitialise their JTables.


        }

        //Overridden so we can exit when window is closed
        /**
         * <p>Creates a new Lucene Index</p>
         */
        private void IndexCollection()
        {
                try
                {
                        mailman.IndexCollection();
                }
                catch(BadEmailException error)
                {
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                }

                catch (Exception error)
                {
                        this.statusBar.setText("Index Failure");
                        System.out.println("Index failure");
                        error.printStackTrace();
                }
        }

        protected void processWindowEvent(WindowEvent e)
        {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING)
                {
                        jMenuFileExit_actionPerformed(null);
                }
        }
        /**
         * <p>The function to get the messages from a remote server</p>
         * @param e MouseEvent
         */
        private void jButton4_mouseClicked(MouseEvent e)
        {
                try
                {
                        this.statusBar.setText("Connecting...");
                        statusBar.updateUI();
                        System.out.println("Connecting...");
                        mailman.toConnect();
                        this.statusBar.setText("Retrieving emails");
                        statusBar.repaint();
                        emails[0] = mailman.getMessages();
                        this.statusBar.setText("Finished");
                        statusBar.updateUI();
                        mailman.close();
                        mailman.saveEmails("INBOX",(MimeMessage[])emails[0]);
                        this.updateEmaillist();


                }
                catch (javax.mail.MessagingException error)
                {
                        errorpane.setError(mailman.getUser().getAccount().getServer() + " can't be reached\n Please check your settings");
                        errorpane.show();
                        error.printStackTrace();
                }
                catch(BadEmailException error)
                {
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                }


                catch (Exception error)
                {
                        this.statusBar.setText("ERROR!!!!");
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                        error.printStackTrace();
                }
        }

        /**
         * <p>To invoke the NewMess class</p>
         * @param e ActionEvent
         */
        private void jMenuItem2_actionPerformed(ActionEvent e)
        {
                nm = new NewMess(mailman, this.errorpane);
                nm.addWindowListener(new java.awt.event.WindowAdapter()
                        {
                                public void windowClosed(WindowEvent e)
                                {
                                        try
                                        {

                                               updateTables(e);

                                        }
                                        catch (Exception error)
                                        {
                                                error.printStackTrace();
                                                errorpane.setError(error.getMessage());
                                                errorpane.show();
                                        }
                                }
                        });

                nm.show();
        }

        public void jMenuFileExit_actionPerformed(ActionEvent e)
        {
                try
                {
                        mailman.close();
                        if(nm!=null)
                                nm.dispose();
                        if(errorpane!=null)
                                errorpane.dispose();
                        this.dispose();
                        System.exit(0);
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }
        }




        private void Index_mouseClicked(MouseEvent e)
        {
                errorpane.show();

        }
        /**
         * <p>To invoke  the pop-up menu</p>
         * @param e MouseEvent
         */
        private void rightClick_mouseClicked(MouseEvent e)
        {
                if(this.panels[0].jTable1.getSelectedRow()!=-1)
                {
                        pop.show(this.panels[0].jTable1, e.getX(), e.getY());
                }

        }
        /**
        * <p>To invoke  the NewMess class in reply mode</p>
        * @param e ActionEvent
        */

        private void jMenuItemp4_actionPerformed(ActionEvent e)
        {
                try
                {
                        int index = panels[0].jTable1.getSelectedRow();

                        nm = new NewMess(mailman, panels[0].getEmail(), this.errorpane);
                        nm.addWindowListener(new java.awt.event.WindowAdapter()
                        {
                                public void windowClosed(WindowEvent e)
                                {
                                        try
                                        {

                                               updateTables(e);

                                        }
                                        catch (Exception error)
                                        {
                                                error.printStackTrace();
                                                errorpane.setError(error.getMessage());
                                                errorpane.show();
                                        }
                                }
                        });
                        nm.show();

                }
                //catch(BadEmailException error)
                //{
                //        errorpane.setError(error.getMessage());
                //        errorpane.show();
                //}

                catch (Exception error)
                {
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                }

        }
        /**
         * <p>To open the configuration panel</p>
         * @param e ActionEvent
         */
        private void jMenuItem3_actionPerformed(ActionEvent e)
        {
                ConfigFrame cf = new ConfigFrame(mailman);
                cf.show();
        }
        /**
         * <p>To delete the specified email(NOT WORKING)</p>
         * @param e ActionEvent
         */

        private void jMenuItemp1_actionPerformed(ActionEvent e)
        {
                try
                {
                        mailman.delete(panels[0].getEmail(), panels[0].getName());
                }
                catch(BadEmailException error)
                {
                        errorpane.setError(error.getMessage());
                        errorpane.show();
                }

        }
        /**
         * <p>To update the Similar tab</p>
         * @param e MouseEvent
         */
        private void ProcessEmail(MouseEvent e)
        {
                MimeMessage email=panels[0].getEmail();
                if(email!=null)
                {
                        try
                        {


                                email.setFlag(Flags.Flag.SEEN,true);
                                this.results.Evaluate(email, this.mailman.getProperties());
                        }
                        catch (MessagingException ex)
                        {
                                ex.printStackTrace();
                        }
                        catch (Exception ex1)
                        {
                                ex1.printStackTrace();
                        }

                }

        }

        private void jMenuItem1_actionPerformed(ActionEvent e)
        {
                jMenuFileExit_actionPerformed(null);
        }
        private void updateTables(WindowEvent e)
        {
                updateEmaillist();
        }
        /**
         * <p>To invoke the creation of a new Index</p>
         * @param e ActionEvent
         */
        private void jMenuItem4_actionPerformed(ActionEvent e)
        {
                this.IndexCollection();
        }


}
