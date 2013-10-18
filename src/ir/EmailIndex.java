package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: Mail Client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian Mclaughlin
 * @version 0.1
 */
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;

public class EmailIndex
{

        /**
         * <p>Rebuilding Index from scratch from mailfolder</p>
         * @param mailfolder String
         * @param em MimeMessage[]
         */
        public EmailIndex(String mailfolder)
        {
        }
        public EmailIndex(String mailfolder, MimeMessage[] em)
        {
                try
                {
                        Date start = new Date();
                        //Index location is hardcoded as of this instance
                        IndexWriter writer = openIndex(mailfolder, true);
                        //calls internal method
                        for (int i = 0; i < em.length; i++)
                        {
                                indexDocs(writer, em[i]);
                        }
                        //Finishes up index and exits
                        writer.optimize();
                        writer.close();

                        Date end = new Date();

                }
                catch (IOException e)
                {
                        if (e.getMessage().startsWith("Cannot create directory"))
                        {
                                File dir = new File(mailfolder);
                                try
                                {
                                        dir.createNewFile();

                                }
                                catch (IOException error)
                                {
                                        System.err.println("The necessary folder doesn't exist " + mailfolder);
                                }
                        }
                        else
                        {

                        }
                }

                catch (Exception e)
                {
                        System.out.println("EmailIndex: caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                        e.printStackTrace();

                }
        }
        /**
         * <p>Adding one email to  an already created Index</p>
         * @param mailfolder String
         * @param em MimeMessage
         */
        public EmailIndex(String mailfolder, MimeMessage em)
        {
                try
                {
                        Date start = new Date();
                        //Index location is hardcoded as of this instance
                        IndexWriter writer = openIndex(mailfolder, false);
                        //calls internal method
                                indexDocs(writer,em);
                        //Finishes up index and exits
                        writer.optimize();
                        writer.close();

                        Date end = new Date();

                }
                catch (IOException e)
                {
                        if (e.getMessage().startsWith("Cannot create directory"))
                        {
                                File dir = new File(mailfolder);
                                try
                                {
                                        dir.createNewFile();

                                }
                                catch (IOException error)
                                {
                                        System.err.println("The necessary folder doesn't exist " + mailfolder);
                                }
                        }

                }

                catch (Exception e)
                {
                        System.out.println("EmailIndex: caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                        e.printStackTrace();

                }
        }
        public synchronized IndexWriter  openIndex(String mailfolder,boolean New)
        {


                try
                {
                        return new IndexWriter(mailfolder, new MyAnalyser(), New);

                }
                catch (Exception e)
                {
                        System.out.println("EmailIndex: caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                        e.printStackTrace();

                }
                return null;

        }
        public static  synchronized IndexReader getIndexReader(String mailfolder)
        {
                try
                {
                        if(!IndexReader.isLocked(mailfolder))
                        {
                                return IndexReader.open(mailfolder);
                        }
                }
                catch(IOException e)
                {
                       System.err.println("Remove firsttime file to stop this error");

                }
                catch (Exception e)
                {
                        System.out.println("EmailIndex: caught a " + e.getClass() +
                                           "\n with message: " + e.getMessage());
                        e.printStackTrace();

                }
                return null;

        }


        /**
         * <p>Does the actually indexing</p>
         * @param writer IndexWriter
         * @param em MimeMessage
         * @throws Exception
         */
        public static void indexDocs(IndexWriter writer, MimeMessage em) throws Exception
        {
                writer.addDocument(EmailDoc.EmailDoc(em));
        }

}
