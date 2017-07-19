package com.mattiasselin.tools.segment.connection;

import java.io.IOException;

public interface ISegmentedConnection {
	public byte[] readSegment() throws IOException;
	public void writeSegment(byte[] segment) throws IOException;
}
