package com.alnpet.api.home;

public enum JspFile {
	VIEW("/jsp/api/home.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
