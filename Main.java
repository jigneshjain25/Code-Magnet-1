import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.DefaultCaret;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
public class Main{
	
	//the output JTextArea
	
	//  for eclipse 
	 	JTextArea output=new JTextArea(230,5);
	
	//for jar file
	//JTextArea output=new JTextArea(20,5);
	 
	int maxLabels=50;
	//String made from file selected by Upload answer , with all spaces removed
	String answer="";
	
	/**
	 * 	Options
	 * 	----Upload Question
	 *  ----Upload Answer
	 *  ----Submit
	 */
	JMenuBar menuBar=new JMenuBar();
	JMenu optionMenu=new JMenu("Options");
	JMenuItem qUp=new JMenuItem("Upload Question");
	JMenuItem aUp=new JMenuItem("Upload Answer");
	JMenuItem submit=new JMenuItem("Submit");
	
	JFrame frame=new JFrame("Code Magnet");
	
	//mainPanel consists of Output and question text areas
	JPanel mainPanel=new JPanel();
	
	//panel consists of question text areas
	JPanel panel=new JPanel(new GridLayout(maxLabels,1,0,2));
	
	JTextArea area[]=new JTextArea[maxLabels];
	JScrollPane lUpScroller = new JScrollPane(output);
	JScrollPane lDownScroller = new JScrollPane(panel);
	
	RSyntaxTextArea right=new RSyntaxTextArea(5,45);
	RTextScrollPane rscroller = new RTextScrollPane(right);
	
	int time=0;
	Thread timer=new Thread(new Runnable(){
		public void run(){
			while(true){
				time++;
				frame.setTitle("Code Magnet "+time);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});
	
	boolean f1=false,f2=false;
	
	public static void main(String[] args){
		new Main().go();
	}
	void go(){

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
		        // not worth my time
		    }
		}
    	
		lDownScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	lDownScroller.getVerticalScrollBar().setUnitIncrement(36);
    	
    	lUpScroller.getVerticalScrollBar().setUnitIncrement(3);
    	
		output.setEditable(false);
    	output.setDragEnabled(false);
    	output.setFont(new Font("Courier New",Font.BOLD,16));
    	
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		right.setAnimateBracketMatching(true);
		right.setAutoIndentEnabled(true);
		right.setFont(new Font("Courier New",Font.PLAIN,15));
		
		rscroller.getVerticalScrollBar().setUnitIncrement(16);
		submit.setEnabled(false);
		
		qUp.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileOpen=new JFileChooser();
				fileOpen.setDialogTitle("Upload Question");
				fileOpen.showOpenDialog(frame);
				File file=fileOpen.getSelectedFile();
               
                try{
                	String s=file.getPath();
                	BufferedReader br=new BufferedReader(new FileReader(s));
                	String l;
                	int count=0;
            
                	l=br.readLine().trim();
                	if(l.equals("0"))
                		right.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
                	else
                		right.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                	
                	while(!((l=(br.readLine())).trim().equals("end")))
                   		output.append(l+"\n");
                   	output.setText(output.getText().trim());
               	
                	while((l=br.readLine())!=null){
                			
                   		    l=l.trim();
                			if(l.equals("end"))
                   		    	count++;
                   		    else
                   		      area[count].append(l+"\n");
                    }
                	
                	qUp.setEnabled(false);
                	
                	f1=true;
                   	if(f1 && f2)	//indicates both question and answer have been uploaded
                	{
                   		panel.setVisible(false);
                   		output.setVisible(false);    
                   		JOptionPane.showConfirmDialog(frame, "Go Techno", "Code Magnet", -1);         		
                   		timer.start();
                   		output.setVisible(true);
                   		panel.setVisible(true);

                	}
                   	
                }catch(Exception ee){
                	ee.printStackTrace();
                }
                for(int i=0;i<maxLabels;i++)area[i].setText(area[i].getText().trim());
                for(int i=0;i<maxLabels;i++)area[i].append("\n");
				
				
			}
		});
		
		aUp.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileOpen=new JFileChooser();
				fileOpen.setDialogTitle("Upload Answer");
				fileOpen.showOpenDialog(frame);
				File file=fileOpen.getSelectedFile();
                try{
                    String s=file.getPath();
                    BufferedReader br=new BufferedReader(new FileReader(s));
                	String l;
                	while((l=br.readLine())!=null)
                	 		answer+=l;
                    answer=answer.replaceAll("\\s","");
                	submit.setEnabled(true);
                	aUp.setEnabled(false);
                	
                	f2=true;
                   	if(f1 && f2)	//indicates both question and answer have been uploaded
                	{
                   		panel.setVisible(false);
                   		output.setVisible(false);    
                   		JOptionPane.showConfirmDialog(frame, "Go Techno", "Code Magnet", -1);         		
                   		timer.start();
                   		output.setVisible(true);
                   		panel.setVisible(true);
                	}
                   	
                }catch(Exception ee){ee.printStackTrace();}
				
			}
		});
		
		submit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
					if(answer.equals(right.getText().replaceAll("\\s","")))
					{
						timer.stop();
						JOptionPane.showMessageDialog(frame,"Correct!\n Please call the organiser","Code Magnet",JOptionPane.INFORMATION_MESSAGE);
					}
					else
						JOptionPane.showMessageDialog(frame,"inCorrect!\nTry Again","Code Magnet",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		for(int i=0;i<maxLabels;i++){
			
			area[i]=new JTextArea(3,50);
			DefaultCaret caret = (DefaultCaret)area[i].getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
			area[i].setFont(new Font("Courier New",Font.PLAIN,14));
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
		optionMenu.add(aUp);
		optionMenu.add(submit);
		menuBar.add(optionMenu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(860, 700);
		frame.setLocation(50, 0);
		frame.setJMenuBar(menuBar);
		//frame.setResizable(false);
		mainPanel.add(lUpScroller);
		mainPanel.add(lDownScroller);
		frame.getContentPane().add(BorderLayout.WEST,mainPanel);
		frame.getContentPane().add(BorderLayout.EAST,rscroller);
		frame.setVisible(true);
		
	}
}