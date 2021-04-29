/**
 * Raul Barbosa 2014-11-07
 */
package meta2.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Properties;

import rmiserver.Voto;

public class HeyBean {
	private Voto server;
	private String username; // username and password supplied by the user
	private String password;
	private String CC;
	private int porto;
	private String serverAddress,clientAddress;
	private String titulo,descricao,tipo;
	private Date dataInicial,dataFinal;


	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			// create Properties class object
			prop = new Properties();
			// load properties file into it
			prop.load(fis);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fis.close();
		}

		return prop;
	}

	public void readDeps() throws IOException {
		String basePath = new File("").getAbsolutePath();
		basePath = basePath.concat("\\config.properties");
		System.out.println(basePath);
		Properties prop = readPropertiesFile(basePath);

		String portoInString = prop.getProperty("portoRMI");
		this.porto = Integer.parseInt(portoInString);
		serverAddress =  prop.getProperty("RMIAddress");
		clientAddress = prop.getProperty("RMIAdminClient");
	}

	public HeyBean() {
		try {
			//readDeps();  mudar isto
			clientAddress = "192.168.1.75";
			serverAddress = "192.168.1.75";
			porto = 7001;
			System.setProperty("java.rmi.server.hostname", clientAddress);
			Registry reg = LocateRegistry.getRegistry(serverAddress,porto);
			server = (Voto) reg.lookup("votacao");
		}
		catch(NotBoundException| RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setCC(String CC) {
		this.CC = CC;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean checkUserExists() throws RemoteException{
		return this.server.login(this.username,this.password,this.CC);
	}

}
