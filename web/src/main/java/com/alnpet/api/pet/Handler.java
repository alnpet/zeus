package com.alnpet.api.pet;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.model.entity.Pet;

public class Handler extends ApiHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	private void handleBind(Context ctx, Payload payload, Model model) {
		try {
			Pet pet = lookupPetByToken(ctx, model, payload.getToken());

			if (pet != null) {
				String type = payload.getType();

				if ("user".equals(type)) {
					m_service.bindUser(pet.getInternalId(), payload.getNickname(), payload.getPhone(), payload.getEmail());
				} else if ("portrait".equals(type)) {
					byte[] portrait = new byte[0]; // TODO
					
					m_service.bindPortrait(pet.getInternalId(), portrait);
				} else if ("device".equals(type)) {
					m_service.bindDevice(pet.getInternalId(), payload.getDevice());
				}

				model.setPet(pet);
			}
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}
	}

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "pet")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();
		Model model = new Model(ctx);

		switch (action) {
		case REGISTER:
			if (!ctx.hasErrors()) {
				handleRegister(ctx, payload, model);
			} else {
				model.error(400, "Bad Request").setErrors(ctx.getErrors());
			}

			renderResponse(model);
			break;
		case BIND:
			if (!ctx.hasErrors()) {
				handleBind(ctx, payload, model);
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
	@OutboundActionMeta(name = "pet")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);

		model.setAction(Action.VIEW);
		model.setPage(ApiPage.PET);

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	private void handleRegister(Context ctx, Payload payload, Model model) {
		try {
			Pet pet = m_service.create(payload.getName(), payload.getGender(), payload.getCategory(), payload.getAge(),
			      payload.getWeight());

			model.setPet(pet);
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}
	}
}
