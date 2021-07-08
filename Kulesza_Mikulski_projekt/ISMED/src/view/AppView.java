package view;

import controller.JdbcConnection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import controller.AppController;
import controller.JdbcConnection;
import model.AppModel;
import model.IncorrectDataException;
import model.Patient;

/**
 * Widok aplikacji
 * 
 * @author akulesza & jmikulsk
 *
 */
public class AppView extends JFrame implements WindowListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6530581067452164988L;

	private AppModel	mModel;
	
	private JTextField	mNameEdit, mPeselEdit, mPressureEdit, mPulseEdit;
	
	private JButton		mAddButton, mAddMButton, mShowButton;
	private JTable		mTable;
	private JDrawPanelModel		mDrawPanel;
	
	/**
	 * Konstruktor
	 * 
	 * @param m model
	 */
	public AppView(AppModel m)
	{
		this.mModel=m;
		
		this.doGui();
	}
	
	/**
	 * Metoda ustawiajaca kontroler
	 * 
	 * @param c kontroler
	 */
	public void setController(AppController c)
	{
		this.mTable.getSelectionModel().addListSelectionListener(c);
		
		this.mAddButton.addActionListener(c);
		this.mAddMButton.addActionListener(c);
		
		this.mShowButton.addActionListener(c);
	}
	
	/**
	 * Metoda tworzaca pacjenta na podstwie pol edycyjnych
	 * 
	 * @return 
	 */
	public Patient getPatient()
	{
		Patient p=null;
		
		String name=this.mNameEdit.getText();
		String pesel=this.mPeselEdit.getText();
		
		
		if (!name.isBlank() && !pesel.isBlank())
			p=new Patient(name,pesel);

		return(p);
	}
	
	/**
	 * Metoda obsługująca wprowadzanie błędnych danych
	 * 
	 * @return 
	 */
	public ArrayList<Double> getMeasurement() throws IncorrectDataException
	{
		ArrayList<Double> m=null;;
		
		String stringPressure = this.mPressureEdit.getText();
		String stringPulse =this.mPulseEdit.getText();
		
		if(stringPressure.isBlank())
			throw new IncorrectDataException("pole z cinieniem nie moe by puste");
		else if (stringPulse.isBlank())
			throw new IncorrectDataException("pole z pulsem nie moe by puste");
		else {
			String[] pressure = stringPressure.split("/");
			if (pressure.length!=2)
				throw new IncorrectDataException("zle zapisane cinienie! Przykad poprawnego zapisu: 120/75");
			else {
				if(pressure[0].isBlank() || pressure[1].isBlank())
					throw new IncorrectDataException("zadna z wartoci nie moe by pusta");
				else {
					double sys = Double.parseDouble(pressure[0]);
					double dia = Double.parseDouble(pressure[1]);
					if (sys < 50) 
						throw new IncorrectDataException("cisnienie skurczowe jest za niskie!");
					else if (sys > 350) 
						throw new IncorrectDataException("cisnienie skurczowe jest za wysokie!");
					else if (dia < 30) 
						throw new IncorrectDataException("cisnienie rozkurczowe jest za niskie!");
					else if (dia > 250) 
						throw new IncorrectDataException("cisnienie rozkurczowe jest za wysokie!");
					else {
						double pulse=Double.parseDouble(stringPulse);
						if (pulse < 30) 
							throw new IncorrectDataException("puls zbyt niski!");
						if (pulse > 300) 
							throw new IncorrectDataException("puls zbyt wysoki!");
						else {
							m=new ArrayList<Double>();
							m.add(sys);
							m.add(dia);
							m.add(pulse);
						}
					}
				}
			}
		}

		return(m);
	}
	
	/**
	 * Metoda ustawiajaca pola edycyjne na podstawie pacjenta
	 * 
	 * @param p pacjent
	 */
	public void setPatient(Patient p)
	{
		if (p!=null)
		{
			this.mNameEdit.setText(p.getName());
			this.mPeselEdit.setText(p.getPesel());
		}
		else
		{
			this.mNameEdit.setText("");
			this.mPeselEdit.setText("");
		}
	}
	
	/**
	 * metoda pobierająca zaznaczony wiersz
	 *
	 * @return
	 */
	public int getSelectedIndex()
	{
		return(this.mTable.getSelectedRow());
	}
	
	/**
	 * Metoda powodujaca ponowne odmalowanie tabeli
	 */
	public void updatePatientList()
	{
		((JTableModel)this.mTable.getModel()).fireTableDataChanged();
		this.mTable.repaint();		
	}
	
	/**
	 * Metoda powodujaca wywietlenie wykresw z pomiarami
	 */
	
	public void showMeasurements(Patient p)
	{
		//wektory przechowujce dane pomiarowe pacjentw:
		Vector<Double> pressureSys = new Vector<Double>();
		Vector<Double> pressureDia = new Vector<Double>();
		//TODO wektor dla cinienia redniego
		Vector<Double> pulse = new Vector<Double>();
		
		for (int i = 0; i < p.getMeasurementsList().size(); i++)
		{
			//pobranie danych do wektorw
			pressureSys.addElement(p.getMeasurementsList().get(i).getPressureSystolic());
			pressureDia.addElement(p.getMeasurementsList().get(i).getPressureDiastolic());
			System.out.println(p.getMeasurementsList().get(i)); 
			pulse.addElement(p.getMeasurementsList().get(i).getPulse());			
		}
		for (int i = 1; i < p.getMeasurementsList().size(); i++)
		{
			//wywietlanie danych na wykresie:
			
			//this.mDrawPanel.paintComponent(getGraphics(), i, pressure.get(i-1), i+1, pressure.get(i));
			this.mDrawPanel.repaint();
		}

	}
	
	/**
	 * Metoda definiujca wyglad aplikacji
	 */
	private void doGui()
	{	
		//panel glowny
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.X_AXIS));
		this.getContentPane().add(mainPanel);
		
		//panel z wykresem i opcją "Zapisz"
		JPanel subpanelW = new JPanel();
		subpanelW.setLayout(new BoxLayout(subpanelW,BoxLayout.Y_AXIS));
		mainPanel.add(subpanelW);
		
		this.mDrawPanel = new JDrawPanelModel();
		this.mDrawPanel.setPreferredSize(new Dimension(350,180));
		this.mDrawPanel.setBackground(Color.GRAY);
		
		subpanelW.add(this.mDrawPanel);
		subpanelW.add(Box.createRigidArea(new Dimension(5,20)));
		
		subpanelW.add(new JButton("Zapisz"));
		subpanelW.add(Box.createRigidArea(new Dimension(5,20)));
		
		//panel z danymi pacjentw i moliwością dodania pacjenta/badania
		JPanel subpanelP = new JPanel();
		subpanelP.setLayout(new BoxLayout(subpanelP,BoxLayout.Y_AXIS));
		mainPanel.add(subpanelP);
		
		//utworzenie modelu tabeli i dodanie tabeli do panelu		
		JTableModel model=new JTableModel(this.mModel);
		this.mTable=new JTable(model);
		
		JScrollPane scroll=new JScrollPane(this.mTable);
		scroll.setPreferredSize(new Dimension(350,180));
		scroll.setMinimumSize(new Dimension(350,180)); 
		subpanelP.add(scroll);
		
		//przycisk "Dodaj" pacjenta
		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BoxLayout(subpanel,BoxLayout.X_AXIS));
		subpanelP.add(subpanel);
		
		subpanel.add(new JLabel("Imie i Nazwisko:"));
		
		subpanel.add(Box.createRigidArea(new Dimension(5,20)));
		
		this.mNameEdit=new JTextField();
		subpanel.add(this.mNameEdit);
		
		subpanel.add(Box.createRigidArea(new Dimension(5,20)));

		subpanel.add(new JLabel("PESEL:"));

		subpanel.add(Box.createRigidArea(new Dimension(5,20)));
		
		this.mPeselEdit=new JTextField();
		subpanel.add(this.mPeselEdit);
		
		subpanel.add(Box.createRigidArea(new Dimension(5,20)));
		
		this.mAddButton=new JButton("Dodaj");
		this.mAddButton.setActionCommand("add_patient");
		subpanel.add(this.mAddButton);
		
		//przynisk "Pokaż badania"
		this.mShowButton=new JButton("Poka� badania");
		this.mShowButton.setActionCommand("show_measurements");
		subpanel.add(this.mShowButton);
		
		//przycisk "Dodaj badanie"
		JPanel subpanelM = new JPanel();
		subpanelM.setLayout(new BoxLayout(subpanelM,BoxLayout.X_AXIS));
		subpanelP.add(subpanelM);
		
		subpanelM.add(new JLabel("Cinienie:"));
		
		subpanelM.add(Box.createRigidArea(new Dimension(5,20)));
		
		this.mPressureEdit=new JTextField();
		subpanelM.add(this.mPressureEdit);
		
		subpanelM.add(Box.createRigidArea(new Dimension(5,20)));

		subpanelM.add(new JLabel("Puls:"));

		subpanelM.add(Box.createRigidArea(new Dimension(5,20)));
		
		this.mPulseEdit=new JTextField();
		subpanelM.add(this.mPulseEdit);
		
		subpanelM.add(Box.createRigidArea(new Dimension(5,20)));
		
		this.mAddMButton=new JButton("Dodaj pomiar");
		this.mAddMButton.setActionCommand("add_measurement");
		subpanelM.add(this.mAddMButton);
		
		//ustawienia ramki okna
		this.setTitle("MVC example");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(this);
		this.pack();
	}

	

	@Override
	public void windowClosing(WindowEvent e) {
		JdbcConnection.close();
		this.dispose();
	}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
