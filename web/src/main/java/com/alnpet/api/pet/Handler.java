package com.alnpet.api.pet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiPage;
import com.alnpet.api.XmlViewer;
import com.alnpet.dal.core.PetDao;
import com.alnpet.dal.core.PetDo;
import com.alnpet.model.entity.Pet;

public class Handler implements PageHandler<Context> {
	@Inject
	private PetDao m_dao;

	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private XmlViewer m_xmlViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "pet")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		switch (action) {
		case REGISTER:
			Model model = new Model(ctx);

			if (!ctx.hasErrors()) {
				handleRegister(payload, model);
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

	private void handleRegister(Payload payload, Model model) {
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
			model.setCode(500);
			model.setMessage(e.getMessage());
			model.setExcpetion(e);
		}
	}
}
