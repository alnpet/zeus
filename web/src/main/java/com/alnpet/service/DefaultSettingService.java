package com.alnpet.service;

import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.annotation.Inject;

import com.alnpet.dal.pet.SettingDao;
import com.alnpet.dal.pet.SettingDo;
import com.alnpet.dal.pet.SettingEntity;

public class DefaultSettingService implements SettingService {
	@Inject
	private SettingDao m_dao;

	@Override
	public String getKey(int petId, String key) throws Exception {
		SettingDo setting = m_dao.findByPetAndKey(petId, key, SettingEntity.READSET_FULL);

		return setting.getValue();
	}

	@Override
	public void setKey(int petId, String key, String value) throws Exception {
		try {
			SettingDo setting = m_dao.findByPetAndKey(petId, key, SettingEntity.READSET_FULL);

			setting.setValue(value);

			m_dao.updateByPK(setting, SettingEntity.UPDATESET_FULL);
		} catch (DalNotFoundException e) {
			SettingDo setting = new SettingDo();

			setting.setPetId(petId);
			setting.setKey(key);
			setting.setValue(value);

			m_dao.insert(setting);
		}
	}
}
