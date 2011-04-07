package akka;

import static akka.actor.Actors.actorOf;

import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.Future;
import akka.japi.Procedure;

public class AsyncRequestReply {

	public static void main(String[] args) {
		ActorRef requester = actorOf(AsyncRequestActor.class).start();
		ActorRef future = actorOf(AsyncFutureActor.class).start();
		ActorRef replier = actorOf(AsyncReplyActor.class).start();
		LoggerFactory.getLogger(AsyncRequestReply.class).info("Starting up...");
		requester.sendOneWay(replier);
		future.sendOneWay(replier);
	}

	public static class AsyncRequestActor extends UntypedActor {
		@Override
		public void onReceive(Object message) throws Exception {
			if (message instanceof ActorRef)
				((ActorRef) message).sendOneWay("Hello from AsyncRequestActor!", getContext());
			else if (message instanceof String)
				log().logger().info("Reply was " + message);
		}
	}

	@SuppressWarnings("unchecked")
	public static class AsyncFutureActor extends UntypedActor {
		@Override
		public void onReceive(Object message) throws Exception {
			if (message instanceof ActorRef) {
				Future future = ((ActorRef) message).sendRequestReplyFuture("Hello from AsyncFutureActor!", 1000, getContext());
				future.onComplete(new Procedure<Future>() {
					public void apply(Future future) {
						log().logger().info("Result was " + future.result().get());
					}
				});
			}
		}
	}

	public static class AsyncReplyActor extends UntypedActor {
		@Override
		public void onReceive(Object message) throws Exception {
			log().logger().info("Request was " + message);
			getContext().replyUnsafe("Hi there!");
		}
	}
}
