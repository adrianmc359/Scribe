package mailthread;

/**
 * Title:           Scribe
 * Description:     An email client with IR
 * Company:         ucd
 * @author          Adrian McLaughlin
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Toolkit;

public class ThreadsW extends JPanel
{
        Toolkit k =java.awt.Toolkit.getDefaultToolkit();
        BorderLayout borderLayout1 = new BorderLayout();
        JLabel labelName = new JLabel("From:");
        JLabel labelID = new JLabel("Message-ID:");
        JSplitPane splitPane = new JSplitPane();
        JSplitPane splitJTree =new JSplitPane();
        JScrollPane js0 = new JScrollPane();
        JScrollPane js2 = new JScrollPane();
        JScrollPane js1;
        JTextPane Contents = new JTextPane();
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Mail Threads");
        Dimension minimumSize = new Dimension(100, 50);
        DefaultMutableTreeNode nodes;
        DefaultMutableTreeNode mailgroupnodes;
        String name;
        BorderLayout borderLayout2 = new BorderLayout();
        JPanel temp = new JPanel();

        /**
         * <p>Constructor</p>
         * @param nodes
         * @param name
         */
        public ThreadsW(MailThread2 thread, String name)
        {
                try
                {
                        this.nodes = thread.getMailThreads();
                        this.mailgroupnodes=thread.getMailGroups();
                        this.name = name;
                        jbInit();
                }
                catch (Exception ex)
                {
                        ex.printStackTrace();
                }
        }

        /**
         * <p> Returns the name of the JPanel</p>
         * @return String
         */
        public String getName()
        {
                return name;
        }

        /**
         * <p>Initialises the JPanel</p>
         * @throws Exception
         */
        public void jbInit() throws Exception
        {
                this.setLayout(borderLayout1);
                temp.setLayout(borderLayout2);
                splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                splitJTree.setOrientation(JSplitPane.VERTICAL_SPLIT);
                final JTree tree1 = new JTree(nodes);
                final JTree tree2 = new JTree(mailgroupnodes);
                tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                tree2.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                tree1.addTreeSelectionListener(new TreeSelectionListener()
                {
                        public void valueChanged(TreeSelectionEvent e)
                        {
                                try
                                {
                                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                                tree1.getLastSelectedPathComponent();

                                        if (node == null)
                                        {
                                                return;
                                        }

                                        if (!node.isRoot())
                                        {
                                                String nodeInfo = String.valueOf( ( (VisualNode) node.getUserObject()).getMimeMessage().getContent());
                                                String sender = String.valueOf( ( (VisualNode) node.getUserObject()).getMimeMessage().getFrom()[0]);
                                                String messageid = String.valueOf( ( (VisualNode) node.getUserObject()).getMimeMessage().getMessageID());
                                                displayContents(nodeInfo, sender, messageid);
                                        }
                                }
                                catch (Exception error)
                                {
                                        error.printStackTrace();
                                }
                        }
                });
                tree2.addTreeSelectionListener(new TreeSelectionListener()
                {
                        public void valueChanged(TreeSelectionEvent e)
                        {
                                try
                                {
                                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                                tree2.getLastSelectedPathComponent();

                                        if (node == null)
                                        {
                                                return;
                                        }

                                        if (!node.isRoot()&&node.isLeaf())
                                        {
                                                String nodeInfo = String.valueOf( ( (VisualNode) node.getUserObject()).getMimeMessage().getContent());
                                                String sender = String.valueOf( ( (VisualNode) node.getUserObject()).getMimeMessage().getFrom()[0]);
                                                String messageid = String.valueOf( ( (VisualNode) node.getUserObject()).getMimeMessage().getMessageID());
                                                displayContents(nodeInfo, sender, messageid);
                                        }
                                }
                                catch (Exception error)
                                {
                                        error.printStackTrace();
                                }
                        }
                });

                js1 = new JScrollPane(tree1);
                js2 = new JScrollPane(tree2);
                splitJTree.add(JSplitPane.TOP,js1);
                splitJTree.add(JSplitPane.BOTTOM,js2);
                //Temp panel fix this

                temp.add(Contents, BorderLayout.CENTER);
                temp.add(labelName, BorderLayout.NORTH);
                temp.add(labelID, BorderLayout.SOUTH);
                js0.getViewport().add(temp);
                splitPane.setTopComponent(splitJTree);
                splitPane.setBottomComponent(js0);
                splitPane.setDividerLocation(300);
                splitJTree.setDividerLocation(300);
                this.add(splitPane);

        }

        private void displayContents(String info, String from, String messageid)
        {
                this.Contents.setText(info);
                Contents.setCaretPosition(0);
                this.updateUI();
                labelName.setText("From:" + from);
                labelID.setText("Message-ID:" + messageid);
        }

}
