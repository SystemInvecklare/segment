package com.mattiasselin.tools.segment.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SegmentedConnection implements ISegmentedConnection {
	private Socket socket;
	
	public SegmentedConnection(Socket socket) {
		this.socket = socket;
	}

	@Override
	public byte[] readSegment() throws IOException {
		return SegmentedConnectionUtil.readSegment(socket.getInputStream());
	}

	@Override
	public void writeSegment(byte[] segment) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		SegmentedConnectionUtil.writeSegment(outputStream, segment);
		outputStream.flush();
	}

}
