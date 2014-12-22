package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;

public class Html extends Element {
	private final Head head;
	private final Body body;

	public Html() {
		super(TagName.of("html"));

		head = new Head();
		body = new Body();

		add(head);
		add(body);
	}

	public Head head() {
		return head;
	}

	public Body body() {
		return body;
	}
}
