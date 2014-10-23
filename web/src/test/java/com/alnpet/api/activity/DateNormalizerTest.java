package com.alnpet.api.activity;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.helper.Dates;

import com.alnpet.api.activity.Payload.DateNormalizer;

public class DateNormalizerTest {
	@Test
	public void testDay() {
		check("day", null, "2014-09-27 00:00:00", "2014-09-27 23:59:59");
		check("day", "20140928", "2014-09-28 00:00:00", "2014-09-28 23:59:59");
	}

	@Test
	public void testWeek() {
		check("week", null, "2014-09-22 00:00:00", "2014-09-28 23:59:59");
		check("week", "20140922", "2014-09-22 00:00:00", "2014-09-28 23:59:59");
		check("week", "20140927", "2014-09-22 00:00:00", "2014-09-28 23:59:59");
	}

	@Test
	public void testMonth() {
		check("month", null, "2014-09-01 00:00:00", "2014-09-30 23:59:59");
		check("month", "20140922", "2014-09-01 00:00:00", "2014-09-30 23:59:59");
		check("month", "20140927", "2014-09-01 00:00:00", "2014-09-30 23:59:59");

		check("month", "201409", "2014-09-01 00:00:00", "2014-09-30 23:59:59");
		check("month", "201410", "2014-10-01 00:00:00", "2014-10-31 23:59:59");
	}

	private void check(String type, String date, String expectedStartDate, String expectedEndDate) {
		Context ctx = new Context();
		Date today = new Date(1411808584129L);
		DateNormalizer n = new DateNormalizer(ctx, today, type, date);

		Date startDate = n.getStartDate();
		Date endDate = n.getEndDate();

		Assert.assertEquals("start date is incorrect!", expectedStartDate,
		      Dates.from(startDate).asString("yyyy-MM-dd HH:mm:ss"));
		Assert.assertEquals("end date is incorrect!", expectedEndDate, Dates.from(endDate)
		      .asString("yyyy-MM-dd HH:mm:ss"));
	}
}
