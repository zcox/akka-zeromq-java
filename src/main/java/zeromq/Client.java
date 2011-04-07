package zeromq;

import org.zeromq.ZMQ;

public class Client {
	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.REQ);

		System.out.println("Connecting to hello world server...");
		socket.connect("tcp://localhost:5555");

		for (int i = 0; i < 10; i++) {
			System.out.println("Sending request " + i + "...");
			byte[] request = "Hello".getBytes();
			socket.send(request, 0);

			byte[] reply = socket.recv(0);
			System.out.println("Received reply " + i + ": [" + new String(reply) + "]");
		}
	}
}
