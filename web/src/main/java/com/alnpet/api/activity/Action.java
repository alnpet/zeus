package com.alnpet.api.activity;

public enum Action implements org.unidal.web.mvc.Action {
	VIEW("2.1", "view"),

	UPDATE("2.1", "update"),

	FEED("2.2", "feed");

	private String m_id;

	private String m_name;

	private Action(String id, String name) {
		m_id = id;
		m_name = name;
	}

	public static Action getByName(String name, Action defaultAction) {
		for (Action action : Action.values()) {
			if (action.getName().equals(name)) {
				return action;
			}
		}

		return defaultAction;
	}

	public String getId() {
		return m_id;
	}

	@Override
	public String getName() {
		return m_name;
	}
}
