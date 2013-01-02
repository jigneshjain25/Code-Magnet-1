import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JOptionPane;

public class submitListener implements ActionListener{
	Main obj;
	public submitListener(Main o) {
		obj=o;
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		String msg="You Can submit only once, no changes are allowed after submitting and \nthe timer shall stop. " +
				"Do you wish to continue ?";
		int n=JOptionPane.showConfirmDialog(obj.frame, msg, "Code Magnet", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
		if(n==JOptionPane.NO_OPTION)return;
		try{
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(obj.userFile));
			bw.write(obj.right.getText());
			bw.write("\n");
			bw.write("//Made using CodeMagnet");
			bw.close();
		}
		catch (Exception e1) {
			System.out.println("Error Saving the file");
			e1.printStackTrace();
		}
		obj.timer.stop();
		obj.lUpScroller.setVisible(false);
       	obj.lDownScroller.setVisible(false);
		obj.submit.setEnabled(false);
		obj.rscroller.setVisible(false);
		
		String msg2="You have successfully submitted the solution!\n" +
				"You took "+obj.time+" s.\n" +
				"Do not close this window and inform the organiser.";
		JOptionPane.showMessageDialog(obj.frame, msg2, "Code Magnet", JOptionPane.INFORMATION_MESSAGE, null);
		
	}

}
