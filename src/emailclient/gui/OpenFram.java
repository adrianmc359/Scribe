package emailclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Toolkit;
import emailclient.*;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */


public class OpenFram extends JFrame
{

        ImageIcon image1;
        BorderLayout borderLayout1 = new BorderLayout();
        JLabel jLabel1 = new JLabel();
        JLabel statusBar = new JLabel();
        Toolkit k =java.awt.Toolkit.getDefaultToolkit();
        double ScreenHeight=k.getScreenSize().getHeight();
        double ScreenWidth=k.getScreenSize().getWidth();
        public OpenFram()
        {

                try
                {
                        jbInit();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        private void jbInit() throws Exception
        {

                image1 = new ImageIcon(emailclient.gui.Scribe2F.class.getResource("Scribe.jpg"));
                jLabel1.setIcon(image1);
                this.getContentPane().setLayout(borderLayout1);
                statusBar.setBackground(new Color(231, 99, 116));
                statusBar.setSize(100, 100);
                this.setResizable(false);
                this.getContentPane().add(jLabel1, BorderLayout.CENTER);
                this.getContentPane().add(statusBar, BorderLayout.SOUTH);
                this.setSize(410, 370);
                this.toFront();
                this.setLocation((int)(this.ScreenWidth/3.5), (int)this.ScreenHeight/4);
        }
        /**
         * <p>to set the text on the bar beneath Image</p>
         * @param text String
         */
        public void setText(String text)
        {
                statusBar.setText(text);
                this.repaint();
        }

}