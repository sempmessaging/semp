package net.davidtanzer.html;

import net.davidtanzer.html.values.AttributeValue;

public interface AttributeProcessor<T extends AttributeValue, R> {
	public R process(T attributeValue);
}
