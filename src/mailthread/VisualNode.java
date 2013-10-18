package mailthread;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An emailclient</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 1.0
 */
import javax.mail.internet.MimeMessage;

public class VisualNode
{
        MimeMessage mess;
        public VisualNode(MimeMessage mess) throws Exception
        {
                this.mess = mess;
        }

        public String toString()
        {
                String temp = "";
                try
                {
                        temp = mess.getSubject();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
                return temp;
        }

        public MimeMessage getMimeMessage()
        {
                return mess;
        }
}