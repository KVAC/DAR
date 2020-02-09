package jds_project.ddns.objects;

import java.io.Serializable;

public class NetWorkCMD implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum cmd {
		end
	}

	private cmd command;

	public cmd getCommand() {
		return command;
	}

	public void setCommand(cmd command) {
		this.command = command;
	}
}
