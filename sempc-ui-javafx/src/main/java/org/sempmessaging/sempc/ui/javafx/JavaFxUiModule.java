package org.sempmessaging.sempc.ui.javafx;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.sempmessaging.sempc.ui.ComponentChangedListener;
import org.sempmessaging.sempc.ui.event.EventHandlerProvider;

import javax.inject.Scope;

public class JavaFxUiModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(ComponentChangedListener.class).to(ConversationsViewComponentChangedListener.class).in(Scopes.SINGLETON);
		bind(EventHandlerProvider.class).to(JavaFxEventHandlerProvider.class).in(Scopes.SINGLETON);
	}
}
