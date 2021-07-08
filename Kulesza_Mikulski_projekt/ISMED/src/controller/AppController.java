package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.AppModel;
import model.IncorrectDataException;
import model.Measurements;
import model.Patient;
import view.AppView;

/**
 * Konstroler aplikacji
 * 
 * @author akulesza & jmikulsk
 *
 */
public class AppController  implements ListSelectionListener,ActionListener
{
	private AppView		mView=null;
	private AppModel	mModel=null;
	
	/**
	 * Konstruktor
	 * 
	 * @param v	widok
	 * @param m	model
	 */
	public AppController(AppView v, AppModel m)
	{
		this.mModel=m;
		this.mView=v;
	}
	
	/**
	 * Obsluga wybrania wiersza z tabeli (implementacja metody interfejsu ListSelectionListener)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		//pobranie z widoku numeru wybranego wiersza
		int index=this.mView.getSelectedIndex();

		//pobranie pacienta z modelu
		Patient p=this.mModel.get(index);
		
		//przekazanie pacienta do widoku
		this.mView.setPatient(p);
	}
	
	/**
	 * Obsluga nacisniecia przyciskow (implementacja metody interfejsu ActionListener)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("add_patient"))
		{		
			//pobranie pacienta z widoku
			Patient p=this.mView.getPatient();
			
			if (p!=null)
			{
				if (this.mModel.matchPatient(p) != null) {
					new IncorrectDataException("Pacjent o takich danych ju¿ znajduje siê w systemie!");
				}else {
					if(!Pattern.matches("[0-9]{11}", p.getPesel())) {
						new IncorrectDataException("nieprawid³owy numer PESEL");
						return;
					}
						
					
					//dodanie pacienta do modelu
					this.mModel.addPatient(p);
					JdbcConnection.addPatient(p.getName(), p.getPesel());
					
					//odswiezenie tabeli pacjentow
					this.mView.updatePatientList();
					
					//wyczyszczenie pol edycyjnych
					this.mView.setPatient(null);
				}
			}
		}
		if (e.getActionCommand().equals("add_measurement"))
		{			
			try {
				//pobranie pacienta z tabeli
				Patient p = this.mView.getPatient();
				if (p==null) {
					int index=this.mView.getSelectedIndex();
					p = this.mModel.get(index);
					throw new IncorrectDataException("dane pomiarowe musz¹ byæ przyporz¹dkowane jakiemuœ pacjentowi! Zaznacz pacjenta lub wprowadŸ dane nowego.");
				}
				Patient p1 = this.mModel.matchPatient(p);
				if (p1==null) {
					//dodanie pacienta do modelu
					this.mModel.addPatient(p);
					JdbcConnection.addPatient(p.getName(), p.getPesel());
					
					//odswiezenie tabeli pacjentow
					this.mView.updatePatientList();
					
					//TODO zaznaczyæ indeks nowododanego pacjenta
					
					p1 = this.mModel.matchPatient(p);
					
				}
			
				double pressureSys =this.mView.getMeasurement().get(0);
				double pressureDia =this.mView.getMeasurement().get(1);
				double pulse =this.mView.getMeasurement().get(2);
				Measurements m = new Measurements(pressureSys, pressureDia, pulse);
				//dodanie pacienta do modelu
				this.mModel.addMeasurement(m,p1);
				JdbcConnection.addMeasurement(m, p1);
			} catch (IncorrectDataException e1) {
				System.out.println("Danych nie wprowadzono, gdy¿ wykryto niepoprawne dane");
			}
			
		}
		
		if (e.getActionCommand().equals("show_measurements"))
		{
			int index = this.mView.getSelectedIndex();
			Patient p = this.mModel.get(index);
			if(p!=null)
			{
				this.mView.showMeasurements(p);
				
			}
		}
	}
}
