package ir;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.2
 */
import ir.MyAnalyser;
import org.apache.lucene.index.Term;
import java.util.Vector;
import java.io.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.document.*;
import java.util.Properties;
public class QueryFormer2
{
      Analyzer ma;
      Vector token;
      BooleanQuery bq;
      QueryEmails qe;
      public QueryFormer2(Properties mailprop)
      {
            ma= new MyAnalyser();
            qe =new QueryEmails(mailprop.getProperty("index")+mailprop.getProperty("defaultINBOX"));
      }

      /**
       *
       * @param Text
       * @return Document[]
       */
       public String formQuery(String Text)
       {
	    bq=new BooleanQuery();
            String myQuery="";
	    String subQuery="";
	    token=new Vector();
	    try{

                  qe.start();
	          final Reader reader = new StringReader(Text);
	          final TokenStream in = ma.tokenStream(reader);

		  for(;;)
                  {
                        final org.apache.lucene.analysis.Token temptoken = in.next();
			if(temptoken==null)
			{
			      in.close();
			      break;
			}
			token.addElement(temptoken.termText());


                  }
		  for(int i=0;i<token.size()/2;i++)
                  {
                        Object temp=token.elementAt(i);
                        token.setElementAt(token.elementAt(token.size()-1-i),i);
                        token.setElementAt(temp,token.size()-1-i);
                  }
		//limits the query size to -(number) long
                int end=token.size();
                int section=15;
                if(end>section+5)
                {
                        end=end-15;
                }




	      if(end==1)
	      {
	          myQuery=String.valueOf(token.elementAt(0));
		  subQuery="subject:"+String.valueOf(token.elementAt(0));
	      }
              else
	      {
		  if(end==2)
                  {
                        myQuery=token.elementAt(0)+" "+token.elementAt(1);
			subQuery="subject:"+String.valueOf(token.elementAt(0));
                  }
		  else
		  {
			if(end >2)
			{
			      double sectionDiv=(double)end/3;
			      myQuery="";
			      int i=0;
			      while(i<Math.floor(sectionDiv))
			      {
				    myQuery=myQuery+token.elementAt(i)+"^4 ";
				    subQuery=subQuery+"subject:"+String.valueOf(token.elementAt(i))+"^5.0 ";
				    i++;
			      }
			      while(i<Math.floor(sectionDiv*2))
			      {
				    myQuery=myQuery+token.elementAt(i)+"^2 ";
				    i++;
			      }
			      while(i<Math.floor(sectionDiv*3)&&i<end)
			      {
				    myQuery=myQuery+token.elementAt(i)+" ";
				    i++;
			      }
			}
		  }

              }
                  if(myQuery=="")
                  {
                        myQuery="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
                  }

	    }
             catch(Exception e)
	    {
                  e.printStackTrace();
	    }

             return myQuery+" "+subQuery;
       }
      public Document[] Start(String Text)
      {
            Document as[]=new Document[10];
            try
            {

                  String myQuery=this.formQuery(Text);
                  Query query=QueryParser.parse(myQuery, "contents", ma);
                  bq.add(new BooleanClause(query,true,false));
		  if(token.size()>0)
                  {

                        as=qe.query(bq);
                  }


            }
	    catch(Exception e)
	    {
	          e.printStackTrace();
	    }
            qe.close();
            return as;


      }

}