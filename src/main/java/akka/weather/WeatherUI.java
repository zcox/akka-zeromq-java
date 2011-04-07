package akka.weather;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.weather.WeatherSensor.WeatherData;

public class WeatherUI extends UntypedActor {
	public static final String Update = "Update";
	private ActorRef sensor;
	private JLabel temperatureLabel;
	private JLabel humidityLabel;

	public WeatherUI(ActorRef sensor, JLabel temperatureLabel, JLabel humidityLabel) {
		this.sensor = sensor;
		this.temperatureLabel = temperatureLabel;
		this.humidityLabel = humidityLabel;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (Update.equals(message))
			sensor.sendOneWay(WeatherSensor.Get, getContext());
		else if (message instanceof WeatherData) {
			final WeatherData data = (WeatherData) message;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					temperatureLabel.setText(String.valueOf(data.temperature));
					humidityLabel.setText(String.valueOf(data.humidity));
				}
			});
		}
	}
}
