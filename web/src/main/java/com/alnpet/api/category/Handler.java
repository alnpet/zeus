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
import com.alnpet.dal.core.Category;
import com.alnpet.dal.core.CategoryDao;
import com.alnpet.dal.core.CategoryEntity;

public class Handler extends ApiHandler<Context> {
	@Inject
	private CategoryDao m_categoryDao;

	@Inject
	private JspViewer m_jspViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "category")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
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
			int petId = lookupPetByToken(ctx, model, payload.getToken());

			if (petId > 0) {
				try {
					List<Category> categories = m_categoryDao.findAllByStatus(1, CategoryEntity.READSET_FULL);

					model.setCategories(categories);
				} catch (Throwable e) {
					handleException(ctx, model, e);
				}
			}

			m_xmlViewer.view(model);
			break;
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}
}
