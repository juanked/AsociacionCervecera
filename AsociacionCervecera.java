import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AsociacionCervecera {
	Statement st = null;
	Connection conn = null;
	public static void main(String[] args) {
		AsociacionCervecera ac = new AsociacionCervecera();
		ac.mainMenu();
	}
	
	public boolean DBconnect() throws Exception {
		boolean conectado = false;
		try{
			String drv = "com.mysql.jdbc.Driver";
			String	 serverAddress = "localhost:3306";
			String db = "ac";
			String user = "bd";
			String pass = "bdupm";
			String url = "jdbc:mysql://" + serverAddress + "/" + db;
			Class.forName(drv);
			System.out.println("Procediendo a conexi�n");
			Connection conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(true);
			conectado = true;
		}
		catch(Exception excepcion){
			excepcion.printStackTrace();
		}
		return conectado;
		
	}

	public boolean DBclose() throws Exception{
		boolean conectado = false;
		System.out.println("Saliendo.. �hasta otra!");
		if (conn != null) {
			try {
				conn.close();
				conectado=true;
			} catch (SQLException e) {
				System.out.println("Ha habido un problema con la desconexi�n:");
				e.printStackTrace();
			}
			System.exit(0);
		} else
			System.exit(0);
		return conectado;

	}

	public boolean createTableEmpleado() throws Exception{
		boolean creado = false;
		try{
			st = conn.createStatement();
			String sql = "CREATE TABLE Empleado" + "(id_empleado INTEGER not NULL,"+"nombre VARCHAR(255)" + "direccion VARCHAR(255)"  + "telefono VARCHAR(255)" + "salario DOUBLE" + "PRIMARY KEY (id_empleado)";
			st.executeUpdate(sql);
			creado = true;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return creado;
	}

	public boolean createTableGusta() throws Exception{
		boolean creado = false;
		try{
			st = conn.createStatement();
			String sql = "CREATE TABLE Gusta" + "(id_empleado INTEGER not NULL,"+"nombre VARCHAR(255)" + "direccion VARCHAR(255)"  + "telefono VARCHAR(255)" + "salario DOUBLE" + "PRIMARY KEY (id_empleado)";
			st.executeUpdate(sql);
			creado = true;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return creado;
	}

	public boolean loadEmpleados() {
		return false;
	}

	public boolean loadGustos(String fileName) {
		return false;
	}

	public ArrayList<Bar> getBarData() {
		return null;
	}

	public ArrayList<Cerveza> getCervezasFabricante() {
		return null;
	}

	public ArrayList<Cerveza> getCervezasPopulares() {
		return null;
	}

	public boolean addFotoColumn() {
		return false;
	}

	public boolean addEmpleadoFoto() {
		return false;
	}

	/*
	 * The private section of the code starts here. It is convenient to take a
	 * glance to the code, but it is STRICTLY FORBIDDEN to modify any part of
	 * the code bellow these lines.
	 * 
	 * Aqu� comienza la parte "privada" del c�digo. Es conviniente echar un
	 * vistazo al c�digo, pero est� ESTR�CTICAMENTE PROHIBIDO modificar
	 * cualquier parte del c�digo bajo estas l�neas.
	 */

	private void mainMenu() {
		Scanner sc = new Scanner(System.in);
		char menuOption = 'a';

		// Main menu loop
		do {
			System.out.println("Escoja una opci�n: ");
			System.out
					.println("  1) Crear las tablas \"empleado\" y \"gusta\".");
			System.out
					.println("  2) Cargar datos de los empleados y los gustos.");
			System.out.println("  3) Listar los bares almacenados.");
			System.out.println("  4) Listar las cervezas de un fabricante.");
			System.out.println("  5) Listar las cervezas m�s populares.");
			System.out
					.println("  6) A�adir columna de foto a la tabla \"empleado\".");
			System.out.println("  7) A�adir un nuevo empleado con foto.");
			System.out.println("  0) Salir de la aplicaci�n.");

			// Read user's option and check that it is a valid option
			menuOption = 'a';
			do {
				String line = sc.nextLine();
				if (line.length() == 1) {
					menuOption = line.charAt(0);
				}
				if (menuOption < '0' || menuOption > '7') {
					System.out.println("Opci�n incorrecta.");
				}
			} while (menuOption < '0' || menuOption > '7');

			ArrayList<Cerveza> cervezas;
			Cerveza c = new Cerveza();
			ArrayList<Bar> bars = getBarData();
			Bar b = new Bar();

			// Call a specific method depending on the option
			switch (menuOption) {
			case '1':
				System.out.println("Creando tabla \"empleado\"...");
				createTableEmpleado();
				System.out.println("Creando tabla \"gusta\"...");
				createTableGusta();
				break;
			case '2':
				System.out
						.println("Cargando datos de la tabla \"empleado\"...");
				loadEmpleados();
				System.out.println("Cargando datos de la tabla \"gusta\"...");
				loadGustos("gustos.csv");
				break;
			case '3':
				bars = getBarData();
				System.out.println(b.barsToString(bars) + '\n');
				break;
			case '4':
				cervezas = getCervezasFabricante();
				System.out.println(c.cervezasToString(cervezas) + '\n');
				break;
			case '5':
				cervezas = getCervezasPopulares();
				System.out.println(c.cervezasToString(cervezas) + '\n');
				break;
			case '6':
				System.out
						.println("A�adiendo columa \"foto\" en la tabla \"empleado\"...");
				addFotoColumn();
				break;
			case '7':
				System.out.println("A�adiendo un nuevo empleado con foto...");
				addEmpleadoFoto();
				break;
			}

			if (menuOption != '0')
				System.out.println("�Qu� m�s desea hacer?");
			else
				System.out.println("�Hasta pronto!");
		} while (menuOption != '0');

		sc.close();
	}

	private ArrayList<Gusto> readData(String fileName) {
		File f = new File(fileName);
		ArrayList<Gusto> result = new ArrayList<Gusto>();

		try {
			Scanner sc_file = new Scanner(f);

			while (sc_file.hasNextLine()) {
				String[] fields = sc_file.nextLine().split(";");
				Gusto row = new Gusto(Integer.valueOf(fields[0]),
						Integer.valueOf(fields[1]), Integer.valueOf(fields[2]));
				result.add(row);
			}

			sc_file.close();
		} catch (Exception e) {
			System.err.println("Error al leer el fichero.");
		}

		return result;
	}

	static private class Bar {
		private int idBar;
		private String nombre;
		private String direccion;

		public Bar() {
			setIdBar(0);
			setNombre("");
			setDireccion("");
		}

		public Bar(int idBar, String nombre, String direccion) {
			setIdBar(idBar);
			setNombre(nombre);
			setDireccion(direccion);
		}

		public int getIdBar() {
			return idBar;
		}

		public void setIdBar(int idBar) {
			this.idBar = idBar;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getDireccion() {
			return direccion;
		}

		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}

		public String barsToString(ArrayList<Bar> bars) {
			String result = "Listado de bares: \n";
			if (bars != null)
				for (Bar bar : bars) {
					result = result + "  " + bar.idBar + " - " + bar.nombre
							+ " - " + bar.direccion + "\n";
				}
			return result;
		}
	}

	static private class Cerveza {
		private int idCerveza;
		private String nombre;
		private String caracteristicas;
		private int idFabricante;

		public Cerveza() {
			setIdCerveza(0);
			setNombre("");
			setCaracteristicas("");
			setIdFabricante(0);
		}

		public Cerveza(int id, String n, String c, int idFabricante) {
			setIdCerveza(0);
			setNombre(n);
			setCaracteristicas(c);
			setIdFabricante(idFabricante);
		}

		public int getIdCerveza() {
			return idCerveza;
		}

		public void setIdCerveza(int idCerveza) {
			this.idCerveza = idCerveza;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getCaracteristicas() {
			return caracteristicas;
		}

		public void setCaracteristicas(String caracteristicas) {
			this.caracteristicas = caracteristicas;
		}

		public int getIdFabricante() {
			return idFabricante;
		}

		public void setIdFabricante(int idFabricante) {
			this.idFabricante = idFabricante;
		}

		public String cervezasToString(ArrayList<Cerveza> cervezas) {
			String result = "Listado de cervezas: \n";
			if (cervezas != null)
				for (Cerveza c : cervezas) {
					result = result + "  " + c.idCerveza + " - " + c.nombre
							+ " - " + c.caracteristicas + " - "
							+ c.idFabricante + "\n";
				}
			return result;
		}
	}

	static private class Gusto {
		private int idSocio;
		private int idCerveza;
		private int idBar;

		public Gusto() {
			setIdSocio(0);
			setIdCerveza(0);
			setIdBar(0);
		}

		public Gusto(int socio, int cerveza, int bar) {
			setIdSocio(socio);
			setIdCerveza(cerveza);
			setIdBar(bar);
		}

		public int getIdSocio() {
			return idSocio;
		}

		public void setIdSocio(int socio) {
			idSocio = socio;
		}

		public int getIdCerveza() {
			return idCerveza;
		}

		public void setIdCerveza(int cerveza) {
			idCerveza = cerveza;
		}

		public int getIdBar() {
			return idBar;
		}

		public void setIdBar(int bar) {
			idBar = bar;
		}
	}

}
