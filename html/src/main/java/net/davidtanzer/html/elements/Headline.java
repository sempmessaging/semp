package net.davidtanzer.html.elements;

import net.davidtanzer.html.Element;
import net.davidtanzer.html.values.TagName;

public class Headline extends Element implements FlowContentNode, HeadingContentNode {
	public static enum HeadlineLevel {
		H1("h1"), H2("h2"), H3("h3"), H4("h4"), H5("h5"), H6("h6");
		private final String tagName;

		HeadlineLevel(final String tagName) {
			this.tagName = tagName;
		}

		@Override
		public String toString() {
			return tagName;
		}
	}

	public Headline(final HeadlineLevel headlineLevel) {
		super(TagName.of(headlineLevel.toString()));
	}

	public void add(final PhrasingContentNode... nodes) {
		super.add(nodes);
	}

	@Override
	public void removeAllChildren() {
		super.removeAllChildren();
	}
}
