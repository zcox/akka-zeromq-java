package zeromq;

import org.zeromq.ZMQ;

public class Subscriber {
	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.SUB);
		socket.connect("tcp://localhost:5555");
		socket.subscribe("".getBytes());

		while (true) {
			String message = new String(socket.recv(0));
			System.out.println("Received " + message);
		}
	}
}
