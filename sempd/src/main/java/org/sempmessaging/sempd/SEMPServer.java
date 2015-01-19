package org.sempmessaging.sempd;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.sempmessaging.libsemp.LibSempModule;
import org.sempmessaging.sempd.configuration.ServerConfiguration;
import org.sempmessaging.sempd.protocol.SempdProtocolModule;
import org.sempmessaging.sempd.protocol.async.AsyncExecutor;
import org.sempmessaging.sempd.protocol.io.TcpSocketServer;
import org.sempmessaging.libsemp.roundrobin.RoundRobin;
import org.sempmessaging.libsemp.roundrobin.RoundRobinThread;

public class SEMPServer {
	private final ServerConfiguration serverConfiguration;
	private final RoundRobin roundRobin;
	private final TcpSocketServer tcpSocketServer;
	private final RoundRobinThread roundRobinThread;
	private final AsyncExecutor asyncExecutor;

	public SEMPServer(final ServerConfiguration serverConfiguration) {
		this(serverConfiguration, Guice.createInjector(new LibSempModule(), new SempdProtocolModule(), new SempServerModule()));
	}

	public SEMPServer(final ServerConfiguration serverConfiguration, final Injector injector) {
		this.serverConfiguration = serverConfiguration;

		roundRobin = injector.getInstance(RoundRobin.class);
		roundRobinThread = injector.getInstance(RoundRobinThread.class);
		tcpSocketServer = injector.getInstance(TcpSocketServer.class);
		asyncExecutor = injector.getInstance(AsyncExecutor.class);

		tcpSocketServer.listenOnPort(serverConfiguration.port());
	}

	public void start() {
		roundRobin.register(tcpSocketServer);
		tcpSocketServer.start();
		roundRobinThread.start();
		asyncExecutor.start();
	}

	public void shutdown() {
		roundRobinThread.shutdown();
		tcpSocketServer.shutdown();
		asyncExecutor.shutdown();
	}
}
