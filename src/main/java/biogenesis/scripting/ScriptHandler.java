/* Copyright (c) 2012 Sebastien Le Callonnec
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
package biogenesis.scripting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jruby.embed.ScriptingContainer;

/**
 *
 * @author Sebastien Le Callonnec
 */
public final class ScriptHandler {
	private final List<File> scriptList = new ArrayList<>();
	
	public final void runScript(ScriptingContainer container) {
		if (scriptList.isEmpty()) {
			loadFiles();
		}
		
		for (File f: scriptList) {
			try {
				container.runScriptlet(new FileInputStream(f), f.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void loadFiles() {
		File configDir = new File(System.getProperty("user.home"), ".biogenesis");
		if (configDir.exists()) {
			File scriptDir = new File(configDir, "scripts");
			if (scriptDir.exists()) {
				File files[] = scriptDir.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File directory, String filename) {
						return filename.endsWith(".rb");
					}
				});
				
				scriptList.addAll(Arrays.asList(files));
			}
		}
	}
}
