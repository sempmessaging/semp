package org.sempmessaging.libsemp.itest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.davidtanzer.jevents.testing.EventTestRule;
import org.junit.*;
import org.sempmessaging.libsemp.LibSempModule;
import org.sempmessaging.libsemp.connection.ServerConnection;
import org.sempmessaging.libsemp.key.*;
import org.sempmessaging.libsemp.request.serverinfo.GetServerInfoRequest;
import org.sempmessaging.libsemp.request.serverinfo.ServerName;
import org.sempmessaging.libsemp.roundrobin.RoundRobinThread;
import org.sempmessaging.sempd.SEMPServer;
import org.sempmessaging.sempd.configuration.ServerConfiguration;
import org.sempmessaging.sempd.core.serverkeys.PublicVerificationKeys;
import org.sempmessaging.sempd.protocol.SempdProtocolModule;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetServerInfoTest {
	public static final int SERVER_TEST_PORT = 19930;

	@Rule
	public EventTestRule eventTestRule = new EventTestRule();

	private SEMPServer server;
	private LibSempItestModule itestModule;
	private ServerConnection serverConnection;
	private Injector injector;
	private RoundRobinThread roundRobinThread;

	@Before
	public void setup() {
		ServerConfiguration serverConfiguration = mock(ServerConfiguration.class);
		when(serverConfiguration.port()).thenReturn(SERVER_TEST_PORT);
		when(serverConfiguration.domains()).thenReturn(Arrays.asList("example.com"));

		itestModule = new LibSempItestModule();
		server = new SEMPServer(serverConfiguration, Guice.createInjector(new LibSempModule(), new SempdProtocolModule(), itestModule));
		server.start();

		injector = Guice.createInjector(new LibSempModule(), itestModule);
		roundRobinThread = injector.getInstance(RoundRobinThread.class);
		roundRobinThread.start();

		serverConnection = injector.getInstance(ServerConnection.class);
		serverConnection.connect("localhost", SERVER_TEST_PORT);
	}

	@After
	public void teardown() {
		try {
			roundRobinThread.shutdown();
			serverConnection.disconnect();
		} finally {
			server.shutdown();
		}
	}

	@Test
	public void getServerPublicVerificationKeysForOwnServerReturnsAnObject() throws InterruptedException {
		PublicVerificationKeys publicVerificationKeys = mock(PublicVerificationKeys.class);
		doAnswer((invocation) -> {
			((Consumer<PublicVerificationKey>) invocation.getArguments()[0]).accept(createKeyData());
			return null;
		}).when(publicVerificationKeys).forEach(any());
		when(itestModule.serverPublicVerificationKeysService().listPublicVerificationKeys())
				.thenReturn(publicVerificationKeys);

		GetServerInfoRequest request = serverConnection.newRequest(GetServerInfoRequest.class);
		request.requestKeysForServer(new ServerName("example.com"));

		eventTestRule.subscribeMandatory(request, request.serverInformationReceivedEvent(), (keys) -> {
			assertNotNull(keys);
		});
		request.start();

		eventTestRule.waitForEventsUntil(1000L);
	}

	private PublicVerificationKey createKeyData() {
		return new PublicVerificationKey(new KeyId("theKeyId"), new PublicKeyData("publicKeyData"), new ObsoleteByKeyId("obsoleteByKeyId"),
				new PreviousKeyId("previousKeyId"), new Signature("signature"));
	}

}
