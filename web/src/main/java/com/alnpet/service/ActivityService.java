package com.alnpet.service;

import java.util.Date;

import com.alnpet.model.entity.Activities;

public interface ActivityService {

	public Activities findActivitiesInDay(int petId, Date startDate, Date endDate) throws Exception;

	public Activities findActivitiesInHour(int petId, Date startDate, Date endDate) throws Exception;

	public void create(int petId, Date hour, int food, int play, int active, int rest) throws Exception;
}
