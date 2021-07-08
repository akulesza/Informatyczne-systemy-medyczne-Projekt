package model;

import javax.swing.JOptionPane;

public class IncorrectDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7226945557483669552L;

	/**
	 * metody obsługujące wyjatki wprowadzania błędnych danych
	 */
	public IncorrectDataException() {
		super();
		JOptionPane.showMessageDialog(null, "Podane dane sa niepoprawne");
	}

	public IncorrectDataException(String message) {
		super(message);
		JOptionPane.showMessageDialog(null, "Podane dane sa niepoprawne: "+ message);
	}

	public IncorrectDataException(Throwable cause) {
		super(cause);
		JOptionPane.showMessageDialog(null, "Podane dane sa niepoprawne");
	}

	public IncorrectDataException(String message, Throwable cause) {
		super(message, cause);
		JOptionPane.showMessageDialog(null, "Podane dane sa niepoprawne: "+ message);
	}


}
