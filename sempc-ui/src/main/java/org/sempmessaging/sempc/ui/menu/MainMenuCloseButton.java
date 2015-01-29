package org.sempmessaging.sempc.ui.menu;

import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.sempc.ui.components.IconButton;

public abstract class MainMenuCloseButton extends IconButton {
	public MainMenuCloseButton() {
		super(new ImageSrc("res:///img/arrowhead7.png"));
		cssClass(new CssClass("panel-header-component"));
	}
}
