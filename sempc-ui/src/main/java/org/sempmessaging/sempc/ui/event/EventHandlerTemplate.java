package org.sempmessaging.sempc.ui.event;

import net.davidtanzer.html.values.EventHandlerScript;

public class EventHandlerTemplate {
	private String pattern;

	public EventHandlerTemplate(final String pattern) {
		this.pattern = pattern;
	}

	public EventHandlerScript eventHandlerScript(final String... params) {
		StringBuilder paramsStringBuilder = new StringBuilder();
		for(String param : params) {
			paramsStringBuilder.append(", ");
			renderParameterToString(paramsStringBuilder, param);
		}
		String script = String.format(pattern, paramsStringBuilder.toString());
		return new EventHandlerScript(script);
	}

	protected void renderParameterToString(final StringBuilder paramsStringBuilder, final String param) {
		paramsStringBuilder.append("\\\"").append(param).append("\\\"");
	}
}
