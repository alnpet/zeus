package com.alnpet.api.activity;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.unidal.helper.Dates;
import org.unidal.helper.Dates.DateHelper;
import org.unidal.lookup.ComponentTestCase;

import com.alnpet.dal.core.ActivityInHourDao;
import com.alnpet.dal.core.ActivityInHourDo;
import com.alnpet.dal.core.PetDao;
import com.alnpet.dal.core.PetDo;

public class MockDataProducer extends ComponentTestCase {
	@Test
	public void produce() throws Exception {
		PetDao petDao = lookup(PetDao.class);
		ActivityInHourDao hourDao = lookup(ActivityInHourDao.class);

		PetDo pet = createLocalPet();
		DateHelper helper = Dates.now().beginOf('d');

		petDao.insert(pet);

		for (int i = 0; i < 10; i++) {
			ActivityInHourDo a = createLocalAcivityInHour(pet.getId(), helper.asDate());

			hourDao.insert(a);
			helper.hour(1);
		}
	}

	private ActivityInHourDo createLocalAcivityInHour(int petId, Date hour) {
		ActivityInHourDo a = new ActivityInHourDo();
		Random r = new Random();

		a.setPetId(petId);
		a.setHour(hour);
		a.setFood(r.nextInt(100));
		a.setPlay(r.nextInt(4) * 20);
		a.setActive(r.nextInt(4) * 20);
		a.setRest(r.nextInt(4) * 20);

		return a;
	}

	private PetDo createLocalPet() {
		PetDo pet = new PetDo();

		pet.setToken(UUID.randomUUID().toString());
		pet.setName("mock name");
		pet.setGender("mock gender");
		pet.setCategory("mock category");
		pet.setDevice("mock device: " + System.currentTimeMillis());

		return pet;
	}
}
