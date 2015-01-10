package com.alnpet.api.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.helper.Files;
import org.unidal.helper.Urls;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.dal.core.ActivityInDayDao;
import com.alnpet.dal.core.ActivityInDayDo;
import com.alnpet.dal.core.ActivityInDayEntity;
import com.alnpet.dal.core.ActivityInHourDao;
import com.alnpet.dal.core.ActivityInHourDo;
import com.alnpet.dal.core.ActivityInHourEntity;
import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;
import com.alnpet.model.entity.Pet;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;

public class Handler extends ApiHandler<Context> {
	@Inject
	private ActivityInHourDao m_hourDao;

	@Inject
	private ActivityInDayDao m_dayDao;

	@Inject
	private JspViewer m_jspViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "activity")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		switch (action) {
		case UPDATE:
			if (!ctx.hasErrors()) {
				Pet pet = lookupPetByDevice(ctx, model, payload.getUid());

				if (pet != null) {
					handleUpdate(ctx, payload, model, pet);
				}
			} else {
				model.setCode(400);
				model.setMessage("Bad Request");
				model.setErrors(ctx.getErrors());
			}

			renderResponse(model);
			break;
		case FEED: // TODO delete it
			int amount = payload.getAmount();

			try {
				Pet pet = lookupPetByDevice(ctx, model, payload.getUid());

				if (pet != null) {
					String url = String.format("http://xxx/%s", amount); // TODO need actual url pattern
					InputStream in = Urls.forIO().connectTimeout(5000).readTimeout(5000).openStream(url);
					String result = Files.forIO().readFrom(in, "utf-8");

					Cat.logEvent("Pet", "FeedResult", Message.SUCCESS, result);
				}
			} catch (Throwable e) {
				handleException(ctx, model, e);
			}

			renderResponse(model);
			break;
		}
	}

	private void handleInDay(Context ctx, Payload payload, Model model, int petId) {
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
			handleException(ctx, model, e);
		}
	}

	private void handleInHour(Context ctx, Payload payload, Model model, int petId) {
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
			handleException(ctx, model, e);
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
				Pet pet = lookupPetByToken(ctx, model, payload.getToken());

				if (pet != null) {
					String type = payload.getType();
					int petId = pet.getInternalId();

					if ("day".equals(type)) {
						handleInHour(ctx, payload, model, petId);
					} else if ("week".equals(type)) {
						handleInDay(ctx, payload, model, petId);
					} else if ("month".equals(type)) {
						handleInDay(ctx, payload, model, petId);
					}
				}

				renderResponse(model);
				break;
			}
		} else {
			model.setCode(400);
			model.setMessage("Bad Request");
			model.setErrors(ctx.getErrors());

			renderResponse(model);
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	private void handleUpdate(Context ctx, Payload payload, Model model, Pet pet) {
		Date date = payload.getDate();
		int[] hours = payload.getHours();
		int[] foods = payload.getFoods();
		int[] plays = payload.getPlays();
		int[] actives = payload.getActives();
		int[] rests = payload.getRests();
		Calendar cal = Calendar.getInstance();
		ActivityInHourDo[] batch = new ActivityInHourDo[hours.length];
		int index = 0;

		cal.setTime(date);

		for (int hour : hours) {
			ActivityInHourDo a = new ActivityInHourDo();

			cal.set(Calendar.HOUR, hour);
			a.setPetId(pet.getInternalId());
			a.setHour(cal.getTime());

			if (foods.length > 0) {
				a.setFood(foods[index]);
			}

			if (plays.length > 0) {
				a.setPlay(plays[index]);
			}

			if (actives.length > 0) {
				a.setActive(actives[index]);
			}

			if (rests.length > 0) {
				a.setRest(rests[index]);
			}

			batch[index++] = a;
		}

		try {
			m_hourDao.insert(batch);
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}
	}
}
