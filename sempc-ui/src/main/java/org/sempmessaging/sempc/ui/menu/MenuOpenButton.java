package org.sempmessaging.sempc.ui.menu;

import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.sempc.ui.HtmlComponent;

public class MenuOpenButton extends HtmlComponent {
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
	}
}
