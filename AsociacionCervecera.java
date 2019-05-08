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
		try {
			String drv = "com.mysql.jdbc.Driver";
			String serverAddress = "localhost:3306";
			String db = "ac";
			String user = "bd";
			String pass = "bdupm";
			String url = "jdbc:mysql://" + serverAddress + "/" + db;
			Class.forName(drv);
			System.out.println("Procediendo a conexión");
			Connection conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(true);
			conectado = true;
		} catch (Exception excepcion) {
			System.out.println("Ha habido un problema con la conexción");
			excepcion.printStackTrace();
		}
		return conectado;

	}

	public boolean DBclose() throws Exception {
		boolean conectado = false;
		System.out.println("Saliendo.. ¡hasta otra!");
		if (conn != null) {
			try {
				conn.close();
				conectado = true;
			} catch (SQLException e) {
				System.out.println("Ha habido un problema con la desconexión:");
				e.printStackTrace();
			}
			System.exit(0);
		} else
			System.exit(0);
		return conectado;

	}

	public boolean createTableEmpleado() throws Exception {
		boolean creado = false;
		try {
			st = conn.createStatement();
			String sql = "CREATE TABLE Empleado"
					+ "(id_empleado INTEGER not NULL," + "nombre VARCHAR(255)"
					+ "direccion VARCHAR(255)" + "telefono VARCHAR(255)"
					+ "salario DOUBLE" + "PRIMARY KEY (id_empleado)";
			st.executeUpdate(sql);
			System.out.println("Creando tabla Empleado");
			creado = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return creado;
	}

	public boolean createTableGusta() throws Exception {
		boolean creado = false;
		try {
			st = conn.createStatement();
			String sql = "CREATE TABLE Gusta" + "(id_bar INTEGER not NULL,"
					+ "id_socio INTEGER not NULL"
					+ "id_cerveza INTEGER not NULL"
					+ "PRIMARY KEY (id_bar, id_socio, id_cerveza)"
					+ "FOREIGN KEY(id_socio) references Socio"
					+ "FOREIGN KEY(id_bar) references Bar";
			st.executeUpdate(sql);
			System.out.println("Creando tabla Gusta");
			creado = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return creado;
	}

	public boolean loadEmpleados() throws Exception {
		boolean correcto = false;
		int id[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String nombre[] = { "Carmen Matin", "Ana Ruiz", "Mario Moreno",
				"Laura Romero", "Luis Ruiz", "Benito Gil", "Dolores Molina",
				"Julio Garrido", "Pilar Romero" };
		String direccion[] = { "C/Sol,1", "C/Luna,2", "C/Estrella,3",
				"C/Mercurio,4", "C/Venus,5", "C/Marte,6", "C/Jupiter,7",
				"C/Jupiter,7", "C/Saturno,8" };
		int telefono[] = { 699999999, 699999988, 699999977, 699999966,
				699999955, 699999944, 699999933, 699999922, 699999911 };
		double salario[] = { 1600.00, 1300.00, 1200.00, 1450.00, 1350.00,
				1500.00, 1350.00, 1350.00, 1650.00 };
		int id_bar[] = { 1, 2, 2, 3, 3, 3, 4, 4, 5 };
		String query = "INSERT INTO Empleado(id_empleado,nombre,direccion,telefono,salario,id_bar) VALUES (INTEGER,VARCHAR(255), VARCHAR(255), INTEGER, DOUBLE, INTEGER)";
		PreparedStatement st = conn.prepareStatement(query);
		for (int i = 0; i < id.length; i++) {
			st.setInt(1, id[i]);
			st.setString(2, nombre[i]);
			st.setString(3, direccion[i]);
			st.setInt(3, telefono[i]);
			st.setDouble(5, salario[i]);
			st.setInt(6, id_bar[i]);
			int resultado = st.executeUpdate();
			if (resultado == 1) {
				System.out.println("Se ha insertado correctamente");
				correcto = true;
			}

			else
				System.out.println("Ha habido un problema con la inserccion");

		}
		return correcto;
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
	 * Aquí comienza la parte "privada" del código. Es conviniente echar un
	 * vistazo al código, pero está ESTRÍCTICAMENTE PROHIBIDO modificar
	 * cualquier parte del código bajo estas líneas.
	 */

	private void mainMenu() {
		Scanner sc = new Scanner(System.in);
		char menuOption = 'a';

		// Main menu loop
		do {
			System.out.println("Escoja una opción: ");
			System.out
					.println("  1) Crear las tablas \"empleado\" y \"gusta\".");
			System.out
					.println("  2) Cargar datos de los empleados y los gustos.");
			System.out.println("  3) Listar los bares almacenados.");
			System.out.println("  4) Listar las cervezas de un fabricante.");
			System.out.println("  5) Listar las cervezas más populares.");
			System.out
					.println("  6) Añadir columna de foto a la tabla \"empleado\".");
			System.out.println("  7) Añadir un nuevo empleado con foto.");
			System.out.println("  0) Salir de la aplicación.");

			// Read user's option and check that it is a valid option
			menuOption = 'a';
			do {
				String line = sc.nextLine();
				if (line.length() == 1) {
					menuOption = line.charAt(0);
				}
				if (menuOption < '0' || menuOption > '7') {
					System.out.println("Opción incorrecta.");
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
						.println("Añadiendo columa \"foto\" en la tabla \"empleado\"...");
				addFotoColumn();
				break;
			case '7':
				System.out.println("Añadiendo un nuevo empleado con foto...");
				addEmpleadoFoto();
				break;
			}

			if (menuOption != '0')
				System.out.println("¿Qué más desea hacer?");
			else
				System.out.println("¡Hasta pronto!");
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
