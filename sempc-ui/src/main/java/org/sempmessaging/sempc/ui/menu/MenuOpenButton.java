package org.sempmessaging.sempc.ui.menu;

import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.Event;
import org.sempmessaging.sempc.ui.ButtonClickedEvent;
import org.sempmessaging.sempc.ui.HtmlComponent;

public abstract class MenuOpenButton extends HtmlComponent {
	@Event
	public abstract ButtonClickedEvent buttonClickedEvent();

	private Img buttonImage;

	public MenuOpenButton() {
		cssClass(new CssClass("panel-header-component"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		return new FlowContentNode[] { buttonImage };
	}

	@Override
	protected void initializeComponent() {
		buttonImage = new Img(new ImageSrc("res:///img/menu24.png"));
		buttonImage.cssClasses(new CssClass("icon-button"));

		buttonImage.events().onClick(eventHandler(() -> send(buttonClickedEvent()).buttonClicked()));
	}
}
