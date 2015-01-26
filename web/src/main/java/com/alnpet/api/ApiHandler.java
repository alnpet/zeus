package com.alnpet.api;

import java.io.IOException;

import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.PageHandler;

import com.alnpet.model.entity.Pet;
import com.alnpet.service.PetService;
import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;

public abstract class ApiHandler<T extends ActionContext<?>> implements PageHandler<T> {
	@Inject
	protected PetService m_service;

	@Inject
	private XmlViewer m_xmlViewer;

	protected void handleException(T ctx, ApiModel<?, ?> model, Throwable e) {
		Cat.logError(e);
		ctx.getHttpServletRequest().setAttribute(CatConstants.CAT_STATE, e.getClass().getName());

		model.error(500, e.getMessage()).setExcpetion(e);
	}

	protected Pet lookupPetByToken(T ctx, ApiModel<?, ?> model, String token) {
		if ("fake".equals(token)) {
			Pet pet = new Pet(token).setFake(true);
			
			model.setPet(pet);
			return pet;
		}
		
		try {
			Pet pet = m_service.lookupByToken(token);

			model.setPet(pet);
			return pet;
		} catch (DalNotFoundException e) {
			model.error(404, "token.invalid");
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}

		return null;
	}

	protected Pet lookupPetByDevice(T ctx, ApiModel<?, ?> model, String device) {
		try {
			Pet pet = m_service.lookupByDevice(device);

			model.setPet(pet);
			return pet;
		} catch (DalNotFoundException e) {
			model.error(404, "device.invalid");
		} catch (Throwable e) {
			handleException(ctx, model, e);
		}

		return null;
	}

	protected void renderResponse(ApiModel<?, ?> model) throws IOException {
		m_xmlViewer.view(model);
	}
}
