package org.sempmessaging.sempc.ui.menu;

import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.sempc.ui.JsEventTest;

import static net.davidtanzer.html.query.HtmlQueries.select;

public class MainMenuButtonTest extends JsEventTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	@Test
	public void sendsEventWhenButtonIsClicked() {
		MainMenuButton mainMenuButton = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(MainMenuButton.class);
		mainMenuButton.setEventHandlerProvider(eventHandlerProvider());

		eventTestRule.subscribeMandatory(mainMenuButton, mainMenuButton.buttonClickedEvent(), () -> {});

		Img buttonImage = (Img) select(new CssClass("icon-button")).singleElement().from(mainMenuButton.getHtml());
		click(buttonImage);
	}
}