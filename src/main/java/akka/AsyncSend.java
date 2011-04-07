package akka;

import static akka.actor.Actors.actorOf;

import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class AsyncSend {

	public static void main(String[] args) {
		ActorRef actor = actorOf(AsyncSendActor.class).start();
		LoggerFactory.getLogger(AsyncSend.class).info("Starting up...");
		actor.sendOneWay("Hello, world!");
	}

	public static class AsyncSendActor extends UntypedActor {
		@Override
		public void onReceive(Object message) throws Exception {
			log().logger().info("Received " + message);
		}
	}
}
