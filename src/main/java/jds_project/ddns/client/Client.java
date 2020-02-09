package jds_project.ddns.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import jds_project.ddns.objects.NetWorkCMD;
import jds_project.ddns.objects.NetWorkCMD.cmd;
import jds_project.ddns.objects.ServerData;

public class Client {
	private String host;
	private int port;
	Socket socket = new Socket();

	int timeout = 10000;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	private ServerData Data;

	public Client() {
	}

	public Client(String Host, int Port) {
		setHost(Host);
		setPort(Port);
	}

	public void openOutputStream() throws IOException {
		oos = new ObjectOutputStream(socket.getOutputStream());
	}

	public void closeOutputStream() throws IOException {
		oos.close();
	}

	public void sendData() throws IOException {
		oos.writeObject(this.Data);
		oos.flush();

		NetWorkCMD endCmd = new NetWorkCMD();
		endCmd.setCommand(cmd.end);
		oos.writeObject(endCmd);
	}

	public void openInputStream() throws IOException {
		ois = new ObjectInputStream(socket.getInputStream());

	}

	public void receiveData() throws IOException, ClassNotFoundException {
		Object obj;
		do {
			try {
				// чтение
				obj = ois.readObject();
				if (obj instanceof String) {
					System.out.println((String) obj);
				} else if (obj instanceof NetWorkCMD) {
					NetWorkCMD nCmd = (NetWorkCMD) obj;
					if (nCmd.getCommand().equals(cmd.end)) {
						break;
					}
				}

			} catch (Exception e) {
				break;
			}
			// Если нет обработки -выходим
		} while (socket.isConnected());
	}

	public void connect() throws IOException, UnknownHostException, SocketException {
		socket = new Socket();
		InetSocketAddress isa = new InetSocketAddress(host, port);
		socket.connect(isa, timeout);
		socket.setSoTimeout(timeout);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ServerData getData() {
		return Data;
	}

	public void setData(ServerData data) {
		Data = data;
	}

}
