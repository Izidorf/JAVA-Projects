package airproject.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideOnViewPanel extends AbstractViewPanel {
	
	private static final int verticalRunwayHeight = 50;
	private static final int const_length_vert_spacing = 25;
	
	private static final int yShiftMax = 98;
	private static final int yShiftMin = -20;
	private static final int defaultYShift = 98;
	
	// Drag state
	private Point currentPositionBeforeDrag = getCurrentPosition();
	private Point dragStartPosition = null;
	private boolean leftClickDrag = true;
	
	private int lengthsYShift = defaultYShift;
	
	float slopeLength = 0;

	private Paint tarmacTexture;
	private Paint grassTexture;
	private Paint stopwayTexture;
	
	public SideOnViewPanel(MainFrame mf) {
		super(mf);
		setBackground(ColourSchema.sky);
		loadResource();
	}

	/*
	 * Load any resources here.
	 */
	private void loadResource() {
		try {
			tarmacTexture = scaleResourceImagePaint("/textures/tarmac2.jpg", 300, 300);
			grassTexture = scaleResourceImagePaint("/textures/grass.jpg", 300, 300);
			stopwayTexture = scaleResourceImagePaint("/textures/stopway.jpg", 300, 300);
		} catch (IOException e) {
			setUsingTextures(false);
			e.printStackTrace();
		}
	}
	
	/*
	 * Paints the view.
	 */
	@Override
	protected void paintView(Graphics2D g2){
		// Create the image buffer for drawing the world.
		BufferedImage world_buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D world_g2 = (Graphics2D) world_buffer.getGraphics();
		// Translate and scale the world.
		world_g2.setTransform(getTransform());
		// Draw the background.
		world_g2.setPaint(getBackground());
		reverseRectangle(world_g2, new Point(0,0), new Point(getWidth(), 0), new Point(getWidth(), getHeight()), new Point(0,getHeight()));
		// Draw the graded region.
		drawGround(world_g2);
		// Draw the runway.
		drawRunway(world_g2);
		// Draw the world to the screen.
		g2.drawImage(world_buffer, 0, 0, null);
		// Draw the overlay to the screen.
		drawOverlay(g2);
	}
	

	/*
	 * Loads button objects.
	 */
	@Override
	protected void loadButtons() {
		List<Button> buttons = new ArrayList<>();
		final int square_size = 100;
		final int seperator_size = 20;
		final int numButtons = 2;
		int y = (getHeight() - 100) / 2;
		int x = (getWidth() - (square_size * numButtons) - (seperator_size * (numButtons - 1))) / 2;
		int sep = square_size + seperator_size;
		// Paint button.
		try {
			buttons.add(new Button(getImage(IconHandler.path_paint), "Toggle paint", x, y,
					square_size, square_size, new Runnable() {
						@Override
						public void run() {
							setUsingTextures(!isUsingTextures());
							repaint();
						}
					}));
			buttons.add(new Button(getImage(IconHandler.path_fullscreen), "Toggle full-screen", x + sep, y,
					square_size, square_size, new Runnable() {
						@Override
						public void run() {
							getFrame().toggleArrangeSide();
						}
					}));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setButtons(buttons);
	}
	
	/*
	 * Draws the obstacle overlay.
	 */
	private void drawObstacle(Graphics2D g2){
		if( obstacleExists() ){
			// Get the overlay screen position of the obstacle.
			Point scr_obs_top = overlayPoint(toScaledX(getObstacleDistance()+getDefaultTHR()), toScaledY(-getObstacleHeight()));
			Point scr_obs_bottom = overlayPoint(toScaledX(getObstacleDistance()+getDefaultTHR()), toScaledY(0));
			
			Font font = new Font("default", Font.BOLD, 16);
			drawLineBetween(g2, font, scr_obs_bottom, scr_obs_top, getObstacleHeight()+"m", 0);
			// Slope visualisation.
			Point scr_slope_end = null;
			if(isBeforeObstacle()){
				scr_slope_end = overlayPoint(toScaledX(getObstacleDistance()+getDefaultTHR()-slopeLength), toScaledY(0));
			}else{
				scr_slope_end = overlayPoint(toScaledX(getObstacleDistance()+getDefaultTHR()+slopeLength), toScaledY(0));
			}
			drawLineBetween(g2, font, scr_obs_top, scr_slope_end, "", 0, false, Color.red);
			drawLineBetween(g2, font, scr_obs_bottom, scr_slope_end, "Slope = "+slopeLength+"m", -2 * getVerticalSpacing(), true, Color.red);
		}
	}
	

	/*
	 * Draws the designator overlays beneath the runway.
	 */
	private void drawDesignators(Graphics2D g2) {
		final int spacing_vert = 15;
		// Set the font.
		Font font = new Font("default", Font.BOLD, 18);
		g2.setFont(font);
		// Calculate overlay screen positions.
		int startX = overlayPoint(Math.min(toScaledX(0), toScaledX(getDefaultTORA())),0).x;
		int endX = overlayPoint(Math.max(toScaledX(0), toScaledX(getDefaultTORA())),0).x;
		int y = overlayPoint(0, toScaledY(0)+verticalRunwayHeight-20).y;
		// Calculate text widths.
		int lowWidth = (int) font.getStringBounds(getLowDesignatorAngle(), new FontRenderContext(g2.getTransform(), true, true)).getWidth();
		int highWidth = (int) font.getStringBounds(getHighDesignatorAngle(), new FontRenderContext(g2.getTransform(), true, true)).getWidth();
		int lowWidth2 = (int) font.getStringBounds(getLowDesignatorSymbol(), new FontRenderContext(g2.getTransform(), true, true)).getWidth();
		int highWidth2 = (int) font.getStringBounds(getHighDesignatorSymbol(), new FontRenderContext(g2.getTransform(), true, true)).getWidth();
		// Calculate shift offsets.
		int lowOffset = (lowWidth - lowWidth2)/2;
		int highOffset = (highWidth - highWidth2)/2;
		// Draw the designator shadows.
		g2.setColor(ColourSchema.txt_shadow_gray);
		g2.drawString(getLowDesignatorAngle(), startX+5-2, y-2);
		g2.drawString(getHighDesignatorAngle(), endX-35-2, y-2);
		g2.drawString(getLowDesignatorSymbol(), startX+5-2 + lowOffset, y-2+spacing_vert);
		g2.drawString(getHighDesignatorSymbol(), endX-35-2 + highOffset, y-2+spacing_vert);
		// Draw the designators.
		g2.setColor(ColourSchema.txt_designator);
		g2.drawString(getLowDesignatorAngle(), startX+5, y);
		g2.drawString(getHighDesignatorAngle(), endX-35, y);
		g2.drawString(getLowDesignatorSymbol(), startX+5 + lowOffset, y+spacing_vert);
		g2.drawString(getHighDesignatorSymbol(), endX-35 + highOffset, y+spacing_vert);
	}
	
	/*
	 * draw overlay, drawn over the transformed image.
	 */
	private void drawLengthsOverlay(Graphics2D g2){
		Font font = new Font("default", Font.BOLD, (int) (14));
		g2.setFont(new Font("default", Font.BOLD, (int) (14)));
		int length_vert_spacing = getVerticalSpacing();
		// Get some useful coordinate values.
		int startX = toScaledX(0);
		int startY = toScaledY(0);
		if (!isBeforeObstacle()) {
			startX = toScaledX(getDefaultTORA() - getRedeclaredTORA());
		}
		// Get the points.
		Point scr_start = overlayPoint(startX, startY);
		Point scr_tora = overlayPoint(startX + scaleX(getRedeclaredTORA()),
				startY);
		Point scr_toda = overlayPoint(startX + scaleX(getRedeclaredTODA()),
				startY);
		Point scr_asda = overlayPoint(startX + scaleX(getRedeclaredASDA()),
				startY);
		// Draw the arrows.
		drawLineBetween(g2, font, scr_start, scr_tora, "TORA = "
				+ getRedeclaredTORA() + "m", 0);
		drawLineBetween(g2, font, scr_start, scr_toda, "TODA = "
				+ getRedeclaredTODA() + "m", length_vert_spacing);
		drawLineBetween(g2, font, scr_start, scr_asda, "ASDA = "
				+ getRedeclaredASDA() + "m", 2 * length_vert_spacing);
		if (isBeforeObstacle()) {
			Point scr_thr = overlayPoint(startX + scaleX(getDefaultTHR()),
					startY);
			Point scr_lda = overlayPoint(startX
					+ scaleX(getDefaultTHR() + getRedeclaredLDA()), startY);
			drawLineBetween(g2, font, scr_thr, scr_lda, "LDA = "
					+ getRedeclaredLDA() + "m", -length_vert_spacing);
			drawLineBetween(g2, font, scr_start, scr_thr, "Threshold = "
					+ getDefaultTHR() + "m", -2 * length_vert_spacing);
		} else {
			Point scr_thr = overlayPoint(toScaledX(getDefaultTODA()
					- getRedeclaredLDA()), startY);
			drawLineBetween(g2, font, scr_thr, scr_tora, "LDA = "
					+ getRedeclaredLDA() + "m", -length_vert_spacing);
		}
	}
	
	private int getVerticalSpacing() {
		return const_length_vert_spacing;
	}


	private void drawOverlay(Graphics2D g2) {
		drawObstacle(g2);
		drawLengthsOverlay(g2);
		drawDesignators(g2);
	}

	/*
	 * Draws the ground to the world.
	 */
	private void drawGround(Graphics2D world_g2) {
		Rectangle ground = new Rectangle
				(	
						-getCurrentPosition().x,	
						toScaledY(0) , 
						getWidth(), 
						getHeight()/4 	
				);
		// Set paint.
		if( isUsingTextures() ){
			world_g2.setPaint( grassTexture );
		}else{
			world_g2.setPaint( ColourSchema.grass );
		}
		world_g2.fill(ground);
		Point p = overlayPoint(new Point(0, toScaledY(0)));
		reverseRectangle(world_g2, 
				new Point(0, p.y), 
				new Point(getWidth(), p.y), 
				new Point(getWidth(), getHeight()), 
				new Point(0, getHeight()));
	}


	/*
	 * Draw the runway to the world.
	 */
	private void drawRunway(Graphics2D g2){
		// Get the start and end X.
		int startX = Math.min(toScaledX(0), toScaledX(getDefaultTORA()));
		int endX = Math.max(toScaledX(0), toScaledX(getDefaultTORA()));
		// Create the rectangle shape.
		Rectangle run = new Rectangle
				(	
						startX,		// x coordinate 
						toScaledY(0) , //  y coordinate
						endX-startX, //width of runway
						verticalRunwayHeight
				);
		// Draw the runway area.
		if( isUsingTextures() ){
			g2.setPaint( tarmacTexture );
		}else{
			g2.setPaint( ColourSchema.tarmac );
		}
		g2.fill(run);
		
		// Draw the stopway in the current direction.
		float facing_stopway_length = getFacingStopway();
		Rectangle facing_stopway = new Rectangle(
					endX,
					toScaledY(0) , //  y coordinate
					scaleAbsX(facing_stopway_length), //width of runway
					verticalRunwayHeight-5 //height of runway	
		);
		if( isUsingTextures() ){
			g2.setPaint( stopwayTexture );
		}else{
			g2.setPaint(ColourSchema.stopway);
		}
		g2.fill(facing_stopway);
		
		// Draw the stopway in the opposite direction.
		float opposite_stopway_length = getOppositeDirectionStopway();
		Rectangle opposite_stopway = new Rectangle(
					startX-scaleAbsX(opposite_stopway_length),
					toScaledY(0), //  y coordinate
					scaleAbsX(opposite_stopway_length), //width of runway
					verticalRunwayHeight-5 //height of runway	
		);
		if( isUsingTextures() ){
			g2.setPaint( stopwayTexture );
		}else{
			g2.setPaint(ColourSchema.stopway);
		}
		g2.fill(opposite_stopway);
	}
	

	/*
	 * Gets the x on-screen ratio.
	 */
	@Override
	protected float getDrawRatioX(){
		return getWidth() / (float)getDefaultTODA() * getRatioOnScreen();
	}
	
	/*
	 * Gets the y on-screen ratio.
	 */
	@Override
	protected float getDrawRatioY(){
		if( obstacleExists() ){
			float height = Math.max(getObstacleHeight(), 20);
			return this.getHeight() / (height*2f)*getRatioOnScreen();
		}else{
			return this.getHeight() * getRatioOnScreen();
		}
		
	}	
	
	@Override
	protected int toScaledY(float yCoordinate) {
		return super.toScaledY(yCoordinate) + getHeight()/4;
	}
	

	@Override
	public void mouseDragged(MouseEvent event) {
		if( dragStartPosition != null ){
			Point newMousePos = event.getPoint();
			if( leftClickDrag ){
				setCurrentPosition( new Point(
					(int)((currentPositionBeforeDrag.x + newMousePos.x - dragStartPosition.x)),
					0)
					);
			}else{
				// Set the y shift
				float ratio = (newMousePos.y - dragStartPosition.y)/(float)getHeight();
				dragStartPosition.y = newMousePos.y;
				setLengthsYShift( (int) (lengthsYShift + (yShiftMax-yShiftMin)*ratio) );
			}
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// Check for drag start
		if( event.getButton() == MouseEvent.BUTTON1 ){
			leftClickDrag = true;
		}else if (event.getButton() == MouseEvent.BUTTON3){
			leftClickDrag = false;
		}else{
			return;
		}
		// Set the drag start point.
		currentPositionBeforeDrag = getCurrentPosition();
		dragStartPosition = event.getPoint();
	}

	/*
	 * Controller's setter methods.
	 */
	public void resetDrag(){
		setCurrentPosition( new Point(0,0) );
		setScale(1);
		lengthsYShift = defaultYShift;
	}
	
	/*
	 * Sets the shift of the distances displayed.
	 */
	public void setLengthsYShift(int lengthsYShift) {
		if( lengthsYShift > yShiftMax ){
			lengthsYShift = yShiftMax;
		}
		else if (lengthsYShift < yShiftMin ){
			lengthsYShift = yShiftMin;
		}
		this.lengthsYShift = lengthsYShift;
	}
	
	/*
	 * Sets the slope length.
	 */
	public void setSlopeLength(float slopeLength) {
		this.slopeLength = slopeLength;
	}

	
}
