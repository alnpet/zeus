package com.alnpet.api.activity;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiPage;
import com.alnpet.api.XmlViewer;
import com.alnpet.dal.core.ActivityInDayDo;
import com.alnpet.dal.core.ActivityInDayDao;
import com.alnpet.dal.core.ActivityInDayEntity;
import com.alnpet.dal.core.ActivityInHourDo;
import com.alnpet.dal.core.ActivityInHourDao;
import com.alnpet.dal.core.ActivityInHourEntity;
import com.alnpet.dal.core.PetDao;
import com.alnpet.dal.core.PetDo;
import com.alnpet.dal.core.PetEntity;
import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;
import com.alnpet.model.entity.Pet;

public class Handler implements PageHandler<Context> {
	@Inject
	private PetDao m_dao;

	@Inject
	private ActivityInHourDao m_hourDao;

	@Inject
	private ActivityInDayDao m_dayDao;

	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private XmlViewer m_xmlViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "activity")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	private void handleInDay(Context ctx, Payload payload, Model model) {
		int petId = lookupPet(payload, model);

		if (petId > 0) {
			try {
				Date startDate = payload.getStartDate();
				Date endDate = payload.getEndDate();

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

				model.setActivities(activities);
			} catch (Throwable e) {
				model.setCode(500);
				model.setMessage(e.getMessage());
				model.setExcpetion(e);
			}
		}
	}

	private void handleInHour(Context ctx, Payload payload, Model model) {
		int petId = lookupPet(payload, model);

		if (petId > 0) {
			try {
				Date startDate = payload.getStartDate();
				Date endDate = payload.getEndDate();
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

				model.setActivities(activities);
			} catch (Throwable e) {
				model.setCode(500);
				model.setMessage(e.getMessage());
				model.setExcpetion(e);
			}
		}
	}

	@Override
	@OutboundActionMeta(name = "activity")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		model.setAction(Action.VIEW);
		model.setPage(ApiPage.ACTIVITY);

		if (!ctx.hasErrors()) {
			switch (action) {
			case VIEW:
				String type = payload.getType();

				if ("day".equals(type)) {
					handleInHour(ctx, payload, model);
				} else if ("week".equals(type)) {
					handleInDay(ctx, payload, model);
				} else if ("month".equals(type)) {
					handleInDay(ctx, payload, model);
				}

				m_xmlViewer.view(model);
				break;
			}
		} else {
			model.setCode(400);
			model.setMessage("Bad Request");
			model.setErrors(ctx.getErrors());
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	private int lookupPet(Payload payload, Model model) {
		int petId = -1;

		try {
			PetDo pet = m_dao.findByToken(payload.getToken(), PetEntity.READSET_FULL);

			petId = pet.getId();
			model.setPet(new Pet(payload.getToken()));
		} catch (DalNotFoundException e) {
			model.setCode(404);
			model.setMessage("token.invalid");
		} catch (Throwable e) {
			model.setCode(500);
			model.setMessage(e.getMessage());
			model.setExcpetion(e);
		}

		return petId;
	}
}
