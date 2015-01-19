package com.alnpet.service;

import com.alnpet.model.entity.Pet;

public interface PetService {
	public Pet lookupByDevice(String device) throws Exception;

	public Pet lookupByToken(String token) throws Exception;

	public Pet create(String name, String gender, String category, double age, double weight) throws Exception;

	public void bindDevice(int petId, String device) throws Exception;

	public void bindUser(int petId, String nickname, String phone, String email) throws Exception;
}
