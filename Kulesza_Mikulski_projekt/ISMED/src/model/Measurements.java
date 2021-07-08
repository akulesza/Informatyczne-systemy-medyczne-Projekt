package model;

/**
 * Klasa badania
 * 
 * @author akulesza & jmikulsk
 *
 */
public class Measurements {
	
	private double mPressureSystolic;
	private double mPressureDiastolic;
	private double mPulse;
	
	/*
	* Konstruktor
	*@param systolic 	ciśnienie skurczowe
	*@param diastolic 	ciśnienie rozkurczowe
	*@param pulse		puls
	*/
	public Measurements(double systolic, double diastolic, double pulse)
	{
		this.mPressureSystolic=systolic;
		this.mPressureDiastolic=diastolic;
		this.mPulse=pulse;
	}

	/*
	* metoda get ciśnienia skurczowego
	*
	*/
	public double getPressureSystolic() {
		return mPressureSystolic;
	}

	/*
	* metoda get ciśnienia rozkurczowego
	*
	*/
	public double getPressureDiastolic() {
		return mPressureDiastolic;
	}

    /*
	* metoda set ciśnienia skurczowego
	*
	*/
	public void setPressureSystolic(double mPressure) throws IncorrectDataException {
		if (mPressure < 50) {
			throw new IncorrectDataException("cinienie skurczowe jest za niskie!");
		}
		if (mPressure > 350) {
			throw new IncorrectDataException("cinienie skurczowe jest za wysokie!");
		}
		this.mPressureSystolic = mPressure;
	}
	
	/*
	* metoda set ciśnienia rozkurczowego
	*
	*/
	public void setPressureDiastolic(double mPressure)  throws IncorrectDataException {
		if (mPressure < 30) {
			throw new IncorrectDataException("cinienie rozkurczowe jest za niskie!");
		}
		if (mPressure > 250) {
			throw new IncorrectDataException("cinienie rozkurczowe jest za wysokie!");
		}
		this.mPressureDiastolic = mPressure;
	}

	/*
	* metoda get pulsu
	*
	*/
	public double getPulse() {
		return mPulse;
	}

	/*
	* metoda set pulsu
	*
	*/
	public void setPulse(double mPulse) throws IncorrectDataException {
		if (mPulse < 30) {
			throw new IncorrectDataException("puls zbyt niski!");
		}
		if (mPulse > 300) {
			throw new IncorrectDataException("puls zbyt wysoki!");
		}
		this.mPulse = mPulse;
	}

	@Override
	public String toString() {
		return ("[" + this.mPressureSystolic + ", " + this.mPressureDiastolic + ", " + this.mPulse + "]");
	}
	

}
