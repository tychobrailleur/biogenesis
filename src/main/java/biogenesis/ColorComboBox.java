package biogenesis;

import java.awt.Color;

import javax.swing.JComboBox;

public class ColorComboBox extends JComboBox {
	private static final long serialVersionUID = Utils.VERSION;
	private static final String[] colorValues = {Messages.getString("T_RED"),Messages.getString("T_GREEN"),  //$NON-NLS-1$//$NON-NLS-2$
		Messages.getString("T_BLUE"),Messages.getString("T_CYAN"),Messages.getString("T_WHITE"),  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
		Messages.getString("T_GRAY"),Messages.getString("T_YELLOW")};  //$NON-NLS-1$//$NON-NLS-2$
	
	public ColorComboBox(Color c) {
		super(colorValues);
		setSelectedColor(c);
	}
	
	public Color getSelectedColor() {
		switch (getSelectedIndex()) {
		case 0: return Color.RED;
		case 1: return Color.GREEN;
		case 2: return Color.BLUE;
		case 3: return Color.CYAN;
		case 4: return Color.WHITE;
		case 5: return Color.GRAY;
		case 6: return Color.YELLOW;
		default: return Utils.ColorBROWN;
		}
	}
	
	public void setSelectedColor(Color c) {
		if (c.equals(Color.RED)) setSelectedIndex(0);
		if (c.equals(Color.GREEN)) setSelectedIndex(1);
		if (c.equals(Color.BLUE)) setSelectedIndex(2);
		if (c.equals(Color.CYAN)) setSelectedIndex(3);
		if (c.equals(Color.WHITE)) setSelectedIndex(4);
		if (c.equals(Color.GRAY)) setSelectedIndex(5);
		if (c.equals(Color.YELLOW)) setSelectedIndex(6);
	}
}
