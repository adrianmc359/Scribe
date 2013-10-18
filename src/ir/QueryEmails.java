package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.MultiSearcher;

public class QueryEmails
{

        private Searcher searcher;
        private Analyzer analyzer;
        private Query query;
        private Hits hits;
        private String mailfolder;
        public QueryEmails(String mailfolder)
        {
                this.mailfolder = mailfolder;
        }

        /**
         * Initialises the Searcher
         * @throws IOException
         */
        public void start() throws IOException
        {
                searcher = new IndexSearcher(this.mailfolder);
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
                        query = QueryParser.parse(qu, "contents", analyzer);

                        Hits hits = searcher.search(query);
                        as = new Document[hits.length()];

                        for (int start = 0; start < hits.length(); start++)
                        {

                                Document doc = hits.doc(start);
                                doc.add(Field.Text("rank", String.valueOf(hits.score(start))));
                                String path = doc.get("path");
                                if (path != null)
                                {
                                        //System.out.println(start + ". " + path);
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
                        query = QueryParser.parse(qu, field, analyzer);
                        //System.out.println(qu);
                        //System.out.println("Searching for: " + query.toString(field));
                        Hits hits = searcher.search(query);
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
                                        //System.out.println(start + ". " + path);
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
                        this.searcher.close();

                }
                catch (Exception e)
                {
                        System.out.println(" caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                }
        }
}