package emailclient;

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
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.ice.javamail.mh.MHFolder;
import com.ice.javamail.mh.MHStore;
import ir.EmailIndex;

public class Mailbox
{

        private Session session;
        private MHStore store;
        private MHFolder folder;
        private MHFolder inbox;
        private MHFolder outbox;
        private Properties mailprop;
        /**
         *
         * @param session JavaMail Session
         * @param mailprop Application Directories
         * @throws IOException
         */
        public Mailbox(Session session, Properties mailprop) throws IOException
        {
                try
                {

                        this.mailprop = mailprop;
                        store = new MHStore(session, new URLName("mh:" + mailprop.getProperty("base")));
                        this.session = session;
                        store.connect();
                        folder = (MHFolder) store.getFolder("Mail");
                        folder.open(Folder.READ_WRITE);
                        inbox = (MHFolder) store.getFolder("Mail/INBOX");
                        if (!inbox.exists())
                        {
                                inbox.create(0);
                        }
                        inbox.open(Folder.READ_WRITE);
                        outbox = (MHFolder) store.getFolder("Mail/OUTBOX");
                        if (!outbox.exists())
                        {
                                outbox.create(0);
                        }
                        outbox.open(Folder.READ_WRITE);
                }
                catch (MessagingException error1)
                {
                        throw new IOException();
                }

        }

        /**
         *
         * @param mailfolder Specific Directory
         * @return MimeMessage[]
         * @throws Exception
         * @throws   BadEmailException
         */
        public MimeMessage[] getEmails(String mailfolder) throws   BadEmailException,Exception
        {
                MHFolder currFolder = (MHFolder) store.getFolder(mailfolder);
                currFolder.open(Folder.READ_WRITE);
                Message[] mess = currFolder.getMessages();
                MimeMessage[] em = sort(Email.getMimeMessages(mess, session));

                int i = 0;
                while (i < em.length)
                {
                        if (mess[i].getHeader("References") != null)
                        {
                                em[i].addHeader("References", mess[i].getHeader("References")[0]);
                        }
                        if (mess[i].getRecipients(Message.RecipientType.TO) != null)
                        {
                                em[i].setRecipient(MimeMessage.RecipientType.TO, mess[i].getRecipients(Message.RecipientType.TO)[0]);
                        }
                        else
                        {
                                em[i].setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("unknown"));
                        }
                        if(!mess[i].getFlags().contains(Flags.Flag.SEEN))
                        {
                                em[i].getFlags().add(Flags.Flag.SEEN);
                                em[i].setFlag(Flags.Flag.SEEN,mess[i].getFlags().contains(Flags.Flag.SEEN));
                                //em[i].saveChanges();
                                //currFolder.setFlags(mess,f,true);

                        }
                        else
                        {

                        }



                        i++;
                }
                currFolder.close(true);
                return em;
        }


        /**
         *
         * @param mailfolder Specific folder
         * @param em MimeMessage
         * @throws Exception
         */

        public void saveEmail(String mailfolder, MimeMessage em) throws Exception
        {
                MHFolder currFolder = (MHFolder) store.getFolder("Mail/"+mailfolder);
                currFolder.open(Folder.READ_WRITE);
                if (!this.contains("Mail/"+mailfolder, em))
                {

                                em.setFlag(Flags.Flag.SEEN,false);
                                MimeMessage[] me ={em};
                                currFolder.appendMessages(me);

                }
                currFolder.close(true);
        }

        /**
         *
         * @return boolean
         * @throws Exception
         */
        public boolean isEmpty() throws Exception
        {
                int size = folder.getMessageCount();
                if (size == 0)
                {
                        return true;
                }
                else
                {
                        return false;
                }
        }

        /**
         *
         * @param mailfolder Specific folder
         * @return boolean
         * @throws Exception
         */
        public boolean isEmpty(String mailfolder) throws Exception
        {
                MHFolder currFolder = (MHFolder) store.getFolder(mailfolder);
                currFolder.open(Folder.READ_WRITE);
                int size = currFolder.getMessageCount();
                currFolder.close(true);
                if (size == 0)
                {
                        return true;
                }
                else
                {
                        return false;
                }

        }

        /**
         *
         * @param mailfolder Specific folder
         * @param em MimeMessage
         * @return boolean
         * @throws Exception
         */
        private boolean contains(String mailfolder, MimeMessage em) throws Exception
        {
                MHFolder currFolder = (MHFolder) store.getFolder(mailfolder);
                currFolder.open(Folder.READ_WRITE);
                boolean test = false;
                Message[] mess = currFolder.getMessages();
                int i = 0;
                while (i < mess.length)
                {
                        if (em.getFrom()[0].equals(mess[i].getFrom()[0]))
                        {

                                if (em.getContent().equals(mess[i].getContent()))
                                {
                                        System.out.println("they are equal");
                                        test = true;
                                }

                        }
                        i++;
                }
                currFolder.close(true);
                return test;
        }

        /**
         *
         * @param mailfolder Specific folder
         * @throws Exception
         * @throws   BadEmailException
         */
        public void Index(String mailfolder) throws BadEmailException,Exception
        {
                MHFolder currFolder = (MHFolder) store.getFolder("Mail/" + mailfolder);
                currFolder.open(Folder.READ_WRITE);
                Message[] mess = currFolder.getMessages();
                MimeMessage[] em = Email.getMimeMessages(mess, session);
                int i = 0;
                while (i < em.length)
                {
                        if (mess[i].getHeader("References") != null)
                        {
                                em[i].addHeader("References", mess[i].getHeader("References")[0]);
                        }
                        i++;
                }
                EmailIndex ei = new EmailIndex(mailprop.getProperty("base") + "/index/" + mailfolder, em);
        }
        public void delete(MimeMessage email,String mailfolder) throws BadEmailException
        {
                try
                {
                        MHFolder currFolder = (MHFolder) store.getFolder("Mail/" + mailfolder);
                        currFolder.open(Folder.READ_WRITE);
                        MimeMessage[] temp = Email.getMimeMessages(currFolder.getMessages(), session);


                        int j = 0;
                        for (int i = 0; i < temp.length; i++)
                        {
                                if (temp[i].getMessageID().equals(email.getMessageID()))
                                {
                                        j=i;
                                        System.out.println(temp[i].getMessageID()+" is equal "+email.getMessageID());
                                }
                                System.out.println(temp[i].getMessageID()+" not equal "+email.getMessageID());
                        }
                        currFolder.getMessage(j).setFlag(Flags.Flag.DELETED,true);
                        currFolder.close(true);
                        currFolder.expunge();
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



}