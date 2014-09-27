package com.alnpet.api.activity;

public enum JspFile {
	VIEW("/jsp/api/activity.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
