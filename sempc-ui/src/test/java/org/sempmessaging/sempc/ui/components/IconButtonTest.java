package org.sempmessaging.sempc.ui.components;

import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.sempc.ui.JsEventTest;
import org.sempmessaging.sempc.ui.menu.MainMenuButton;

import static net.davidtanzer.html.query.HtmlQueries.select;
import static org.junit.Assert.assertEquals;

public class IconButtonTest extends JsEventTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	@Test
	public void sendsEventWhenButtonIsClicked() {
		IconButton iconButton = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(TestButton.class);
		iconButton.setEventHandlerProvider(eventHandlerProvider());

		eventTestRule.subscribeMandatory(iconButton, iconButton.buttonClickedEvent(), () -> {});

		Img buttonImage = (Img) select(new CssClass("icon-button")).singleElement().from(iconButton.getHtml());
		click(buttonImage);
	}

	@Test
	public void showsSpecifiedIcon() {
		IconButton iconButton = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(TestButton.class);
		iconButton.setEventHandlerProvider(eventHandlerProvider());

		ImageSrc src = (ImageSrc) select(new CssClass("icon-button")).singleElement().selectAttribute(AttributeName.of("src")).from(iconButton.getHtml());

		assertEquals(new ImageSrc("res:///test.png"), src);
	}

	public static abstract class TestButton extends IconButton {
		public TestButton() {
			super(new ImageSrc("res:///test.png"));
		}
	}
}