package com.mattiasselin.tools.segment.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.mattiasselin.tools.segment.connection.ISegmentedConnectionHandler;
import com.mattiasselin.tools.segment.connection.SegmentedConnection;

public class ClientUtil {
	
	public static void launchClient(String host, int port, ISegmentedConnectionHandler connectionHandler) throws IOException {
		launchClient(host, port, connectionHandler, 0, 0);
	}
	
	public static void launchClient(String host, int port, ISegmentedConnectionHandler connectionHandler, int connectionTimeout, int readTimeout) throws IOException
	{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(host, port), connectionTimeout);
		socket.setSoTimeout(readTimeout);
		try
		{
			connectionHandler.handle(new SegmentedConnection(socket));
		}
		finally
		{
			socket.close();
		}
	}
}
