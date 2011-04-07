package akkazeromq;

import org.zeromq.ZMQ;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/** Akka actor that wraps a 0MQ subscriber socket. */
public class Subscriber extends UntypedActor {
	public static final String Receive = "Receive";
	private final String endpoint;
	private final Reader<Object> reader;
	private final ActorRef actor;
	private ZMQ.Context context;
	private ZMQ.Socket socket;

	public Subscriber(String endpoint, Reader<Object> reader, ActorRef actor) {
		this.endpoint = endpoint;
		this.reader = reader;
		this.actor = actor;
	}

	@Override
	public void preStart() {
		context = ZMQ.context(1);
		socket = context.socket(ZMQ.SUB);
		socket.connect(endpoint);
		socket.subscribe("".getBytes());
	}

	@Override
	public void postStop() {
		socket.close();
		context.term();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (Receive.equals(message)) {
			byte[] bytes = socket.recv(ZMQ.NOBLOCK);
			if (bytes != null)
				actor.sendOneWay(reader.read(bytes), getContext());
			else
				Thread.sleep(1);
			getContext().sendOneWay(Receive);
		}
	}
}
