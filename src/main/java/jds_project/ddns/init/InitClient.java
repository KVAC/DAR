package jds_project.ddns.init;

import java.io.IOException;
import java.util.Arrays;

import jds_project.ddns.client.Client;
import jds_project.ddns.header.HEADER;
import jds_project.ddns.header.HEADER.what;
import jds_project.ddns.objects.ServerData;

public class InitClient {

	public static void main(String[] args) {
		new HEADER();
		if (!HEADER.servers.exists()) {
			try {
				HEADER.servers.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(2);
		}
		try {

			Client client = new Client();
			if (args.length < 3) {
				System.out.println();
			}
			ServerData serverData = new ServerData();

			client.setData(serverData);
			int pp = 65500;
			for (int i = 0; i < args.length; i++) {
				// HOST
				if (i == 0) {
					client.setHost(args[0]);
				}
				// PORT
				if (i == 1) {
					pp = Integer.parseInt(args[1]);
					if (pp >= 0 && pp <= 65535) {
						client.setPort(pp);
					} else {
						System.err.println("Указанный порт не входит в диапазон [0:65535]");
						System.exit(2);
					}
				}
				// CMD
				if (i == 2) {
					try {

						client.getData().setCommand(what.valueOf(args[2]));
					} catch (Exception e) {
						System.err.println("Доступные комманды " + Arrays.toString(what.values()));
						System.exit(2);
					}
				}
				// ID
				if (i == 3) {
					serverData.setID(args[3]);
				}
				// PASSWD
				if (i == 4) {
					serverData.setPassword(args[4]);
				}
				// EXTERNAL IP
				if (i == 5) {
					serverData.setExternal_IP(args[5]);
				}
				if (HEADER.debug == true) {
					System.out.println(i + ":" + args[i]);

				}
			}
			if (HEADER.debug == true) {
				System.out.println(
						//
						"Входящие данные " + client.getHost() + ":" + client.getPort() + ":"
								+ client.getData().getCommand() + ":" + client.getData().getID() + ":"
								+ client.getData().getPassword() + ":" + client.getData().getExternal_IP());
			}
			//
			client.connect();

			client.openOutputStream();

			client.sendData();

			// client.closeOutputStream();
			client.openInputStream();
			client.receiveData();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
