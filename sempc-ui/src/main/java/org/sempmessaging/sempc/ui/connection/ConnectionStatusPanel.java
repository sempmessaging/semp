package org.sempmessaging.sempc.ui.connection;

import com.google.inject.Inject;
import net.davidtanzer.html.elements.FlowContentNode;
import net.davidtanzer.html.elements.Img;
import net.davidtanzer.html.elements.values.ImageSrc;
import net.davidtanzer.html.values.CssClass;
import org.sempmessaging.libsemp.arguments.Args;
import org.sempmessaging.sempc.core.account.ConnectionStatus;
import org.sempmessaging.sempc.ui.HtmlComponent;
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

	@Inject
	public ConnectionStatusPanel(final ConnectionStatusViewModel viewModel) {
		Args.notNull(viewModel, "viewModel");

		this.viewModel = viewModel;
		this.viewModel.subscribe(this.viewModel.propertyChangedEvent(), this::viewModelPropertyChanged);

		cssClass(new CssClass("connection-status-panel"));
	}

	@Override
	protected FlowContentNode[] getInnerHtml() {
		Img connectionImage = new Img(connectionStatusImages.get(viewModel.overallConnectionStatus.get()));
		connectionImage.cssClass(new CssClass("connection-status-icon"));
		return new FlowContentNode[] { connectionImage };
	}

	private void viewModelPropertyChanged(final Property<?> property) {
		componentChanged();
	}
}
