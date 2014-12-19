package org.sempmessaging.sempc.ui;

import com.google.inject.Inject;
import org.sempmessaging.libsemp.arguments.Args;

public abstract class HtmlComponent {
	private static int nextComponentId =0;
	private final String id = nextId();

	private ComponentChangedListener componentChangedListener;

	private static synchronized String nextId() {
		int componentId = nextComponentId;
		nextComponentId++;
		return "html_component_id_"+componentId;
	}

	public String getHtml() {
		return "<div id=\"" + id + "\">" + getInnerHtml() + "</div>";
	}

	public abstract String getInnerHtml();

	protected void componentChanged() {
		if(componentChangedListener != null) {
			componentChangedListener.htmlComponentChanged(id, getInnerHtml());
		}
	}

	@Inject
	public void setComponentChangedListener(final ComponentChangedListener componentChangedListener) {
		Args.notNull(componentChangedListener, "componentChangedListener");
		Args.setOnce(this.componentChangedListener, "componentChangedListener");

		this.componentChangedListener = componentChangedListener;
	}
}
