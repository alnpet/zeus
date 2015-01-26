package com.alnpet.service;

import java.util.Date;

import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;
import com.alnpet.model.entity.Pet;

public interface ActivityService {
	public void create(int petId, Date hour, int food, int play, int active, int rest) throws Exception;

	public Activities findActivities(Pet pet, String type, Date startDate, Date endDate) throws Exception;

	public Activity findActivity(Pet pet, Date startDate, Date endDate) throws Exception;
}
