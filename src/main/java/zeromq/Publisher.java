package zeromq;

import org.zeromq.ZMQ;

public class Publisher {
	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);
		ZMQ.Socket socket = context.socket(ZMQ.PUB);
		socket.bind("tcp://*:5555");

		int count = 0;
		while (true) {
			String message = "Message " + count++;
			socket.send(message.getBytes(), 0);
			System.out.println("Sent " + message);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
