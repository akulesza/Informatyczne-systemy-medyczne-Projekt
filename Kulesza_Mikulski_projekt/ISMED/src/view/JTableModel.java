package view;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import model.AppModel;
import model.Patient;

/**
 * Klasa modelu tabeli JTable 
 * 
 * @author akulesza & jmikulsk
 *
 */
public class JTableModel extends AbstractTableModel
{
	private final String COLUMN_NAMES[]= {"Imie i Nazwisko", "PESEL"};
			
	private AppModel mModel=null;

	/**
 	* konstruktor 
 	* @param m
 	*
 	*/
	public JTableModel(AppModel m)
	{
		this.mModel=m;
	}


	/**
 	* metoda licząca liczbę wierszy w tabeli 
 	* @return
 	*
 	*/
	@Override
	public int getRowCount()
	{
		if (this.mModel.size()<10)
			return(10);
		else
			return(this.mModel.size());
	}

	/**
 	* metoda licząca liczbę kolumn w tabeli 
 	* @return
 	*
 	*/
	@Override
	public int getColumnCount()
	{
		return(COLUMN_NAMES.length);
	}

	/**
 	* metoda zwrajacąca nazwę kolumny
 	* @return
 	*
 	*/
	@Override
	public String getColumnName(int columnIndex)
	{
		return(COLUMN_NAMES[columnIndex]);
	}
	
	/**
 	* metoda zwracająca dane pacjenta z wybranego wiersza 
 	* @return
 	*
 	*/
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (rowIndex<this.mModel.size())
		{
			Patient p=this.mModel.get(rowIndex);
				
			if (columnIndex==0)
				return(p.getName());
			else if (columnIndex==1)
				return(p.getPesel());
		}
		return null;
	}


	@Override
	public Class getColumnClass(int columnIndex)
	{
        if (this.getValueAt(0,columnIndex)!=null)
			return(this.getValueAt(0,columnIndex).getClass());
		
		return(String.class);		
	}
}
