package org.sempmessaging.sempc.ui.connection;

import com.google.inject.Inject;
import net.davidtanzer.html.TextNode;
import net.davidtanzer.html.elements.Div;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import net.davidtanzer.html.values.CssStyle;
import net.davidtanzer.html.values.EventHandlerScript;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.AccountStatus;
import org.sempmessaging.sempc.core.account.ConnectionStatus;
import org.sempmessaging.sempc.ui.HtmlComponent;
import org.sempmessaging.sempc.ui.event.EventHandler;
import org.sempmessaging.sempc.ui.viewmodel.Property;

import java.util.HashMap;
import java.util.Map;

public class ConnectionStatusPanel extends HtmlComponent {
	private ConnectionStatusViewModel viewModel;
	private Map<ConnectionStatus, ImageSrc> connectionStatusImages = new HashMap<ConnectionStatus, ImageSrc>() {{
		put(ConnectionStatus.UNKNOWN, new ImageSrc("res:///img/plug42.png"));
		put(ConnectionStatus.CONNECTING, new ImageSrc("res:///img/plug42_orange.png"));
		put(ConnectionStatus.ERROR, new ImageSrc("res:///img/plug42_red.png"));
		put(ConnectionStatus.CONNECTED, new ImageSrc("res:///img/plug42_green.png"));
	}};

	private Img connectionImage;
	private Div connectionDetails;

	@Inject
	public ConnectionStatusPanel(final ConnectionStatusViewModel viewModel) {
		Args.notNull(viewModel, "viewModel");

		this.viewModel = viewModel;
		this.viewModel.subscribe(this.viewModel.propertyChangedEvent(), this::viewModelPropertyChanged);

		cssClass(new CssClass("connection-status-panel"));
	}

	@Override
	protected void initializeComponent() {
		connectionImage = new Img(connectionStatusImages.get(viewModel.overallConnectionStatus.get()));
		connectionImage.cssClass(new CssClass("connection-status-icon"));
		connectionImage.events().onMouseOver(eventHandler(this::showConnectionDetails));
		connectionImage.events().onMouseOut(eventHandler(this::hideConnectionDetails));

		connectionDetails = new Div();
		connectionDetails.style(new CssStyle("display: none;"));
		connectionDetails.cssClass(new CssClass("connection-status-details"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		connectionImage.src(connectionStatusImages.get(viewModel.overallConnectionStatus.get()));
		return new FlowContentNode[] { connectionImage, connectionDetails };
	}

	private void viewModelPropertyChanged(final Property<?> property) {
		if(property == viewModel.accountStatuses) {
			reinitializeConnectionDetailsDiv();
		}
		componentChanged();
	}

	private void reinitializeConnectionDetailsDiv() {
		connectionDetails.removeAllChildren();

		for(AccountStatus accountStatus : viewModel.accountStatuses.get()) {
			Div accountStatusDiv = new Div();

			Img statusImage = new Img(connectionStatusImages.get(accountStatus.connectionStatus()));
			statusImage.cssClass(new CssClass("connection-status-icon"));
			accountStatusDiv.add(statusImage);

			accountStatusDiv.add(new TextNode(" "));
			TextNode statusText = new TextNode(accountStatus.accountName());
			accountStatusDiv.add(statusText);

			connectionDetails.add(accountStatusDiv);
		}
	}

	private void showConnectionDetails() {
		connectionDetails.style(new CssStyle("display: block;"));
		componentChanged();
	}

	private void hideConnectionDetails() {
		connectionDetails.style(new CssStyle("display: none;"));
		componentChanged();
	}
}
