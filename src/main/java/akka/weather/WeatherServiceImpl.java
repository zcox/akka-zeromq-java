package akka.weather;

import java.util.Random;

import akka.actor.TypedActor;
import akka.dispatch.Future;
import akka.weather.WeatherSensor.WeatherData;

public class WeatherServiceImpl extends TypedActor implements WeatherService {
	private Random random = new Random(System.currentTimeMillis());

	public Future<WeatherData> get() {
		try {
			Thread.sleep(1000); // sensor is slow...
		} catch (InterruptedException e) {
		}
		int temperature = random.nextInt(215) - 80 + 1;
		int humidity = random.nextInt(50) + 10 + 1;
		WeatherData data = new WeatherData(temperature, humidity);
		return future(data);
	}
}
