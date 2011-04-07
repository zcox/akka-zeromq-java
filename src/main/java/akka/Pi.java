package akka;

import static akka.actor.Actors.actorOf;
import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

/** Inspired by http://www.earldouglas.com/estimating-pi-with-akka */
public class Pi {

	public static void main(String[] args) {
		final int iterations = 10000;
		int length = 100000;
		System.out.println("iterations=" + iterations + ", length=" + length + ", total=" + (iterations * length));

		ActorRef accumulator = actorOf(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Accumulator(iterations);
			}
		}).start();

		// (0,99999), (100000,199999), (200000,299999), (300000,399999)...
		for (int x = 0; x < iterations; x++) {
			ActorRef worker = actorOf(Worker.class).start();
			int start = x * length;
			int end = (x + 1) * length - 1;
			worker.sendOneWay(new Range(start, end), accumulator);
		}
	}

	public static final class Range {
		public final int start;
		public final int end;

		public Range(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	public static class Worker extends UntypedActor {
		@Override
		public void onReceive(Object message) throws Exception {
			Range range = (Range) message;
			double sum = 0.0d;
			for (int k = range.start; k <= range.end; k++)
				sum += 4 * Math.pow(-1, k) / (2 * k + 1);
			getContext().replyUnsafe(sum);
		}
	}

	public static class Accumulator extends UntypedActor {
		private final int iterations;
		private int count = 0;
		private double pi = 0.0d;
		private long start;

		public Accumulator(int iterations) {
			this.iterations = iterations;
		}

		@Override
		public void onReceive(Object message) throws Exception {
			pi += (Double) message;
			count += 1;
			if (count == iterations)
				Actors.registry().shutdownAll();
		}

		@Override
		public void preStart() {
			start = System.currentTimeMillis();
		}

		@Override
		public void postStop() {
			System.out.println("\n>>> result: " + pi + " (error: " + (Math.abs(Math.PI - pi)) + ")");
			System.out.println(">>> run time: " + (System.currentTimeMillis() - start) + " ms\n");
		}
	}
}
