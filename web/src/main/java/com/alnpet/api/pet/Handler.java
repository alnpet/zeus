package com.alnpet.api.pet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.dal.core.PetDo;
import com.alnpet.dal.core.PetEntity;
import com.alnpet.model.entity.Pet;

public class Handler extends ApiHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

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
				model.setCode(400);
				model.setMessage("Bad Request");
				model.setErrors(ctx.getErrors());
			}

			m_xmlViewer.view(model);
			break;
		case UPDATE:
			if (!ctx.hasErrors()) {
				handleUpdate(ctx, payload, model);
			} else {
				model.setCode(400);
				model.setMessage("Bad Request");
				model.setErrors(ctx.getErrors());
			}

			m_xmlViewer.view(model);
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
		PetDo pet = new PetDo();
		String token = UUID.randomUUID().toString();

		pet.setToken(token);
		pet.setName(payload.getName());
		pet.setGender(payload.getGender());
		pet.setCategory(payload.getCategory());
		pet.setDevice(payload.getDevice());

		if (payload.getAge() > 0) {
			pet.setAge(payload.getAge());
		}

		if (payload.getWeight() > 0) {
			pet.setWeight(payload.getWeight());
		}

		try {
			m_dao.insert(pet);

			model.setPet(new Pet(token));
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}
	}

	private void handleUpdate(Context ctx, Payload payload, Model model) {
		String token = payload.getToken();

		try {
			PetDo pet = m_dao.findByToken(token, PetEntity.READSET_FULL);

			if (payload.getNickname() != null) {
				pet.setNickname(payload.getNickname());
			}

			if (payload.getPhone() != null) {
				pet.setPhone(payload.getPhone());
			}

			if (payload.getEmail() != null) {
				pet.setEmail(payload.getEmail());
			}

			m_dao.updateByPK(pet, PetEntity.UPDATESET_FULL);

			model.setPet(new Pet(token));
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}
	}
}
