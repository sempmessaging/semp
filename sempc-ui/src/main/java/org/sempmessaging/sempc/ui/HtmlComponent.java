package org.sempmessaging.sempc.ui;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.EventHandlerScript;
import net.davidtanzer.html.values.Id;
import net.davidtanzer.jevents.EventComponent;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.event.EventHandler;
import org.sempmessaging.sempc.ui.event.EventHandlerProvider;
import org.sempmessaging.sempc.ui.event.EventHandlerTemplate;

import java.util.ArrayList;
import java.util.List;

public abstract class HtmlComponent extends EventComponent {
	private static int nextComponentId =0;
	private final String id = nextId();

	private ComponentChangedListener componentChangedListener;
	private List<CssClass> cssClasses = new ArrayList<>();
	private EventHandlerProvider eventHandlerProvider;
	private boolean isInitialized = false;

	private static synchronized String nextId() {
		int componentId = nextComponentId;
		nextComponentId++;
		return "html_component_id_"+componentId;
	}

	public FlowContentNode getHtml() {
		if(!isInitialized) {
			initializeComponent();
			isInitialized = true;
		}

		Div container = new Div();
		container.id(new Id(id));
		if(!cssClasses.isEmpty()) {
			container.cssClasses(cssClasses.toArray(new CssClass[cssClasses.size()]));
		}
		container.add(getInnerHtml());

		return container;
	}

	protected void initializeComponent() {
	}

	protected abstract FlowContentNode[] getInnerHtml();

	protected void componentChanged() {
		if(componentChangedListener != null) {
			componentChangedListener.htmlComponentChanged(id, getInnerHtml());
		}
	}

	protected void cssClass(final CssClass cssClass) {
		Args.notNull(cssClass, "cssClass");

		this.cssClasses.add(cssClass);
	}

	@Inject
	public void setComponentChangedListener(final ComponentChangedListener componentChangedListener) {
		Args.notNull(componentChangedListener, "componentChangedListener");
		Args.setOnce(this.componentChangedListener, "componentChangedListener");

		this.componentChangedListener = componentChangedListener;
	}

	@Inject
	public void setEventHandlerProvider(final EventHandlerProvider eventHandlerProvider) {
		Args.notNull(eventHandlerProvider, "eventHandlerProvider");
		Args.setOnce(this.eventHandlerProvider, "eventHandlerProvider");

		this.eventHandlerProvider = eventHandlerProvider;
	}

	protected EventHandlerScript eventHandler(final EventHandler eventHandler) {
		return eventHandlerProvider.provideEventHandler(eventHandler);
	}

	protected EventHandlerTemplate eventHandlerTemplate(final EventHandler eventHandler) {
		return eventHandlerProvider.provideEventHandlerTemplate(eventHandler);
	}
}
