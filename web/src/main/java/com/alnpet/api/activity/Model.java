package com.alnpet.api.activity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.unidal.web.mvc.ErrorObject;
import org.unidal.web.mvc.ViewModel;
import org.unidal.web.mvc.view.annotation.ElementMeta;
import org.unidal.web.mvc.view.annotation.EntityMeta;
import org.unidal.web.mvc.view.annotation.ModelMeta;

import com.alnpet.api.ApiPage;
import com.alnpet.model.entity.Activities;

@ModelMeta("model")
public class Model extends ViewModel<ApiPage, Action, Context> {
	@ElementMeta
	private int m_code = 200;

	@ElementMeta
	private String m_message;

	@ElementMeta
	private String m_detailMessage;

	@EntityMeta
	private Activities m_activities;

	public Model(Context ctx) {
		super(ctx);
	}

	public Activities getActivities() {
   	return m_activities;
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

	public void setActivities(Activities activities) {
   	m_activities = activities;
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

	public void setExcpetion(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		e.printStackTrace(pw);

		m_detailMessage = sw.toString();
	}

	public void setMessage(String message) {
		m_message = message;
	}
}
