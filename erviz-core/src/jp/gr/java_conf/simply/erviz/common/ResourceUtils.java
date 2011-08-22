package jp.gr.java_conf.simply.erviz.common;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * This is a utility class to generate resource bundle objects.
 * 
 * @author kono
 */
public final class ResourceUtils {
	
	//resource files  (don't add "./")
	private static final String APP_INFO_PROPERTIES = "resources/app_info.properties";
	
	private static final String APP_NAME_FORMAT = "{name} version {version}";
	
	/**
	 * Any instance of this class doesn't created.
	 */
	private ResourceUtils() {
	}
	
	/**
	 * Reads the application name from the resource text.
	 * This name has version number.
	 * 
	 * @return application name text
	 */
	public static String readAppNameText() {
		
		Properties prop;
		
		try {
			prop = readResouceProperties(APP_INFO_PROPERTIES, IoUtils.class);
		} catch (IOException ex) {
			//fatal error
			System.err.println(ex.getMessage());
			for (StackTraceElement e : ex.getStackTrace()) {
				System.err.println(e.toString());
			}
			System.exit(1);
			return null;
		}
		
		if (prop.isEmpty()) {
			return "";
		}
		
		final String name = prop.getProperty("app.name");
		final String version = prop.getProperty("app.version");
		final String appName = APP_NAME_FORMAT.replace("{name}", name).replace("{version}", version);
		return appName; 
	}
	
	/**
	 * Reads properties from the specified resource.
	 * If you specify a relative path, remove the head "./" 
	 * which may make troubles in JAR.
	 * 
	 * @param path the resource path.
	 * @param basis the class object which indicates base path.
	 * @return the properties read from the specified resource data
	 * @throws IOException if an error occurred when reading from the input stream
	 * @throws NullPointerException if basis is null
	 */
	private static Properties readResouceProperties(String path, Class<?> basis) 
			throws IOException {
		
		InputStream is = null;
		try {
			Properties prop = new Properties();
			is = basis.getResourceAsStream(path);
			prop.load(is);
			
			return prop;
		} finally {
			close(is);
		}
		
	}
	
	/**
	 * Reads lines from the specified text resource.
	 * If you specify a relative path, remove the head "./" 
	 * which may make troubles in JAR.
	 * 
	 * @param path the resource path.
	 * @param basis the class object which indicates base path.
	 * @return lines of the specified resource data
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NullPointerException if basis is null
	 */
	public static List<String> readResouceLines(String path, Class<?> basis) 
			throws IOException, FileNotFoundException {
		InputStream in = basis.getResourceAsStream(path);
		return IoUtils.readStreamLines(in);
	}
	
	/**
	 * Retrieves the resource bundle object. The default lacale is used.
	 * 
	 * @param resourceName resource name
	 * @return a resource bundle object
	 */
	public static ResourceBundle getResourceBundle(String resourceName) {
		ResourceBundle rb = ResourceBundle.getBundle(resourceName, Locale.getDefault(), new CustomResourceBundleControl());
		return rb;
	}
	
	/**
	 * See javadoc for ResourceBundle.Control class.
	 * 
	 * @author kono
	 * @see ResourceBundle.Control
	 */
	private static class CustomResourceBundleControl extends ResourceBundle.Control {
		
		@Override
		public List<String> getFormats(String baseName) {
			if (baseName == null) {
				throw new NullPointerException();
			}
			return Arrays.asList("xml");
		}
		
		@Override
		public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
				throws IOException {
			
			if (baseName == null || locale == null || format == null || loader == null) {
				throw new NullPointerException();
			}
			
			if (!format.equals("xml")) {
				throw new IllegalArgumentException("format not supported [" + format + "]");
			}
			
			final String bundleName = toBundleName(baseName, locale);
			final String resourceName = toResourceName(bundleName, format);
			
			InputStream is = null;
			if (reload) {
				URL url = loader.getResource(resourceName);
				if (url == null) {
					throw new IOException();
				}
				
				//throws IOException
				URLConnection connection = url.openConnection();
				connection.setUseCaches(false); //Disable caches to get fresh data for reloading.
				
				//throws IOException
				is = connection.getInputStream();
				
			} else {
				is = loader.getResourceAsStream(resourceName);
				if (is == null) {
					throw new IOException();
				}
			}
			
			try {
				is = new BufferedInputStream(is);
				if (format.equals("xml")) {
					return new XMLResourceBundle(is);
				} else {
					assert false : "format not supported [" + format + "]";
					return null;
				}
			} finally {
				close(is);
			}
			
		}
	}
	
	/**
	 * A resource bundle class for XML resource.
	 * @author kono
	 */
	private static class XMLResourceBundle extends ResourceBundle {
		
		private final Properties props = new Properties();
		
		XMLResourceBundle(InputStream is) throws IOException {
			this.props.loadFromXML(is);
		}
		
		@Override
		protected Object handleGetObject(String key) {
			return this.props.getProperty(key);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public Enumeration<String> getKeys() {
			return (Enumeration<String>)this.props.propertyNames();
		}
		
		@Override
		public String toString() {
			return this.props.toString();
		}
		
	}
	
	//utility method for close()
	private static void close(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException ex) {
			}
		}
	}
	
}
