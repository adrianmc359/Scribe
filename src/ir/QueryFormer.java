package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import java.util.Vector;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class QueryFormer
{

        MyAnalyser ma;
        BooleanQuery bq;
        QueryEmails qe;
        public QueryFormer(Properties mailprop)
        {
                ma = new MyAnalyser();
                qe = new QueryEmails(mailprop.getProperty("index")+mailprop.getProperty("defaultINBOX"));
        }

        /**
         *
         * @param Text
         * @return Document[]
         */
        public Document[] Start(String Text)
        {
                Vector token = new Vector();
                bq = new BooleanQuery();
                Document as[] = new Document[10];
                try
                {


                        qe.start();
                        final Reader reader = new StringReader(Text);
                        final TokenStream in = ma.tokenStream(reader);

                        for (; ; )
                        {
                                final org.apache.lucene.analysis.Token temptoken = in.next();
                                if (temptoken == null)
                                {
                                        in.close();
                                        break;
                                }
                                token.addElement(temptoken.termText());

                        }
                        int end = token.size();
                        String myQuery = "";
                        //Sets the window over the over document
                        while (end > (token.size() - 10) && (end > 0))
                        {

                                end--;
                                myQuery = myQuery + token.elementAt(end) + " ";

                        }
                        if (myQuery == "")
                        {
                                myQuery = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
                        }
                        Query query = QueryParser.parse(myQuery, "contents", ma);
                        bq.add(new BooleanClause(query, true, false));
                        if (token.size() > 0)
                        {

                                as = qe.query(bq);
                        }

                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
                qe.close();
                return as;

        }

}