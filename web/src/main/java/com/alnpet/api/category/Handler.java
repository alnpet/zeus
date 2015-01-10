package com.alnpet.api.category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.category.CategoryManager;
import com.alnpet.category.PetType;
import com.alnpet.dal.core.CategoryDao;
import com.alnpet.dal.core.CategoryDo;
import com.alnpet.dal.core.CategoryEntity;
import com.alnpet.model.entity.Category;

public class Handler extends ApiHandler<Context> {
	@Inject
	private CategoryManager m_manager;

	@Inject
	private CategoryDao m_categoryDao;

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
					m_manager.setup();
				} catch (Throwable e) {
					handleException(ctx, model, e);
				}
			} else {
				model.setCode(400);
				model.setMessage("Bad Request");
				model.setErrors(ctx.getErrors());
			}

			renderResponse(model);
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
				List<CategoryDo> categories = m_categoryDao.findAllByStatus(1, CategoryEntity.READSET_FULL);
				List<Category> list = new ArrayList<Category>();

				for (CategoryDo category : categories) {
					Category item = new Category();

					item.setId(category.getId());
					item.setName(category.getName());
					item.setType(PetType.getById(category.getType(), PetType.UNKNOWN).getName());
					list.add(item);
				}

				model.setCategories(list);
			} catch (Throwable e) {
				handleException(ctx, model, e);
			}

			renderResponse(model);
			break;
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}
}
