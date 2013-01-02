import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.DefaultCaret;

import org.fife.ui.rtextarea.RTextScrollPane;
public class Main{
	
	String userName=null;
	File userFile;
	//the output JTextArea
	
	//  for java 6
	// JTextArea output=new JTextArea(230,5);
	
	//for java 7
	JTextArea output=new JTextArea(35,5);
	 
	int maxLabels=50;
	
	JMenuBar menuBar=new JMenuBar();
	JMenu optionMenu=new JMenu("Options");
	JMenuItem qUp=new JMenuItem("Upload Question");
	JMenuItem submit=new JMenuItem("Submit");
	
	JFrame frame=new JFrame("Code Magnet");
	
	//mainPanel consists of Output and question text areas
	JPanel mainPanel=new JPanel();
	
	//panel consists of question text areas
	JPanel panel=new JPanel(new GridLayout(maxLabels,1,0,2));
	
	JTextArea area[]=new JTextArea[maxLabels];
	JScrollPane lUpScroller = new JScrollPane(output);
	JScrollPane lDownScroller = new JScrollPane(panel);
	
	//RSTA right=new RSTA(5,45);
	RSTA right=new RSTA(5,45);
	RTextScrollPane rscroller = new RTextScrollPane(right);
	
	int time=0;
	Thread timer=new Thread(new timerJob(this));
	
	public static void main(String[] args){
		new Main().go();
	}
	
	void go(){
		System.out.println("Working Directory: "+System.getProperty("user.dir"));
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, fall back to cross-platform
		    try {
		        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		    } catch (Exception ex) {
		        System.out.println("Error setting the look and feel");
		        ex.printStackTrace();
		    }
		}
    	
		DefaultCaret caret1 = (DefaultCaret)output.getCaret();
		caret1.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		
		lDownScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	lDownScroller.getVerticalScrollBar().setUnitIncrement(36);
    	
    	lUpScroller.getVerticalScrollBar().setUnitIncrement(3);
    	
		output.setEditable(false);
    	output.setDragEnabled(false);
    	//output.setFont(new Font("Courier New",Font.BOLD,16));
    	output.setFont(new Font("Monospaced",Font.BOLD,14));
    	
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		right.setAnimateBracketMatching(true);
		right.setAutoIndentEnabled(true);
		//right.setFont(new Font("Courier New",Font.PLAIN,15));
		right.setFont(new Font("Monospaced",Font.PLAIN,15));
		right.setCloseCurlyBraces(false);
		
		rscroller.getVerticalScrollBar().setUnitIncrement(16);
		submit.setEnabled(false);
		
		//diable typing - allow only enter and backspace
		right.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e1){
				char c=e1.getKeyChar();
				if ( (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_ENTER) ) 
			         e1.consume();  // ignore event
			}			
		});
		//KeyStroke remove = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK);
		//InputMap im = right.getInputMap();
		//im.put(remove, "none");
		
		qUp.addActionListener(new qUpListener(this));
		submit.addActionListener(new submitListener(this));
		
		for(int i=0;i<maxLabels;i++){
			
			//area[i]=new JTextArea(3,50);
			area[i]=new JTextArea(3,50);
			DefaultCaret caret = (DefaultCaret)area[i].getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
			//area[i].setFont(new Font("Courier New",Font.PLAIN,14));
			area[i].setFont(new Font("Monospaced",Font.PLAIN,14));
			area[i].setEditable(false);
			area[i].setTransferHandler(new TransferHandler("text"));
			area[i].setDropTarget(null);
	    	area[i].addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent e){
								JTextArea a = (JTextArea)e.getSource();
								TransferHandler handle = a.getTransferHandler();
								handle.exportAsDrag(a, e, TransferHandler.COPY);
								
				           }
				 });
	    	area[i].setLineWrap(true);
	    	area[i].setWrapStyleWord(true);
			area[i].setDragEnabled(true);
			panel.add(area[i]);
			
		}
		
		optionMenu.add(qUp);
		optionMenu.add(submit);
		menuBar.add(optionMenu);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(885, 700);
		frame.setLocation(50, 5);
		frame.setJMenuBar(menuBar);
		mainPanel.add(lUpScroller);
		mainPanel.add(lDownScroller);
		frame.getContentPane().add(BorderLayout.WEST,mainPanel);
		frame.getContentPane().add(BorderLayout.EAST,rscroller);
		frame.addWindowListener(new myWindowListener(this));
		frame.setVisible(true);
		frame.setResizable(false);
		
	}
}