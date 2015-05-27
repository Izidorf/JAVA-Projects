package airproject.view;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class IconHandler {

	public static final String path_compass = "/icons/compass.png";
	public static final String path_fullscreen = "/icons/view_full_screen.png";
	public static final String path_paint = "/icons/paint_bucket.png";
	
	static Map<String, Image> icons = new HashMap<>();
	
	public static Image getIcon(String path) throws IOException{
		if( !icons.containsKey(path)){
			icons.put(path, ImageIO.read(IconHandler.class.getResourceAsStream(path)));
		}
		return icons.get(path);
	}


}
