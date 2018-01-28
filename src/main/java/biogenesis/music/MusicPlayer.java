/* Copyright (C) 2014-2018 Sébastien Le Callonnec
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package biogenesis.music;

import biogenesis.event.OrganismCollidedEvent;
import biogenesis.event.OrganismCollidedListener;
import biogenesis.event.OrganismCreatedEvent;
import biogenesis.event.OrganismCreatedListener;
import java.util.Random;
import org.jfugue.Player;

/**
 * Plays music on different events, using JFugue.
 *
 * @author Sébastien Le Callonnec
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
