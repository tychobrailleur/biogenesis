/* Copyright (C) 2006-2010  Joan Queralt Molina
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
package biogenesis;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class BioFileFilter extends FileFilter {
	private String validExtension = ""; //$NON-NLS-1$
	public static final String WORLD_EXTENSION = "bgw"; //$NON-NLS-1$
	public static final String GENETIC_CODE_EXTENSION = "bgg"; //$NON-NLS-1$
	
	public BioFileFilter(String ext) {
		super();
		validExtension = ext;
	}
	
	public String getValidExtension() {
		return validExtension;
	}
	
	public BioFileFilter(Object obj) {
		super();
		if (obj instanceof World)
			validExtension = WORLD_EXTENSION;
		if (obj instanceof GeneticCode)
			validExtension = GENETIC_CODE_EXTENSION;
	}
	
	

	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;
		
		String extension = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            extension = s.substring(i+1).toLowerCase();
        }
        
		if (extension != null) {
			if (extension.equals(validExtension)) {
				return true;
		    }
		}
		return false;
	}

	@Override
	public String getDescription() {
		if (validExtension.equals(WORLD_EXTENSION))
			return Messages.getString("T_BIOGENESIS_WORLD_FILES"); //$NON-NLS-1$
		if (validExtension.equals(GENETIC_CODE_EXTENSION))
			return Messages.getString("T_BIOGENESIS_GENETIC_CODE_FILES"); //$NON-NLS-1$
		if (validExtension.equals("png")) //$NON-NLS-1$
			return Messages.getString("T_PNG_IMAGE_FILES"); //$NON-NLS-1$
		return null;
	}

}
