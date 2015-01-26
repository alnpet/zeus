package com.alnpet.service;

import java.util.Date;

import org.junit.Test;
import org.unidal.helper.Dates;
import org.unidal.lookup.ComponentTestCase;

import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;
import com.alnpet.model.entity.Pet;

public class ActivityServiceTest extends ComponentTestCase {
	@Test
	public void activityInDay() throws Exception {
		ActivityService service = lookup(ActivityService.class);
		Date startDate = Dates.now().beginOf('d').asDate();
		Date endDate = Dates.now().endOf('d').asDate();
		Activity activity = service.findActivity(new Pet().setFake(true), startDate, endDate);

		System.out.println(activity);
	}

	@Test
	public void activitiesByWeek() throws Exception {
		ActivityService service = lookup(ActivityService.class);
		Date startDate = Dates.now().beginOf('d').asDate();
		Date endDate = Dates.now().endOf('d').asDate();
		Activities activities = service.findActivities(new Pet().setFake(true), "week", startDate, endDate);

		System.out.println(activities);
	}
	
	@Test
	public void activitiesInMonth() throws Exception {
		ActivityService service = lookup(ActivityService.class);
		Date startDate = Dates.now().beginOf('d').asDate();
		Date endDate = Dates.now().endOf('d').asDate();
		Activities activities = service.findActivities(new Pet().setFake(true), "month", startDate, endDate);
		
		System.out.println(activities);
	}
}
