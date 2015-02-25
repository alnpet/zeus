package com.alnpet.biz.payment;

public enum JspFile {
	VIEW("/jsp/biz/payment.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
