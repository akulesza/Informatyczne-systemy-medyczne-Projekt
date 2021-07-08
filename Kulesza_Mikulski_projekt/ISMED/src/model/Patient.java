package model;

/**
 * Klasa reprezentujaca pacjenta
 * 
 * @author akulesza & jmikulsk
 *
 */
import java.util.ArrayList;

public class Patient implements Comparable<Patient>
{

	private String mName;
	private String mPesel;
	private ArrayList<Measurements> mMeasurementsList=new ArrayList<Measurements>();

	/*
	* konstruktor
	*@param name 			imie i nazwisko
	*@param pesel			pesel
	*@param measurements	pomiary
	*/
	public Patient(String name,String pesel,ArrayList<Measurements> measurements)
	{
		this.mName=name;
		this.mPesel=pesel;
		this.mMeasurementsList=measurements;
	}
	public Patient(String name,String pesel)
	{
		this.mName=name;
		this.mPesel=pesel;
		this.mMeasurementsList=new ArrayList<Measurements>();
	}
	
	/*
	* metoda get imienia i nazwiska
	*
	*/
	public String getName()
	{
		return(this.mName);
	}
	/*
	* metoda get pesel
	*
	*/
	public String getPesel()
	{
		return(this.mPesel);
	}
	
	/*
	* metoda set pesel
	*
	*/
	public void setPesel(String pesel)
	{
		this.mPesel = pesel;
	}
	/*
	* metoda set imienia i nazwiska
	*
	*/
	public void setName(String name)
	{
		this.mName = name;
	}

	/*
	* metoda get listy badań
	*
	*/
	public ArrayList<Measurements> getMeasurementsList() 
	{
		return (this.mMeasurementsList);
	}

	/*
	* metoda set listy badań
	*
	*/
	public void setMeasurementsList(ArrayList<Measurements> measurements) 
	{
		this.mMeasurementsList = measurements;
	}


	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;

		result = prime * result	+ ((this.mPesel == null) ? 0 : this.mPesel.hashCode());

		return (result);
	}

	/*
	* metoda porównująca dwóch pacjentow
	*
	*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || this.getClass() != obj.getClass())
			return false;

		Patient other = (Patient) obj;
		if (this.mPesel == null) {
			if (other.mPesel != null)
				return(false);
		} else if (!this.mPesel.equals(other.mPesel))
			return(false);
		return(true);
	}

	@Override
	public int compareTo(Patient other) {
		return (this.mPesel.compareTo(other.mPesel));
	}

	@Override
	public String toString() {
		return ("[" + this.mPesel + ", " + this.mName + "]");
	}
}