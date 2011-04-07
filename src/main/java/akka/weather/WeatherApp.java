package akka.weather;

import static akka.actor.Actors.actorOf;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import akka.actor.ActorRef;
import akka.actor.Scheduler;
import akka.actor.TypedActor;
import akka.actor.TypedActorFactory;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class WeatherApp {
	public static void main(String[] args) {
		final JLabel temperature = new JLabel("-");
		final JLabel humidity = new JLabel("-");

		// UntypedActor
		final ActorRef sensor = actorOf(WeatherSensor.class).start();
		final ActorRef ui = actorOf(new UntypedActorFactory() {
			public UntypedActor create() {
				return new WeatherUI(sensor, temperature, humidity);
			}
		}).start();

		// TypedActor
		final WeatherService weatherService = TypedActor.newInstance(WeatherService.class, WeatherServiceImpl.class);
		final WeatherServiceUI weatherServiceUI = TypedActor.newInstance(WeatherServiceUI.class, new TypedActorFactory() {
			public TypedActor create() {
				return new WeatherServiceUIImpl(weatherService, temperature, humidity);
			}
		});

		JButton update = new JButton("Update");
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// ui.sendOneWay(WeatherUI.Update);
				weatherServiceUI.update();
			}
		});

		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Scheduler.schedule(ui, WeatherUI.Update, 0, 1, SECONDS);
			}
		});

		JButton stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Scheduler.shutdown();
			}
		});

		JFrame frame = new JFrame("Weather");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout());
		frame.getContentPane().add(update);
		frame.getContentPane().add(start);
		frame.getContentPane().add(stop);
		frame.getContentPane().add(temperature);
		frame.getContentPane().add(humidity);
		frame.pack();
		frame.setVisible(true);
	}
}
