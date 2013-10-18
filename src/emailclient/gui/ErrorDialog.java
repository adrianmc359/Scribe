package emailclient.gui;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import emailclient.*;

/**
 * <p>Title: Scribe</p>
 * <p>Description: An email client with IR</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UCD</p>
 * @author Adrian McLaughlin
 * @version 0.1
 */


public class ErrorDialog extends JDialog
{
        JLabel jLabel1 = new JLabel();
        JButton jButton1 = new JButton();
        JScrollPane jScrollPane1 = new JScrollPane();
        JTextPane jTextPane1 = new JTextPane();
        public ErrorDialog() throws HeadlessException
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
                jLabel1.setText("     Error Dialog");
                jButton1.setText("Close");
                jButton1.addMouseListener(new java.awt.event.MouseAdapter()
                {
                        public void mouseClicked(MouseEvent e)
                        {
                                jButton1_mouseClicked(e);
                        }
                });
                jTextPane1.setEditable(false);
                this.getContentPane().add(jLabel1, BorderLayout.NORTH);
                this.getContentPane().add(jButton1, BorderLayout.SOUTH);
                this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
                jScrollPane1.getViewport().add(jTextPane1, null);
                this.setSize(500, 250);

        }
        /**
         *
         * @param message error message
         */
        public void setError(String message)
        {
                jTextPane1.setText(jTextPane1.getText() + "\n" + message + "\n");
        }

        void jButton1_mouseClicked(MouseEvent e)
        {
                this.dispose();
        }

}
