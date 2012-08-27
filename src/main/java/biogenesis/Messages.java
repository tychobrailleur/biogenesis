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

import java.util.MissingResourceException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A class used to translate messages in the appropriate language.
 */
public class Messages {
	/**
	 * The root name of files containing localized messages.
	 */
	private static final String BUNDLE_NAME = "biogenesis/messages/messages"; //$NON-NLS-1$
	/**
	 * The selected locale to be used.
	 */
	private static Locale currentLocale = Locale.getDefault();
	/**
	 * The ResourceBundle used to access localized messages
	 */
	private static ResourceBundle appResourceBundle = 
		ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
	/**
	 * The names of all supported languages
	 */
	private static final String[] supportedLocalesNames = new String[3];
	/**
	 * The standard codes of all supported languages
	 */
	private static final String[] supportedLocalesCodes = {"ca","en","es"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	/**
	 * The default locale index in supportedLocalesCodes when the system language is
	 * not supported by the program. The default language is English.
	 */
	private static final int DEFAULT_LOCALE_INDEX = 1;
	/**
	 * Obtains mnemonic for key message in the current selected language.
	 * This mnemonic should appear in the message file as key_MNEMONIC.
	 * 
	 * @param key  The key string to find
	 * @return  The mnemonic for this key or the first letter in the key if it is not found
	 */
	public static Integer getMnemonic(String key) {
		try {
			return Integer.valueOf(appResourceBundle.getString(key+"_MNEMONIC").codePointAt(0)); //$NON-NLS-1$
		} catch (MissingResourceException e) {
			return Integer.valueOf(key.codePointAt(0));
		}
	}
	/**
	 * Translate key message into the current selected language.
	 * Key message is looked up the corresponding message file and
	 * the translation retreived.
	 * 
	 * @param key  The key string to find
	 * @return  The translated String or the string !key! if it is not found
	 */
	public static String getString(String key) {
		try {
			return appResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	/**
	 * Translate key message into the current selected language.
	 * Key message is looked up the corresponding message file and
	 * the translation retreived. The $1 token is substitued by the
	 * string param1 in the translated string.
	 * 
	 * @param key  The key string to find
	 * @param param1  The string that $1 represents on the translated string
	 * @return  The translated String or the string !key! if it is not found
	 */
	public static String getString(String key, String param1) {
		return getString(key).replace("$1", param1); //$NON-NLS-1$
	}
	/**
	 * Translate key message into the current selected language.
	 * Key message is looked up the corresponding message file and
	 * the translation retreived. The $1 token is substitued by the
	 * string param1 in the translated string and $2 by param2.
	 * 
	 * @param key  The key string to find
	 * @param param1  The string that substitute $1 on the translated string
	 * @param param2  The string that substitute $2 on the translated string 
	 * @return  The translated String or the string !key! if it is not found
	 */
	public static String getString(String key, String param1, String param2) {
		return getString(key, param1).replace("$2", param2); //$NON-NLS-1$
	}
	/**
	 * Translate key message into the current selected language.
	 * Key message is looked up the corresponding message file and
	 * the translation retreived. $i tokens, where i is an integer are
	 * substitued by string params[i-1] in the translated string.
	 * 
	 * @param key  The key string to find
	 * @param params  The strings that substitue $i on the translated string
	 * @return  The translated String or the string !key! if it is not found
	 */
	public static String getString(String key, String[] params) { 
		String string = getString(key);
		String replacedString;
		for (int i=1; i<=params.length; i++) { 
			replacedString = "$"+i; //$NON-NLS-1$
			string = string.replace(replacedString, params[i-1]);
		}
		return string;
	}
	/**
	 * Getter for the list of supported locales
	 * 
	 * @return  An array of strings containing the names of all
	 * supported locales.
	 */
	public static String[] getSupportedLocalesNames() {
		String[] names = new String[supportedLocalesNames.length];
		for (int i=0; i<names.length; i++)
			names[i] = supportedLocalesNames[i];
		return names;
	}
	/**
	 * Set a new locale using its index in the list of supported locales
	 * 
	 * @param index
	 */
	public static void setLocale(int index) {
		currentLocale = new Locale(supportedLocalesCodes[index]);
		appResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
		changeLocale();
	}
	/**
	 * Set a new locale from a language code
	 * 
	 * @param language
	 */
	public static void setLocale(String language) {
		currentLocale = new Locale(language);
		appResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
		changeLocale();
	}
	/**
	 * Return the language code of the current locale
	 * 
	 * @return  The language code of the current locale
	 */
	public static String getLanguage() {
		return currentLocale.getLanguage();
	}
	/**
	 * Return the current locale
	 * 
	 * @return  The current locale
	 */
	public static Locale getLocale() {
		return currentLocale;
	}
	/**
	 * Return the current locale index in the supported language list
	 * 
	 * @return  The index of the current locale in the supported language list
	 */
	public static int getLocaleIndex() {
		for (int i=0; i<supportedLocalesCodes.length; i++)
			if (currentLocale.getLanguage().equals(supportedLocalesCodes[i]))
				return i;
		return DEFAULT_LOCALE_INDEX;
	}
	/**
	 * Called when the locale is changed to translate the names of the supported
	 * languages to the new locale
	 */
	protected static void changeLocale() {
		supportedLocalesNames[0] = Messages.getString("T_CATALAN"); //$NON-NLS-1$
		supportedLocalesNames[1] = Messages.getString("T_ENGLISH"); //$NON-NLS-1$
		supportedLocalesNames[2] = Messages.getString("T_SPANISH"); //$NON-NLS-1$
	}
}
