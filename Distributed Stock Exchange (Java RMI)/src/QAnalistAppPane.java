

import javax.swing.JTabbedPane;

public class QAnalistAppPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	public QAnalistAppPane() {
		init();
	}
	
	private void init() {
		QAnalistAppPanel p0 = new QAnalistAppPanel(0);
		QAnalistAppPanel p1 = new QAnalistAppPanel(1);
		QAnalistAppPanel p2 = new QAnalistAppPanel(2);
		QAnalistAppPanel p3 = new QAnalistAppPanel(3);
		QAnalistAppPanel p4 = new QAnalistAppPanel(4);
		QAnalistAppPanel p5 = new QAnalistAppPanel(5);

		
		this.add("S0", p0);
		this.add("S1", p1);
		this.add("S2", p2);
		this.add("S3", p3);
		this.add("S4", p4);
		this.add("S5", p5);

	}
	
	
	
	
}
