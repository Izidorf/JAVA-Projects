package airproject.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import airproject.model.Model;

public class LoadAirport implements ActionListener {
	
	Model model;
	String file;
	JLabel airportName;
	
	public  LoadAirport(String file, JLabel airportName){
		model= Model.getInstance();
		this.airportName=airportName;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

}
