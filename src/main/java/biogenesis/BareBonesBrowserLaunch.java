package biogenesis;

////////////////////////////////////////////////////////
//Bare Bones Browser Launch                          //
//Version 1.5                                        //
//December 10, 2005                                  //
//Supports: Mac OS X, GNU/Linux, Unix, Windows XP    //
//Example Usage:                                     //
// String url = "http://www.centerkey.com/";       //
// BareBonesBrowserLaunch.openURL(url);            //
//Public Domain Software -- Free to Use as You Like  //
/////////////////////////////////////////////////////////

import java.lang.reflect.Method;
import javax.swing.JOptionPane;

public class BareBonesBrowserLaunch {

	private static final String errMsg = "Error attempting to launch web browser"; //$NON-NLS-1$

	public static void openURL(String url) {
		String osName = System.getProperty("os.name"); //$NON-NLS-1$
		try {
			if (osName.startsWith("Mac OS")) { //$NON-NLS-1$
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager"); //$NON-NLS-1$
				Method openURL = fileMgr.getDeclaredMethod("openURL", //$NON-NLS-1$
						new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows")) //$NON-NLS-1$
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + url); //$NON-NLS-1$
			else { // assume Unix or Linux
				String[] browsers = {
						"x-www-browser", "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "iceweasel" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime
							.getRuntime()
							.exec(new String[] { "which", browsers[count] }).waitFor() == 0) //$NON-NLS-1$
						browser = browsers[count];
				if (browser == null)
					throw new Exception("Could not find web browser"); //$NON-NLS-1$
				Runtime.getRuntime().exec(new String[] { browser, url });
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, errMsg
					+ ":\n" + e.getLocalizedMessage()); //$NON-NLS-1$
		}
	}

}