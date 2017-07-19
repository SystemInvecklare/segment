package com.mattiasselin.tools.segment.connection;

import java.net.Socket;

import com.mattiasselin.tools.segment.server.IConnectionHandler;

public class SegmentedConnectionHandlerAdapter implements IConnectionHandler {
	private ISegmentedConnectionHandler segmentedConnectionHandler;
	
	public SegmentedConnectionHandlerAdapter(ISegmentedConnectionHandler segmentedConnectionHandler) {
		this.segmentedConnectionHandler = segmentedConnectionHandler;
	}



	@Override
	public void handle(Socket socket) throws Exception {
		segmentedConnectionHandler.handle(new SegmentedConnection(socket));
	}
}
