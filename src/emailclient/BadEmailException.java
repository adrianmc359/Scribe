package emailclient;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */


public class BadEmailException extends Throwable
{
        public BadEmailException()
        {
                super("Badly Formatted Email");
        }
        public static void main(String[] args)
        {
                BadEmailException badEmailException1 = new BadEmailException();
        }

}