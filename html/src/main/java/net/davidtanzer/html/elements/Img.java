package net.davidtanzer.html.elements;

import net.davidtanzer.html.EmptyElement;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.AttributeName;
import net.davidtanzer.html.values.TagName;

public class Img extends EmptyElement implements FlowContentNode {
	public Img(final ImageSrc imageSrc) {
		super(TagName.of("img"));
		setAttribute(AttributeName.of("src"), imageSrc);
	}
}
