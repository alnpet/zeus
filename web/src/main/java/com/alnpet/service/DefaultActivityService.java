package com.alnpet.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.unidal.lookup.annotation.Inject;

import com.alnpet.dal.core.ActivityInDayDao;
import com.alnpet.dal.core.ActivityInDayDo;
import com.alnpet.dal.core.ActivityInDayEntity;
import com.alnpet.dal.core.ActivityInHourDao;
import com.alnpet.dal.core.ActivityInHourDo;
import com.alnpet.dal.core.ActivityInHourEntity;
import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;

public class DefaultActivityService implements ActivityService {
	@Inject
	private ActivityInHourDao m_hourDao;

	@Inject
	private ActivityInDayDao m_dayDao;

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

	@Override
	public Activities findActivitiesInDay(int petId, Date startDate, Date endDate) throws Exception {
		List<ActivityInDayDo> list = m_dayDao.findAllByPetAndDateRange(petId, startDate, endDate,
		      ActivityInDayEntity.READSET_FULL);
		Activities activities = new Activities().setStartDate(startDate).setEndDate(endDate);

		for (ActivityInDayDo item : list) {
			Activity a = new Activity();

			a.setDay(item.getDay());
			a.setFood(item.getFood());
			a.setPlay(item.getPlay());
			a.setActive(item.getActive());
			a.setRest(item.getRest());
			activities.addActivity(a);
		}

		return activities;
	}

	@Override
	public Activities findActivitiesInHour(int petId, Date startDate, Date endDate) throws Exception {
		List<ActivityInHourDo> list = m_hourDao.findAllByPetAndDateRange(petId, startDate, endDate,
		      ActivityInHourEntity.READSET_FULL);
		Activities activities = new Activities().setStartDate(startDate).setEndDate(endDate);
		Calendar cal = Calendar.getInstance();

		for (ActivityInHourDo item : list) {
			Activity a = new Activity();

			cal.setTime(item.getHour());

			a.setHour(cal.get(Calendar.HOUR_OF_DAY));
			a.setFood(item.getFood());
			a.setPlay(item.getPlay());
			a.setActive(item.getActive());
			a.setRest(item.getRest());
			activities.addActivity(a);
		}

		return activities;
	}
}
