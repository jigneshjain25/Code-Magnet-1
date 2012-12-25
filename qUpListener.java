import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;


public class qUpListener implements ActionListener{
	Main obj;
	public qUpListener(Main o) {
		obj=o;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JFileChooser fileOpen=new JFileChooser();
		fileOpen.setDialogTitle("Upload Question");
		fileOpen.showOpenDialog(obj.frame);
		File file=fileOpen.getSelectedFile();
		if(file==null)return;
        try{
           	BufferedReader br=new BufferedReader(new FileReader(file));
        	String l;
        	int count=0;
    
        	l=br.readLine().trim();
        	if(l.equals("0"))
        		obj.right.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        	else
        		obj.right.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        	
        	while(!((l=(br.readLine())).trim().equals("end")))
        		obj.output.append(l+"\n");
        	obj.output.setText(obj.output.getText().trim());
       	
        	while((l=br.readLine())!=null){
        			
           		    l=l.trim();
        			if(l.equals("end"))
           		    	count++;
           		    else
           		    	obj.area[count].append(l+"\n");
            }
        	
        	for(int i=0;i<obj.maxLabels;i++)obj.area[i].setText(obj.area[i].getText().trim());
        	for(int i=0;i<obj.maxLabels;i++)obj.area[i].append("\n");
        	obj.qUp.setEnabled(false);
        	obj.submit.setEnabled(true);

        	obj.lUpScroller.setVisible(false);
        	obj.lDownScroller.setVisible(false);
           	
           	while(obj.userName==null || obj.userName=="")
           		obj.userName=(String)JOptionPane.showInputDialog(obj.frame, "Enter you full Name", "Code Magnet", JOptionPane.INFORMATION_MESSAGE, null, null, null);
    
           	obj.userName=obj.userName.replaceAll("\\s", "_");
           	
           	if(obj.right.getSyntaxEditingStyle()==SyntaxConstants.SYNTAX_STYLE_JAVA)
           		obj.userFile = new File(System.getProperty("user.dir"),obj.userName+".java");
			else 
				obj.userFile = new File(System.getProperty("user.dir"),obj.userName+".cpp");
           
           	obj.timer.start();
           	obj.lUpScroller.setVisible(true);
           	obj.lDownScroller.setVisible(true);
        }catch(Exception ee){
        	System.out.println("Error uploading the question");
        	ee.printStackTrace();
        }
    }
}
