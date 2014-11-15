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

	// view
	@PathMeta("pathes")
	private String[] m_pathes;

	@FieldMeta("token")
	private String m_token;

	@FieldMeta("uid")
	private String m_uid;

	// update
	@FieldMeta(value = "date", format = "yyyy-MM-dd")
	private Date m_date;

	@FieldMeta("hour")
	private int[] m_hours;

	@FieldMeta("food")
	private int[] m_foods;

	@FieldMeta("play")
	private int[] m_plays;

	@FieldMeta("active")
	private int[] m_actives;

	@FieldMeta("rest")
	private int[] m_rests;

	// feed
	@FieldMeta("amount")
	private int m_amount;

	private String m_type;

	private Date m_startDate;

	private Date m_endDate;

	@Override
	public Action getAction() {
		return m_action;
	}

	public int[] getActives() {
		return m_actives;
	}

	public Date getDate() {
		return m_date;
	}

	public Date getEndDate() {
		return m_endDate;
	}

	public int getAmount() {
		return m_amount;
	}

	public int[] getFoods() {
		return m_foods;
	}

	public int[] getHours() {
		return m_hours;
	}

	@Override
	public ApiPage getPage() {
		return m_page;
	}

	public int[] getPlays() {
		return m_plays;
	}

	public int[] getRests() {
		return m_rests;
	}

	public Date getStartDate() {
		return m_startDate;
	}

	public String getToken() {
		return m_token;
	}

	public String getType() {
		return m_type;
	}

	public String getUid() {
		return m_uid;
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

		switch (m_action) {
		case VIEW:
			String date = null;

			if (m_token == null) {
				ctx.addError("token.required");
			}

			if (m_pathes != null) {
				int index = 0;

				m_type = m_pathes.length > index ? m_pathes[index++] : null;
				date = m_pathes.length > index ? m_pathes[index++] : null;
			}

			if (m_type == null) {
				m_type = "day";
			}

			DateNormalizer normalizer = new DateNormalizer(ctx, m_type, date);

			m_startDate = normalizer.getStartDate();
			m_endDate = normalizer.getEndDate();
			break;
		case UPDATE:
			boolean valid = true;
			int len = m_hours.length;

			if (m_hours != null && len > 0) {
				if (m_date == null) {
					m_date = Dates.now().beginOf('d').asDate();
				}

				if (m_foods.length + m_plays.length + m_actives.length + m_rests.length == 0) {
					valid = false;
				}

				if (valid && m_foods.length > 0 && m_foods.length != len) {
					valid = false;
				}

				if (valid && m_plays.length > 0 && m_plays.length != len) {
					valid = false;
				}

				if (valid && m_actives.length > 0 && m_actives.length != len) {
					valid = false;
				}

				if (valid && m_rests.length > 0 && m_rests.length != len) {
					valid = false;
				}
			} else {
				valid = false;
			}

			if (m_uid == null) {
				ctx.addError("uid.required");
			}

			if (!valid) {
				ctx.addError("request.invalid");
			}

			break;
		case FEED:
			if (m_uid == null) {
				ctx.addError("uid.required");
			}

			if (m_amount <= 0) {
				ctx.addError("amount.required");
			}

			break;
		}
	}

	static class DateNormalizer {
		private ActionContext<?> m_ctx;

		private String m_type;

		private String m_date;

		private Date m_startDate;

		private Date m_endDate;

		private Date m_today;

		public DateNormalizer(ActionContext<?> ctx, Date today, String type, String date) {
			m_ctx = ctx;
			m_type = type;
			m_date = date;
			m_today = today;

			initialize();
		}

		public DateNormalizer(ActionContext<?> ctx, String type, String date) {
			this(ctx, new Date(), type, date);
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
	}
}
