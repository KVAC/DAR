package jds_project.ddns.objects;

import java.io.Serializable;

import jds_project.ddns.header.HEADER.what;

public class ServerData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID;

	private String external_IP;
	
	private what command;

	public String getExternal_IP() {
		return external_IP;
	}

	public void setExternal_IP(String external_IP) {
		this.external_IP = external_IP;
	}

	private String password;

	public void prepareToGetIP() {
		this.setPassword(null);
		if (this.external_IP == null) {
			throw new NullPointerException("external_IP in NULL");
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public what getCommand() {
		return command;
	}

	public void setCommand(what command) {
		this.command = command;
	}

}
