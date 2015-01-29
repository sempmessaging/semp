package org.sempmessaging.sempc.ui.menu;

import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.sempc.ui.components.IconButton;

public abstract class MainMenuButton extends IconButton {
	public MainMenuButton() {
		super(new ImageSrc("res:///img/menu24.png"));
		cssClass(new CssClass("panel-header-component"));
	}
}
