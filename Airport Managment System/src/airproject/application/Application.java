package airproject.application;

import java.lang.reflect.InvocationTargetException;

import airproject.controller.Controller;
import airproject.model.Model;
import airproject.view.View;

public class Application {

	public static void main(String[] args) {
		// Initialise the model.
		Model.getInstance();
		// Initialise the view.
		try {
			View view = new View();
			new Controller(view);
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		// Initialise the controller, links the model and the view.
	}
}
