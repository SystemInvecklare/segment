package com.mattiasselin.tools.segment.server;

import java.io.IOException;
import java.net.ServerSocket;

import com.mattiasselin.tools.segment.connection.ISegmentedConnectionHandler;
import com.mattiasselin.tools.segment.connection.SegmentedConnectionHandlerAdapter;

public class ServerUtil {
	public static void launchServer(int port, ISegmentedConnectionHandler segmentedConnectionHandler) throws IOException
	{
		new Thread(new ServerRunnable(new ServerSocket(port), new SegmentedConnectionHandlerAdapter(segmentedConnectionHandler))).start();
	}
	
	public static Runnable getServerRunnable(int port, ISegmentedConnectionHandler segmentedConnectionHandler) throws IOException
	{
		return new ServerRunnable(new ServerSocket(port), new SegmentedConnectionHandlerAdapter(segmentedConnectionHandler));
	}
}
