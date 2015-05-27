package airproject.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import airproject.model.io.Logger;

public class LoggerPanel extends JPanel implements ActionListener {

	private JScrollPane scroller;
	private static JPanel textContainer;
	public static volatile boolean isCurrent = true;
	
	private static Pattern pattern = Pattern.compile("(?s)\\{(\\d+?)\\}\\[(.*?)\\] - (.*)");

	public LoggerPanel() {
		super(new BorderLayout());
		textContainer = new JPanel();
		textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.PAGE_AXIS));
		scroller = new JScrollPane(textContainer);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JButton open = new JButton("Log History");
		open.addActionListener(this);
		add(scroller);
		add(open, BorderLayout.PAGE_END);
		readLog(Logger.getLog(new Date()));
	}
	
	public void readLog(final File file) {
		SwingWorker<Void, Void> logReader = new SwingWorker<Void, Void>() {
			
			protected Void doInBackground() throws Exception {
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					try {
						isCurrent = Logger.isCurrentDate(file);
					} catch (IllegalArgumentException iae) {
						return null;
					}
					textContainer.removeAll();
					String line = null;
					while ((line = reader.readLine()) != null) {
						addLog(line);
					}
				}
				return null;
			}
			
		};
		logReader.execute();
	}
	
	public static void addLog(String text) {
		final Matcher m = pattern.matcher(text);
		if (m.matches()) {
			String[] lines = m.group(3).split("\\$");
			final StringBuilder sb = new StringBuilder(50);
			for (String s : lines) {
				if (s != "") {
					sb.append(String.format("%s%s%n", new String(new char[10]).replace("\0", " "), s.trim()));
				}
			}
			Color color;
			switch (Logger.MessageType.getType(Integer.parseInt(m.group(1)))) {
			
			case START_STOP:
				color = new Color(15, 190, 15);
				break;
				
			case RUNWAY:
				color = Color.GRAY;
				break;
				
			case OBSTACLE:
				color = Color.RED;
				break;
			
			case CALCULATION:
				color = new Color(102, 0, 153);
				break;
				
			case RUNWAY_SWITCH:
				color = Color.CYAN;
				
			case VIEW:
				color = Color.BLUE;
				break;
				
			case IMPORT_EXPORT:
				color = Color.YELLOW.darker();
				break;
				
			default:
				color = Color.BLACK;
				break;
			
			}
			final Color backgroundColor = color;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textContainer.add(new InnerTextArea(m.group(2), sb.toString(), backgroundColor));
					textContainer.revalidate();
				}
			});
		}
	}
	
	private static class InnerTextArea extends JTextArea{
		
		private InnerTextArea(String date, String text, Color color) {
			setText(text);
			setFont(new Font("Arial", Font.BOLD, 12));
			setForeground(Color.WHITE);
			setEditable(false);
			TitledBorder border = BorderFactory.createTitledBorder(date);
			border.setBorder(new LineBorder(Color.BLACK));
			setBorder(border);
			setBackground(color);
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(Logger.LOGGING_DIR);
		int choice = chooser.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			readLog(chooser.getSelectedFile());
		}
	}
	
}
