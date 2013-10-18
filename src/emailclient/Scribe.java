package emailclient;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.UIManager;
import emailclient.gui.*;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */

public class Scribe
{
        boolean packFrame = false;

        //Construct the application
        public Scribe()
        {
                Scribe2F frame = new Scribe2F();
                //Validate frames that have preset sizes
                //Pack frames that have useful preferred size info, e.g. from their layout
                if (packFrame)
                {
                        frame.pack();
                }
                else
                {
                        frame.validate();
                }
                //Center the window
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension frameSize = frame.getSize();
                if (frameSize.height > screenSize.height)
                {
                        frameSize.height = screenSize.height;
                }
                if (frameSize.width > screenSize.width)
                {
                        frameSize.width = screenSize.width;
                }
                frame.setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
                frame.setVisible(true);
        }

        //Main method
        public static void main(String[] args)
        {
                try
                {

                        UIManager.getLookAndFeel();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
                new Scribe();
        }
}