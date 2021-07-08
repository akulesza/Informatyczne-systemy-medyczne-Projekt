package controller;
import java.sql.*;
import java.util.ArrayList;

import model.Measurements;
import model.Patient;
import run.AppMain;

public class JdbcConnection
{
	/*
	 * System bazodanowy Apache Derby (tryb embedded, jezeli baza danych nie istnieje, to zostanie utworzona)
	 */
	public static final String JDBC_DRIVER="org.apache.derby.jdbc.EmbeddedDriver";
	public static final String DB_URL="jdbc:derby:IsmedDB;create=true";
	public static final String DB_USER="";
	public static final String DB_PASSWORD="";
	
	private static Connection conn;
	
	
	public static void load()
	{
		
		/*
		 * Rejestrowanie sterownika bazy danych 
		 */
		try
		{
			Class.forName(JDBC_DRIVER);			
			System.out.println("Sterownik bazy danych zostal wczytany\n");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Blad przy wczytywaniu sterownika\n");
			e.printStackTrace();
			System.exit(1);
		} 
		
		/*
		 * Tworzenie polaczenia z baza danych
		 */		
		try 
		{
			conn=DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			//System.out.println("Polaczenie z baza danych: " + conn.getMetaData().getURL());
			//System.out.println("Klasa obiektu polaczenia: " + conn.getClass());
			loadTables();
			loadData();

		}
		catch (SQLException e)
		{
			System.out.println("Blad przy tworzeniu polaczenia\n");
			e.printStackTrace();
		}
	}


	private static void loadData() {
		System.out.println("Wczytywanie danych z bazy...");
		try (Statement stmt = conn.createStatement())
		{
			//pobranie rekordow tabeli pacjentow uporzadkowanych po nazwisku
			try (ResultSet rs=stmt.executeQuery("SELECT * FROM Patients ORDER BY Name"))
			{
				
				while (rs.next()) {
					String patientID = rs.getString("PatientId");
					String patientName = rs.getString("Name");
					String patientPesel = rs.getString("Pesel");
					ArrayList<Measurements> measurements=new ArrayList<Measurements>();
					try (Statement stmt2 = conn.createStatement()){
						try (ResultSet rs2=stmt2.executeQuery("SELECT * FROM Measurements WHERE PatientId="+patientID)){
							while (rs2.next()) {
								Measurements meas = new Measurements(rs2.getDouble("Systolic"), rs2.getDouble("Diastolic"), rs2.getDouble("Pulse"));
								measurements.add(meas);
							}
						}
					}
					Patient p = new Patient(patientName, patientPesel, measurements);
					AppMain.m.addPatient(p);
					System.out.println(rs.getString("PatientId") + "\t" + rs.getString("Name") + "\t" + rs.getString("Pesel"));
				}
				
 				System.out.println("Dane poprawnie za³adowane z bazy danych :> ");
			}


		}catch(SQLException e) {
			System.out.println("Nast¹pi³ b³¹d przy wczytywaniu danych z bazy danych");
		}
	}

	private static void loadTables() {
		try {
			Statement stmt = conn.createStatement();
			if (!tableExists(conn,"Patients"))
			{
				stmt.execute("CREATE TABLE Patients" +
							 "(" +
							 "PatientId		INTEGER NOT NULL PRIMARY KEY" +
							 "				GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +	//Derby
							 "Name			VARCHAR(128) NOT NULL," +
							 "Pesel			CHAR(11) NOT NULL UNIQUE" +
							 ")");

				System.out.println("Utworzono tabele Patients");
			}
			
			if (!tableExists(conn,"Measurements"))
			{
				stmt.execute("CREATE TABLE Measurements" +
						 "(" +
						 "TestId		INTEGER NOT NULL PRIMARY KEY" +
						 "				GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +	//Derby				 
						 "Systolic		INTEGER NOT NULL," +
						 "Diastolic		INTEGER NOT NULL," +
						 "Pulse			INTEGER NOT NULL," +
						 "PatientId		INTEGER NOT NULL REFERENCES Patients(PatientId)" +
						 ")");
			
				System.out.println("Utworzono tabele Measurements");
			}

		}catch (SQLException e)
		{
			System.out.println("B³¹d");
			e.printStackTrace();
		}
	}
	
	public static boolean addPatient(String name, String pesel) {
		try {
			Statement stmt = conn.createStatement();
			String values = "('";
			values += name;
			values += "', '";
			values += pesel;
			values += "')";
			stmt.executeUpdate("INSERT INTO Patients (Name, Pesel) VALUES "+values);
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Nast¹pi³ b³¹d przy zapisie danych pacjenta do bazy danych");
			return false;
		}
		return true;
	}
	public static boolean addMeasurement(Measurements m, Patient p) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery("SELECT PatientId FROM Patients WHERE Pesel='"+p.getPesel()+"'");
			rs.next();
			int id = rs.getInt("PatientId");

			Statement stmt2 = conn.createStatement();
			String values = "(";
			values += Double.toString(m.getPressureSystolic());
			values += ", ";
			values += Double.toString(m.getPressureDiastolic());
			values += ", ";
			values += Double.toString(m.getPulse());
			values += ", ";
			values += Integer.toString(id);
			values += ")";
			stmt2.executeUpdate("INSERT INTO Measurements (Systolic, Diastolic, Pulse, PatientId) VALUES "+values);
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Nast¹pi³ b³¹d przy zapisie danych pomiarowych do bazy danych");
			return false;
		}
		return true;
	}
	
	private static boolean tableExists(Connection conn, String tableName) throws SQLException
	{
		boolean exists=false;
		
		DatabaseMetaData dbmd=conn.getMetaData();
        
		try (ResultSet rs=dbmd.getTables(null,null,tableName.toUpperCase(),null))
		{
			exists=rs.next();
		}
        
		return(exists);
	}



	public static void close() {
		try {
			//Statement stmt = conn.createStatement();
			//stmt.executeUpdate("DELETE FROM Patients WHERE Name='sss'");
			
			System.out.println("Zamykanie po³¹czenia...");
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("B³¹d przy zamykaniu po³¹czenia z baz¹ danych");
		}
		
	}
}
