package com.alnpet.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.unidal.helper.Files;
import org.unidal.helper.Urls;
import org.unidal.test.jetty.JettyServer;
import org.xml.sax.SAXException;

import com.alnpet.model.entity.Model;
import com.alnpet.model.transform.DefaultSaxParser;

public class ApiIntegrationTest extends JettyServer {
	private static String PREFIX;

	@After
	public void after() throws Exception {
		super.stopServer();
	}

	@Before
	public void before() throws Exception {
		System.setProperty("devMode", "true");
		super.startServer();

		PREFIX = String.format("http://localhost:%s", getServerPort());
	}

	private void call(Context ctx, ModelHandler handler, String pattern, Object... params) throws IOException,
	      SAXException {
		Object[] args = new Object[params.length];
		int index = 0;

		for (Object param : params) {
			if (param instanceof String) {
				// attribute
				args[index++] = ctx.get((String) param);
			} else {
				// final value
				args[index++] = param;
			}
		}

		String url = PREFIX + String.format(pattern, args);
		String xml = Files.forIO().readFrom(Urls.forIO().openStream(url), "utf-8");

		Model model = DefaultSaxParser.parse(xml);

		Assert.assertEquals(String.format("message: %s, detailMessage: %s\n", model.getMessage(),
		      model.getDetailMessage()), 200, model.getCode().intValue());

		if (handler != null) {
			handler.handle(ctx, model);
		}
	}

	@Override
	protected String getContextPath() {
		return "/";
	}

	@Override
	protected int getServerPort() {
		return 2568;
	}

	@Test
	public void testAll() throws Exception {
		Context ctx = new Context();

		// (3.0) Get pet categories
		call(ctx, new ModelHandler() {
			@Override
			public void handle(Context ctx, Model model) {
				Assert.assertEquals("Category size not expected.", 149, model.getCategories().size());
			}
		}, "/api/category");

		// (1.1) Register a pet
		call(ctx, new ModelHandler() {
			@Override
			public void handle(Context ctx, Model model) {
				ctx.set("token", model.getPet().getId());
			}
		}, "/api/pet?op=register&name=[name]&gender=[gender]&category=1&age=10&weight=20");

		// (1.2.1) Bind pet's owner information
		call(ctx, null, "/api/pet?op=bind&type=user&token=%s&nickname=[nickname]&email=[email]&phone=[phone]", "token");

		// (1.2.2) Bind pet's feeder device id
		String deviceId = UUID.randomUUID().toString();

		ctx.set("device", deviceId);
		call(ctx, null, "/api/pet?op=bind&type=device&token=%s&device=%s", "token", "device");

		// (2.0) Get pet's activities
		call(ctx, new ModelHandler() {
			@Override
			public void handle(Context ctx, Model model) {
				Assert.assertEquals("Not activities should be found.", 0, model.getActivities().getActivities().size());
			}
		}, "/api/activity?token=%s", "token");

		// (2.1) Upload pet's activities
		for (int i = 0; i < 5; i++) {
			call(ctx, null, "/api/activity?op=update&uid=%s&hour=%s&food=%s&play=%s&active=%s&rest=%s", //
			      "device", i, i, 2 * i, 3 * i, 4 * i);
		}

		// (2.0) Get pet's activities
		call(ctx, new ModelHandler() {
			@Override
			public void handle(Context ctx, Model model) {
				Assert.assertEquals("Not activities is found.", 5, model.getActivities().getActivities().size());
			}
		}, "/api/activity?token=%s", "token");
	}

	public static class Context {
		private Map<String, Object> m_attributes = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		public <T> T get(String key) {
			return (T) m_attributes.get(key);
		}

		public void set(String key, Object value) {
			m_attributes.put(key, value);
		}
	}

	public interface ModelHandler {
		public void handle(Context ctx, Model model);
	}
}
