import java.util.ArrayList;
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
	Statement st;
	Connection conn;

	public static void main(String[] args) {
		AsociacionCervecera ac = new AsociacionCervecera();
		ac.DBconnect();
		ac.mainMenu();
		ac.DBclose();
	}

	public boolean DBconnect() {
		boolean conectado = false;
		try {
			String drv = "com.mysql.jdbc.Driver";
			String serverAddress = "localhost:3306";
			String db = "AsociacionCervecera";
			String user = "bd";
			String pass = "bdupm";
			String url = "jdbc:mysql://" + serverAddress + "/" + db;
			Class.forName(drv);
			System.out.println("Procediendo a conexión");
			conn = DriverManager.getConnection(url, user, pass);
			conn.setAutoCommit(true);
			conectado = true;
		} catch (Exception excepcion) {
			System.out.println("Ha habido un problema con la conexión");
			excepcion.printStackTrace();
		}
		return conectado;

	}

	public boolean DBclose() {
		boolean desconectado = false;
		if (conn != null) {
			try {
				conn.close();
				desconectado = true;
			} catch (SQLException e) {
				System.out.println("Ha habido un problema con la desconexión:");
				e.printStackTrace();
			}
			System.exit(0);
		} else
			System.exit(0);
		return desconectado;

	}

	public boolean createTableEmpleado() {
		boolean creado = false;
		try {
			st = conn.createStatement();
			String query = "SHOW TABLES LIKE 'empleado';";
			ResultSet rs = st.executeQuery(query);
			System.out.println(rs.toString());
			
//			if (rs.toString().contentEquals("com.mysql.jdbc.JDBC42ResultSet@701fc37a")) {
				st = conn.createStatement();
				String sql = "CREATE TABLE empleado" + "(id_empleado INTEGER not NULL," + "nombre VARCHAR(50),"
						+ "direccion VARCHAR(100)," + "telefono VARCHAR(15)," + "salario DOUBLE,"
						+ "id_bar INTEGER not NULL," + "PRIMARY KEY (id_empleado),"
						+ "FOREIGN KEY(id_bar) REFERENCES bar(ID_bar));";
				st.executeUpdate(sql);
				System.out.println("Creando tabla Empleado PATATA");
				creado = true;
//			} else {
//				System.out.println("La tabla ya existe");
//				creado = false;
//			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido crear la tabla empleado");
		}
		System.out.println(creado);
		return creado;
	}

	public boolean createTableGusta() {
		boolean creado = false;
		try {
			st = conn.createStatement();
			String query = "SHOW TABLES LIKE 'gusta';";
			ResultSet rs = st.executeQuery(query);
			System.out.println(rs.toString());
			if (rs.toString().contentEquals("com.mysql.jdbc.JDBC42ResultSet@7fad8c79")) {

				st = conn.createStatement();
				String sql = "CREATE TABLE gusta" + "(id_socio INTEGER not NULL," + "id_cerveza INTEGER not NULL,"
						+ "id_bar INTEGER not NULL," + "PRIMARY KEY (id_socio, id_cerveza, id_bar),"
						+ "FOREIGN KEY(id_socio) REFERENCES socio(ID_socio),"
						+ "FOREIGN KEY(id_cerveza) REFERENCES cerveza(ID_cerveza),"
						+ "FOREIGN KEY(id_bar) REFERENCES bar(ID_bar));";
				st.executeUpdate(sql);
				System.out.println("Creando tabla Gusta");
				creado = true;
			} else {
				System.out.println("La tabla ya existe");
				creado = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido crear la tabla gusta");
		}
		System.out.println(creado);
		return creado;
	}

	public boolean loadEmpleados() {
		boolean correcto = false;
		int id[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String nombre[] = { "Carmen Matin", "Ana Ruiz", "Mario Moreno", "Laura Romero", "Luis Ruiz", "Benito Gil",
				"Dolores Molina", "Julio Garrido", "Pilar Romero" };
		String direccion[] = { "C/Sol,1", "C/Luna,2", "C/Estrella,3", "C/Mercurio,4", "C/Venus,5", "C/Marte,6",
				"C/Jupiter,7", "C/Jupiter,7", "C/Saturno,8" };
		int telefono[] = { 699999999, 699999988, 699999977, 699999966, 699999955, 699999944, 699999933, 699999922,
				699999911 };
		float salario[] = { 1600, 1300, 1200, 1450, 1350, 1500, 1350, 1350, 1650 };
		int id_bar[] = { 1, 2, 2, 3, 3, 3, 4, 4, 5 };
		String query = "INSERT INTO empleado(id_empleado,nombre,direccion,telefono,salario,id_bar) VALUES (?,?,?,?,?,?)";
		try {
			PreparedStatement st = conn.prepareStatement(query);
			for (int i = 0; i < id.length; i++) {
				st.setInt(1, id[i]);
				st.setString(2, nombre[i]);
				st.setString(3, direccion[i]);
				st.setInt(4, telefono[i]);
				st.setFloat(5, salario[i]);
				st.setInt(6, id_bar[i]);
				int resultado = st.executeUpdate();
				if (resultado == 1) {
					System.out.println("Se ha insertado correctamente"+i);
					correcto = true;
				}
				else
					System.out.println("Ha habido un problema con la inserccion de empleado");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(correcto);
		return correcto;
	}

	public boolean loadGustos(String fileName) {
		boolean carga = false;
		try {
			ArrayList<Gusto> gusto = readData(fileName);
			String query = "INSERT INTO gusta(id_socio, id_cerveza, id_bar) VALUES(?,?,?)";
			PreparedStatement st = conn.prepareStatement(query);
			for (int i = 0; i < gusto.size(); i++) {
				st.setInt(1, gusto.get(i).idSocio);
				st.setInt(2, gusto.get(i).idCerveza);
				st.setInt(3, gusto.get(i).idBar);
				st.executeUpdate();
			}
			carga = true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Ha habido un problema con la inserccion de gusta");
		}
		System.out.println(carga);
		return carga;
	}

	public ArrayList<Bar> getBarData() {
		ArrayList<Bar> respuesta = new ArrayList<Bar>();
		try {
			String query = "SELECT * FROM bar";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				int id = rs.getInt("ID_bar");
				String nombre = rs.getString("nombre");
				String direccion = rs.getString("direccion");
				respuesta.add(new Bar(id, nombre, direccion));
			}
//			int resultado = st.executeUpdate();
//			if (resultado == 1) {
//				System.out.println("Se ha insertado correctamente");
//			} else {
//				System.out.println("Ha habido un problema");
//				respuesta = null;
//			}

		} catch (SQLException e) {
			e.printStackTrace();
			respuesta = null;
		}
		return respuesta;
	}

	public ArrayList<Cerveza> getCervezasFabricante() {
		return null;
	}

	public ArrayList<Cerveza> getCervezasPopulares() {
		return null;
	}

	public boolean addFotoColumn() {
		boolean correcto = false;
		try {
			String query = "ALTER TABLE empleado ADD COLUMN foto LONGBLOB";
			PreparedStatement pst = conn.prepareStatement(query);
			int resultado = pst.executeUpdate();
			if (resultado == 1) {
				System.out.println("Se ha insertado correctamente");
				correcto = true;
			} else
				System.out.println("Ha habido un problema con la inserccion de la columna1");
		} catch (SQLException e) {
			System.out.println("Ha habido un problema con la inserccion de la columna");
			e.printStackTrace();
		}
		return correcto;
	}

	public boolean addEmpleadoFoto() {
		boolean correcto = false;
		try {
			String query = "INSERT INTO empleado(id_empleado,nombre,direccion,telefono,salario,id_bar, foto)"
					+ " VALUES (?,?,?,?,?,?,?)"; // sentencia SQL que se ejecutará
			PreparedStatement pst = conn.prepareStatement(query);
			File file = new File("HomerSimpson.jpg");
			FileInputStream fis = new FileInputStream(file);
			// Inserciones según el enunciado de la práctica
			pst.setInt(1, 10);
			pst.setString(2, "Homer Simpson");
			pst.setString(3, "742 Evergreen Terrace");
			pst.setNull(4, java.sql.Types.INTEGER); // null pero definimos que en ese campo tiene que ir un INTEGER
			pst.setFloat(5, 1500);
			pst.setInt(6, 1);
			pst.setBinaryStream(7, fis, (int) file.length());
			int resultado = pst.executeUpdate();
			if (resultado == 1) {
				System.out.println("Se ha insertado correctamente");
				correcto = true;
			} else
				System.out.println("Ha habido un problema con la inserccion de los datos");
		} catch (SQLException e) {
			System.out.println("Ha habido un problema con la inserccion de los datos");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("El archivo indicado no existe o no se encuentra en esta dirección");
			e.printStackTrace();
		}
		return correcto;
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
