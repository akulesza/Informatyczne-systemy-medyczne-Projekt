package run;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import controller.AppController;
import controller.JdbcConnection;
import model.AppModel;
import model.Measurements;
import model.Patient;
import view.AppView;

/**
 * Klasa startujaca aplikacjê
 * 
 * @author akulesza & jmikulsk
 *
 */
public class AppMain
{
	public static AppModel m;
	/**
	 * Metoda glowna
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater( new Runnable() {
			public void run()
			{
				//model
				m=new AppModel();
				
				JdbcConnection.load();
				
				addExampleData(m);
				
				
				//widok
				AppView v = new AppView(m);
				
				//kontroler
				AppController c=new AppController(v, m);
				v.setController(c);
				
				v.setVisible(true);
		
			       
			}
	    });
	}
	
	public static void addExampleData(AppModel m) {
		ArrayList<Measurements> patient1 = new ArrayList<Measurements>();
		patient1.add(new Measurements (120,75,60));
		patient1.add(new Measurements (128, 82,92));
		patient1.add(new Measurements (118,78, 72));
		
		ArrayList<Measurements> patient2 = new ArrayList<Measurements>();
		patient2.add(new Measurements (142,87, 67));
		patient2.add(new Measurements (147,92, 70));
		patient2.add(new Measurements (150,90, 71));
		
		ArrayList<Measurements> patient3 = new ArrayList<Measurements>();
		patient3.add(new Measurements (152,75, 85));
		patient3.add(new Measurements (155, 78,86));
		patient3.add(new Measurements (150,72, 82));
		
		m.addPatient(new Patient("Jan Kowalski","75101500123",patient1));
		m.addPatient(new Patient("Agata Malinowska","70091000453",patient2));
		m.addPatient(new Patient("Ktos Inny","11111111111",patient3));
	}
}
