package com.alnpet.service;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.ComponentTestCase;

import com.alnpet.dal.pet.SettingDao;
import com.alnpet.dal.pet.SettingDo;

public class SettingServiceTest extends ComponentTestCase {
	@Before
	public void before() throws Exception {
		SettingDao dao = lookup(SettingDao.class);
		SettingDo setting = new SettingDo().setPetId(-1);

		dao.deleteAllByPetId(setting);
	}

	@Test
	public void test() throws Exception {
		SettingService service = lookup(SettingService.class);

		try {
			service.getKey(-1, "a");

			Assert.fail("Key(a) is already existed.");
		} catch (DalNotFoundException e) {
			// expected
		}

		service.setKey(-1, "a", "va");

		String va = service.getKey(-1, "a");

		Assert.assertEquals("va", va);
		
		try {
			service.getKey(-1, "a.b");
			
			Assert.fail("Key(a.b) is already existed.");
		} catch (DalNotFoundException e) {
			// expected
		}
		
		service.setKey(-1, "a.b", "vab");

		String vab = service.getKey(-1, "a.b");

		Assert.assertEquals("vab", vab);
	}
}
