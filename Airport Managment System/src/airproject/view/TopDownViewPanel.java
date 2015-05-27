package airproject.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

public class TopDownViewPanel extends AbstractViewPanel {

	private static final int const_spacing_vert = 25;

	// The properties being displayed
	float runwayWidth = 40f;

	// Auto rotate
	private int dRotation = 0;

	// Drag state
	private Point currentPositionBeforeDrag = getCurrentPosition();
	private Float currentRotationBeforeDrag = null;
	private Float mouseVectorRotationBeforeDrag = null;
	private Point dragStartPosition = null;
	private Boolean rotateMode = null;

	// Texturing
	private Paint tarmacTexture;
	private Paint dirtTexture;
	private Paint grassTexture;
	private Paint stopwayTexture;

	// Buttons
	public TopDownViewPanel(MainFrame mf) {
		super(mf);
		// Set the preferred dimensions.
		this.setPreferredSize(new Dimension(600, 300));
		// Set the background colour of the component.
		Color background = ColourSchema.grass;
		setBackground(background);
		// Load resources.
		loadResource();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loadButtons();
			}
		});
	}

	/*
	 * Loads button objects.
	 */
	@Override
	protected void loadButtons() {
		List<Button> buttons = new ArrayList<>();
		final int square_size = 100;
		final int seperator_size = 20;
		final int numButtons = 3;
		int y = (getHeight() - 100) / 2;
		int x = (getWidth() - (square_size * numButtons) - (seperator_size * (numButtons - 1))) / 2;
		int sep = square_size + seperator_size;
		// Compass button.
		try {
			buttons.add(new Button(getImage(IconHandler.path_compass), "Auto-rotate", x, y, square_size,
					square_size, new Runnable() {
						@Override
						public void run() {
							setRotation(dRotation);
							setButtonMenuOpen(false);
						}
					}));
			// Paint button.
			buttons.add(new Button(getImage(IconHandler.path_paint), "Toggle paint", x + sep, y,
					square_size, square_size, new Runnable() {
						@Override
						public void run() {
							setUsingTextures(!isUsingTextures());
							repaint();
						}
					}));
			// Fullscreen button.
			buttons.add(new Button(getImage(IconHandler.path_fullscreen), "Full-screen", x + sep*2, y,
					square_size, square_size, new Runnable() {
						@Override
						public void run() {
							getFrame().toggleArrangeTop();
						}
					}));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setButtons(buttons);
	}

	/*
	 * Load any resources here.
	 */
	private void loadResource() {
		try {
			tarmacTexture = scaleResourceImagePaint("/textures/tarmac2.jpg",
					300, 300);
			grassTexture = scaleResourceImagePaint("/textures/grass.jpg", 300,
					300);
			dirtTexture = scaleResourceImagePaint("/textures/dirt2.jpg", 150,
					150);
			stopwayTexture = scaleResourceImagePaint("/textures/stopway.jpg",
					300, 300);
		} catch (IOException e) {
			setUsingTextures(false);
			e.printStackTrace();
		}
	}

	protected void paintView(Graphics2D g2){
		// Create the image buffer for drawing the world.
		BufferedImage world_buffer = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D world_g2 = (Graphics2D) world_buffer.getGraphics();
		world_g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Translate and scale the world.
		world_g2.setTransform(getTransform());
		// Draw the background.
		try {
			drawBackground(world_g2);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		// Draw the graded region.
		drawGraded(world_g2);
		// Draw the runway.
		drawRunway(world_g2);
		// Draw the world to the screen.
		g2.drawImage(world_buffer, 0, 0, null);
		// Draw the overlay to the screen.
		drawOverlay(g2);
	}


	private void drawBackground(Graphics2D world_g2)
			throws NoninvertibleTransformException {
		if (isUsingTextures()) {
			world_g2.setPaint(grassTexture);
		} else {
			world_g2.setPaint(getBackground());
		}
		reverseRectangle(world_g2, new Point(0, 0), new Point(0, getHeight()),
				new Point(getWidth(), getHeight()), new Point(getWidth(), 0));
	}

	private void drawGraded(Graphics2D g2) {
		GeneralPath poly;

		/*
		 * X coordinates of the polygon edges
		 */
		int clearedAreaMinX = toScaledX(0 - getOppositeDirectionStopway()
				- getOppositeStripEnd());
		int gradedStartMinX = toScaledX(150);
		int gradedEndMinX = toScaledX(300);
		int gradedEndMaxX = toScaledX(getDefaultTORA() - 300);
		int gradedStartMaxX = toScaledX(getDefaultTORA() - 150);
		int clearedAreaMaxX = toScaledX(getDefaultASDA() + getFacingStripEnd());

		int[] xPoints = {
		/* 01 */clearedAreaMinX,
		/* 02 */gradedStartMinX,
		/* 03 */gradedEndMinX,
		/* 04 */gradedEndMaxX,
		/* 05 */gradedStartMaxX,
		/* 06 */clearedAreaMaxX,
		/* 07 */clearedAreaMaxX,
		/* 08 */gradedStartMaxX,
		/* 09 */gradedEndMaxX,
		/* 10 */gradedEndMinX,
		/* 11 */gradedStartMinX,
		/* 12 */clearedAreaMinX };

		/*
		 * Y coordinates of polygon vertices
		 */
		int[] yPoints = {
		/* 01 */(int) (toScaledY(75)),
		/* 02 */(int) (toScaledY(75)),
		/* 03 */(int) (toScaledY(105)),
		/* 04 */(int) (toScaledY(105)),
		/* 05 */(int) (toScaledY(75)),
		/* 06 */(int) (toScaledY(75)),
		/* 07 */(int) (toScaledY(-75)),
		/* 08 */(int) (toScaledY(-75)),
		/* 09 */(int) toScaledY(-105),
		/* 10 */(int) toScaledY(-105),
		/* 11 */(int) (toScaledY(-75)),
		/* 12 */(int) (toScaledY(-75)) };
		
		// Create a path.
		poly = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
		// Draw points in path.
		poly.moveTo(xPoints[0], yPoints[0]);
		for (int index = 1; index < xPoints.length; index++) {
			poly.lineTo(xPoints[index], yPoints[index]);
		}
		poly.closePath();
		// Fill CGA.
		if (isUsingTextures()) {
			g2.setPaint(dirtTexture);
		} else {
			g2.setPaint(new Color(125, 215, 120));
		}
		g2.fill(poly);
		// Draw the outline of CGA.
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((this.getWidth() / 300 > 1) ? (this
				.getWidth() / 300) : 1));
		g2.draw(poly);
	}

	/*
	 * Draw the runway rectangle
	 */
	private void drawRunway(Graphics2D g2) {
		// Get the start and end X.
		int startX = Math.min(toScaledX(0), toScaledX(getDefaultTORA()));
		int endX = Math.max(toScaledX(0), toScaledX(getDefaultTORA()));
		// Create the rectangle shape.
		Rectangle run = new Rectangle(startX, // x coordinate
				toScaledY(-30), // y coordinate
				endX - startX, // width of runway
				scaleY(60) // height of runway
		);
		// Draw the runway area.
		if (isUsingTextures()) {
			g2.setPaint(tarmacTexture);
		} else {
			g2.setPaint(ColourSchema.tarmac);
		}
		g2.fill(run);

		// Draw the stopway in the current direction.
		float facing_stopway_length = getFacingStopway();
		Rectangle facing_stopway = new Rectangle(endX, toScaledY(-30), // y
																		// coordinate
				scaleAbsX(facing_stopway_length), // width of runway
				scaleY(60) // height of runway
		);
		if (isUsingTextures()) {
			g2.setPaint(stopwayTexture);
		} else {
			g2.setPaint(ColourSchema.stopway);
		}
		g2.fill(facing_stopway);

		// Draw the stopway in the opposite direction.
		float opposite_stopway_length = getOppositeDirectionStopway();
		Rectangle opposite_stopway = new Rectangle(startX
				- scaleAbsX(opposite_stopway_length), toScaledY(-30), // y
																		// coordinate
				scaleAbsX(opposite_stopway_length), // width of runway
				scaleY(60) // height of runway
		);
		if (isUsingTextures()) {
			g2.setPaint(stopwayTexture);
		} else {
			g2.setPaint(ColourSchema.stopway);
		}
		g2.fill(opposite_stopway);
	}

	/*
	 * Draws the obstacle overlay to the screen.
	 */
	private void drawObstacleOverlay(Graphics2D g2) {
		if (obstacleExists()) {
			// Get the overlay screen position of the obstacle.
			Point scr_obs = overlayPoint(new Point(
					toScaledX(getObstacleDistance() + getDefaultTHR()),
					toScaledY(getObstacleCenterDisp())));
			int x = scr_obs.x;
			int y = scr_obs.y;
			// Draw obstacle cross.
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(2f));
			g2.drawLine(x - 4, y - 4, x + 4, y + 4);
			g2.drawLine(x - 4, y + 4, x + 4, y - 4);
			Font font = new Font("default", Font.BOLD, (int) (14));
			drawString(g2, font, "Obstacle", x, y - 15, Color.white,
					Color.black);
		}
	}

	/*
	 * Draw the direction overlay arrow.
	 */
	private void drawDirectionOverlay(Graphics2D g2) {
		Font font = new Font("default", Font.BOLD, 16);
		int length_vert_spacing = getVerticalSpacing();
		drawLineBetween(g2, font, overlayPoint(toScaledX(0), toScaledY(0)),
				overlayPoint(toScaledX(getDefaultTORA() / 10), toScaledY(0)),
				"Current direction", 3 * length_vert_spacing);
	}

	/*
	 * draw overlay, drawn over the transformed image.
	 */
	private void drawOverlay(Graphics2D g2) {
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
		drawLineBetween(g2, font, scr_start, scr_tora, 
				String.format("TORA = %.2fm", getRedeclaredTORA()), 0);
		drawLineBetween(g2, font, scr_start, scr_toda, 
				String.format("TODA = %.2fm", getRedeclaredTODA()), length_vert_spacing);
		drawLineBetween(g2, font, scr_start, scr_asda, 
				String.format("ASDA = %.2fm", getRedeclaredASDA()), 2*length_vert_spacing);
		if (isBeforeObstacle()) {
			Point scr_thr = overlayPoint(startX + scaleX(getDefaultTHR()),
					startY);
			Point scr_lda = overlayPoint(startX
					+ scaleX(getDefaultTHR() + getRedeclaredLDA()), startY);
			drawLineBetween(g2, font, scr_thr, scr_lda, 
					String.format("LDA = %.2fm", getRedeclaredLDA()), 
					-length_vert_spacing);
			if( getDefaultTHR() > 0)
			drawLineBetween(g2, font, scr_start, scr_thr, 
					String.format("Threshold = %.2fm", getDefaultTHR()), -2 * length_vert_spacing);
		} else {
			Point scr_thr = overlayPoint(toScaledX(getDefaultTODA()
					- getRedeclaredLDA()), startY);
			drawLineBetween(g2, font, scr_thr, scr_tora, 
					String.format("LDA = %.2fm", getRedeclaredLDA()), -length_vert_spacing);
		}
		drawDesignators(g2);
		drawObstacleOverlay(g2);
		drawDirectionOverlay(g2);
	}

	/*
	 * Draws the designator overlays beneath the runway.
	 */
	private void drawDesignators(Graphics2D g2) {
		// Set the font.
		Font font = new Font("default", Font.BOLD, 18);
		g2.setFont(font);
		// Calculate overlay screen positions.
		int startX = Math.min(toScaledX(0) - 20,
				toScaledX(getDefaultTORA()) + 20);
		int endX = Math
				.max(toScaledX(0) - 20, toScaledX(getDefaultTORA()) + 20);
		int y = toScaledY(0);
		Point scr_low = overlayPoint(new Point(startX, y));
		Point scr_high = overlayPoint(new Point(endX, y));

		scr_low.y += 10;
		scr_high.y += 10;

		drawString(g2, font, getLowDesignatorAngle(), scr_low.x, scr_low.y,
				ColourSchema.txt_designator, ColourSchema.txt_shadow_gray);
		drawString(g2, font, getLowDesignatorSymbol(), scr_low.x,
				scr_low.y + 15, ColourSchema.txt_designator,
				ColourSchema.txt_shadow_gray);
		drawString(g2, font, getHighDesignatorAngle(), scr_high.x, scr_high.y,
				ColourSchema.txt_designator, ColourSchema.txt_shadow_gray);
		drawString(g2, font, getHighDesignatorSymbol(), scr_high.x,
				scr_high.y + 15, ColourSchema.txt_designator,
				ColourSchema.txt_shadow_gray);
	}

	private int getVerticalSpacing() {
		if (const_spacing_vert / getScale() < 25) {
			return 25;
		}
		return (int) (const_spacing_vert / getScale());
	}

	/*
	 * Gets the x on-screen ratio.
	 */
	@Override
	protected float getDrawRatioX() {
		return getWidth() / (float) getDefaultTODA() * getRatioOnScreen();
	}

	/*
	 * Gets the y on-screen ratio.
	 */
	@Override
	protected float getDrawRatioY() {
		return this.getHeight() / ((float) getInstrumentWidth() * 2f)
				* getRatioOnScreen();
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		super.mouseDragged(event);
		// If not in any mode, do nothing.
		if (buttonMenuOpen() || rotateMode == null || dragStartPosition == null)
			return;
		// If in drag mode
		if (!rotateMode.booleanValue()) {
			Point newMousePos = event.getPoint();
			setCurrentPosition(new Point(
					(int) ((currentPositionBeforeDrag.x + newMousePos.x - dragStartPosition.x)),
					(int) ((currentPositionBeforeDrag.y + newMousePos.y - dragStartPosition.y))));
			repaint();
		}
		// If in rotate mode
		if (rotateMode.booleanValue()) {
			// Get mouse position relative to centre.
			Point newMousePos = event.getPoint();
			int xd = newMousePos.x - getWidth()/2;
			int yd = newMousePos.y - getHeight()/2;
			// Calculate the difference between the mouse position angle before drag and presently.
			float diff = mouseVectorRotationBeforeDrag - vectorToAngle(xd, yd);
			// Add this as an offset to the original rotation.
			setRotation(currentRotationBeforeDrag + diff);
			repaint();
		}
	}
	
	/*
	 * Calculated the angle of a 2D vector.
	 */
	private static float vectorToAngle(int dx, int dy){
		// 0 checks
		if(dx == 0){
			if(dy > 0){
				return 180;
			}else{
				return 0;
			}
		}
		if(dy == 0){
			if( dx > 0 ){
				return 90;
			}else{
				return 270;
			}
		}
		// Get the local angle from the triangle.
		float localAngle = (float)Math.abs(Math.toDegrees(Math.atan(dy/(float)dx)));
		// Calculate the global angle.
		if( dx > 0 ){
			// Positive x.
			if( dy > 0 ){
				// Positive y.
				return 90-localAngle;
			}else{
				// Negative y.
				return 90+localAngle;
			}
		}else{
			// Negative x.
			if( dy > 0 ){
				// Positive y.
				return 270+localAngle;
			}else{
				// Negative y.
				return 270-localAngle;
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		super.mousePressed(event);
		// Interface dragging.
		if (buttonMenuOpen()) {
			return;
		}
		// Check for drag start
		if (event.getButton() == MouseEvent.BUTTON1) {
			// Put into drag mode.
			rotateMode = new Boolean(false);
			currentPositionBeforeDrag = getCurrentPosition();
		} else if (event.getButton() == MouseEvent.BUTTON3) {
			// Put into rotate mode.
			rotateMode = new Boolean(true);
			// Get the world's current rotation before the drag.
			currentRotationBeforeDrag = getRotation();
			Point newMousePos = event.getPoint();
			int xd = newMousePos.x - getWidth()/2;
			int yd = newMousePos.y - getHeight()/2;
			// Calculate the angle the mouse makes with centre of screen.
			mouseVectorRotationBeforeDrag = vectorToAngle(xd, yd);
		} else {
			return;
		}
		// Set the drag start point.
		dragStartPosition = event.getPoint();
	}


	@Override
	public void mouseReleased(MouseEvent event) {
		// Button clicks
		if (buttonMenuOpen()) {
			return;
		}
		dragStartPosition = null;
		if (event.getButton() == MouseEvent.BUTTON1) {
			currentPositionBeforeDrag = null;
		} else if (event.getButton() == MouseEvent.BUTTON3) {
			currentRotationBeforeDrag = null;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		setZoom(getScale() + event.getWheelRotation() * -0.1f);
		repaint();
	}

	/*
	 * Controller's setter methods.
	 */

	public void resetDrag() {
		setCurrentPosition(new Point(0, 0));
		setRotation(0);
		setScale(1);
	}

	/*
	 * Sets the auto-rotation amount of the designator.
	 */
	public void setAutoRotation(int dRotation) {
		this.dRotation = dRotation;
	}

}
