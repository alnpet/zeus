package com.alnpet.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;

import com.alnpet.dal.core.ActivityInDayDao;
import com.alnpet.dal.core.ActivityInDayDo;
import com.alnpet.dal.core.ActivityInDayEntity;
import com.alnpet.dal.core.ActivityInHourDao;
import com.alnpet.dal.core.ActivityInHourDo;
import com.alnpet.dal.core.ActivityInHourEntity;
import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;
import com.alnpet.model.entity.Item;
import com.alnpet.model.entity.Pet;
import com.alnpet.model.transform.BaseVisitor;

public class DefaultActivityService implements ActivityService {
	@Inject
	private ActivityInHourDao m_hourDao;

	@Inject
	private ActivityInDayDao m_dayDao;

	private boolean m_fake;

	@Override
	public void create(int petId, Date hour, int food, int play, int active, int rest) throws Exception {
		ActivityInHourDo a = new ActivityInHourDo();

		a.setPetId(petId);
		a.setHour(hour);

		if (food > 0) {
			a.setFood(food);
		}

		if (play > 0) {
			a.setPlay(play);
		}

		if (active > 0) {
			a.setActive(active);
		}

		if (rest > 0) {
			a.setRest(rest);
		}

		m_hourDao.insert(a);
	}

	private void fakeActivities(Pet pet, String type, Date startDate, Date endDate, Activities activities) {
		Random r = new Random();
		Calendar cal = Calendar.getInstance();

		if ("week".equals(type)) {
			int weekday = cal.get(Calendar.DAY_OF_WEEK);

			for (int i = 0; i < weekday; i++) {
				Activity a = new Activity().setDay(i);

				a.setFood(3 * (32 + r.nextInt(64)));
				a.setPlay(3 * (r.nextInt(256)));
				a.setActive(15 * r.nextInt(32));
				a.setRest(24 * r.nextInt(16));

				a.setSport0(1000);

				activities.addActivity(a);
			}
		} else if ("month".equals(type)) {
			int day = cal.get(Calendar.DATE);

			for (int i = 1; i <= day; i++) {
				Activity a = new Activity().setDay(i);

				cal.set(Calendar.DAY_OF_MONTH, i);

				int week = cal.get(Calendar.WEEK_OF_MONTH);

				a.setWeek(week);
				a.setFood(3 * (32 + r.nextInt(64)));
				a.setPlay(3 * (r.nextInt(256)));
				a.setActive(15 * r.nextInt(32));
				a.setRest(24 * r.nextInt(16));

				a.setSport0(1000);

				activities.addActivity(a);
			}
		}
	}

	private void fakeActivity(Pet pet, Date startDate, Date endDate, Activity activity) {
		Random r = new Random();

		for (int i = 0; i < 23; i++) {
			Item item = new Item();

			item.setHour(i);

			if (i == 8 || i == 14 || i == 20) {
				item.setFood(32 + r.nextInt(64));
			}

			if (i == 7 || i == 17 || i == 18) {
				item.setPlay(r.nextInt(256));
			}

			if (i >= 7 && i <= 21) {
				item.setActive(r.nextInt(32));
			}

			item.setRest(r.nextInt(16));

			activity.addItem(item);
		}

		activity.setFeeling(90);
		activity.setHealth(95);
		activity.setFood0(300);
		activity.setSport0(1000);
	}

	@Override
	public Activities findActivities(Pet pet, String type, Date startDate, Date endDate) throws Exception {
		Activities activities = new Activities().setType(type).setDate(startDate);

		if (m_fake) {
			fakeActivities(pet, type, startDate, endDate, activities);
		} else {
			loadActivities(pet, startDate, endDate, activities);
		}

		activities.accept(new ModelVisitor());

		return activities;
	}

	@Override
	public Activity findActivity(Pet pet, Date startDate, Date endDate) throws Exception {
		Activity activity = new Activity().setDate(startDate);

		if (m_fake) {
			fakeActivity(pet, startDate, endDate, activity);
		} else {
			loadActivity(pet, startDate, endDate, activity);
		}

		activity.accept(new ModelVisitor());

		return activity;
	}

	private void loadActivities(Pet pet, Date startDate, Date endDate, Activities activities) throws DalException {
		List<ActivityInDayDo> list = m_dayDao.findAllByPetAndDateRange(pet.getInternalId(), startDate, endDate,
		      ActivityInDayEntity.READSET_FULL);

		for (ActivityInDayDo item : list) {
			Activity a = new Activity();

			a.setDate(item.getDay());
			a.setFood(item.getFood());
			a.setPlay(item.getPlay());
			a.setActive(item.getActive());
			a.setRest(item.getRest());

			activities.addActivity(a);
		}
	}

	private void loadActivity(Pet pet, Date startDate, Date endDate, Activity activity) throws DalException {
		List<ActivityInHourDo> list = m_hourDao.findAllByPetAndDateRange(pet.getInternalId(), startDate, endDate,
		      ActivityInHourEntity.READSET_FULL);
		Calendar cal = Calendar.getInstance();
		int food = 0;
		int sport = 0;

		for (ActivityInHourDo a : list) {
			Item item = new Item();

			cal.setTime(a.getHour());

			item.setHour(cal.get(Calendar.HOUR_OF_DAY));
			item.setFood(a.getFood());
			item.setPlay(a.getPlay());
			item.setActive(a.getActive());
			item.setRest(a.getRest());

			food += item.getFood();
			sport += item.getPlay();
			activity.addItem(item);
		}

		activity.setFood(food);
		activity.setSport(sport);
	}

	@Override
	public void setFake(boolean fake) {
		m_fake = fake;
	}

	private static class ModelVisitor extends BaseVisitor {
		@Override
		public void visitActivities(Activities activities) {
			super.visitActivities(activities);
		}

		@Override
		public void visitActivity(Activity activity) {
			super.visitActivity(activity);

			if (!activity.getItems().isEmpty()) {
				int food = 0;
				int play = 0;
				int active = 0;
				int rest = 0;

				for (Item item : activity.getItems()) {
					food += item.getFood();
					play += item.getPlay();
					active += item.getActive();
					rest += item.getRest();
				}

				// sum
				activity.setFood(food);
				activity.setPlay(play);
				activity.setActive(active);
				activity.setRest(rest);
			}

			// formula
			activity.setSport(activity.getPlay() * 2 + activity.getActive());

			// formula
			int delta = activity.getSport0() - activity.getSport();
			if (delta > 0) {
				activity.setToSpoon(delta / 7);
				activity.setToWalk(delta / 17);
				activity.setToWalk(delta / 27);
			}
		}
	}
}
