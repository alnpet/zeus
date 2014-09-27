package com.alnpet.api.activity;

import java.util.Calendar;
import java.util.Date;

import org.unidal.helper.Dates;
import org.unidal.helper.Dates.DateHelper;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;
import org.unidal.web.mvc.payload.annotation.PathMeta;

import com.alnpet.api.ApiPage;

public class Payload implements ActionPayload<ApiPage, Action> {
	private ApiPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@PathMeta("pathes")
	private String[] m_pathes;

	private String m_type;

	private String m_date;

	private Date m_startDate;

	private Date m_endDate;

	@Override
	public Action getAction() {
		return m_action;
	}

	public Date getEndDate() {
		return m_endDate;
	}

	@Override
	public ApiPage getPage() {
		return m_page;
	}

	public Date getStartDate() {
		return m_startDate;
	}

	public String getType() {
		return m_type;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public void setPage(String page) {
		m_page = ApiPage.getByName(page, ApiPage.ACTIVITY);
	}

	public void setPathes(String[] pathes) {
		m_pathes = pathes;
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		if (m_pathes != null) {
			int index = 0;

			m_type = m_pathes.length > index ? m_pathes[index++] : null;
			m_date = m_pathes.length > index ? m_pathes[index++] : null;
		}

		if (m_type == null) {
			m_type = "day";
		}

		DateNormalizer normalizer = new DateNormalizer(ctx, m_type, m_date);

		m_startDate = normalizer.getStartDate();
		m_endDate = normalizer.getEndDate();
	}

	static class DateNormalizer {
		private ActionContext<?> m_ctx;

		private String m_type;

		private String m_date;

		private Date m_startDate;

		private Date m_endDate;

		private Date m_today;

		public DateNormalizer(ActionContext<?> ctx, String type, String date) {
			m_ctx = ctx;
			m_type = type;
			m_date = date;
			m_today = new Date();

			initialize();
		}

		public Date getEndDate() {
			return m_endDate;
		}

		public Date getStartDate() {
			return m_startDate;
		}

		private void initialize() {
			if ("day".equals(m_type)) {
				DateHelper helper;

				if (m_date == null || m_date.length() == 0) {
					helper = Dates.from(m_today);
				} else if (m_date.length() == 8) {
					helper = Dates.from(m_date, "yyyyMMdd");
				} else {
					m_ctx.addError("date.invalid");
					return;
				}

				m_startDate = helper.beginOf('d').asDate();
				m_endDate = helper.endOf('d').asDate();
			} else if ("week".equals(m_type)) {
				DateHelper helper;

				if (m_date == null || m_date.length() == 0) {
					helper = Dates.from(m_today);
				} else if (m_date.length() == 8) {
					helper = Dates.from(m_date, "yyyyMMdd");
				} else {
					m_ctx.addError("date.invalid");
					return;
				}

				helper.firstDayOfWeek(Calendar.MONDAY);

				m_startDate = helper.beginOf('w').asDate();
				m_endDate = helper.endOf('w').asDate();
			} else if ("month".equals(m_type)) {
				DateHelper helper;

				if (m_date == null || m_date.length() == 0) {
					helper = Dates.from(m_today);
				} else if (m_date.length() == 6) {
					helper = Dates.from(m_date, "yyyyMM");
				} else if (m_date.length() == 8) {
					helper = Dates.from(m_date, "yyyyMMdd");
				} else {
					m_ctx.addError("date.invalid");
					return;
				}

				m_startDate = helper.beginOf('M').asDate();
				m_endDate = helper.endOf('M').asDate();
			} else {
				m_ctx.addError("type.invalid");
			}
		}

		// for test purpose
		void setToday(Date today) {
			m_today = today;
		}
	}
}
