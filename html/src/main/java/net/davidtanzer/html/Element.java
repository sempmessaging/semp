package net.davidtanzer.html;

import net.davidtanzer.html.values.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Element<T extends Element> extends BaseElement implements Node {
	private final List<Node> children = new ArrayList<>();

	protected Element(final TagName tagName) {
		super(tagName);
	}

	protected void add(final Node... nodes) {
		for(Node node : nodes) {
			children.add(node);
		}
	}

	protected void removeAllChildren() {
		children.clear();
	}

	@Override
	public String render() {
		StringBuilder renderedResultBuilder = new StringBuilder();
		render(renderedResultBuilder);
		return renderedResultBuilder.toString();
	}

	@Override
	public void render(final StringBuilder renderedResultBuilder) {
		renderOpenTag(renderedResultBuilder);
		renderChildren(renderedResultBuilder);
		renderCloseTag(renderedResultBuilder);
	}

	private void renderChildren(final StringBuilder renderedResultBuilder) {
		for(Node child : children) {
			child.render(renderedResultBuilder);
		}
	}

	@Override
	public void visit(final NodeVisitor nodeVisitor) {
		for(Node child : children) {
			child.visit(nodeVisitor);
		}
		nodeVisitor.visitNode(this);
	}

	public boolean containsChild(final Node node) {
		return children.contains(node);
	}
}
