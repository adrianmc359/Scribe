package emailclient;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class UserPref
{
        private File datafile;
        private String name;
        private String homedir;
        private Account ac;
        private Vector myFile;
        public UserPref(File datafile) throws IOException
        {
                this.datafile = datafile;
                if (!datafile.exists())
                {
                        this.createDefaultFile();
                }

                ac = new Account();
                Parse();
        }

        public User getUser() throws IOException
        {
                return new User(name, ac, homedir);
        }

        public void setUser(User user)
        {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                try
                {

                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.newDocument();
                        Element eUser = (Element) doc.createElement("User");
                        Element eName = (Element) doc.createElement("name");
                        Element eHost = (Element) doc.createElement("host");
                        Element eUsername = (Element) doc.createElement("username");
                        Element eEmailAddress = (Element) doc.createElement("emailaddress");
                        Element ePassword = (Element) doc.createElement("password");
                        Element ePath = (Element) doc.createElement("path");
                        doc.appendChild(eUser);
                        eUser.appendChild(eName);
                        eUser.appendChild(eHost);
                        eUser.appendChild(eUsername);
                        eUser.appendChild(eEmailAddress);
                        eUser.appendChild(ePassword);
                        eUser.appendChild(ePath);
                        eName.appendChild(doc.createTextNode(user.getName()));
                        eHost.appendChild(doc.createTextNode(user.getAccount().getServer()));
                        eUsername.appendChild(doc.createTextNode(user.getAccount().getUserName()));
                        ePassword.appendChild(doc.createTextNode(user.getAccount().getPassword()));
                        eEmailAddress.appendChild(doc.createTextNode(user.getAccount().getEmailAddress()));
                        ePath.appendChild(doc.createTextNode(user.getDir()));
                        doc.getDocumentElement().normalize();

                        TransformerFactory tFactory = TransformerFactory.newInstance();
                        Transformer transformer = tFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(this.datafile);
                        transformer.transform(source, result);
                }
                catch (TransformerConfigurationException tce)
                { // Error generated by the parser
                        System.out.println("\n** Transformer Factory error");
                        System.out.println(" " + tce.getMessage()); // Use the contained exception, if any
                        Throwable x = tce;
                        if (tce.getException() != null)
                        {
                                x = tce.getException();
                        }
                        x.printStackTrace();
                }
                catch (TransformerException te)
                { // Error generated by the parser
                        System.out.println("\n** Transformation error");
                        System.out.println(" " + te.getMessage()); // Use the contained exception, if any
                        Throwable x = te;
                        if (te.getException() != null)
                        {
                                x = te.getException();
                        }
                        x.printStackTrace();
                }

                catch (ParserConfigurationException pce)
                { // Parser with specified options can't be built
                        pce.printStackTrace();
                }

                catch (IOException ioe)
                { // I/O error
                        ioe.printStackTrace();
                }

        }

        public void createDefaultFile() throws IOException
        {
                File Sdir = new File(System.getProperty("user.home") + "/Scribe");
                if (!Sdir.exists())
                {
                        Sdir.mkdir();
                        Sdir = new File(System.getProperty("user.home") + "/Scribe/index");
                        Sdir.mkdir();
                        Sdir = new File(System.getProperty("user.home") + "/Scribe/Mail");
                        Sdir.mkdir();
                }

                name = System.getProperty("user.name");
                this.homedir = System.getProperty("user.home");
                Account account = new Account("none", "none", "none", "none");
                this.setUser(new User(name, account, homedir));
        }

        public void Parse() throws IOException
        {
                Document document;
                try
                {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        document = builder.parse(datafile);
                        Element eUser = (Element) document.getFirstChild();
                        Element eName = (Element) eUser.getChildNodes().item(0);
                        Element eHost = (Element) eUser.getChildNodes().item(1);
                        Element eUsername = (Element) eUser.getChildNodes().item(2);
                        Element eEmailAddress = (Element) eUser.getChildNodes().item(3);
                        Element ePassword = (Element) eUser.getChildNodes().item(4);
                        Element ePath = (Element) eUser.getChildNodes().item(5);
                        name = eName.getFirstChild().getNodeValue();
                        ac.setServer(eHost.getFirstChild().getNodeValue());
                        ac.setUserName(eUsername.getFirstChild().getNodeValue());
                        ac.setEmailAddress(eEmailAddress.getFirstChild().getNodeValue());
                        ac.setPassword(ePassword.getFirstChild().getNodeValue());
                        this.homedir = ePath.getFirstChild().getNodeValue();

                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }

        }

}