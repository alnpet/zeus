package com.alnpet.ajax.newsletter;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.ajax.AjaxPage;
import com.alnpet.dal.trx.User;
import com.alnpet.dal.trx.UserDao;
import com.alnpet.dal.trx.UserEntity;
import com.dianping.cat.Cat;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private UserDao m_dao;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "newsletter")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		if (action == Action.SIGNUP) {
			if (!ctx.hasErrors()) {
				User user = new User();

				user.setName(payload.getName());
				user.setEmail(payload.getEmail());

				try {
					m_dao.insert(user);

					ctx.sendJson("status", "added");
				} catch (DalException e) {
					if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
						ctx.sendJson("status", "duplicated");
					} else {
						Cat.logError(e);
						ctx.sendJsonResponse("500", user, e.getCause());
					}
				}
			}

			ctx.stopProcess();
		}
	}

	@Override
	@OutboundActionMeta(name = "newsletter")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();
		Model model = new Model(ctx);

		model.setAction(Action.VIEW);
		model.setPage(AjaxPage.NEWSLETTER);

		if (action == Action.LIST) {
			showList(ctx, model);
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	private void showList(Context ctx, Model model) {
		try {
			List<User> users = m_dao.findAll(UserEntity.READSET_FULL);

			model.setUsers(users);
		} catch (DalException e) {
			Cat.logError(e);
		}
	}
}
