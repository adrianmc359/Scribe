package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An emailclient</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 1.0
 */
import java.util.Enumeration;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public class IndexCheck
{
        IndexReader reader;
        public IndexCheck(String mailfolder)
        {
                try
                {
                        reader = IndexReader.open("C:/Documents and Settings/adrian/Scribe/index/" + mailfolder);
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        public void getFreq(String term)
        {
                try
                {
                        int i = 0;
                        while(i<reader.numDocs())
                        {
                                System.out.println(reader.document(i).get("message-id"));
                                System.out.println(reader.document(i).get("references"));
                                i++;
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        public void getDoc(int nu)
        {
                try
                {
                        Document as = reader.document(nu);
                        for (Enumeration e = as.fields(); e.hasMoreElements(); )
                        {
                                String field = String.valueOf(e.nextElement());
                                System.out.println("field:" + field + "\n");
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }

        }

        public static void main(String[] args)
        {
                IndexCheck ic =new IndexCheck("INBOX");
                ic.getFreq("er");
        }
}