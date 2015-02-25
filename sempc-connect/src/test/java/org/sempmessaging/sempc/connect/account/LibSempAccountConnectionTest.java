package org.sempmessaging.sempc.connect.account;

import com.google.inject.Provider;
import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.libsemp.connection.ServerConnection;
import org.sempmessaging.sempc.core.account.Account;
import org.sempmessaging.sempc.core.account.config.AccountConnectionConfiguration;
import org.sempmessaging.sempc.core.account.value.ConnectionStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LibSempAccountConnectionTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private LibSempAccountConnection accountConnection;
	private ServerConnection serverConnection;

	@Before
	public void setup() {
		accountConnection = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(LibSempAccountConnection.class);

		serverConnection = mock(ServerConnection.class);
		Provider<ServerConnection> provider = mock(Provider.class);
		when(provider.get()).thenReturn(serverConnection);
		accountConnection.setServerConnectionProvider(provider);
	}

	@Test
	public void connectStartsNewServerConnection() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = mock(AccountConnectionConfiguration.class);
		when(config.host()).thenReturn("localhost");
		when(config.port()).thenReturn(1234);

		accountConnection.connect(account, config);

		verify(serverConnection).connect("localhost", 1234);
	}

	@Test
	public void sendsConnectionErrorEventWhenConnectFails() {
		Account account = mock(Account.class);
		AccountConnectionConfiguration config = mock(AccountConnectionConfiguration.class);
		when(config.host()).thenReturn("localhost");
		when(config.port()).thenReturn(1234);

		doAnswer((invocationOnMock) -> { throw new IllegalStateException(); }).when(serverConnection).connect(anyString(), anyInt());
		eventTestRule.subscribeMandatory(accountConnection, accountConnection.connectionStatusChanged(), (s, m) -> assertEquals(ConnectionStatus.ERROR, s));

		accountConnection.connect(account, config);
	}
}