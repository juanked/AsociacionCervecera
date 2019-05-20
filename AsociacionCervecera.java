import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AsociacionCervecera {
	private Connection conn;

	public static void main(String[] args) {
		AsociacionCervecera ac = new AsociacionCervecera();
		ac.mainMenu();
		ac.DBclose();
	}

	/*
	 * Método para conectarse a la base de datos 
	 * return true si se ha realizado correctamente
	 * return false si ha habido algún problema
	 */
	public boolean DBconnect() {
		// Driver utilizado para la conexión con la base de datos
		String drv = "com.mysql.jdbc.Driver";
		// Parámetros para conectarse a la base de datos
		String serverAddress = "localhost:3306";
		String db = "AsociacionCervecera";
		String user = "bd";
		String pass = "bdupm";
		String url = "jdbc:mysql://" + serverAddress + "/" + db;
		try {
			// Se comprueba si ya hay una conexión abierta y de no haberla se crea
			if (conn == null || conn.isClosed()) {
				Class.forName(drv);
				System.out.println("Procediendo a conexión");
				conn = DriverManager.getConnection(url, user, pass);
				// Nos aseguramos de que se haga commit automaticamente
				conn.setAutoCommit(true);
			}
		// En caso de dar algún problema se notifica y devuelve false
		} catch (SQLException e) {
			System.err.println("Ha habido un problema con la conexión");
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		// En caso de que no haya habido ningún problema devuelve true
		return true;
	}

	/*
	 * Método para cerrar la conexión con la base de datos 
	 * return true si se ha desconectado correctamente 
	 * return false si ha habido algún problema
	 */
	public boolean DBclose() {
		// Se comprueba que no hay ningúna conexión abierta previamente
		if (conn != null) {
			try {
				// Se cierra la conexión
				conn.close();
			// En caso de haber algún problema lo notifica y devuelve false
			} catch (SQLException e) {
				System.out.println("Ha habido un problema con la desconexión:");
				e.printStackTrace();
				return false;
			}
			// Devuelve true si no ha habido ningún problema
			System.exit(0);
			return true;
		}
		// En caso de no haber establecido una conexion previamente lo notifica y
		// devuelve false
		System.out.println("No hay ningúna conexión abierta:");
		System.exit(0);
		return false;
	}

	/*
	 * Método que crea la tabla empleado 
	 * return true si se ha creado con éxito
	 * return false si no ha podido
	 */
	public boolean createTableEmpleado() {
		Statement st =null;
		// Sentencia SQL usada para crear la tabla
		String sql = "CREATE TABLE empleado" + "(ID_empleado INTEGER not NULL," + "nombre VARCHAR(50),"
				+ "direccion VARCHAR(100)," + "telefono VARCHAR(15)," + "salario DOUBLE," + "ID_bar INTEGER not NULL,"
				+ "PRIMARY KEY (ID_empleado)," + "FOREIGN KEY(ID_bar) REFERENCES bar(ID_bar));";
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			DBconnect();
			st = conn.createStatement(); 						// Se crea el statement
			st.executeUpdate(sql); 								// Se ejecuta la sentencia Sql
		// En caso de dar algún problema se notifica y devuelve false
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("No se ha podido crear la tabla empleado");
			return false;
		// Se cierra el Statement
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Si no ha habido ningún problema devuelve true
		return true;
	}

	/*
	 * Método que crea la tabla gusta 
	 * return true si se ha creado con éxito 
	 * return false si no ha podido
	 */
	public boolean createTableGusta() {
		Statement st =null;
		// Sentencia SQL usada para crear la tabla
		String sql = "CREATE TABLE gusta" + "(ID_socio INTEGER not NULL," + "ID_cerveza INTEGER not NULL,"
				+ "ID_bar INTEGER not NULL," + "PRIMARY KEY (ID_socio, ID_cerveza, ID_bar),"
				+ "FOREIGN KEY(ID_socio) REFERENCES socio(ID_socio),"
				+ "FOREIGN KEY(ID_cerveza) REFERENCES cerveza(ID_cerveza),"
				+ "FOREIGN KEY(ID_bar) REFERENCES bar(ID_bar));";
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			DBconnect();
			st = conn.createStatement(); 						// Se crea el statement
			st.executeUpdate(sql); 								// Se ejecuta la sentencia Sql
		// En caso de dar algún problema se notifica y devuelve false
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("No se ha podido crear la tabla gusta");
			return false;
		// Se cierra el Statement
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Si no ha habido ningún problema devuelve true
		return true;
	}

	/*
	 * Método para cargar los datos en la tabla empleado 
	 * return true si se ha realizado con éxito 
	 * return false si ha habido algún problema
	 */
	public boolean loadEmpleados() {
		PreparedStatement pst = null;
		// Sentencia SQL usada para insertar los datos
		String query = "INSERT INTO empleado(ID_empleado,nombre,direccion,telefono,salario,ID_bar) VALUES (?,?,?,?,?,?)";
		// Listas con los datos para insertar en la tabla empleados
		int id[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String nombre[] = { "Carmen Matin", "Ana Ruiz", "Mario Moreno", "Laura Romero", "Luis Ruiz", "Benito Gil",
				"Dolores Molina", "Julio Garrido", "Pilar Romero" };
		String direccion[] = { "C/Sol,1", "C/Luna,2", "C/Estrella,3", "C/Mercurio,4", "C/Venus,5", "C/Marte,6",
				"C/Jupiter,7", "C/Jupiter,7", "C/Saturno,8" };
		int telefono[] = { 699999999, 699999988, 699999977, 699999966, 699999955, 699999944, 699999933, 699999922,
				699999911 };
		float salario[] = { 1600, 1300, 1200, 1450, 1350, 1500, 1350, 1350, 1650 };
		int ID_bar[] = { 1, 2, 2, 3, 3, 3, 4, 4, 5 };
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			// Inserta los datos en la tabla
			DBconnect();
			pst = conn.prepareStatement(query);
			for (int i = 0; i < id.length; i++) {
				pst.setInt(1, id[i]);
				pst.setString(2, nombre[i]);
				pst.setString(3, direccion[i]);
				pst.setInt(4, telefono[i]);
				pst.setFloat(5, salario[i]);
				pst.setInt(6, ID_bar[i]);
				pst.executeUpdate();
			}
		// En caso de dar algún problema se notifica y devuelve false
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		// Se cierra el prepareStatement
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Si no ha habido ningún problema devuelve true 
		return true;
	}

	/*
	 * Método para cargar los datos en la tabla gusta 
	 * return true si se ha realizado con éxito 
	 * return false si ha habido algún problema
	 */
	public boolean loadGustos(String fileName) {
		PreparedStatement pst = null;
		// Se cargan los datos de gusta en el arrayList
		ArrayList<Gusto> gusto = readData(fileName);
		// Sentencia SQL usada para insertar los datos
		String query = "INSERT INTO gusta(ID_socio, ID_cerveza, ID_bar) VALUES(?,?,?)";
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			// Inserta los datos en la tabla
			DBconnect();
			pst = conn.prepareStatement(query);
			for (int i = 0; i < gusto.size(); i++) {
				pst.setInt(1, gusto.get(i).idSocio);
				pst.setInt(2, gusto.get(i).idCerveza);
				pst.setInt(3, gusto.get(i).idBar);
				pst.executeUpdate();
			}
		// En caso de dar algún problema se notifica y devuelve false
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Ha habido un problema con la inserccion de gusta");
			return false;
		// Se cierra el prepareStatement
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Si no ha habido ningún problema devuelve true
		return true;
	}

	/*
	 * Método que carga los datos de la tabla "bar" en un ArrayList 
	 * return el ArrayList de objetos bar
	 * En caso saltar una excepción devuelve null
	 */
	public ArrayList<Bar> getBarData() {
		Statement st = null;
		ResultSet rs = null;
		// Creamos el ArrayList
		ArrayList<Bar> respuesta = new ArrayList<Bar>();
		// Sentencia SQL usada para descargarse los datos de la tabla bar
		String query = "SELECT * FROM bar";
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			// Se descarga los datos en el objeto rs (ResultSet)
			DBconnect();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			// Va insertando los datos en el ArrayList
			while (rs.next()) {
				int id = rs.getInt("ID_bar");
				String nombre = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				respuesta.add(new Bar(id, nombre, direccion));
			}
		// En caso de dar algún problema se notifica y devuelve null
		} catch (SQLException e) {
			e.printStackTrace();
			respuesta = null;
		// Se cierra el Statement y el ResultSet
		} finally {
			try {
				if (st != null)
					st.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Devuelve el ArrayList con los datos
		return respuesta;
	}
	
	/*
	 * Método que devuelve un ArrayList con las cervezas del fabricante seleccionado
	 * return el ArrayList de cervezas
	 * En caso saltar una excepción devuelve null
	 */
	public ArrayList<Cerveza> getCervezasFabricante() {
		// Se crea los Statement, ResultSet y el Scanner
		Statement st =null;
		Statement st2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		Scanner sc = null;
		// Sentencia SQL usada para descargarse los id y los nombres de los fabricantes
		String query = "SELECT ID_fabricante, nombre FROM fabricante";		
		ArrayList<Cerveza> cervezas = new ArrayList<Cerveza>();
		Map<String, Integer> fabricante = new HashMap<String, Integer>();
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			// Ejecuta la query y almacena en el map fabricante el id y el nombre de los fabricantes
			DBconnect();
			sc = new Scanner(System.in);
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				int id = rs.getInt("ID_fabricante");
				String nombre = rs.getString("nombre");
				fabricante.put(nombre, id);
			}
			// Se imprime el map con los fabricantes y se escoge uno
			System.out.println("Escoja una opción");
			System.out.println(fabricante.keySet());
			String entrada = sc.nextLine();
			while(!fabricante.containsKey(entrada)){
				System.out.println("Fabricante no encontrado, vuelva a intentarlo");
				System.out.println(fabricante.keySet());
				entrada = sc.nextLine();
			}
			// Se ejecuta la segunda query y se almacena el resultado en el ArrayList cervezas
			String query2 = "SELECT * FROM cerveza WHERE ID_fabricante =" + fabricante.get(entrada) + ";";
			st2 = conn.createStatement();
			rs2 = st2.executeQuery(query2);
			while (rs2.next()) {
				int idCerveza = rs2.getInt("ID_cerveza");
				String nombre2 = rs2.getString("nombre");
				String caracteristicas = rs2.getString("caracteristicas");
				int idFabricante = rs2.getInt("ID_fabricante");
				cervezas.add(new Cerveza(idCerveza, nombre2, caracteristicas, idFabricante));
			}
		// En caso de dar algún problema se notifica y devuelve null
		} catch (SQLException e) {
			e.printStackTrace();
			cervezas = null;
		// Se cierran los Statement,  ResultSet y el Scanner
		} finally {
			try {
				if (st != null)
					st.close();
				if (st2 != null)
					st2.close();
				if (rs != null)
					rs.close();
				if (rs2 != null)
					rs2.close();
				sc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Devuelve el ArrayList con los datos 
		return cervezas;
	}
	
	
	/*
	 * Método que devuelve las cervezas más populares
	 * return el ArrayList de cervezas
	 * En caso saltar una excepción devuelve null
	 */
	public ArrayList<Cerveza> getCervezasPopulares() {
		// Se crea los Statement y ResultSet
		Statement st = null;
		Statement st2 = null;
		Statement st3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ArrayList<Cerveza> populares = new ArrayList<>();
		int porcentaje;
		// Sentencia SQL que cuenta las cervezas totales
		String query = "SELECT COUNT(ID_cerveza) AS suma FROM cerveza;";
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			// 
			DBconnect();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			rs.next();
			porcentaje = (int) (rs.getInt("suma") * 0.1);
			st2 = conn.createStatement();
			// 
			String query2 = "select ID_cerveza, count(distinct ID_socio) as conteo\r\n"
					+ "from AsociacionCervecera.gusta\r\n" + "group by ID_cerveza\r\n"
					+ "having count(distinct ID_bar) > 0 ORDER BY conteo DESC LIMIT " + porcentaje + ";";
			rs2 = st2.executeQuery(query2);
			rs2.next();
			st3 = conn.createStatement();
			// 
			String query3 = "SELECT * FROM cerveza WHERE ID_cerveza = " + rs2.getInt("ID_cerveza") + ";";
			rs3 = st3.executeQuery(query3);
			while (rs3.next()) {
				int idCerveza = rs3.getInt("ID_cerveza");
				String nombre = rs3.getString("nombre");
				String caracteristicas = rs3.getString("caracteristicas");
				int idFabricante = rs3.getInt("ID_fabricante");
				populares.add(new Cerveza(idCerveza, nombre, caracteristicas, idFabricante));
			}
		// En caso de dar algún problema se notifica y devuelve null
		} catch (SQLException e) {
			e.printStackTrace();
			populares = null;
		// Se cierran los Statement y ResultSet 
		} finally {
			try {
				if (st != null)
					st.close();
				if (st2 != null)
					st2.close();
				if (st3 != null)
					st3.close();
				if (rs != null)
					rs.close();
				if (rs2 != null)
					rs2.close();
				if (rs3 != null)
					rs3.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Devuelve el ArrayList con los datos 
		return populares;
	}

	/*
	 * Método que modifica la tabla  empleado para añadir una nueva columna llamada “foto”
	 * return true si se ha realizado con éxito 
	 * return false si ha habido algún problema
	 */
	public boolean addFotoColumn() {
		PreparedStatement pst = null;
		// Sentencia SQL usada para modificar la tabla
		String query = "ALTER TABLE empleado ADD COLUMN foto LONGBLOB";
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			// Ejecuta la query 
			DBconnect();
			pst = conn.prepareStatement(query); 				
			pst.executeUpdate();
		// En caso de dar algún problema se notifica y devuelve false
		} catch (SQLException e) {
			System.err.println("Ha habido un problema con la inserccion de la columna");
			e.printStackTrace();
			return false;
		// Se cierra el PreparedStatement
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}				
		}
		// Si no ha habido ningún problema devuelve true
		return true;
	}

	/*
	 * Método que inserta un nuevo empleado con foto
	 * return true si se ha realizado con éxito 
	 * return false si ha habido algún problema
	 */
	public boolean addEmpleadoFoto() {
		PreparedStatement pst = null;
		// Sentencia SQL usada para insertar los datros
		String query = "INSERT INTO empleado(ID_empleado,nombre,direccion,telefono,salario,ID_bar, foto)"
				+ " VALUES (?,?,?,?,?,?,?)"; // sentencia SQL que se
		try {
			// Intenta conectarse a la base de datos por si no está conectada
			DBconnect();
			pst = conn.prepareStatement(query);
			File file = new File("HomerSimpson.jpg");
			FileInputStream fis = new FileInputStream(file);
			// Inserciones según el enunciado de la práctica
			pst.setInt(1, 10);
			pst.setString(2, "Homer Simpson");
			pst.setString(3, "742 Evergreen Terrace");
			pst.setNull(4, java.sql.Types.INTEGER); // null pero definimos que
													// en ese campo tiene que ir
													// un INTEGER
			pst.setFloat(5, 1500);
			pst.setInt(6, 1);
			pst.setBinaryStream(7, fis, (int) file.length());
			pst.executeUpdate();
		// En caso de dar algún problema con SQL se notifica y devuelve false
		} catch (SQLException e) {
			System.out.println("Ha habido un problema con la inserccion de los datos");
			e.printStackTrace();
			return false;
		// En caso de dar algún problema con el archivo se notifica y devuelve false
		} catch (FileNotFoundException e2) {
			System.out.println("El archivo indicado no existe o no se encuentra en esta dirección");
			e2.printStackTrace();
			return false;
		// Se cierra el PreparedStatement
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		// Si no ha habido ningún problema devuelve true
		return true;
	}

	/*
	 * The private section of the code starts here. It is convenient to take a
	 * glance to the code, but it is STRICTLY FORBIDDEN to modify any part of the
	 * code bellow these lines.
	 * 
	 * Aquí comienza la parte "privada" del código. Es conviniente echar un vistazo
	 * al código, pero está ESTRÍCTICAMENTE PROHIBIDO modificar cualquier parte del
	 * código bajo estas líneas.
	 */

	private void mainMenu() {
		Scanner sc = new Scanner(System.in);
		char menuOption = 'a';

		// Main menu loop
		do {
			System.out.println("Escoja una opción: ");
			System.out.println("  1) Crear las tablas \"empleado\" y \"gusta\".");
			System.out.println("  2) Cargar datos de los empleados y los gustos.");
			System.out.println("  3) Listar los bares almacenados.");
			System.out.println("  4) Listar las cervezas de un fabricante.");
			System.out.println("  5) Listar las cervezas más populares.");
			System.out.println("  6) Añadir columna de foto a la tabla \"empleado\".");
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
				System.out.println("Cargando datos de la tabla \"empleado\"...");
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
				System.out.println("Añadiendo columa \"foto\" en la tabla \"empleado\"...");
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
				Gusto row = new Gusto(Integer.valueOf(fields[0]), Integer.valueOf(fields[1]),
						Integer.valueOf(fields[2]));
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
					result = result + "  " + bar.idBar + " - " + bar.nombre + " - " + bar.direccion + "\n";
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
			setIdCerveza(id); // lo hemos cambiado para que funcione
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
					result = result + "  " + c.idCerveza + " - " + c.nombre + " - " + c.caracteristicas + " - "
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
