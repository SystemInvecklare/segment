package com.mattiasselin.tools.segment.server;

import java.net.Socket;

public interface IConnectionHandler {
	public void handle(Socket socket) throws Exception;
}
