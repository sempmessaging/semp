package net.davidtanzer.html;

public interface Node {
	String render();
	void render(StringBuilder renderedResultBuilder);
}
