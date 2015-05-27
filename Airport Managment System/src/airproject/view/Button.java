package airproject.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Button {

	Image img;
	Rectangle area;
	Runnable onClick;
	String text;
	
	public Button(Image img, String text, int x, int y, int w, int h, Runnable onClick) {
		this.img = img;
		this.text = text;
		area = new Rectangle(x,y,w,h);
		this.onClick = onClick;
	}
	
	public boolean assessMouseClick(Point p){
		if( area.contains(p)){
			onClick.run();
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D g2){
		g2.setColor(new Color(180,180,180,240));
		g2.fillRect(area.x, area.y, area.width, area.height);
		g2.drawImage(img, area.x, area.y, area.width, area.width, null);
	}
	
}
