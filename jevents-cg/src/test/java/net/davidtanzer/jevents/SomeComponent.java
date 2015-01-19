package net.davidtanzer.jevents;

public abstract class SomeComponent extends EventComponent {
	@Event
	public abstract DoSomething doSomething();

	public void componentFunctionality() {
		doSomething().doIt();
	}
}
