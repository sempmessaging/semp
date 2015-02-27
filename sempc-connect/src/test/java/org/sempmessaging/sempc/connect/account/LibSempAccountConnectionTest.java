package org.sempmessaging.sempc.connect.account;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.sempmessaging.libsemp.connection.ServerConnection;
import org.sempmessaging.libsemp.request.serverinfo.GetServerInfoRequest;
import org.sempmessaging.libsemp.request.serverinfo.ServerInformationReceivedEvent;
import org.sempmessaging.libsemp.request.serverinfo.ServerName;
import org.sempmessaging.libsemp.server.ServerInformation;
import org.sempmessaging.sempc.connect.server.ServerInformationCache;
import org.sempmessaging.sempc.core.account.Account;
import org.sempmessaging.sempc.core.account.config.AccountConnectionConfiguration;
import org.sempmessaging.sempc.core.account.config.value.HostName;
import org.sempmessaging.sempc.core.account.config.value.Port;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LibSempAccountConnectionTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private LibSempAccountConnection accountConnection;
	private ServerConnection serverConnection;
	private ServerInformationCache serverInfoCache;
	private GetServerInfoRequest getServerInfoRequest;

	@Before
	public void setup() {
		accountConnection = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(LibSempAccountConnection.class);

		serverConnection = mock(ServerConnection.class);
		Provider<ServerConnection> provider = mock(Provider.class);
		when(provider.get()).thenReturn(serverConnection);
		getServerInfoRequest = mock(GetServerInfoRequest.class);
		when(serverConnection.newRequest(eq(GetServerInfoRequest.class))).thenReturn(getServerInfoRequest);
		accountConnection.setServerConnectionProvider(provider);

		serverInfoCache = mock(ServerInformationCache.class);
		accountConnection.setServerInformationCache(serverInfoCache);
	}

	@Test
	public void connectStartsNewServerConnection() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = createAccountConnectionConfiguration();

		accountConnection.connect(account, config);

		verify(serverConnection).connect("localhost", 1234);
	}

	@Test
	public void sendsConnectionErrorEventWhenConnectFails() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = createAccountConnectionConfiguration();

		doAnswer((invocationOnMock) -> { throw new IllegalStateException(); }).when(serverConnection).connect(anyString(), anyInt());
		eventTestRule.subscribeMandatory(accountConnection, accountConnection.connectionStatusChanged(), (s, m) -> assertEquals(ConnectionStatus.ERROR, s));

		accountConnection.connect(account, config);
	}

	@Test
	public void sendsStatusConnectingWhenInitialConnectionWasEstablished() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = createAccountConnectionConfiguration();

		eventTestRule.subscribeMandatory(accountConnection, accountConnection.connectionStatusChanged(), (s, m) -> assertEquals(ConnectionStatus.CONNECTING, s));

		accountConnection.connect(account, config);
	}

	@Test
	public void getsServerInfoOnReconnectIfSavedServerInfoIsExpired() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = createAccountConnectionConfiguration();

		when(serverInfoCache.getServerInformation()).thenReturn(null);

		accountConnection.connect(account, config);
		verify(getServerInfoRequest).requestKeysForServer(new ServerName("localhost"));
	}

	@Test
	public void updatesServerInfoCacheAfterReceivingServerInformatoin() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = createAccountConnectionConfiguration();

		when(serverInfoCache.getServerInformation()).thenReturn(null);

		accountConnection.connect(account, config);
		ArgumentCaptor<ServerInformationReceivedEvent> receivedEventCaptor = ArgumentCaptor.forClass(ServerInformationReceivedEvent.class);
		verify(getServerInfoRequest).subscribe(any(ServerInformationReceivedEvent.class), receivedEventCaptor.capture());

		ServerInformation serverInformation = mock(ServerInformation.class);
		receivedEventCaptor.getValue().serverInformationReceived(serverInformation);

		verify(serverInfoCache).putServerInformation(serverInformation);
	}

	@Test
	public void doesNotGetServerInfoOnReconnectIfCachedServerInfoAvailable() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = createAccountConnectionConfiguration();

		ServerInformation serverInfo = mock(ServerInformation.class);
		when(serverInfoCache.getServerInformation()).thenReturn(serverInfo);

		accountConnection.connect(account, config);
		verify(getServerInfoRequest, never()).requestKeysForServer(any());
	}

	private AccountConnectionConfiguration createAccountConnectionConfiguration() {
		AccountConnectionConfiguration config = mock(AccountConnectionConfiguration.class);
		when(config.host()).thenReturn(new HostName("localhost"));
		when(config.port()).thenReturn(new Port(1234));
		return config;
	}
}
