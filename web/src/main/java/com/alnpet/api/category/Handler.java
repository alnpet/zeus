package com.alnpet.api.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.model.entity.Category;
import com.alnpet.service.CategoryService;

public class Handler extends ApiHandler<Context> {
	@Inject
	private CategoryService m_service;

	@Inject
	private JspViewer m_jspViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "category")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		switch (action) {
		case SETUP:
			if (!ctx.hasErrors()) {
				try {
					m_service.setup();
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
	@OutboundActionMeta(name = "category")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();

		model.setAction(Action.VIEW);
		model.setPage(ApiPage.CATEGORY);

		Action action = payload.getAction();

		switch (action) {
		case VIEW:
			try {
				List<Category> list = m_service.findActiveCategories();

				model.setCategories(list);
			} catch (Throwable e) {
				handleException(ctx, model, e);
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
}
