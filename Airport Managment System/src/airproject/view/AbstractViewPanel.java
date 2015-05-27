package airproject.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public abstract class AbstractViewPanel extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener {

	private boolean usingTexturing = true;
	// this determines scale of the images drawn.
	private float scale = 1f;
	private float zoomMin = 0.6f;
	private float zoomMax = 2f;
	
	private float rotation = 0f;

	
	// The main frame reference
	private MainFrame frame;
	
	// The current position.
	private Point currentPosition = new Point(0,0);
	
	//this determines how much of the screen runway captures
	private float ratioOnScreen = 0.88f;
	//The physical area being displayed
	private float instrumentWidth=150f;
	
	// Is the view showing low or high.
	private boolean isLowDesignator = true;

	// The default properties
	private float TORA=3660;
	private float TODA=3660;
	private float ASDA=3660;
	private float LDA=3353;
	private float THR=307;
	
	// The redeclared properties.
	private float rTORA=3660;
	private float rTODA=3660;
	private float rASDA=3660;
	private float rLDA=3660;
	
	// relative end info
	private float oppositeDirectionStopway = 0;
	private float facingStripEnd = 60;
	private float oppositeStripEnd = 60;
	
	// The obstacle properties.
	private boolean obstacleExists = false;
	private float obstacleDistance = -50;
	private float obstacleCenterDisp = 20;
	private boolean beforeObstacle = true;
	private float obstacleHeight = 5;
	
	// Designators
	private String lowDes = "";
	private String lowDesD = "";
	private String highDes = "";
	private String highDesD = "";

	//Buttons
	private boolean show_btns = false;
	private List<Button> buttons;
	
	public AbstractViewPanel(MainFrame mf) {
		// Set the main-frame reference.
		frame = mf;
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	
	
	/*
	 * Loads an image in from a resource file and caches a version of the size given.
	 */
	protected TexturePaint scaleResourceImagePaint(String resource, int width, int height) throws IOException{
		InputStream is = getClass().getResourceAsStream(resource);
		BufferedImage original = ImageIO.read(is);
		BufferedImage rescaled = new BufferedImage(width*4, height*4, BufferedImage.TYPE_INT_ARGB);
		rescaled.getGraphics().drawImage(original, 0, 0, width*4 + 1, height*4 +1, null);
		return new TexturePaint(rescaled, new Rectangle(0,0,width,height));
	}
	
	protected Image getImage(String path) throws IOException{
		return IconHandler.getIcon(path);
	}
	
	protected void drawString(Graphics2D g2, Font font, String message, int x, int y, Color c1, Color c2){
		g2.setFont(font);
		Rectangle2D bounds = font.getStringBounds(message, new FontRenderContext(g2.getTransform(), true, true));
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight()-4;
		// Draw text shadow
		g2.setColor(c2);
		g2.drawString(message, x-width/2-2, y-height/2-2);
		// Draw text
		g2.setColor(c1);
		g2.drawString(message, x-width/2, y-height/2-2);
	}

	protected void reverseRectangle(Graphics2D g2, Point a, Point b, Point c, Point d){
		AffineTransform t = getTransform();
		try {
			t.inverseTransform(a, a);
			t.inverseTransform(b, b);
			t.inverseTransform(c, c);
			t.inverseTransform(d, d);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		GeneralPath gp = new GeneralPath();
		gp.moveTo(a.x, a.y);
		gp.lineTo(b.x, b.y);
		gp.lineTo(c.x, c.y);
		gp.lineTo(d.x, d.y);
		gp.lineTo(a.x, a.y);
		g2.fill(gp);
	}
	
	protected void drawLineBetween(Graphics2D g2, Font font, Point screen_start, Point screen_end, String message, float offset, boolean decorate, Color lineColour){
		final int head_length = 10;
		final int arrow_length = 5;
		// Compute the perpendicular vector!
		Point end_minus_start = new Point(screen_end.x-screen_start.x, screen_end.y - screen_start.y);
		float x = end_minus_start.x;
		float y = end_minus_start.y;
		float a = (float) Math.sqrt(1 - (x*x)/(x*x+y*y));
		float b = (float) Math.sqrt((x*x)/(x*x+y*y));
		Point2D.Float vector;
		// Check for both positive.
		if( (end_minus_start.x >= 0 && end_minus_start.y >= 0) || (end_minus_start.x <= 0 && end_minus_start.y <= 0) )
		{
			vector = new Point2D.Float(a,-b);
		}else{
			vector = new Point2D.Float(-a,-b);
		}
		Point screen_start_adjusted = new Point(screen_start.x + (int)(vector.x * offset), screen_start.y + (int)(vector.y * offset));
		Point screen_end_adjusted = new Point(screen_end.x + (int)(vector.x * offset), screen_end.y + (int)(vector.y * offset));
		// Draw the line.
		g2.setColor(lineColour);
		g2.setStroke(new BasicStroke(1.5f));
		g2.drawLine(screen_start_adjusted.x, screen_start_adjusted.y, screen_end_adjusted.x, screen_end_adjusted.y);
		// Draw the string in position.
		drawString(g2, font, message, (screen_start_adjusted.x+screen_end_adjusted.x)/2, (screen_start_adjusted.y+screen_end_adjusted.y)/2 + 8, Color.white, Color.black);
		// Decoration section.
		if(decorate){
			// Draw the marker line
			g2.drawLine(
					screen_start_adjusted.x-(int)(vector.x*head_length), 
					screen_start_adjusted.y-(int)(vector.y*head_length),
					screen_start_adjusted.x+(int)(vector.x*head_length), 
					screen_start_adjusted.y+(int)(vector.y*head_length)
					);
			// Draw the arrow head
			float line_length = (float) Math.sqrt(end_minus_start.x*end_minus_start.x + end_minus_start.y*end_minus_start.y);
			g2.drawLine(
					screen_end_adjusted.x, 
					screen_end_adjusted.y,
					screen_end_adjusted.x+(int)(vector.x*arrow_length)-(int)(end_minus_start.x/line_length*arrow_length), 
					screen_end_adjusted.y+(int)(vector.y*arrow_length)-(int)(end_minus_start.y/line_length*arrow_length)
					);
			g2.drawLine(
					screen_end_adjusted.x, 
					screen_end_adjusted.y,
					screen_end_adjusted.x-(int)(vector.x*arrow_length)-(int)(end_minus_start.x/line_length*arrow_length), 
					screen_end_adjusted.y-(int)(vector.y*arrow_length)-(int)(end_minus_start.y/line_length*arrow_length)
					);
		}
	}
	
	protected void drawLineBetween(Graphics2D g2, Font font, Point screen_start, Point screen_end, String message, float offset){
		drawLineBetween(g2, font, screen_start, screen_end, message, offset, true, Color.white);
	}
	
	/*
	 * Draws the overlay arrows used for distances.
	 */
	protected void drawHorizontalArrow(Graphics2D g2, int lowX, int highX, int y, String message){
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(1f));
		g2.drawLine(lowX, y, highX, y);
		int offset = (int) (5);
		if( isLowDesignator() ){
			g2.drawLine(lowX, y-offset, lowX, y+offset);
			drawArrowEnd(g2, highX, y);
		}else{
			g2.drawLine(lowX, y-offset, lowX, y+offset);
			drawArrowStart(g2, highX, y);
		}
		// stop if offscreen
		if( (highX < 0 && lowX < 0) || (lowX > getWidth() && highX > getWidth())){
			return;
		}
		int textX = 0;
		if( isLowDesignator ){
			textX = (Math.max(lowX, 0)+Math.min(highX, getWidth()))/2 - (int) g2.getFont().getStringBounds(message, new FontRenderContext(g2.getTransform(), true, true)).getWidth() / 2;;
		}else{
			textX = (Math.max(highX, 0)+Math.min(lowX, getWidth()))/2 - (int) g2.getFont().getStringBounds(message, new FontRenderContext(g2.getTransform(), true, true)).getWidth() / 2;;
		}
		// Draw text shadow
		g2.setColor(Color.BLACK);
		g2.drawString(message, textX-2, y-2-2);
		// Draw text
		g2.setColor(Color.WHITE);
		g2.drawString(message, textX, y-2);
	}

	/*
	 * Draws a left facing arrowhead.
	 */
	private void drawArrowStart(Graphics2D g2, int x, int y){
		GeneralPath path = new GeneralPath();
		path.moveTo(x, y);
		path.lineTo(x+5,y+5);
		path.lineTo(x+5,y-5);
		path.lineTo(x, y);
		g2.fill(path);
	}
	
	/*
	 * Draws a right facing arrowhead.
	 */
	private void drawArrowEnd(Graphics2D g2, int x, int y){
		GeneralPath path = new GeneralPath();
		path.moveTo(x, y);
		path.lineTo(x-5,y+5);
		path.lineTo(x-5,y-5);
		path.lineTo(x, y);
		g2.fill(path);
	}	
	
	/*
	 * Gets if the panel is using textures.
	 */
	public boolean isUsingTextures(){
		return this.usingTexturing;
	}
	
	/*
	 * Sets textures variable
	 * */
	public void setUsingTextures(boolean b){
		this.usingTexturing=b;
	}
	

	public void setZoom(float ratio){
		ratio = Math.min(ratio, zoomMax);
		ratio = Math.max(ratio, zoomMin);
		float oldScale = scale;
		this.scale=ratio;
	}
	
	public float getScale() {
		return scale;
	}
	
	/*
	 * Set the default runway properties.
	 */
	public void setDefaultProperties(float TORA, float TODA, float ASDA, float LDA, float THR){
		this.TORA = TORA;
		this.TODA = TODA;
		this.ASDA = ASDA;
		this.LDA = LDA;
		this.THR = THR;
	}
	
	/*
	 * Set the redeclared runway properties.
	 */
	public void setRedeclaredProperties(float _TORA, float _TODA, float _ASDA, float _LDA){
		rTORA = _TORA;
		rTODA = _TODA;
		rASDA = _ASDA;
		rLDA = _LDA;
	}
	
	/*
	 * Gets the default TORA.
	 */
	public float getDefaultTORA() {
		return TORA;
	}

	/*
	 * Gets the default TODA.
	 */
	public float getDefaultTODA() {
		return TODA;
	}

	/*
	 * Gets the default ASDA.
	 */
	public float getDefaultASDA() {
		return ASDA;
	}

	/*
	 * Gets the default LDA.
	 */
	public float getDefaultLDA() {
		return LDA;
	}
	
	/*
	 * Gets the default threshold.
	 */
	public float getDefaultTHR() {
		return THR;
	}
	
	/*
	 * Gets the redeclared TORA.
	 */
	public float getRedeclaredTORA() {
		return rTORA;
	}
	
	/*
	 * Gets the redeclared TODA.
	 */
	public float getRedeclaredTODA() {
		return rTODA;
	}
	
	/*
	 * Gets the redeclared TORA.
	 */
	public float getRedeclaredASDA() {
		return rASDA;
	}
	
	/*
	 * Gets the redeclared TODA.
	 */
	public float getRedeclaredLDA() {
		return rLDA;
	}
	
	public float getObstacleHeight() {
		return obstacleHeight;
	}
	
	
	/*
	 * Set the designator being displayed is low.
	 */
	public void showLowDesignator() {
		this.isLowDesignator = true;
	}
	
	/*
	 * Gets the current position.
	 */
	public Point getCurrentPosition() {
		return currentPosition;
	}
	
	public boolean obstacleExists() {
		return obstacleExists;
	}
	
	/*
	 * Sets the current position.
	 */
	public void setCurrentPosition(Point currentPosition) {
		this.currentPosition = currentPosition;
	}

	/*
	 * Set the designator being displayed is high.
	 */
	public void showHighDesignator() {
		this.isLowDesignator = false;
	}
	
	/*
	 * Get whether the designator being displayed is low.
	 */
	public boolean isLowDesignator() {
		return isLowDesignator;
	}
	
	/*
	 * Set the obstacle properties.
	 */
	public void setObstacleProperties(float dispFromDesig, float centeralDisplacement, float height){
		obstacleExists = true;
		obstacleDistance = dispFromDesig;
		obstacleCenterDisp = centeralDisplacement;
		obstacleHeight = height;
	}
	
	/*
	 * Set the designator strings.
	 */
	public void setDesignatorStrings(String low, String lowDir, String high, String highDir){
		this.lowDes = low;
		this.lowDesD = lowDir;
		this.highDes = high;
		this.highDesD = highDir;
	}
	
	
	/*
	 * Gets the distance to the obstacle from the relative designator.
	 */
	public float getObstacleDistance() {
		return obstacleDistance;
	}
	
	/*
	 * Gets the displacement from the center line to the obstacle.
	 */
	public float getObstacleCenterDisp() {
		return obstacleCenterDisp;
	}
	
	/*
	 * Sets the stopway for the opposite side.
	 */
	public void setOppositeStopway(float opposite_stopway) {
		this.oppositeDirectionStopway = opposite_stopway;
	}
	
	/*
	 * Gets the stopway for the opposite side.
	 */
	public float getOppositeDirectionStopway() {
		return oppositeDirectionStopway;
	}
	
	/*
	 * Sets the strip end for the opposite side.
	 */
	public void setOppositeStripEnd(float opposite_strip_end) {
		this.oppositeStripEnd = opposite_strip_end;
	}

	/*
	 * Gets the strip end for the opposite side.
	 */
	public float getOppositeStripEnd() {
		return oppositeStripEnd;
	}

	/*
	 * Sets the strip end for the relative side.
	 */
	public void setFacingStripEnd(float facing_strip_end) {
		this.facingStripEnd = facing_strip_end;
	}

	/*
	 * Gets the strip end for the relative side.
	 */
	public float getFacingStripEnd() {
		return facingStripEnd;
	}

	/*
	 * Sets the stopway for the relative side.
	 */
	public float getFacingStopway(){
		return ASDA - TORA;
	}
	
	/*
	 * Removes the obstacle.
	 */
	public void removeObstacle(){
		obstacleExists = false;
		setBeforeObstacle(true);
	}
	
	/*
	 * Sets whether using the strip before or after the obstacle.
	 */
	public void setBeforeObstacle(boolean beforeObstacle) {
		this.beforeObstacle = beforeObstacle;
	}
	
	public boolean isBeforeObstacle() {
		return beforeObstacle;
	}
	
	/*
	 * Gets the low designator's angle.
	 */
	public String getLowDesignatorAngle() {
		return lowDes;
	}
	
	/*
	 * Gets the low designator's symbol.
	 */
	public String getLowDesignatorSymbol() {
		return lowDesD;
	}
	
	/*
	 * Gets the high designator's angle.
	 */
	public String getHighDesignatorAngle() {
		return highDes;
	}
	
	/*
	 * Gets the high designator's symbol.
	 */
	public String getHighDesignatorSymbol() {
		return highDesD;
	}
	
	/*
	 * Get the instrument width.
	 */
	public float getInstrumentWidth() {
		return instrumentWidth;
	}
	
	/*
	 * Get the ratio on the screen.
	 */
	public float getRatioOnScreen() {
		return ratioOnScreen;
	}
	
	/*
	 * Gets the x scaled-world ratio.
	 */
	protected abstract float getDrawRatioX();
	
	/*
	 * Gets the y scaled-world ratio.
	 */
	protected abstract float getDrawRatioY();
	
	/*
	 * Converts a runway x to a scaled-world x.
	 * This will take into account which designator is being displayed
	 */
	protected int toScaledX(float xCoordinate){
		float drawRatioX = getDrawRatioX();
		float offset = getWidth() * (1-ratioOnScreen)/2 - getWidth()/2;
		int adjustedCoordinate = (int) (xCoordinate * drawRatioX + offset);
		if( isLowDesignator() ){
			return adjustedCoordinate;
		}else{
			return  - adjustedCoordinate;
		}
	}
	
	/*
	 * Converts a runway y to a scaled-world y.
	 */
	protected int toScaledY(float yCoordinate){
		float drawRatioY = getDrawRatioY();
		float offset = (this.getHeight() / 2f) - getHeight()/2;
		int adjustedCoordinate = (int) (yCoordinate * drawRatioY + offset);
			return adjustedCoordinate;
	
	}

	/*
	 * Scales an x distance to a positive scaled-world distance.
	 */
	protected int scaleAbsX(float length){
		float drawRatioX = getDrawRatioX();
		return (int) (length * drawRatioX);
	}
	
	/*
	 * Scales an x distance to an scaled-world distance.
	 */
	protected int scaleX(float length){
		float drawRatioX = getDrawRatioX();
		if( isLowDesignator() ){
			return (int) (length * drawRatioX);
		}else{
			return -(int) (length * drawRatioX);
		}
	}

	/*
	* Scales a y distance to an scaled-world distance.
	 */
	protected int scaleY(float length){
		float drawRatioY = getDrawRatioY();
		return (int) (drawRatioY * length);
	}

	
	/*
	 * Converts a scaled-world position into an on-screen position.
	 */
	protected Point overlayPoint(Point p){
		Point result = new Point();
		AffineTransform t = getTransform();
		t.transform(p,result);
		return result;
	}
	
	protected Point overlayPoint(int a, int b){
		return overlayPoint(new Point(a,b));
	}
	
	/*
	 * Takes degrees.
	 */
	protected AffineTransform getTransform(){
		AffineTransform trans = new AffineTransform();
		trans.concatenate(AffineTransform.getTranslateInstance(getWidth()/2, getHeight()/2));
		trans.concatenate(AffineTransform.getScaleInstance(getScale(), getScale()));
		trans.concatenate(AffineTransform.getTranslateInstance(currentPosition.x, currentPosition.y));
		trans.concatenate(AffineTransform.getRotateInstance(Math.toRadians(rotation), toScaledX(getDefaultTORA()/2), toScaledY(0)));
		return trans;
	}
	
	/*
	 * Sets the rotation of the view.
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation % 360;
	}
	
	/*
	 * Gets the rotation of the view.
	 */
	public float getRotation() {
		return rotation;
	}
	
	/*
	 * Sets the scale of the view.
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		paintView(g2);
		if(show_btns){
			drawButtons(g2);
		}
	}
	
	/*
	 * To be extended by views.
	 */
	protected void paintView(Graphics2D gra){
		//stub
	}
	
	/*
	 * Buttons to be loaded here.
	 */
	protected void loadButtons() {
		buttons = new ArrayList<>();
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		loadButtons();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Sets the button list for the panel.
	 */
	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}
	
	/*
	 * Gets whether the button menu is open.
	 */
	public boolean buttonMenuOpen() {
		return show_btns;
	}
	
	/*
	 * Sets whether to display the button menu.
	 */
	public void setButtonMenuOpen(boolean show_btns) {
		this.show_btns = show_btns;
		if( show_btns ){
			frame.enforceOneOpenMenu(this);
		}
		repaint();
	}
	
	/*
	 * Checks input against all buttons in menu.
	 */
	protected boolean checkButtonMenu(Point mouse) {
		// Button clicks
		if (buttonMenuOpen()) {
			for (Button b : buttons) {
				if (b.assessMouseClick(mouse)) {
					repaint();
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * Draws the buttons menu.
	 */
	private void drawButtons(Graphics2D g2) {
		if (show_btns) {
			g2.setColor(new Color(180, 180, 180, 100));
			g2.fillRect(0, 0, getWidth(), getHeight());
			for (Button b : buttons) {
				b.draw(g2);
			}
		}
	}
	
	public MainFrame getFrame() {
		return frame;
	}
	
	/*
	 * Interface methods
	 */
	
	@Override
	public void mouseClicked(MouseEvent event) {
		// Button clicks
		if (buttonMenuOpen()) {
			if (checkButtonMenu(event.getPoint()))
				return;
		}
		// toggle buttons shown.
		setButtonMenuOpen(!buttonMenuOpen());
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}