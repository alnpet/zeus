package com.alnpet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.servlet.GzipFilter;
import org.unidal.test.jetty.JettyServer;

@RunWith(JUnit4.class)
public class TestServer extends JettyServer {
	public static void main(String[] args) throws Exception {
		TestServer server = new TestServer();

		server.startServer();
		server.startWebapp();
		server.stopServer();
	}

	@Before
	public void before() throws Exception {
		System.setProperty("devMode", "true");
		super.startServer();
	}

	@Override
	protected String getContextPath() {
		return "/alnpet";
	}

	@Override
	protected int getServerPort() {
		return 2567;
	}

	@Override
	protected void postConfigure(WebAppContext context) {
		context.addFilter(GzipFilter.class, "/api/*", Handler.ALL);
	}

	@Test
	public void startWebapp() throws Exception {
		// open the page in the default browser
		// display("/alnpet/api/pet?op=register&name=name&category=category&device=d101");
		// display("/alnpet/api/pet?op=update&token=fefa2021-f0f1-4215-aebe-db52118daafc&nickname=nickname&email=abc@example.com");
		display("/alnpet/api/activity/day");
		waitForAnyKey();
	}
}
