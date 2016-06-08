package net.flyingfat.common.biz.domain;

import java.io.Serializable;

public class Host  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String hostType;
	
	private String hostAddr;

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	public String getHostAddr() {
		return hostAddr;
	}

	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}

}
