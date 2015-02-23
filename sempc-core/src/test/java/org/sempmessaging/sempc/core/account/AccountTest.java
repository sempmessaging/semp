package org.sempmessaging.sempc.core.account;

import net.davidtanzer.jevents.EventComponents;
import net.davidtanzer.jevents.cg.JavassistComponentCodeGenerator;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.sempmessaging.sempc.core.account.config.AccountConfiguration;
import org.sempmessaging.sempc.core.account.config.AccountConnectionConfiguration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountTest {
	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private Account account;
	private AccountConnection accountConnection;

	@Before
	public void setup() {
		account = new EventComponents(new JavassistComponentCodeGenerator()).createComponent(Account.class);

		accountConnection = mock(AccountConnection.class);
		account.setAccountConnection(accountConnection);
	}

	@Test
	public void connectsAccountConnectionToAccountWithGivenConfiguration() {
		AccountConfiguration configuration = mock(AccountConfiguration.class);
		AccountConnectionConfiguration connectionConfig = mock(AccountConnectionConfiguration.class);
		when(configuration.connectionConfiguration()).thenReturn(connectionConfig);

		account.connect(configuration);

		verify(accountConnection).connect(account, configuration.connectionConfiguration());
	}
}