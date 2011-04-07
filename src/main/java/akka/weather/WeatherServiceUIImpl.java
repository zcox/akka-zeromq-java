package akka.weather;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import akka.actor.TypedActor;
import akka.dispatch.Future;
import akka.japi.Procedure;
import akka.weather.WeatherSensor.WeatherData;

public class WeatherServiceUIImpl extends TypedActor implements WeatherServiceUI {
	private WeatherService service;
	private JLabel temperatureLabel;
	private JLabel humidityLabel;

	public WeatherServiceUIImpl(WeatherService service, JLabel temperatureLabel, JLabel humidityLabel) {
		this.service = service;
		this.temperatureLabel = temperatureLabel;
		this.humidityLabel = humidityLabel;
	}

	public void update() {
		service.get().onComplete(new Procedure<Future<WeatherData>>() {
			public void apply(Future<WeatherData> future) {
				final WeatherData data = future.result().get();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						temperatureLabel.setText(String.valueOf(data.temperature));
						humidityLabel.setText(String.valueOf(data.humidity));
					}
				});
			}
		});
	}

}
