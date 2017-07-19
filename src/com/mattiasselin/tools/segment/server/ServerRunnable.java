package com.mattiasselin.tools.segment.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;


public class ServerRunnable implements Runnable {
	private ServerSocket serverSocket;
	private IConnectionHandler connectionHandler;

	public ServerRunnable(ServerSocket serverSocket, IConnectionHandler connectionHandler) {
		this.serverSocket = serverSocket;
		this.connectionHandler = connectionHandler;
	}

	@Override
	public void run() {
		while(true)
		{
			try {
				System.out.println("Listening...");
				Socket socket = serverSocket.accept();
				System.out.println("Accepted! "+socket.getRemoteSocketAddress()+" at "+new Date(System.currentTimeMillis()));
				new HandlerThread(socket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}
	
	private class HandlerThread extends Thread {
		private Socket socket;

		public HandlerThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try
			{
				try
				{
					connectionHandler.handle(socket);
				}
				finally
				{
					socket.close();
				}
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
