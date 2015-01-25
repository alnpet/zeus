package com.alnpet.api.setting;

public enum JspFile {
	VIEW("/jsp/api/setting.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
