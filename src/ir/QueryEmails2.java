package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An Email Client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author not attributable
 * @version 1.0
 */

import java.io.IOException;

import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.*;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.MultiSearcher;
import java.util.StringTokenizer;

public class QueryEmails2
{

        private Searcher searcher1;
        private Searcher searcher2;
        private MultiSearcher mSearcher;
        private Analyzer analyzer;
        private Query query;
        private Hits hits;
        private String mailfolder1;
         private String mailfolder2;
        public QueryEmails2(String mailfolder1,String mailfolder2)
        {
                this.mailfolder1 = mailfolder1;
                this.mailfolder2 = mailfolder2;
        }

        /**
         * Initialises the Searcher
         * @throws IOException
         */
        public void start() throws IOException
        {
                searcher1 = new IndexSearcher(this.mailfolder1);
                searcher2 = new IndexSearcher(this.mailfolder2);
                Searcher[] searchers={searcher1,searcher2};
                mSearcher=new MultiSearcher(searchers);
                analyzer = new MyAnalyser();
        }

        /**
         * Closes the Searcher
         */
        public void end()
        {
                this.close();
        }

        /**
         *
         * @param qu
         * @return Document
         * @throws IOException
         */
        public Document[] query(String qu) throws IOException
        {
                start();
                Document[] as = null;
                try
                {

                        //System.out.println(qu);
                        query = QueryParser.parse(qu, "contents", analyzer);
                        //System.out.println("Searching for: " + query.toString("contents"));
                        Hits hits = searcher1.search(query);
                        as = new Document[hits.length()];
                        //System.out.println(hits.length() + " total matching documents");
                        for (int start = 0; start < hits.length(); start++)
                        {

                                Document doc = hits.doc(start);
                                doc.add(Field.Text("rank", String.valueOf(hits.score(start))));
                                String path = doc.get("path");
                                if (path != null)
                                {
                                        System.out.println(start + ". " + path);
                                }
                                else
                                {

                                        as[start] = doc;
                                }
                        }

                }
                catch (Exception e)
                {
                        System.out.println(" caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                }
                end();
                return as;
        }

        public Document[] query(String qu, String field) throws IOException
        {
                start();

                Document[] as = null;
                try
                {

                        PhraseQuery pq =new PhraseQuery();
                        StringTokenizer st =new StringTokenizer(qu,"?");
                        int i=0;
                        while(st.hasMoreElements()&&i<3)
                        {
                                pq.add(new Term(field,String.valueOf(st.nextElement()).toLowerCase()));
                                i++;
                        }

                        query = pq;
                        //System.out.println(qu);
                        //System.out.println("Searching for: "+ query.toString(field));
                        Hits hits = mSearcher.search(query);
                        //System.out.println(hits.length() + " total matching documents");
                        as = new Document[hits.length()];

                        for (int start = 0; start < hits.length(); start++)
                        {

                                Document doc = hits.doc(start);
                                ;
                                doc.add(Field.Text("rank", String.valueOf(hits.score(start))));
                                String path = doc.get("path");
                                if (path != null)
                                {
                                        System.out.println(start + ". " + path);
                                }
                                else
                                {

                                        as[start] = doc;
                                }
                        }

                }
                catch (Exception e)
                {
                        System.out.println(" caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                }
                end();
                return as;
        }

        /**
         *
         * @param qu
         * @return Document
         * @throws IOException
         */
        public Document[] query(BooleanQuery qu) throws IOException
        {
                return this.query(qu.toString("contents"));

        }

        /**
         *
         */
        public void close()
        {
                try
                {
                        this.mSearcher.close();
                        searcher1.close();
                        searcher2.close();

                }
                catch (Exception e)
                {
                        System.out.println(" caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                }
        }
}
