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

			renderResponse(model);
			break;
		case BIND:
			if (!ctx.hasErrors()) {
				Pet pet = lookupPetByToken(ctx, model, payload.getToken());

				if (pet != null) {
					handleBind(ctx, payload, model, pet);
				}
			} else {
				model.setCode(400);
				model.setMessage("Bad Request");
				model.setErrors(ctx.getErrors());
			}

			renderResponse(model);
			break;
		default:
			break;
		}
	}

	@Override
	@OutboundActionMeta(name = "pet")
	public void handleOutbound(Context ctx) throws ServletException,
			IOException {
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

	private void handleBind(Context ctx, Payload payload, Model model, Pet pet) {
		try {
			PetDo p = m_dao.findByPK(pet.getInternalId(),
					PetEntity.READSET_FULL);
			String type = payload.getType();

			if ("user".equals(type)) {
				if (payload.getNickname() != null) {
					p.setNickname(payload.getNickname());
				}

				if (payload.getPhone() != null) {
					p.setPhone(payload.getPhone());
				}

				if (payload.getEmail() != null) {
					p.setEmail(payload.getEmail());
				}
			} else if ("device".equals(type)) {
				p.setDevice(payload.getDevice());
			}

			m_dao.updateByPK(p, PetEntity.UPDATESET_FULL);

			model.setPet(pet);
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}
	}
}
