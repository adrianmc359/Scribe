package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Hashtable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import java.io.*;

public class MyAnalyser extends Analyzer
{

        /**
         * An array containing some common words that
         * are not usually useful for searching.
         */
        private static final String[] STOP_WORDS =
                {

                "<", ">", "about", "above", "according", "across"
                , "actually", "add", "adj", "advice", "after", "afterwards",
                "again", "against", "agree", "ahead", "all", "almost", "alone"
                , "along", "already", "also", "although", "always",
                "among", "amongst", "an", "another", "any",
                "anyhow", "anyone", "anything", "anywhere", "are",
                "aren't", "around", "as", "at", "away", "be", "became", "because", "become",
                "becomes", "becomin", "been", "before", "beforehand",
                "begin", "beginning", "behind", "being", "below", "beside"
                , "besides", "best", "between", "beyond", "billion", "both",
                "but", "by", "can", "can't", "cannot", "caption", "co",
                "co.", "come", "coming", "continually", "could", "couldn't",
                "did", "didn't", "do", "doing", "does", "doesn't", "don't", "down", "during", "each",
                "eg", "eight", "eighty", "either", "else", "elsewhere",
                "ending", "enough", "etc", "even", "ever", "every", "everyone"
                , "everything", "everywhere", "except", "few", "fifty", "first"
                , "five", "for", "former", "formerly", "forty", "found", "four"
                , "from", "further", "get", "give", "given", "go", "going", "had", "has", "hasn't", "have", "haven't"
                , "he", "he'd", "he'll", "he's", "hence", "her", "here", "here's"
                , "hereafter", "hereby", "herein", "hereupon", "hers", "herself"
                , "him", "himself", "his", "how", "however", "huge", "hundred", "i", "i'd",
                "i'll", "i'm", "i've", "ie", "if", "in", "inc.", "including", "indeed", "instead"
                , "into", "is", "isn't", "it", "it's", "its", "itself", "last", "later"
                , "latter", "latterly", "least", "less", "let", "let's", "like", "likely"
                , "ltd", "made", "make", "makes", "many", "maybe", "me", "meantime",
                "meanwhile", "might", "million", "miss", "more", "moreover", "most",
                "mostly", "mr", "mrs", "much", "must", "my", "myself", "namely", "neither"
                , "never", "nevertheless", "next", "nine", "ninety", "no", "nobody", "none"
                , "nonetheless", "none", "nor", "not", "nothing", "now", "nowhere", "of",
                "off", "often", "on", "once", "one", "one's", "only", "onto",
                "or", "other", "others", "otherwise", "our", "ours", "ourselves"
                , "out", "over", "overall", "own", "particularly", "per", "perhaps", "rather",
                "recent", "recently", "says", "said", "same", "see", "seem", "seen", "seemed",
                "seeming", "seems", "seven",
                "seventy", "several", "she", "she'd", "she'll", "she's", "should",
                "shouldn't", "since", "six", "sixty", "so", "some", "somehow", "someone"
                , "something", "sometime", "sometimes", "somewhere", "small", "still", "stop",
                "such", "taking", "ten", "than", "that", "that'll", "that's", "that've",
                "the", "their", "them", "themselves", "then", "thence", "there", "there'd"
                , "there'll", "there're", "there's", "there've", "thereafter", "thereby"
                , "therefore", "therein", "thereupon", "these", "they", "they'd", "they'll"
                , "they're", "they've", "think", "thirty", "this", "those", "though", "thousand",
                "three", "through", "throughout", "thru", "thus", "tiny", "together", "to", "too", "took"
                , "toward", "towards", "trillion", "twenty", "two", "under", "unless", "unlike"
                , "unlikely", "until", "up", "upon", "us", "used", "using", "vast", "very", "via", "was"
                , "wasn't", "we", "we'd", "we'll", "we're", "we've", "well", "were", "weren't"
                , "what", "what'll", "what's", "what've", "whatever", "when", "whence",
                "whenever", "where", "where's", "whereafter", "whereas", "whereby", "wherein"
                , "whereupon", "wherever", "whether", "which", "while", "whither", "who", "who'd"
                , "who'll", "who's", "whoever", "whole", "whom", "whomever", "whose", "why",
                "will", "with", "within", "without", "won't", "would", "wouldn't",
                "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "end",
                "yourself"
        };

        /*
         * Stop table
         */
        final static private Hashtable stopTable = StopFilter.makeStopTable(STOP_WORDS);

        /**
         *
         * @param reader
         * @return TokenStream
         */
        public final TokenStream tokenStream(final Reader reader)
        {
                TokenStream result = new StandardTokenizer(reader);
                result = new StandardFilter(result);
                result = new LowerCaseFilter(result);
                result = new StopFilter(result, stopTable);
                result = new PorterStemFilter(result);
                return result;
        }

        public static void main(String[] args) throws Exception
        {
                MyAnalyser my = new MyAnalyser();
                StandardAnalyzer ty = new StandardAnalyzer();
                FileReader filereader = new FileReader(new File("C:/Documents and Settings/Adrian/Scribe/Mail/INBOX/167"));
                final Reader reader = new BufferedReader(filereader);
                final TokenStream in = my.tokenStream(reader);
                final TokenStream in2 = ty.tokenStream(reader);

                //String New="";
                int New = 0;
                String to;
                for (; ; )
                {
                        final org.apache.lucene.analysis.Token token = in.next();

                        if (token == null)
                        {
                                break;
                        }
                        to = token.termText();
                        //New=New+to;
                        New++;
                        System.out.println("[" + to + "]");
                }
                System.out.println("---------------------------------------------");
                //System.out.println("Length of original="+test.length());
                System.out.println("Length of New=" + New);
                //System.out.println("Ratio="+Math.round(((double)New.length()/test.length())*100)+"%");

        }
}
