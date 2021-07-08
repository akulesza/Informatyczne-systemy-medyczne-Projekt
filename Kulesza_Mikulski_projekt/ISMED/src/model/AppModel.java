package model;

/**
 * Klasa modelu aplikacji, reprezentujaca liste pacjentow
 * 
 * @author akulesza & jmikulsk
 *
 */
import java.util.ArrayList;
import java.util.List;

public class AppModel
{
	private List<Patient> mPatientList=new ArrayList<Patient>();
	
	/**
	* Kontruktor
	*@param
	*/
	public AppModel() {}
	
	public int size()
	{
		return(this.mPatientList.size());
	}

	/**
	* Metoda pobierajaca dane pacjenta z zaznaczonego wiersza
	*@return
	*/
	public Patient get(int index)
	{
		if (index>=0 && index<this.mPatientList.size())
			return(this.mPatientList.get(index));
		
		return(null);
	}
	
	/**
	* Metoda dodająca nowego pacjenta
	*@return
	*/
	public boolean addPatient(Patient val)
	{
		if (!this.mPatientList.contains(val))
			return(this.mPatientList.add(val));
		return(false);
	}
	
	/**
	* Metoda dodająca nowe badanie do wybranego pacjenta
	*@return
	*/
	@Deprecated
	public boolean addMeasurement(Measurements val, int index)
	{
		System.out.println(this.mPatientList.get(index).getMeasurementsList());
		return(this.mPatientList.get(index).getMeasurementsList().add(val));

	}
	
	public boolean addMeasurement(Measurements val, Patient p)
	{
		System.out.println(p.getMeasurementsList());
		return(p.getMeasurementsList().add(val)); 

	}

	/**
	* Metoda usuwajaca pacjenta
	*@return
	*/
	public void removePatient(int index)
	{
		if (index>=0 && index<this.mPatientList.size())		
			this.mPatientList.remove(index);
	}
	
	public void remove(Patient val)
	{
		this.mPatientList.remove(val);
	}

	/**
	* Metoda do porównania dwóch pacjentów
	*@return
	*/
	public Patient matchPatient(Patient p) {
		 Patient pMatch = null;
		 for (Patient patient : mPatientList) {
			 if (patient.getName().equals( p.getName()) && patient.getPesel().equals(p.getPesel())) {
				 pMatch = patient;
				 break;
			 }
		 }
		 return pMatch;
	}
}
