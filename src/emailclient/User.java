package emailclient;
/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

import java.io.IOException;

public class User
{
        private Account ac;
        private String name;
        private String defdir;
        public User(String name, Account ac) throws IOException
        {
                this.name = name;
                this.ac = ac;
                this.defdir = System.getProperty("user.home");
        }

        public User(String name, Account ac, String defdir) throws IOException
        {
                this.name = name;
                this.ac = ac;
                this.defdir = defdir;
        }

        public Account getAccounts()
        {
                return ac;
        }

        public Account getAccount()
        {
                return ac;
        }

        public String getName()
        {
                return name;
        }

        public void setAccount(Account ac)
        {
                this.ac = ac;
        }

        public void setName(String name)
        {
                this.name = name;
        }

        public void setDir(String path) throws IOException
        {
                defdir = path;
        }

        public String getDir() throws IOException
        {
                return defdir;
        }

}
