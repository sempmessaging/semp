package org.sempmessaging.sempc.ui;

public class ConversationsPanel extends HtmlComponent {
	private String content = "Conversations";

	public ConversationsPanel() {
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
					content+="! <strong>Conversations!</strong>";
					componentChanged();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public String getInnerHtml() {
		return content;
	}
}