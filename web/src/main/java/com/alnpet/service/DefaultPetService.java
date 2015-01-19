package com.alnpet.service;

import java.util.UUID;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;

import com.alnpet.dal.core.PetDao;
import com.alnpet.dal.core.PetDo;
import com.alnpet.dal.core.PetEntity;
import com.alnpet.model.entity.Pet;

public class DefaultPetService implements PetService {
	@Inject
	protected PetDao m_dao;

	@Override
	public Pet lookupByToken(String token) throws Exception {
		try {
			PetDo p = m_dao.findByToken(token, PetEntity.READSET_FULL);
			Pet pet = new Pet(token);

			pet.setInternalId(p.getId());
			return pet;
		} catch (DalException e) {
			throw e;
		}
	}

	@Override
	public Pet lookupByDevice(String device) throws Exception {
		try {
			PetDo p = m_dao.findByDevice(device, PetEntity.READSET_FULL);
			Pet pet = new Pet(p.getToken());

			pet.setInternalId(p.getId());
			return pet;
		} catch (DalException e) {
			throw e;
		}
	}

	@Override
	public Pet create(String name, String gender, String category, double age, double weight) throws Exception {
		PetDo pet = new PetDo();
		String token = UUID.randomUUID().toString();

		pet.setToken(token);
		pet.setName(name);
		pet.setGender(gender);
		pet.setCategory(category);

		if (age > 0) {
			pet.setAge(age);
		}

		if (weight > 0) {
			pet.setWeight(weight);
		}

		m_dao.insert(pet);
		return new Pet(token).setInternalId(pet.getId());
	}

	@Override
   public void bindUser(int petId, String nickname, String phone, String email) throws Exception {
		PetDo p = m_dao.findByPK(petId, PetEntity.READSET_FULL);

		if (nickname != null) {
			p.setNickname(nickname);
		}

		if (phone != null) {
			p.setPhone(phone);
		}

		if (email != null) {
			p.setEmail(email);
		}

		m_dao.updateByPK(p, PetEntity.UPDATESET_FULL);
	}

	@Override
   public void bindDevice(int petId, String device) throws Exception {
		PetDo p = m_dao.findByPK(petId, PetEntity.READSET_FULL);

		p.setDevice(device);

		m_dao.updateByPK(p, PetEntity.UPDATESET_FULL);
	}
}
