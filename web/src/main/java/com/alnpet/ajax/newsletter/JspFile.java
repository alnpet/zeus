package com.alnpet.ajax.newsletter;

public enum JspFile {
	VIEW("/jsp/ajax/newsletter.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
