package org.sempmessaging.sempc.ui;

import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;

public class ConnectionStatusPanel extends HtmlComponent {
	public ConnectionStatusPanel() {
		cssClass(new CssClass("connection-status-panel"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		Img connectionImage = new Img(new ImageSrc("res:///img/plug42.png"));
		connectionImage.cssClass(new CssClass("connection-status-icon"));
		return new FlowContentNode[] { connectionImage };
	}
}
