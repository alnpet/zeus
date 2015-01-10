package com.alnpet.api;

import java.io.IOException;

import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.PageHandler;

import com.alnpet.dal.core.PetDao;
import com.alnpet.dal.core.PetDo;
import com.alnpet.dal.core.PetEntity;
import com.alnpet.model.entity.Pet;
import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;

public abstract class ApiHandler<T extends ActionContext<?>> implements PageHandler<T> {
	@Inject
	protected PetDao m_dao;

	@Inject
	private XmlViewer m_xmlViewer;

	protected void handleException(T ctx, ApiModel<?, ?> model, Throwable e) {
		Cat.logError(e);
		ctx.getHttpServletRequest().setAttribute(CatConstants.CAT_STATE, e.getClass().getName());

		model.setCode(500);
		model.setMessage(e.getMessage());
		model.setExcpetion(e);
	}

	protected Pet lookupPetByToken(T ctx, ApiModel<?, ?> model, String token) {
		try {
			PetDo p = m_dao.findByToken(token, PetEntity.READSET_FULL);
			Pet pet = new Pet(token);

			pet.setInternalId(p.getId());
			model.setPet(pet);
			return pet;
		} catch (DalNotFoundException e) {
			model.setCode(404);
			model.setMessage("token.invalid");
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}

		return null;
	}

	protected Pet lookupPetByDevice(T ctx, ApiModel<?, ?> model, String device) {
		try {
			PetDo p = m_dao.findByDevice(device, PetEntity.READSET_FULL);
			Pet pet = new Pet(p.getToken());

			pet.setInternalId(p.getId());
			model.setPet(pet);
			return pet;
		} catch (DalNotFoundException e) {
			model.setCode(404);
			model.setMessage("device.invalid");
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}

		return null;
	}

	protected void renderResponse(ApiModel<?, ?> model) throws IOException {
		m_xmlViewer.view(model);
	}
}
