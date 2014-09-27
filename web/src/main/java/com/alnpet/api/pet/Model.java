package com.alnpet.api.pet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.unidal.web.mvc.ErrorObject;
import org.unidal.web.mvc.ViewModel;
import org.unidal.web.mvc.view.annotation.ElementMeta;
import org.unidal.web.mvc.view.annotation.EntityMeta;
import org.unidal.web.mvc.view.annotation.ModelMeta;

import com.alnpet.api.ApiPage;
import com.alnpet.model.entity.Pet;

@ModelMeta("model")
public class Model extends ViewModel<ApiPage, Action, Context> {
	@ElementMeta
	private int m_code = 200;

	@ElementMeta
	private String m_message;

	@ElementMeta
	private String m_detailMessage;
	
	@EntityMeta("pet")
	private Pet m_pet;

	public Model(Context ctx) {
		super(ctx);
	}

	public int getCode() {
		return m_code;
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
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

	public void setCode(int code) {
		m_code = code;
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

	public void setMessage(String message) {
		m_message = message;
	}

	public void setPet(Pet pet) {
		m_pet = pet;
	}

	public void setExcpetion(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		e.printStackTrace(pw);

		m_detailMessage = sw.toString();
	}
}
