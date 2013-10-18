package mailthread;

import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Session;
import java.util.Date;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.mail.internet.MimeMessage;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import emailclient.*;
import ir.*;
import java.util.Enumeration;


/**
 * <p>Title: Scribe</p>
 * <p>Description: Mail Client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian Mclaughlin
 * @version 0.1
 */

public class MailThread2
{
        Document[] docs;
        QueryEmails2 qe;
        Properties mailprop;
        IndexReader reader1;
        IndexReader reader2;
        Session session;
        private DefaultMutableTreeNode thread1;
        private DefaultMutableTreeNode thread2;
        /**
         * <p>Constructor</p>
         * @param mailprop
         * @param session
         * @throws Exception
         */
        public MailThread2(Properties mailprop, Session session) throws Exception
        {
                this.mailprop = mailprop;
                this.start();
                this.session = session;
                docs = getReplies();
                this.stop();

                if (!(docs.length<=0))
                {

                        int u=0;
                        qe = new QueryEmails2(mailprop.getProperty("index") + mailprop.getProperty("defaultINBOX"),mailprop.getProperty("index") + mailprop.getProperty("defaultOUTBOX"));
                        Vector rt = findRoot(docs);
                        thread1=this.Final(rt);

                }
                thread2=this.findGroupThread();
                stop();


        }

        /**
         * <p>Secures Access to the Index</p>
         * @throws Exception
         */
        private void start() throws Exception
        {
                //reader1 = IndexReader.open(mailprop.getProperty("index") + mailprop.getProperty("defaultINBOX"));
                //reader2 = IndexReader.open(mailprop.getProperty("index") + mailprop.getProperty("defaultOUTBOX"));
                reader1=EmailIndex.getIndexReader(mailprop.getProperty("index") + mailprop.getProperty("defaultINBOX"));
                reader2=EmailIndex.getIndexReader(mailprop.getProperty("index") + mailprop.getProperty("defaultOUTBOX"));
        }

        /**
         * <p>Releases access to the Index</p>
         * @throws Exception
         */
        private void stop() throws Exception
        {
                reader1.close();
                reader2.close();
        }

        /**
         * <p>Finds each reply's original email</p>
         * @param docc
         * @return Vector
         * @throws Exception
         */
        private Vector findRoot(Document[] docc) throws Exception
        {
                Vector threads = new Vector();
                int currDoc = 0;
                DefaultMutableTreeNode node2 = null;
                while (currDoc < docc.length && docc[currDoc] != null)
                {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new VisualNode(Email.Email(docc[currDoc], session)));
                        StringTokenizer st = new StringTokenizer(docc[currDoc].get("references"));
                        String rootRef = "";
                        qe.start();
                        while (st.hasMoreTokens())
                        {
                                rootRef = st.nextToken();
                        }
                        rootRef = preprocess(rootRef);
                        Document[] temp = qe.query(rootRef , "message-id");
                        Document temp0 = null;
                        if (temp.length > 0)
                        {
                                temp0 = temp[0];
                        }
                        if (temp0 != null)
                        {
                                //System.out.println(temp0.get("subject")+" "+temp0.get("sentdate"));
                                node2 = new DefaultMutableTreeNode(new VisualNode(Email.Email(temp0, session)));
                                node2.add(node);

                                threads.addElement(node2);

                        }

                        currDoc++;

                }
                qe.close();
                return threads;

        }

        /**
         * <p>Tries to remove special characters from the string</p>
         * <p>That the search engine might misinterpreter</p>
         * @param query
         * @return String
         */
        private String preprocess(String query)
        {
                char[] espchars =
                        {
                        '$','+', '-', '&', '|', '(', ')','.', '{', '}', '^', '"', '~', '*', ':', '\\','<','>','@'};


                int i = 0;
                int head=0;
                int length = query.length();
                char[] preq =query.toCharArray();
                while(head <length)
                {

                        i = 0;
                        while (i < espchars.length)
                        {

                                if (preq[head]==espchars[i])
                                {
                                        preq[head]='?';
                                        i=espchars.length;
                                }
                                i++;
                        }
                        head++;
                }

                query = String.valueOf(preq);
                //System.out.println(query);
                return query;

        }

        /**
         * <p>Retrives all emails that are replies by looking at their 'references' section</p>
         * @return Document[]
         * @throws Exception
         */
        private Document[] getReplies() throws Exception
        {
                Vector tempDocs = new Vector();
                int indexLength = reader1.numDocs();
                int i = 0;
                while (i < indexLength)
                {
                        if (!reader1.document(i).get("references").equals("none"))
                        {
                                tempDocs.addElement(reader1.document(i));
                        }
                        i++;
                }
                i=0;
                indexLength=reader2.numDocs();
                while (i < indexLength)
                {
                        if (!reader2.document(i).get("references").equals("none"))
                        {
                                tempDocs.addElement(reader2.document(i));
                        }
                        i++;
                }

                Document[] as = new Document[tempDocs.size()];
                i = 0;
                while (i < as.length)
                {
                        as[i] = (Document) tempDocs.elementAt(i);
                        i++;
                }
                return as;
        }

        /**
         * <p>Returns a tree structure contains the mail threads</p>
         * @return DefaultMutableTreeNode
         * @throws Exception
         */
        public DefaultMutableTreeNode getMailThreads() throws Exception
        {
                return thread1;
        }
        public DefaultMutableTreeNode getMailGroups() throws Exception
        {
                return thread2;
        }


        /**
         * <p>takes all the double trees and builds a single tree</p>
         * @param threads
         * @return DefaultMutableTreeNode
         * @throws Exception
         */
        private DefaultMutableTreeNode Final(Vector threads) throws Exception
        {
                DefaultMutableTreeNode top = new DefaultMutableTreeNode("Mail Threads");
                DefaultMutableTreeNode curr = null;
                int i = 0;
                while (i < threads.size())
                {
                        curr = (DefaultMutableTreeNode) threads.elementAt(i);
                        top = attachtoTree(top, curr);
                        i++;
                }
                return top;
        }

        /**
         * <p>Searches a tree looking for a node</p>
         * @param tree1
         * @param tree2
         * @return int
         * @throws Exception
         */
        private int findNode(DefaultMutableTreeNode tree1, DefaultMutableTreeNode tree2) throws Exception
        {
                int treeSize = tree1.getLeafCount()-1;
                int i = 0;
                while (i < treeSize)
                {
                        if (treeSize != 0)
                        {
                                DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) tree1.getChildAt(i);
                                VisualNode n = (VisualNode) leaf.getUserObject();
                                VisualNode n2 = (VisualNode) tree2.getUserObject();
                                System.out.println("is "+n2+" equal to "+n);
                                if(n.getMimeMessage().getMessageID().equals(n2.getMimeMessage().getMessageID()))
                                {

                                        return tree1.getIndex(leaf);
                                }
                                else
                                {
                                }
                                i++;

                        }

                }
                //System.out.println(tree2+" doesn't exists in "+tree1);
                return -1;
        }
        private DefaultMutableTreeNode attachtoTree(DefaultMutableTreeNode tree1, DefaultMutableTreeNode tree2) throws Exception
        {
                Vector queue=new Vector();
                VisualNode n2 = (VisualNode) tree2.getUserObject();
                for(int i=0;i<tree1.getChildCount();i++)
                 {
                         queue.addElement(tree1.getChildAt(i));
                 }
                while(!queue.isEmpty()&&tree1.getChildCount()!=0)
                {
                        DefaultMutableTreeNode node= (DefaultMutableTreeNode) queue.lastElement();
                        queue.remove(queue.size()-1);
                        if(!(node.getUserObject() instanceof String))
                        {
                                VisualNode n = (VisualNode) node.getUserObject();
                                if(n.getMimeMessage().getMessageID().equals(n2.getMimeMessage().getMessageID()))
                                {

                                        node.add(tree2.getNextNode());
                                        return tree1;
                                }
                                else
                                {
                                        for(int i=0;i<node.getChildCount();i++)
                                        {
                                                queue.addElement(node.getChildAt(i));
                                        }
                                }

                        }
                }
                tree1.add(tree2);
                return tree1;
        }
        public DefaultMutableTreeNode findGroupThread() throws Exception
        {

                start();
                Vector tempDocs = new Vector();
                int indexLength = reader1.numDocs();
                int i = 0;
                while (i < indexLength)
                {
                        if (!reader1.document(i).get("reply").equals("none"))
                        {
                                tempDocs.addElement(reader1.document(i));

                        }
                        i++;
                }
                i=0;
                indexLength=reader2.numDocs();
                while (i < indexLength)
                {
                        if (!reader2.document(i).get("reply").equals("none"))
                        {
                                tempDocs.addElement(reader2.document(i));

                        }
                        i++;
                }
                if(!(tempDocs.size()<=0))
                {
                        Document[] as = new Document[tempDocs.size()];
                        i = 0;
                        while (i < as.length)
                        {
                                as[i] = (Document) tempDocs.elementAt(i);
                                i++;
                        }
                        stop();
                        //group similar threads
                        i=0;
                        mailgroups mgs= new mailgroups();
                        mgs.addGroup(new mailgroup(as[i].get("reply")));
                        mgs.getGroup(i).addMail(as[i]);
                        i++;
                        while(i<as.length)
                        {

                                String reply=as[i].get("reply");
                                int test=mgs.findMailgroup(reply);
                                if(test!=-1)
                                {
                                        mgs.getGroup(test).addMail(as[i]);
                                }
                                else
                                {
                                        mgs.addGroup(new mailgroup(as[i].get("reply")));
                                        mgs.getGroup(mgs.length()-1).addMail(as[i]);
                                }
                                i++;
                        }
                        DefaultMutableTreeNode mtop=new DefaultMutableTreeNode("mail groups");
                        i=0;
                        while(i<mgs.length())
                        {
                                mailgroup test=mgs.getGroup(i);
                                if(!(test.length()<2))
                                {
                                        int j=0;
                                        DefaultMutableTreeNode nNode=new DefaultMutableTreeNode(test.getGroup());
                                        while(j<test.length())
                                        {
                                                nNode.add(new DefaultMutableTreeNode(new VisualNode(Email.Email(test.getMail(j),session))));
                                                j++;
                                        }
                                        mtop.add(nNode);
                                }
                                i++;
                        }
                        return mtop;
                }
                return new DefaultMutableTreeNode();
        }
        public class mailgroup
        {
                private Vector mail=new Vector();
                private String group;
                public mailgroup(String group)
                {
                        this.group=group;
                }
                public String getGroup()
                {
                        return this.group;
                }
                public void addMail(Document doc)
                {
                        mail.addElement(doc);
                }
                public void addMail(Document doc,int i)
               {
                       mail.setElementAt(doc,i);
               }
               public int length()
                {
                        return mail.size();
                }

                public Document getMail(int i)
                {
                        return (Document)mail.elementAt(i);
                }
                public Date getDate(Document doc)
                {
                        String sdate=doc.get("date");
                        return new Date(sdate);
                }
                public Document[] getMail()
                {
                        Object[] temp =new Object[mail.size()];
                        int head=0;
                        int tail;
                        while(!(head>mail.size()+1))
                        {
                                tail=head+1;
                                while(tail<mail.size())
                                {
                                        if(getDate(getMail(head)).after(getDate(getMail(tail))))
                                        {
                                                Document dtemp=getMail(tail);
                                                addMail(getMail(head),tail);
                                                addMail(dtemp,head);
                                        }
                                        tail++;
                                }
                                head++;

                        }
                        mail.copyInto(temp);
                        return (Document[])temp;
                }

        }
        public class mailgroups
        {
                Vector groups=new Vector();
                public mailgroups()
                {
                }
                public void addGroup(mailgroup mg)
                {
                        groups.addElement(mg);
                }
                public mailgroup getGroup(int i)
                {
                        return (mailgroup)groups.elementAt(i);
                }
                public int findMailgroup(String group)
                {
                        int i=0;

                        while(i<groups.size())
                        {
                                if(((mailgroup)groups.elementAt(i)).getGroup().equals(group))
                                {
                                        return i;
                                }
                                i++;
                        }
                        return -1;
                }
                public int length()
                {
                        return groups.size();
                }
                public Vector mailgroups()
                {
                        return groups;
                }
        }



}