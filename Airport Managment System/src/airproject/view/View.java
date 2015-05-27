package airproject.view;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;


/*
 * Encapsulates the view module of our project
 */
public class View {
	
	private MainFrame mf;
	
	public View() throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				mf = new MainFrame("Runway Redeclaration System (RRS)");
				mf.init();
			}
		});
	}
	
	public MainFrame getFrame(){
		return mf;
	}

	public TopDownViewPanel getTopDownView() {
		return getFrame().getTopDownView();
	}

	public SideOnViewPanel getSideOnView() {
		return getFrame().getSideOnView();
	}

}
