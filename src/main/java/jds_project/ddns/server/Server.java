package jds_project.ddns.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import jds_project.ddns.header.HEADER;
import jds_project.ddns.objects.ServerData;

public class Server extends Thread implements Runnable {
	ServerSocket serverSocket;

	volatile ArrayList<ClientH> clients = new ArrayList<ClientH>();

	private void checkConfig() throws IOException {
		if (!HEADER.server_clients.exists()) {
			HEADER.server_clients.createNewFile();
		}

	}

	@Override
	public void run() {
		try {
			checkConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serverSocket = new ServerSocket(65500);
			loadClients();
			while (true) {
				checkConfig();
				Socket clientSocket = serverSocket.accept();
				clientSocket.setSoTimeout(10000);
				ClientH clientH = new ClientH(clientSocket, this);
				clients.add(clientH);
				new Thread(clientH).start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void loadClients() {
		try {
			checkFiles();
			FileInputStream fis = new FileInputStream(HEADER.server_clients);
			boolean cont = true;
			ObjectInputStream input = new ObjectInputStream(fis);
			while (cont) {
				try {
					Object obj = input.readObject();
					if (obj != null) {
						if (obj instanceof ArrayList<?>) {
							ArrayList<?> list = (ArrayList<?>) obj;
							for (Object object : list) {
								if (object instanceof ServerData) {
									ServerData data = (ServerData) object;
									if (!checkContains(HEADER.datas, data)) {
										HEADER.datas.add(data);
										System.out.println("Загружен " + data.getID());
									}
								}
							}
						}
					} else {
						cont = false;
					}
				} catch (Exception e) {
					cont = false;
				}
			}
			input.close();
		} catch (Exception e) {
			if (e.getClass().equals(java.io.EOFException.class)) {
				System.err.println("НЕТ ДАННЫХ");
			}
		}
		System.out.println("ЗАГРУЖЕНО " + HEADER.datas.size());
	}

	public static boolean checkContains(ArrayList<ServerData> datas, ServerData data) {
		for (ServerData serverData : datas) {
			if (serverData.getID().equals(data.getID())) {
				return true;
			}
		}
		return false;
	}

	public synchronized void save() {
		try {
			HEADER.server_clients.delete();
			checkFiles();

			FileOutputStream fos = new FileOutputStream(HEADER.server_clients);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(HEADER.datas);

			oos.close();
			System.err.println("СОХРАНЁНО{" + HEADER.datas.size() + "}:" + HEADER.datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkFiles() throws IOException {
		if (!HEADER.server_clients.exists()) {
			HEADER.server_clients.createNewFile();
		}
	}

}
