package akka.weather;

import java.util.Random;

import akka.actor.UntypedActor;

public class WeatherSensor extends UntypedActor {
	public static final String Get = "Get";
	private Random random = new Random(System.currentTimeMillis());

	@Override
	public void onReceive(Object message) throws Exception {
		if (Get.equals(message)) {
			Thread.sleep(1000); // sensor is slow...
			int temperature = random.nextInt(215) - 80 + 1;
			int humidity = random.nextInt(50) + 10 + 1;
			WeatherData data = new WeatherData(temperature, humidity);
			getContext().replyUnsafe(data);
		}
	}

	public static final class WeatherData {
		public final int temperature;
		public final int humidity;

		public WeatherData(int temperature, int humidity) {
			this.temperature = temperature;
			this.humidity = humidity;
		}
	}
}
