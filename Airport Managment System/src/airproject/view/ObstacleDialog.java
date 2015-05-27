package airproject.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;

public class ObstacleDialog extends JDialog{
	private JTextField txtHeight;
	private JTextField txtCentrDist;
	private JTextField txtLowThrDist;
	private JTextField txtHighDist;
	private JLabel lblHighDist;
	private JTextField txtName;
	private JLabel lblName;
	private JButton btnNewButton;
	private JButton btnCancel;
	private JButton btnAdd;
	
	public static void main(String[] args) {
		ObstacleDialog n = new ObstacleDialog(null,"New Obstale",false,true);
	}
	
	public ObstacleDialog(JFrame owner, String title,boolean modal, boolean isVisible) {
		super(owner, title, modal);
		this.setVisible(isVisible);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	
		this.setSize(new Dimension(180, 315));
		this.setResizable(false);
	
		initGUI();
		getContentPane().setLayout(null);	
		
	}
	
		private void initGUI(){
			
			//lblName = new JLabel("Name:");
		//	lblName.setBounds(6, 10, 61, 16);
			//getContentPane().add(lblName);
			
		//	txtName = new JTextField();
			//txtName.setColumns(10);
			//txtName.setBounds(5, 25, 166, 28);
			//getContentPane().add(txtName);
			
			JLabel lblHeight = new JLabel("Height:");
			lblHeight.setBounds(5, 50, 61, 16);
			getContentPane().add(lblHeight);
		
			txtHeight = new JTextField();
			txtHeight.setBounds(5, 71, 166, 28);
			getContentPane().add(txtHeight);
			txtHeight.setColumns(10);
			
			JLabel lblCentrDist = new JLabel("Distance to the central line: ");
			lblCentrDist.setBounds(5, 100, 197, 16);
			getContentPane().add(lblCentrDist);
			
			txtCentrDist = new JTextField();
			txtCentrDist.setColumns(10);
			txtCentrDist.setBounds(5, 121, 166, 28);
			getContentPane().add(txtCentrDist);
			
			JLabel lblLowThrDist = new JLabel("Distance to the low THR:");
			lblLowThrDist.setBounds(5, 148, 215, 16);
			getContentPane().add(lblLowThrDist);
			
			txtLowThrDist = new JTextField();
			txtLowThrDist.setColumns(10);
			txtLowThrDist.setBounds(5, 170, 166, 28);
			getContentPane().add(txtLowThrDist);
			
			lblHighDist = new JLabel("Distance to the high THR:");
			lblHighDist.setBounds(5, 202, 166, 16);
			getContentPane().add(lblHighDist);
			
			txtHighDist = new JTextField();
			txtHighDist.setColumns(10);
			txtHighDist.setBounds(5, 219, 166, 28);
			getContentPane().add(txtHighDist);
			
			btnCancel = new JButton("Cancel");
			btnCancel.setBounds(6, 259, 73, 29);
			getContentPane().add(btnCancel);
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ObstacleDialog.this.dispose();	
				}
				
			});
			
			
			btnAdd = new JButton("Add");
			btnAdd.setBounds(98, 259, 73, 29);
			getContentPane().add(btnAdd);
			
		}

		
		private JTextField getTxtHeight() {
			return txtHeight;
		}

		private JTextField getTxtCentrDist() {
			return txtCentrDist;
		}
		
		private JTextField getTxtLowDist() {
			return txtLowThrDist;
		}

		private JTextField getTxtHighDist() {
			return txtHighDist;
		}

		
		public JTextField getTxtLowThrDist() {
			return txtLowThrDist;
		}

		public void setTxtLowThrDist(float txtLowThrDist) {
			this.txtLowThrDist.setText(Float.toString(txtLowThrDist));
		}

		public void setTxtHeight(float txtHeight) {
			this.txtHeight.setText(Float.toString(txtHeight));
		}

		public void setTxtCentrDist(float txtCentrDist) {
			this.txtCentrDist.setText(Float.toString(txtCentrDist));
		}

		public void setTxtHighDist(float txtHighDist) {
			this.txtHighDist.setText(Float.toString(txtHighDist));
		}

		public void setTxtName(float txtName) {
			this.txtName.setText(Float.toString(txtName));
		}

		/*
		 * Gets the obstacle parameters in the form of float array.
		 * 
		 * Throws GuiException if a parameter is poorly formatted.
		 */
		public float[] getObstacleParameters() throws GuiException {
			float[] params = new float[4];
			// Attempt to convert height text to float.
			try {
				params[GuiConstants.DATA_OBSTACLE_HEIGHT] = Float
						.parseFloat((String) getTxtHeight().getText());
			} catch (NumberFormatException e) {
				throw new GuiException("The obstacle height value input is not a valid number!");
			}

			// Attempt to convert height text to float.
			try {
				params[GuiConstants.DATA_OBSTACLE_DISP_LOW] = Float
						.parseFloat((String) getTxtLowDist().getText());
			} catch (NumberFormatException e) {
				throw new GuiException("The obstacle displacement from lowest threshold value input is not a valid number!");
			}

			// Attempt to convert height text to float.
			try {
				params[GuiConstants.DATA_OBSTACLE_DISP_HIGH] = Float
						.parseFloat((String) getTxtHighDist().getText());
			} catch (NumberFormatException e) {
				throw new GuiException("The obstacle displacement from highest threshold value input is not a valid number!");
			}

			// Attempt to convert height text to float.
			try {
				params[GuiConstants.DATA_OBSTACLE_DISP_CENTER] = Float
						.parseFloat((String) getTxtCentrDist().getText());
			} catch (NumberFormatException e) {
				throw new GuiException("The obstacle centerline displacement value input is not a valid number!");
			}
			return params;
		}
		
		
		public void setCreateListener(ActionListener al){
			this.btnAdd.addActionListener(al);
		}
		
		public void setObstacleParameters(float[] params){
			setTxtHeight(params[GuiConstants.DATA_OBSTACLE_HEIGHT]);
			setTxtLowThrDist(params[GuiConstants.DATA_OBSTACLE_DISP_LOW]);
			setTxtHighDist(params[GuiConstants.DATA_OBSTACLE_DISP_HIGH]);
			setTxtCentrDist(params[GuiConstants.DATA_OBSTACLE_DISP_CENTER]);
			
			
		}

		
}
