package org.sempmessaging.sempc.sidehatch;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;
import org.sempmessaging.sempc.SideHatch;
import org.sempmessaging.sempc.ui.MainHtmlPage;

import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.servlet;

public class HttpSideHatch implements SideHatch {

	public static final String CONTEXT_PATH = "/sidehatch";
	static MainHtmlPage mainHtmlPage;
	private Undertow server;

	@Override
	public void start(final MainHtmlPage mainHtmlPage) {
		startUndertow(servlet("SideHatchServlet", SideHatchServlet.class)
				.addMapping("/*"));

		this.mainHtmlPage = mainHtmlPage;
	}

	@Override
	public void shutdown() {
		server.stop();
	}

	private void startUndertow(final ServletInfo servlet) {
		try {
			DeploymentInfo servletBuilder = deployment()
					.setClassLoader(HttpSideHatch.class.getClassLoader())
					.setContextPath(CONTEXT_PATH)
					.setDeploymentName("test.war")
					.addServlets(servlet);

			DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
			manager.deploy();

			HttpHandler servletHandler = manager.start();
			PathHandler path = Handlers.path(Handlers.redirect(CONTEXT_PATH))
					.addPrefixPath(CONTEXT_PATH, servletHandler);
			server = Undertow.builder()
					.addHttpListener(8080, "localhost")
					.setHandler(path)
					.build();
			server.start();
		} catch (ServletException e) {
			throw new RuntimeException(e);
		}
	}
}
