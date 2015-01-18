package com.alnpet.api;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.unidal.web.mvc.Action;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ErrorObject;
import org.unidal.web.mvc.ViewModel;
import org.unidal.web.mvc.view.annotation.ElementMeta;
import org.unidal.web.mvc.view.annotation.EntityMeta;

import com.alnpet.model.entity.Pet;

public abstract class ApiModel<A extends Action, M extends ActionContext<?>> extends ViewModel<ApiPage, A, M> {
	@ElementMeta
	private int m_code = 200;

	@ElementMeta
	private String m_message;

	@ElementMeta
	private String m_detailMessage;

	@EntityMeta("pet")
	private Pet m_pet;

	public ApiModel(M ctx) {
		super(ctx);
	}

	public int getCode() {
		return m_code;
	}

	public String getDetailMessage() {
		return m_detailMessage;
	}

	public String getMessage() {
		return m_message;
	}

	public Pet getPet() {
		return m_pet;
	}

	public ApiModel<A, M> error(int code, String message) {
		m_code = code;
		m_message = message;
		return this;
	}

	public void setErrors(List<ErrorObject> errors) {
		StringBuilder sb = new StringBuilder(256);

		for (ErrorObject error : errors) {
			if (sb.length() > 0) {
				sb.append(',');
			}

			sb.append(error.getCode());
		}

		m_detailMessage = sb.toString();
	}

	public void setExcpetion(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		e.printStackTrace(pw);

		m_detailMessage = sw.toString();
	}

	public void setPet(Pet pet) {
		m_pet = pet;
	}
}
