package com.alnpet.api.pet;

public enum JspFile {
	VIEW("/jsp/api/pet.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
