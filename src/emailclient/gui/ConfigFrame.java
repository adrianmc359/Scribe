package emailclient.gui;
import java.io.IOException;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.mail.NoSuchProviderException;
import emailclient.*;
/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 * Frame to set Program Functions like server name,email address, etc
 *
 */

public class ConfigFrame
        extends JFrame
{
        JLabel jTitle = new JLabel();
        JLabel UserName = new JLabel();
        JTextField usernamebox = new JTextField();
        JLabel UserName1 = new JLabel();
        JTextField serverbox = new JTextField();
        JLabel UserName2 = new JLabel();
        JPasswordField passwordbox = new JPasswordField();
        JButton jButton1 = new JButton();
        JButton jButton2 = new JButton();
        MailClient1 mailman;
        User currentUser;
        JLabel UserName3 = new JLabel();
        JTextField emailaddressbox = new JTextField();
        /**
         *
         * @param mailman MailClient1
         * @throws HeadlessException
         */
        public ConfigFrame(MailClient1 mailman) throws HeadlessException
        {
                this.mailman = mailman;
                try
                {
                        jbInit();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        private void jbInit() throws Exception
        {
                jTitle.setBackground(Color.cyan);
                jTitle.setDebugGraphicsOptions(0);
                jTitle.setText("Scribe Configuration");
                jTitle.setBounds(new Rectangle(0, 0, 560, 16));
                this.getContentPane().setLayout(null);
                this.setResizable(false);
                UserName.setText("User name");
                UserName.setBounds(new Rectangle(4, 31, 129, 16));
                usernamebox.setBounds(new Rectangle(4, 68, 433, 30));
                UserName1.setBounds(new Rectangle(3, 114, 129, 16));
                UserName1.setText("Server");
                serverbox.setBounds(new Rectangle(2, 145, 433, 30));
                UserName2.setText("Password");
                UserName2.setBounds(new Rectangle(7, 186, 129, 16));
                passwordbox.setBounds(new Rectangle(6, 212, 433, 30));
                jButton1.setBounds(new Rectangle(59, 337, 81, 26));
                jButton1.setText("Cancel");
                jButton1.addActionListener(new ConfigFrame_jButton1_actionAdapter(this));
                jButton2.setBounds(new Rectangle(288, 337, 81, 26));
                jButton2.setText("OK");
                jButton2.addActionListener(new ConfigFrame_jButton2_actionAdapter(this));
                UserName3.setBounds(new Rectangle(7, 252, 129, 16));
                UserName3.setText("Email Address");
                emailaddressbox.setBounds(new Rectangle(5, 277, 433, 30));
                this.getContentPane().add(jTitle, null);
                this.getContentPane().add(jButton1, null);
                this.getContentPane().add(jButton2, null);
                this.getContentPane().add(UserName, null);
                this.getContentPane().add(usernamebox, null);
                this.getContentPane().add(UserName1, null);
                this.getContentPane().add(serverbox, null);
                this.getContentPane().add(UserName2, null);
                this.getContentPane().add(passwordbox, null);
                this.getContentPane().add(UserName3, null);
                this.getContentPane().add(emailaddressbox, null);
                this.setSize(500, 400);
                currentUser = mailman.getUser();
                this.usernamebox.setText(currentUser.getAccount().getUserName());
                this.serverbox.setText(currentUser.getAccount().getServer());
                this.passwordbox.setText(currentUser.getAccount().getPassword());
                this.emailaddressbox.setText(currentUser.getAccount().getEmailAddress());
        }

        public static void main(String[] args)
        {
                //ConfigFrame as =new ConfigFrame();
                //as.show();
        }

        void jButton1_actionPerformed(ActionEvent e)
        {
                this.dispose();
        }

        void jButton2_actionPerformed(ActionEvent e)
        {

                try
                {
                        Account ac = new Account(serverbox.getText(), usernamebox.getText(), passwordbox.getText(), this.emailaddressbox.getText());
                        currentUser.setAccount(ac);
                        mailman.saveDataFile(this.currentUser, mailman.getDataFile());
                }
                catch (NoSuchProviderException error)
                {
                        ErrorDialog ep = new ErrorDialog();
                        ep.setError("No Such Provider");
                        ep.show();
                }
                catch (IOException error)
                {
                        ErrorDialog ep = new ErrorDialog();
                        ep.setError("Problem in saving settings");
                        ep.show();
                }


                this.dispose();

        }

}

class ConfigFrame_jButton1_actionAdapter
        implements java.awt.event.ActionListener
{
        ConfigFrame adaptee;

        ConfigFrame_jButton1_actionAdapter(ConfigFrame adaptee)
        {
                this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e)
        {
                adaptee.jButton1_actionPerformed(e);
        }
}

class ConfigFrame_jButton2_actionAdapter
        implements java.awt.event.ActionListener
{
        ConfigFrame adaptee;

        ConfigFrame_jButton2_actionAdapter(ConfigFrame adaptee)
        {
                this.adaptee = adaptee;
        }

        public void actionPerformed(ActionEvent e)
        {
                adaptee.jButton2_actionPerformed(e);
        }
}