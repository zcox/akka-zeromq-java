package akka.weather;

import akka.dispatch.Future;
import akka.weather.WeatherSensor.WeatherData;

public interface WeatherService {
	public Future<WeatherData> get();
}
