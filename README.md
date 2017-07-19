# segment
Basic networking in Java - made easy!

The java.net.#insert various useful classes here# has a lot of good basic functionality. The problem is that is it too basic! **Segment** lets you send an array of bytes and recieve them in their entierty. 

## A simple server-client Hello World example
The following sets up a server listening on port 8080. The client program attempts to connect to localhost:8080.

This is your server program:
```Java
package server.example;

import java.io.IOException;
import java.nio.charset.Charset;

import com.mattiasselin.tools.segment.connection.ISegmentedConnection;
import com.mattiasselin.tools.segment.connection.ISegmentedConnectionHandler;
import com.mattiasselin.tools.segment.server.ServerUtil;

public class ExampleServer {
	public static final Charset UTF_8 = Charset.forName("utf-8");
	public static void main(String[] args) throws IOException {
		
		ServerUtil.launchServer(8080, new ISegmentedConnectionHandler() {
			@Override
			public void handle(ISegmentedConnection segmentedConnection) throws IOException {
				segmentedConnection.writeSegment("Welcome! What is your name?".getBytes(UTF_8));
				String clientName = new String(segmentedConnection.readSegment(), UTF_8);
				segmentedConnection.writeSegment(("Nice to meet you "+clientName).getBytes(UTF_8));
			}
		});
	}
}
```
And this is your client program:
```Java
package server.example;

import java.io.IOException;
import java.nio.charset.Charset;

import com.mattiasselin.tools.segment.connection.ISegmentedConnection;
import com.mattiasselin.tools.segment.connection.ISegmentedConnectionHandler;
import com.mattiasselin.tools.segment.server.ClientUtil;

public class ExampleClient {
	public static final Charset UTF_8 = Charset.forName("utf-8");
	public static void main(String[] args) throws IOException {
		ClientUtil.launchClient("localhost", 8080, new ISegmentedConnectionHandler() {
			@Override
			public void handle(ISegmentedConnection con) throws IOException {
				String serverGreeting = new String(con.readSegment(), UTF_8);
				System.out.println(serverGreeting);
				
				//Send name
				con.writeSegment("John Doe".getBytes(UTF_8));
				
				String serverResponse = new String(con.readSegment(), UTF_8);
				System.out.println(serverResponse);
			}
		});
	}
}
```
