package com.alnpet.api.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;

import org.unidal.helper.Files;
import org.unidal.helper.Urls;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;
import com.alnpet.model.entity.Pet;
import com.alnpet.service.ActivityService;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;

public class Handler extends ApiHandler<Context> {
	@Inject
	private ActivityService m_service;

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
				handleUpdate(ctx, payload, model);
			} else {
				model.error(400, "Bad Request").setErrors(ctx.getErrors());
			}

			renderResponse(model);
			break;
		case FEED: // TODO delete it
			int amount = payload.getAmount();

			try {
				Pet pet = lookupPetByDevice(ctx, model, payload.getUid());

				if (pet != null) {
					String url = String.format("http://xxx/%s", amount); // TODO
					// need
					// actual
					// url
					// pattern
					InputStream in = Urls.forIO().connectTimeout(5000).readTimeout(5000).openStream(url);
					String result = Files.forIO().readFrom(in, "utf-8");

					Cat.logEvent("Pet", "FeedResult", Message.SUCCESS, result);
				}
			} catch (Throwable e) {
				handleException(ctx, model, e);
			}

			renderResponse(model);
			break;
		case FAKE:
			if (!ctx.hasErrors()) {
				try {
					m_service.setFake(payload.isFake());
				} catch (Throwable e) {
					handleException(ctx, model, e);
				}
			} else {
				model.error(400, "Bad Request").setErrors(ctx.getErrors());
			}

			renderResponse(model);
			break;
		default:
			break;
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
				handleView(ctx, payload, model);

				renderResponse(model);
				break;
			default:
				break;
			}
		} else {
			model.error(400, "Bad Request").setErrors(ctx.getErrors());

			renderResponse(model);
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	private void handleUpdate(Context ctx, Payload payload, Model model) {
		Pet pet = lookupPetByDevice(ctx, model, payload.getUid());

		if (pet != null) {
			Date date = payload.getDate();
			int[] hours = payload.getHours();
			int[] foods = payload.getFoods();
			int[] plays = payload.getPlays();
			int[] actives = payload.getActives();
			int[] rests = payload.getRests();
			Calendar cal = Calendar.getInstance();
			int index = 0;

			cal.setTime(date);

			try {
				for (int hour : hours) {
					cal.set(Calendar.HOUR, hour);

					m_service.create(pet.getInternalId(), cal.getTime(), foods[index], plays[index], actives[index],
					      rests[index]);
				}
			} catch (Throwable e) {
				handleException(ctx, model, e);
			}
		}
	}

	private void handleView(Context ctx, Payload payload, Model model) {
		Pet pet = lookupPetByToken(ctx, model, payload.getToken());

		if (pet != null) {
			try {
				String type = payload.getType();

				if ("day".equals(type)) {
					Activity activity = m_service.findActivity(pet, payload.getStartDate(), payload.getEndDate());

					model.setActivity(activity);
				} else if ("week".equals(type)) {
					Activities activities = m_service.findActivities(pet, type, payload.getStartDate(), payload.getEndDate());

					model.setActivities(activities);
				} else if ("month".equals(type)) {
					Activities activities = m_service.findActivities(pet, type, payload.getStartDate(), payload.getEndDate());

					model.setActivities(activities);
				} else {
					throw new IllegalArgumentException("type=" + type);
				}
			} catch (Throwable e) {
				handleException(ctx, model, e);
			}
		}
	}
}
