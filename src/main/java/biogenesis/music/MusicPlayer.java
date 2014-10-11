package biogenesis.music;

import biogenesis.event.OrganismCollidedEvent;
import biogenesis.event.OrganismCollidedListener;
import biogenesis.event.OrganismCreatedEvent;
import biogenesis.event.OrganismCreatedListener;
import java.util.Random;
import org.jfugue.Player;

/**
 *
 * @author sebastien
 */
public class MusicPlayer implements OrganismCreatedListener, OrganismCollidedListener {

	final Player player = new Player();
	final String[] notes = {"A", "B", "C", "D", "E", "F", "G"};

	@Override
	public void perform(OrganismCreatedEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Random rand = new Random();
				String note = notes[rand.nextInt(notes.length)];
				player.play(note);
			}
		}).start();

	}

	@Override
	public void perform(OrganismCollidedEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Random rand = new Random();
				String note = notes[rand.nextInt(notes.length)];
				player.play(note +"6");
			}
		}).start();
	}

}
