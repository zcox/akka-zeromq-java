package zeromq;

import org.zeromq.ZMQ;

public class Server {
	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.REP);
		socket.bind("tcp://*:5555");

		while (true) {
			byte[] request = socket.recv(0);
			System.out.println("Received request: [" + new String(request) + "]");

			try {
				Thread.sleep(1000); // Do some 'work'
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			byte[] reply = "World".getBytes();
			socket.send(reply, 0);
		}
	}
}
