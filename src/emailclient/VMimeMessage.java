package emailclient;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */


public class VMimeMessage extends MimeMessage
{

        public VMimeMessage(Session session)
        {
                super(session);
        }

        public String toString()
        {
                try
                {
                        return this.getSubject();
                }
                catch (MessagingException error)
                {
                        error.printStackTrace();
                }
                return "";
        }
}