package testers;
import emailclient.*;
import ir.*;
import javax.mail.internet.MimeMessage;
import mailthread.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;
import java.io.*;
import java.util.Date;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */


public class Threadtest
{
        private String contents;
        private QueryEmails qu;
        private File report;
        public Threadtest(MimeMessage em)throws Exception
        {

                qu=new QueryEmails("INBOX");
                Date date =new Date();
                report=new File("C:/Documents and Settings/adrian/jbproject/Scribe/report"+date.getMinutes()+date.getSeconds()+".txt");
                qu.start();
                contents=String.valueOf(em.getContent());
                Document[] docs=qu.query(contents);
                qu.end();
                FileWriter writer=new FileWriter(report);
                PrintWriter printwrit=new PrintWriter(writer);
                printwrit.println("For message "+em.getSubject());
                int i=0;
                while(i<docs.length)
                {
                        printwrit.println(docs[i].get("subject")+" "+docs[i].get("message-id"));
                        i++;
                }
                printwrit.close();
                writer.close();


        }


}