package com.alnpet.ajax.newsletter;

import java.util.List;

import com.alnpet.ajax.AjaxPage;
import com.alnpet.dal.biz.User;

import org.unidal.web.mvc.ViewModel;

public class Model extends ViewModel<AjaxPage, Action, Context> {
	private List<User> m_users;

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public List<User> getUsers() {
		return m_users;
	}

	public void setUsers(List<User> users) {
		m_users = users;
	}
}
