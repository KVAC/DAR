package jds_project.ddns.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import jds_project.ddns.header.HEADER;
import jds_project.ddns.header.HEADER.what;
import jds_project.ddns.objects.NetWorkCMD;
import jds_project.ddns.objects.ServerData;

public class ClientH implements Runnable {

	private Socket socket;
	private Server server;

	ObjectInputStream ois;

	ObjectOutputStream oos;

	public ClientH(Socket clientSocket, Server server) {
		this.setSocket(clientSocket);
		this.setServer(server);

	}

	@Override
	public void run() {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			// reader
			ois = new ObjectInputStream(socket.getInputStream());
			Object object;
			do {
				try {
					// чтение
					object = ois.readObject();

					if (object instanceof ServerData) {
						ServerData data = (ServerData) object;
						System.out.println(data.getCommand() + ":" + data.getID() + ":" + data.getPassword() + ":"
								+ data.getExternal_IP());

						if (!Server.checkContains(HEADER.datas, data)) {
							// НЕТ
							if (data.getCommand().equals(what.register)) {
								data.setCommand(null);
								if (data.getPassword() == null) {
									stringBuilder.append(" нет 5 аргумента (место пароля) ");
									break;
								} else if (data.getExternal_IP() == null) {
									stringBuilder.append(" нет 6 аргумента (место внешнего адреса) ");
									break;
								}
								HEADER.datas.add(data);
								stringBuilder
										.append("зарегистрирован ID:" + data.getID() + " пароль:" + data.getPassword());
							}
						}
						// ЕСТЬ
						else {
							if (data.getCommand().equals(what.update)) {
								for (ServerData element : HEADER.datas) {
									if (element.getID().equals(data.getID())) {
										if (element.getPassword().equals(data.getPassword())) {
											if (data.getExternal_IP() == null) {
												stringBuilder.append("НЕТ ВНЕШНЕГО IP");
											} else {
												element.setExternal_IP(data.getExternal_IP());
											}
										} else {
											stringBuilder.append("не верный пороль");
										}
									}
								}
							} else if (data.getCommand().equals(what.getIP)) {
								for (ServerData element : HEADER.datas) {
									if (element.getID().equals(data.getID())) {
										System.out.println(element.getExternal_IP());
										stringBuilder.append(element.getExternal_IP());
									}
								}
							}
						}
					} else if (object instanceof NetWorkCMD) {
						NetWorkCMD cmd = (NetWorkCMD) object;
						if (cmd.getCommand().equals(jds_project.ddns.objects.NetWorkCMD.cmd.end)) {
							System.out.println("aaa");
							break;
						}
					}
				} catch (Exception e) {
					break;
				}
				// Если нет обработки -выходим
			} while (socket.isConnected());
			// reader
			System.err.println(stringBuilder);
			try {
				// отправка
				oos = new ObjectOutputStream(socket.getOutputStream());
				sendObject(stringBuilder.toString());
				// sendObject();
			} catch (Exception e) {
				System.err.println(socket.getInetAddress().getCanonicalHostName());
				e.printStackTrace();
			}
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		getServer().save();

	}

	public void sendObject(Object object) throws IOException {
		oos.writeObject(object);
		oos.flush();
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
