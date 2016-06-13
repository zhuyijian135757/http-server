package net.flyingfat.common.serialization.protocol.xip;

import java.util.UUID;

import net.flyingfat.common.lang.DefaultPropertiesSupport;

public class AbstractXipSignal extends DefaultPropertiesSupport implements
		XipSignal {
	private UUID uuid = UUID.randomUUID();
	private int  des;

	public void setIdentification(UUID id) {
		this.uuid = id;
	}

	public UUID getIdentification() {
		return this.uuid;
	}

	public int getDes() {
		return des;
	}

	public void setDes(int des) {
		this.des = des;
	}

	public int hashCode() {
		int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.uuid == null ? 0 : this.uuid.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractXipSignal other = (AbstractXipSignal) obj;
		if (this.uuid == null) {
			if (other.uuid != null) {
				return false;
			}
		} else if (!this.uuid.equals(other.uuid)) {
			return false;
		}
		return true;
	}
}
