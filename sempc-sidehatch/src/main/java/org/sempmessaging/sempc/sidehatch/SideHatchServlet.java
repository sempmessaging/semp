package org.sempmessaging.sempc.sidehatch;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SideHatchServlet extends HttpServlet {
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getPathInfo();
		if(path == null) {
			path = "/";
		}
		if(path.startsWith("/res/")) {
			renderResource(path.substring("/res".length()), resp);
		} else {
			renderMainPage(resp);
		}
		resp.getOutputStream().close();
	}

	private void renderMainPage(final HttpServletResponse resp) throws IOException {
		String renderedPage = HttpSideHatch.mainHtmlPage.getHtml().render();
		renderedPage = replaceResUrls(renderedPage);
		resp.getOutputStream().print(renderedPage);
	}

	private String replaceResUrls(String str) {
		return str.replaceAll("res:///", "/sidehatch/res/");
	}

	private void renderResource(final String path, final HttpServletResponse resp) throws IOException {
		if(path.endsWith(".css") || path.endsWith(".js")) {
			sendStringResource(path, resp);
		} else {
			sendBinaryResource(path, resp);
		}
	}

	private void sendBinaryResource(final String path, final HttpServletResponse resp) throws IOException {
		InputStream inputStream = getClass().getResourceAsStream(path);
		byte[] buffer = new byte[1024];

		try {
			int bytesRead = inputStream.read(buffer);
			while (bytesRead >= 0) {
				resp.getOutputStream().write(buffer, 0, bytesRead);
				bytesRead = inputStream.read(buffer);
			}
		} finally {
			inputStream.close();
		}
	}

	private void sendStringResource(final String path, final HttpServletResponse resp) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
		try {
			String line = reader.readLine();
			while (line != null) {
				line = replaceResUrls(line);
				resp.getOutputStream().println(line);

				line = reader.readLine();
			}
		} finally {
			reader.close();
		}
	}
}
