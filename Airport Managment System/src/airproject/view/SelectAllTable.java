package airproject.view;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.text.JTextComponent;

public class SelectAllTable extends JTable
{
	public void changeSelection(int row, int column, boolean toggle, boolean extend) {
		super.changeSelection(row, column, toggle, extend);

		if (editCellAt(row, column)) {
			Component editor = getEditorComponent();
			editor.requestFocusInWindow();
			((JTextComponent) editor).selectAll();
		}
	}
}