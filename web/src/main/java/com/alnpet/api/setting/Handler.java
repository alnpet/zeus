package com.alnpet.api.setting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.model.entity.Pet;
import com.alnpet.service.SettingService;

public class Handler extends ApiHandler<Context> {
	@Inject
	private SettingService m_setting;

	@Inject
	private JspViewer m_jspViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "setting")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();
		Model model = new Model(ctx);

		switch (action) {
		case UPDATE:
			if (!ctx.hasErrors()) {
				handleUpdate(ctx, payload, model);
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
	@OutboundActionMeta(name = "setting")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		model.setAction(Action.VIEW);
		model.setPage(ApiPage.SETTING);

		switch (action) {
		case VIEW:
			if (!ctx.hasErrors()) {
				handleView(ctx, payload, model);
			} else {
				model.error(400, "Bad Request").setErrors(ctx.getErrors());
			}

			renderResponse(model);
			break;
		default:
			break;
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	private void handleUpdate(Context ctx, Payload payload, Model model) {
		try {
			Pet pet = lookupPetByToken(ctx, model, payload.getToken());

			if (pet != null) {
				List<String> keys = payload.getKeys();
				HttpServletRequest req = ctx.getHttpServletRequest();

				for (String key : keys) {
					String value = req.getParameter(key);

					if (value != null) {
						m_setting.setKey(pet.getInternalId(), key, value);
					}
				}
			}
		} catch (Exception e) {
			handleException(ctx, model, e);
		}
	}

	private void handleView(Context ctx, Payload payload, Model model) {
		try {
			Pet pet = lookupPetByToken(ctx, model, payload.getToken());

			if (pet != null) {
				List<String> keys = payload.getKeys();

				for (String key : keys) {
					try {
						String value = m_setting.getKey(pet.getInternalId(), key);

						model.setSetting(key, value);
					} catch (DalNotFoundException e) {
						model.setSetting(key, null);
					}
				}
			}
		} catch (Exception e) {
			handleException(ctx, model, e);
		}
	}
}
