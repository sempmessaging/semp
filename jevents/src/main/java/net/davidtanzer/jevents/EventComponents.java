package net.davidtanzer.jevents;

import java.lang.reflect.Method;

public class EventComponents {
	private ComponentCodeGenerator codeGenerator;

	public EventComponents(final ComponentCodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}

	public <T extends EventComponent> T createComponent(final Class<T> componentClass) {
		return codeGenerator.createEventComponentImplementation(componentClass, this::handleEventMethod);
	}

	private Object handleEventMethod(final Object self, final Method thisMethod) {
		assert self instanceof EventComponent : "Because event component implementations are only created for EventComponents";

		return ((EventComponent) self).getEvent(thisMethod.getName(), thisMethod.getReturnType());
	}
}
