package org.sempmessaging.sempc.ui.components;

import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.Event;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.ui.HtmlComponent;

public abstract class IconButton extends HtmlComponent {
	private ImageSrc imageSrc;

	@Event
	public abstract ButtonClickedEvent buttonClickedEvent();

	private Img buttonImage;

	protected IconButton(final ImageSrc imageSrc) {
		Args.notNull(imageSrc, "imageSrc");
		this.imageSrc = imageSrc;
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		return new FlowContentNode[] { buttonImage };
	}

	@Override
	protected void initializeComponent() {
		buttonImage = new Img(imageSrc);
		buttonImage.cssClasses(new CssClass("icon-button"));

		buttonImage.events().onClick(eventHandler((args) -> send(buttonClickedEvent()).buttonClicked()));
	}
}
