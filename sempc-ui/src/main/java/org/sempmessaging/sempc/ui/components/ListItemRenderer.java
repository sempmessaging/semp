package org.sempmessaging.sempc.ui.components;

import org.sempmessaging.sempc.ui.HtmlComponent;

public interface ListItemRenderer<T> {
	public HtmlComponent render(T listItemContent);
}
