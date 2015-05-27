

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;



public class StockAppPanel extends JPanel {


	private static final long serialVersionUID = 1L;
	String sourceId;
	String stockName;
	NotificationSource nSource;
	JButton openMarket; 
	JTextArea area = new JTextArea();
	JScrollPane sp = new JScrollPane(area);

	public StockAppPanel(String sourceId, String stockName) {
		this.sourceId=sourceId;
		this.openMarket= new JButton("Click to OPEN market for "+stockName);
		this.stockName=stockName;
		try {
			this.nSource = new NotificationSource(stockName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		init();
	}

	public void init(){
		
	
		
		// The following just adds a bit of padding
		Border eb = new EmptyBorder(50,50,50,50);
		this.setBorder(eb);

		area.setSize(this.getHeight()-10, this.getWidth()-10);

		this.setLayout(new BorderLayout());
		this.add(openMarket, BorderLayout.SOUTH );
		this.add(sp, BorderLayout.CENTER );

		//now add listeners
		OpenMarketListener listener = new OpenMarketListener();
		openMarket.addActionListener(listener);

	}




	/*Inner Class to add listner to the button */
	class OpenMarketListener implements ActionListener {

		Boolean marketOpen=false;

		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Click to OPEN market for "+stockName)){
				openMarket.setText("Click to CLOSE market for "+stockName);
				marketOpen=true;


				class StockData extends SwingWorker<Object, Object> {


					@Override
					protected Object doInBackground() throws Exception {
						while(marketOpen){
							String data = generateStockData(stockName);
							area.append(data);
							
							//Stock App removes functionality of the Notification framework from the app and sends data to registered clients
							StockApp app = new StockApp();
							app.sendDataToClients(nSource, data, sourceId);
							
							
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e1) {


							}
						}
						return null;
					}

					private String generateStockData(String stockName){
						String stockvalue = stockName+" -- "+Double.toString(Math.random()*100)+" -- Time: "+new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime())+" -- Volume: "+Math.ceil(Math.random()*100)+" \n";
						return stockvalue;
					}

				}
				final SwingWorker<Object, Object> sw = new StockData();
				sw.execute();


			}  else if(e.getActionCommand().equals("Click to CLOSE market for "+stockName)) {
				openMarket.setText("Click to OPEN market for "+stockName);
				marketOpen=false;
			}


		}



	}
}
