package jds_project.ddns.header;

import java.io.File;
import java.util.ArrayList;

import jds_project.ddns.objects.ServerData;

public class HEADER {
	// SERVER SIDE
	public static volatile File server_clients = new File("clients");
	public static volatile ArrayList<ServerData> datas = new ArrayList<ServerData>();

	// CLIENT SIDE
	public static volatile File servers = new File("server");

	public enum what {
		register, getIP, update
	}

	public static String HELP_CLIENT = "java -jar path/to/this.jar "
			+ "HOST PORT on_from{register, getIP, update}  ID <PASS> EXTERNAL_IP";

	public static String HELP_CLIENT_EXAMPLE_1 = "java -jar path/to/this.jar "
			+ "HOST PORT getIP  86050b6b-3231-46d8-8ef0-db451ddcb6dc";

	public static String HELP_CLIENT_EXAMPLE_2 = "java -jar path/to/this.jar "
			+ "HOST PORT register  86050b6b-3231-46d8-8ef0-db451ddcb6dc ekw0SbJlyqbAGA 90.20.30.40";

	public static String HELP_CLIENT_EXAMPLE_3 = "java -jar path/to/this.jar "
			+ "HOST PORT update  86050b6b-3231-46d8-8ef0-db451ddcb6dc ekw0SbJlyqbAGA 91.21.31.41";

	public static String HELP_SERVER;
	public static boolean debug=false;

	{

	}
}
