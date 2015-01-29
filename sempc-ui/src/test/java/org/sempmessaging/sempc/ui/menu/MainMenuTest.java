package org.sempmessaging.sempc.ui.menu;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.sempc.ui.components.ButtonClickedEvent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MainMenuTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private MainMenu mainMenu;
	private MainMenuButton mainMenuButton;

	@Before
	public void setup() {
		mainMenu = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(MainMenu.class);
		mainMenuButton = mock(MainMenuButton.class);
		mainMenu.setMainMenuButton(mainMenuButton);
	}

	@Test
	public void sendsShowMenuEventWhenMainMenuButtonIsClicked() {
		ArgumentCaptor<ButtonClickedEvent> eventCaptor = ArgumentCaptor.forClass(ButtonClickedEvent.class);
		verify(mainMenuButton).subscribe(any(), eventCaptor.capture());

		eventTestRule.subscribeMandatory(mainMenu, mainMenu.showMainMenuEvent(), (show) -> assertTrue(show));
		eventCaptor.getValue().buttonClicked();
	}
}