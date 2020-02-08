package jds_project.ddns.init;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import jds_project.ddns.header.HEADER;
import jds_project.ddns.objects.ServerData;
import jds_project.ddns.server.Server;

public class InitServer {

	public static ServerData genData() {
		ServerData data = new ServerData();
		data.setID(UUID.randomUUID().toString());
		data.setPassword(UUID.randomUUID().toString());
		data.setExternal_IP(UUID.randomUUID().toString());
		return data;
	}

	public static void createData() {
		ArrayList<ServerData> data = new ArrayList<ServerData>();
		for (int i = 0; i < 10; i++) {
			ServerData d = genData();
			System.out.println(d);
			data.add(d);
		}
		try {
			if (!HEADER.server_clients.exists()) {
				HEADER.server_clients.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(HEADER.server_clients);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			System.err.println("СОХРАНЁН:" + HEADER.datas);
			oos.writeObject(data);

			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();

	}

}
