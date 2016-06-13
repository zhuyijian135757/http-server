package net.flyingfat.common.lang;

import java.util.UUID;

public interface Identifiable
{
    void setIdentification(UUID paramUUID);
  
    UUID getIdentification();
  
	int getDes();

	void setDes(int des); 
}