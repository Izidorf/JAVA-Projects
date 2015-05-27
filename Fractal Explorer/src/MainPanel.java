import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;


public class MainPanel{

	public static void main(String[] args) {

		MSFrame window = new MSFrame("Fractal Explorer");
		window.init();
	}
}

class MSFrame extends JFrame{

	public MSFrame(String name){
		super(name);
	}

	public void init(){
		MPanel p = new MPanel();
		this.setContentPane(p);
		//this.setResizable(false);
		p.init();

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE) ;
		this.setSize(1600,800);
		this.setVisible(true);
	}
}//ends MSFrame


class MPanel extends JPanel{
	
	JPanel statusButtons; //This stores status buttons
	Fractal leftFractal;
	Fractal juliaSet;
	JPanel sets1; //This is a panel that both fractals are attached to
	
	//four importaint textFileds which determine the range
	TextField minRe;
	TextField maxRe;
	TextField minIm;
	TextField maxIm;
	
	
	//Listeners
	Fractal.FractalZoom zoom; //Zoom listener
	Fractal.UserSelectedPointListener pointListener; //User selected point listeners
	
	public MPanel(Fractal leftFractal, Fractal juliaSet){
		this.leftFractal=leftFractal;
		this.juliaSet=juliaSet;
	}
	
	public MPanel(){

	}

	//Gridbag Layout helper method to make the adding of the components clearer 
		public void addComponent(GridBagLayout layout, GridBagConstraints c, Component com, int row, int column, int width, int height){
			c.gridx = column;
			c.gridy = row;
			c.gridwidth =width;
			c.gridheight=height;
			layout.setConstraints(com, c);
			statusButtons.add(com);
		}
		
		
	public void init(){
		
		/*GUI HEADER*/
		
		JMenuBar menuBar = new JMenuBar();
		
		//Build the first menu.
		JMenu file = new JMenu("File");
		JMenuItem saveAsPng = new JMenuItem("save as png",  KeyEvent.VK_T);
		JMenuItem exit = new JMenuItem("exit",  KeyEvent.VK_T);
		file.add(saveAsPng);
		file.add(exit);
		
		//Build second menu
		JMenu chooseSet = new JMenu("Choose Fractal");
		JMenuItem mand = new JMenuItem("Mandelbrot",  KeyEvent.VK_T);
		JMenuItem julia = new JMenuItem("Julia Set",  KeyEvent.VK_T);
		JMenuItem bShip = new JMenuItem("Burning Ship",  KeyEvent.VK_T);
		chooseSet.add(mand);
		chooseSet.add(julia);
		chooseSet.add(bShip);
		
		//Build third menu
		JMenu favourites = new JMenu("Favourites");
		JMenuItem addFav = new JMenuItem("add to favourites",  KeyEvent.VK_T);
		//Here comes the  loop of all the favourites
		favourites.add(addFav);
	/*	Database d = new Database();
			try {
			HashMap<String, Complex> hm = d.readData();
			Iterator<Complex> it1 = hm.values().iterator();
			//now loop throug hashMap and print out keys and the number of times the word appears
		while(it1.hasNext()) {			
		Complex f = it1.next();
		System.out.println( f.getReal() +" : "+f.getImag() ); 
		}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		*/
		
		
		//Build fourth menu
		JMenu help = new JMenu("help");
		JMenuItem about = new JMenuItem("about",  KeyEvent.VK_T);
		JMenuItem instructions = new JMenuItem("instructions",  KeyEvent.VK_T);
		help.add(about);
		help.add(instructions);
		menuBar.add(file);
		menuBar.add(chooseSet);
		menuBar.add(favourites);
		menuBar.add(help);
		
		
		
		
		
		/*GUI CONTENT/MIDDLE*/
		//Create sets
		leftFractal = new MandelbrotSet();
		juliaSet = new JuliaSet();
				
		//Add sets to GridLayout panel
		sets1 = new JPanel();
		sets1.setLayout(new GridLayout(1,2));
		sets1.add(leftFractal);
		sets1.add(juliaSet);
		
		
		
		
		/*GUI BOTTOM*/
		//create textfields for axis resizing
		JLabel levelText = new JLabel("Level");
		TextField level = new TextField(7);
		JLabel minReText = new JLabel("MinRe");
		minRe = new TextField(7);	
		JLabel maxReText = new JLabel("MaxRe");
		 maxRe = new TextField(7);	
		JLabel minImText = new JLabel("MinIm");
		minIm = new TextField(7);
		JLabel maxImText = new JLabel("MaxIm");
		maxIm = new TextField(7);
		
		//create status buttons
		JLabel clickedNumText = new JLabel("clicked:");
		TextField clickedNum = new TextField(30);
		JLabel currentNumText = new JLabel("hover:");
		TextField currentNum = new TextField(30);
		JButton save = new JButton("save");
		
		//create buttons for set/resize
		JButton reset = new JButton("reset");
		JButton set = new JButton("set");
		
		//Create panel with all range buttons
		JPanel range = new JPanel();
		range.setLayout(new GridLayout(2,4));
		range.add(minReText); range.add(minRe); range.add(maxReText); range.add(maxRe);
		range.add(minImText); range.add(minIm); range.add(maxImText); range.add(maxIm);
		
		//Create panel with all other buttons
		statusButtons = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		statusButtons.setLayout(layout);
		
		//c.fill = GridBagConstraints.HORIZONTAL;
		addComponent(layout, c,clickedNumText, 0, 0, 1, 1);
		addComponent(layout, c,clickedNum, 0, 1, 3, 1); 
		addComponent(layout, c,currentNumText, 2, 0, 1, 1); 
		addComponent(layout, c,currentNum, 2, 1, 3, 1);
		addComponent(layout, c,levelText, 4, 0, 1, 1); 
		addComponent(layout, c,level, 4, 1, 1, 1); 
		addComponent(layout, c,reset, 4, 2, 1, 1); 
		addComponent(layout, c,set, 4, 3, 1, 1); 
		
		
		//fill text fields with current variables values
		level.setText("100"); //Displays current value
		minRe.setText(Double.toString(leftFractal.getMinRe())); //Displays current value
		maxRe.setText(Double.toString(leftFractal.getMaxRe())); //Displays current value
		minIm.setText(Double.toString(leftFractal.getMinIm())); //Displays current value
		maxIm.setText(Double.toString(leftFractal.getMaxIm())); //Displays current value
		
		
		JPanel bottonComponents = new JPanel();
		bottonComponents.setLayout(new FlowLayout());
		bottonComponents.add(range);
		bottonComponents.add(statusButtons);

		
		//Add everything to the main JPanel
		this.setLayout(new BorderLayout());
		this.add(menuBar, BorderLayout.NORTH);
		this.add(sets1, BorderLayout.CENTER);
		this.add(bottonComponents, BorderLayout.SOUTH);

		//Add actionListeners
		/*First add buttonListeners to mandelbrot set
		 * this syntax instantiates an inner class FractalListener */
		Fractal.MandelbrotListener buttonListeners = leftFractal.new MandelbrotListener(level, minRe, maxRe, minIm, maxIm, set, reset,juliaSet, leftFractal); //level, minRe, maxRe, minIm, maxIm, reset, set, mandelbrotSet1
		set.addActionListener(buttonListeners);
		reset.addActionListener(buttonListeners);
		save.addActionListener(buttonListeners);
		saveAsPng.addActionListener(buttonListeners);
		exit.addActionListener(buttonListeners);
		addFav.addActionListener(buttonListeners);
		about.addActionListener(buttonListeners);
		instructions.addActionListener(buttonListeners);
		
		// Now add listener which reads mouse coordinates
		pointListener = leftFractal.new UserSelectedPointListener(leftFractal, juliaSet, clickedNum, currentNum);
		leftFractal.addMouseListener(pointListener);
		leftFractal.addMouseMotionListener(pointListener);
		
		//Add Zoom listener
		zoom = leftFractal.new FractalZoom(leftFractal, minRe, maxRe, minIm, maxIm );
		leftFractal.addMouseListener(zoom);
		leftFractal.addMouseMotionListener(zoom);
		
		//Add menue bar listener
		//chooseSet.addActionListener(new ButtonListener());
		ButtonListener menu = new ButtonListener();
		julia.addActionListener(menu);
		bShip.addActionListener(menu);
		mand.addActionListener(menu); 
	}
	
	//This code handels if somebody wants to display a different fractal
	class ButtonListener implements ActionListener{

		public ButtonListener() {
			
		}
		
		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Burning Ship")){
				
				leftFractal = new BurningShip();
				sets1.remove(0);
				sets1.add(leftFractal, 0);
				sets1.updateUI();
				
				zoom = leftFractal.new FractalZoom(leftFractal, minRe, maxRe, minIm, maxIm );
				sets1.addMouseListener(zoom);
				sets1.addMouseListener(pointListener);
			}
			
			if(e.getActionCommand().equals("Julia Set")){
				
				leftFractal = new JuliaSet();
				sets1.remove(0);
				sets1.add(leftFractal, 0);
				sets1.updateUI();
				
				zoom = leftFractal.new FractalZoom(leftFractal, minRe, maxRe, minIm, maxIm );
				sets1.addMouseListener(zoom);
				sets1.addMouseListener(pointListener);
			}
			
			if(e.getActionCommand().equals("Mandelbrot")){
				
				leftFractal = new MandelbrotSet();
				sets1.remove(0);
				sets1.add(leftFractal, 0);
				sets1.updateUI();
				
				zoom = leftFractal.new FractalZoom(leftFractal, minRe, maxRe, minIm, maxIm );
				sets1.addMouseListener(zoom);
				sets1.addMouseListener(pointListener);
			}
			
			
			
		}
		
	}

}