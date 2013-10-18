package emailclient;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

import java.util.Date;
import java.util.StringTokenizer;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email
{
        /**
         * <p>Creates a MimeMessage from a Lucene Document</p>
         * @param doc Lucene Document
         * @param session JavaMail Session
         * @return MimeMessage
         * @throws MessagingException
         */
        public static MimeMessage Email(org.apache.lucene.document.Document doc, Session session) throws MessagingException
        {
                MimeMessage message = new MimeMessage(session);
                String[] references = null;
                if (!doc.get("references").equals("none"))
                {
                        message.addHeader("references", doc.get("reference"));
                }
                StringTokenizer st = new StringTokenizer(doc.get("reply"));
                InternetAddress[] Ereply = new InternetAddress[st.countTokens()];
                int i = 0;
                while (st.hasMoreTokens())
                {
                        Ereply[i] = new InternetAddress(st.nextToken());
                        i++;
                }

                message.setFrom(new InternetAddress(doc.get("sentby")));
                message.setSubject(doc.get("subject"));
                message.setSentDate(new Date(doc.get("sentdate")));
                message.setReplyTo(Ereply);
                message.setContent(doc.get("contents"), String.valueOf(doc.get("contents")));
                message.setHeader("Message-ID", doc.get("message-id"));
                return message;
        }

        /**
         *
         * @param em1 First MimeMessage
         * @param em2 Second MimeMessage
         * @return boolean
         * @throws MessagingException
         */
        public static boolean equals(MimeMessage em1, MimeMessage em2) throws MessagingException
        {
                boolean test = true;
                if (!em1.getSubject().equals(em2.getSubject()))
                {
                        test = test && false;
                }
                InternetAddress add1 = (InternetAddress) em1.getFrom()[0];
                InternetAddress add2 = (InternetAddress) em2.getFrom()[0];
                if (!add1.getAddress().equals(add2.getAddress()))
                {
                        test = test && false;
                }
                return test;
        }

        /**
         * <p>Forms  a MimeMessage from  a message</p>
         * @param mess JavaMail Message
         * @param session JavaMail Session
         * @return MimeMessage
         * @throws Exception
         * @throws BadEmailException
         */
        public static MimeMessage getMimeMessage(Message mess, Session session) throws BadEmailException, Exception
        {
                MimeMessage mimemess = new MimeMessage(session);
                try{
                        if(mess.getHeader("Message-ID")!=null)
                        {
                                mimemess.setHeader("Message-ID", mess.getHeader("Message-ID")[0]);
                        }
                        else
                        {
                                mimemess.setHeader("Message-ID", "bademail"+new Date().getDate());
                        }
                        if(mess.getSubject()!=null)
                        {
                                mimemess.setSubject(mess.getSubject());
                        }
                        else
                        {
                                mimemess.setSubject("");
                        }
                        if(mess.getFrom()!=null)
                        {
                                mimemess.setFrom(mess.getFrom()[0]);
                        }
                        else
                        {
                                mimemess.setFrom(new InternetAddress("notspecified"));
                        }
                        if(mess.getSentDate()!=null)
                        {
                                mimemess.setSentDate(mess.getSentDate());
                        }
                        else
                        {
                                mimemess.setSentDate(new Date());
                        }
                        if(mess.getReplyTo()!=null)
                        {
                                mimemess.setReplyTo(mess.getReplyTo());
                        }
                        else
                        {
                                if(mess.getFrom()!=null)
                                {
                                        //mimemess.setReplyTo(mess.getFrom());
                                        InternetAddress[] as={new InternetAddress("none")};
                                        mimemess.setReplyTo(as);

                                }
                                else
                                {
                                        InternetAddress[] bademail={new InternetAddress("BadEmail")};
                                        mimemess.setReplyTo(bademail);
                                }
                        }
                        if(!mess.getContentType().endsWith("charset=X-UNKNOWN"))
                        {

                                if(mess.getContent()!=null)
                                {
                                        Object part=mess.getContent();
                                        if(part instanceof String)
                                        {
                                                mimemess.setText(mess.getContent().toString());
                                        }
                                        else
                                        {
                                                if(part instanceof Multipart)
                                                {
                                                        String body="";
                                                        Multipart multi =(Multipart)part;
                                                        int count =multi.getCount();
                                                        int i=0;
                                                        while(i<count)
                                                        {
                                                                body=body+multi.getBodyPart(i).getContent();
                                                                i++;
                                                        }
                                                        mimemess.setText(body);
                                                }
                                        }
                                }
                                else
                                {
                                        mimemess.setText("");
                                }
                                if(mess.getFlags().contains(Flags.Flag.SEEN))
                                {
                                        mimemess.getFlags().add(Flags.Flag.SEEN);
                                        mimemess.setFlag(Flags.Flag.SEEN,mess.getFlags().contains(Flags.Flag.SEEN));
                                        mimemess.saveChanges();
                                }


                        }
                        else
                        {
                                mimemess.setText("");
                        }
                }
                 catch(java.lang.NullPointerException err)
                   {
                        throw new BadEmailException();
                }
                return mimemess;

        }
                /**
         * <p>converts a Message array to MimeMessage array</p>
         * @param mess JavaMail Message
         * @param session JavaMail Session
         * @return MimeMessage[]
         * @throws BadEmailException
         * @throws Exception
         */
        public static MimeMessage[] getMimeMessages(Message[] mess, Session session) throws BadEmailException,Exception
        {
                int cycle = mess.length;
                MimeMessage[] mimemess = new MimeMessage[cycle];
                int i = 0;
                while (i < cycle)
                {

                        mimemess[i] = Email.getMimeMessage(mess[i], session);
                        i++;
                }
                return mimemess;
        }

}
