package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: Mail Client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian Mclaughlin
 * @version 0.1
 */
import org.apache.lucene.document.*;
import emailclient.Email;
import javax.mail.MessagingException;
import java.io.*;
import java.util.Date;
import javax.mail.internet.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.Properties;
import java.util.Enumeration;
public class EmailDoc
{

      /**
       * <p>Makes an Email as an indexable Document</p>
       * @param f
       * @return Document
       * @throws IOException
       * @throws MessagingException
       */
      public static Document EmailDoc(MimeMessage f) throws java.io.IOException,MessagingException
      {

            Document email = new Document();
            if(f.getFrom()!=null)
            {
                  email.add(Field.Text("sentby",((InternetAddress)f.getFrom()[0]).getAddress()));
            }

            if(f.getHeader("References")!=null)
            {
                  String[] references=f.getHeader("References");
                  String ref="";
                  for(int i=0;i<references.length;i++)
                  {
                        ref=ref+" "+references[i];
                  }
                  email.add(Field.Text("references",ref));
            }
            else
            {
                  email.add(Field.Text("references","none"));
            }
            if(f.getReplyTo()!=null)
            {
                  String Replies="";
                  int i=0;
                  InternetAddress[] replies=(InternetAddress[])f.getReplyTo();
                  while(i<replies.length)
                  {
                        Replies=Replies+" "+replies[i].getAddress();
                        i++;
                  }
                  email.add(Field.Text("reply",Replies));
            }
            email.add(Field.Text("message-id",removeChar(f.getMessageID())));
            email.add(Field.Text("subject", removeChar(f.getSubject())));
            email.add(Field.Text("sentdate",f.getSentDate().toGMTString()));
            email.add(Field.Text("contents",removeChar(f.getContent().toString())));
            return email;
      }
      public static String removeChar(String str)
      {
              final char[] escapeChars={'+','-','|','!','(',')','{','}','[',']','^','"','~','*','?',':','\\','<','>','.'};
              char[] orig=str.toCharArray();
              int i=0;
              int j;
              while(i<orig.length)
              {
                    j=0;
                    while(j<escapeChars.length)
                    {
                            if(escapeChars[j]==orig[i])
                            {
                                    orig[i]=' ';
                            }
                            j++;
                    }
                    i++;
              }
              return String.valueOf(orig);
      }

}