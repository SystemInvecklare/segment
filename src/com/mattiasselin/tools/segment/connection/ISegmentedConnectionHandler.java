package com.mattiasselin.tools.segment.connection;

import java.io.IOException;

public interface ISegmentedConnectionHandler {
	public void handle(ISegmentedConnection segmentedConnection) throws IOException;
}
