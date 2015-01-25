package com.alnpet.service;

public interface SettingService {
	public String getKey(int petId, String key) throws Exception;

	public void setKey(int petId, String key, String value) throws Exception;
}
