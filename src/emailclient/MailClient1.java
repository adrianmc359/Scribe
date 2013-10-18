package emailclient;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Vector;
import com.sun.mail.smtp.SMTPTransport;
import ir.EmailIndex;

public class MailClient1
{
        private User user1;
        private Mailbox mbox;
        private String host;
        private String username;
        private String password;
        private Store store;
        private Session session;
        private Properties props;
        private Properties mailprop;
        private Folder folder;
        private File userfile;
        private String emailaddress;
        private Vector emailtobesaved;
        private Vector placetobesaved;
        public MailClient1() throws Exception
        {
                //read in userfile
                try
                {
                        setup();

                }
                catch (IOException error)
                {
                        error.printStackTrace();
                }

        }
        private void setup()throws IOException,NoSuchProviderException
        {

                        emailtobesaved=new Vector();
                        placetobesaved=new Vector();
                        mailprop = new Properties();
                        this.userfile = new File(System.getProperty("user.home") + "/Scribe/user.xml");
                        this.readDataFile(this.userfile);
                        host = (user1.getAccount()).getServer();
                        username = user1.getAccount().getUserName();
                        password = user1.getAccount().getPassword();
                        mailprop.setProperty("base", user1.getDir() + "/Scribe");
                        mailprop.setProperty("index", user1.getDir() + "/Scribe/index");
                        mailprop.setProperty("defaultMail", "/Mail");
                        mailprop.setProperty("defaultINBOX", "/INBOX");
                        mailprop.setProperty("defaultOUTBOX", "/OUTBOX");
                        emailaddress = user1.getAccount().getEmailAddress();
                        // Create empty properties
                        props = new Properties();
                        // Get session
                        session = Session.getDefaultInstance(props, null);
                        // Get the store
                        store = session.getStore("pop3");
                        mbox = new Mailbox(session, mailprop);




        }

        /**
         * <p>Attempt to make a connection to remote mailbox</p>
         * @throws javax.mail.MessagingException
         */
        public void toConnect() throws javax.mail.MessagingException
        {
                store.connect("pop."+host, username, password);
        }


        public String getEmailAddress()
        {
                return emailaddress;
        }


        public Session getSession()
        {
                return session;
        }

        public User getUser()
        {
                return this.user1;
        }

        /**
         * <p>Close connection to remote mailbox</p>
         * @throws Exception
         */
        public void close() throws Exception
        {
                //Index new emails
                if(emailtobesaved.size()!=0)
                {


                        ;
                        for(int i=0;i<emailtobesaved.size();i++)
                        {
                                if(emailtobesaved.elementAt(i)!=null)
                                {
                                        EmailIndex ei = new EmailIndex(mailprop.getProperty("base") + "/index/" + (String)placetobesaved.elementAt(i), (MimeMessage)emailtobesaved.elementAt(i));
                                }
                        }

                }
                // Close connection
                if(folder!=null)
                {
                        if(folder.isOpen())
                        {
                                folder.close(false);
                        }
                }
                if(store!=null)
                {
                        if(store.isConnected())
                        {
                                store.close();
                        }
                }
        }

        /**
         * <p>get Emails from remote mailbox</p>
         * @return Email[]
         * @throws BadEmailException
         * @throws Exception
         */
        public MimeMessage[] getMessages()  throws BadEmailException, Exception
        {
                //get Emails from remote mailbox
                folder = store.getFolder("INBOX");
                // Open read-only
                folder.open(Folder.READ_ONLY);
                // Get directory
                MimeMessage[] message =Email.getMimeMessages(folder.getMessages(),session);
                return message;

        }

        /**
         * <p>Reads in data for XML file</p>
         * @param datafile XML configuration file
         * @throws IOException
         */
        public void readDataFile(File datafile) throws IOException
        {
                //Reads in data for XML file
                UserPref userPref1 = new UserPref(datafile);
                user1 = userPref1.getUser();

        }

        public File getDataFile()
        {
                return this.userfile;
        }

        public Properties getProperties()
        {
                return this.mailprop;
        }

        public void saveDataFile(User user, File datafile) throws NoSuchProviderException,IOException
        {

                UserPref userPref1 = new UserPref(datafile);
                userPref1.setUser(user);
                setup();
        }

        /**
         * <p>Saves emails in the local mailbox</p>
         * @param em MimeMessage[]
         * @param mailfolder String
         * @throws Exception
         */
        public void saveEmails(String mailfolder, MimeMessage[] em)
        {
                try
                {
                        int i = 0;
                        while (i < em.length)
                        {
                                //Passes emails to mailbox to store
                                mbox.saveEmail(mailfolder, em[i]);
                                emailtobesaved.addElement(em[i]);
                                placetobesaved.addElement(mailfolder);
                                i++;

                        }

                }
                catch(Exception e)
                {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                }

        }

        public void IndexCollection()  throws BadEmailException,Exception
        {
                mbox.Index("INBOX");
                mbox.Index("OUTBOX");
        }

        /**
         * <p>Gets emails from local mailbox</p>
         * @return Email[]
         * @param mailfolder String
         * @throws BadEmailException
         * @throws Exception
         */
        public MimeMessage[] getEmails(String mailfolder)  throws BadEmailException, Exception
        {
                //Gets emails from local mailbox
                return mbox.getEmails(mailfolder);
        }

        /**
         * @param mailfolder String
         * @return boolean
         * @throws Exception
         */

        public boolean isEmpty(String mailfolder) throws Exception
        {
                if (mbox.isEmpty(mailfolder))
                {
                        return true;
                }
                else
                {
                        return false;
                }
        }

        public void sendEmail(MimeMessage[] em) throws MessagingException
        {
                int i = 0;
                while (i < em.length)
                {
                        URLName urlname = new URLName(em[i].getRecipients(MimeMessage.RecipientType.TO)[0].toString());
                        InternetAddress[] ia =
                                {
                                new InternetAddress(urlname.toString())};
                        SMTPTransport trans = new SMTPTransport(session, urlname);
                        trans.connect("smtp."+host, 25, username, password);
                        if (trans.isConnected())
                        {
                                System.out.println("connected");
                                System.out.println(em[i].getRecipients(MimeMessage.RecipientType.TO)[0].toString());
                                trans.sendMessage(em[i], ia);
                        }
                        i++;
                }
        }

        public void delete(MimeMessage email,String mailfolder) throws BadEmailException
        {
                mbox.delete(email,mailfolder);
        }

}
