package net.davidtanzer.html;

public interface Node {
	default String render() {
		StringBuilder renderedResultBuilder = new StringBuilder();
		render(renderedResultBuilder);
		return renderedResultBuilder.toString();
	}

	void render(StringBuilder renderedResultBuilder);
}
