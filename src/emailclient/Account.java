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

public class Account
{
        private String server;
        private String username;
        private String pass;
        private String emailaddress;
        /**
         *<p>Default Constructor</p>
         * @throws IOException
         */
        public Account() throws IOException
        {
                server = "none";
                username = "none";
                pass = "none";
                emailaddress = "unknown@unknown.com"; ;
        }

        /**
         *
         * @param server Server Name
         * @param username User Name
         * @param pass Password
         * @param emailaddress Email Address
         * @throws IOException
         */
        public Account(String server, String username, String pass, String emailaddress) throws IOException
        {
                this.server = server;
                this.username = username;
                this.pass = pass;
                this.emailaddress = emailaddress;
        }

        /**
         *<p>Returns Email Address</p>
         * @return String
         *
         */
        public String getEmailAddress()
        {
                return emailaddress;
        }

        /**
         *
         * @return String
         */
        public String getServer()
        {
                return server;
        }

        /**
         *
         * @return String
         */
        public String getUserName()
        {
                return username;
        }

        /**
         *
         * @return String
         */
        public String getPassword()
        {
                return pass;
        }

        /**
         *
         * @param emailaddress Email Address
         */
        public void setEmailAddress(String emailaddress)
        {
                this.emailaddress = emailaddress;
        }

        /**
         *
         * @param server Server
         */
        public void setServer(String server)
        {
                this.server = server;
        }

        /**
         *
         * @param username User Name
         */
        public void setUserName(String username)
        {
                this.username = username;
        }

        /**
         *
         * @param password Password
         */
        public void setPassword(String password)
        {
                this.pass = password;
        }

        /**
         *
         * @return String
         */
        public String toString()
        {
                String printout = String.valueOf("Account Name:" + this.getServer() + "\n" + "Server:" + this.getServer() + "\n" + "User Name:" + this.getUserName() + "\n" + "Email Address:" + this.getEmailAddress() + "\n" + "Password:" + this.getPassword());
                return printout;
        }
}
