package org.sempmessaging.sempc.ui.components;

import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Li;
import net.davidtanzer.html.elements.Ul;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.Event;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.event.EventHandlerTemplate;
import org.sempmessaging.sempc.ui.viewmodel.Property;

import java.util.ArrayList;

public abstract class List<T> extends HtmlComponent {
	private Ul list;
	private Property<? extends Iterable<T>> listContent;
	private ListItemRenderer<T> listItemRenderer = new DefaultListItemRenderer();
	private EventHandlerTemplate eventTemplate;

	private final ArrayList<T> listContentItems = new ArrayList<>();
	private final ArrayList<Li> listItems = new ArrayList<>();
	private Li selectedItem;

	@Event
	public abstract ItemSelectedEvent<T> itemSelectedEvent();

	public void setListContent(final Property<? extends Iterable<T>> listContent) {
		Args.notNull(listContent, "listContent");
		this.listContent = listContent;
	}

	public void setListItemRenderer(final ListItemRenderer<T> listItemRenderer) {
		Args.notNull(listItemRenderer, "listItemRenderer");
		this.listItemRenderer = listItemRenderer;
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		reInitializeList();
		return new FlowContentNode[] { list };
	}

	private void reInitializeList() {
		listContentItems.clear();
		listItems.clear();
		selectedItem = null;

		for(T contentItem : listContent.get()) {
			listContentItems.add(contentItem);

			Li li = new Li();
			listItems.add(li);

			li.add(listItemRenderer.render(contentItem).getHtml());
			li.events().onClick(eventTemplate.eventHandlerScript(Integer.toString(listContentItems.size()-1)));

			list.add(li);
		}
	}

	@Override
	protected void initializeComponent() {
		eventTemplate = eventHandlerTemplate(this::listItemSelected);
		list = new Ul();
	}

	private void listItemSelected(String... params) {
		assert params != null : "Expected a params array, but got null.";
		assert params.length == 1 : "Expected exactly one parameter, was: "+params.length;

		int clickedItemPosition = Integer.parseInt(params[0]);

		if(selectedItem != null) {
			selectedItem.removeCssClass(new CssClass("list-item-selected"));
		}
		selectedItem = listItems.get(clickedItemPosition);
		selectedItem.cssClasses(new CssClass("list-item-selected"));
		componentChanged();

		T clickedItem = listContentItems.get(clickedItemPosition);
		send(itemSelectedEvent()).itemSelected(clickedItem);
	}

	private class DefaultListItemRenderer implements ListItemRenderer<T> {
		@Override
		public HtmlComponent render(final T listItemContent) {
			return new HtmlComponent() {
				@Override
				protected FlowContentNode[] getInnerHtml() {
					return new FlowContentNode[] { new TextNode(String.valueOf(listItemContent)) };
				}
			};
		}
	}
}
