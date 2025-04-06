package com.sillypantscoder.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * This class allows you to load resources from the "src/resources" directory.
 * It will automatically unpack the JAR file if running from a JAR file.
 */
public class AssetLoader {
	public static File unpack_dir = new File(System.getProperty("user.dir") + "/.java_assetloader_files_" + Math.round(Math.random() * 10000000));
	/**
	 * Gets the location of the root folder of the java files. If running from a JAR file, gets the location of the file. If running from a folder, gets the root `src` folder.
	 * @return Location of the root folder.
	 */
	public static String getRootLocation() {
		try {
			return new File(AssetLoader.class.getProtectionDomain().getCodeSource().getLocation()
				.toURI()).getPath();
		} catch (URISyntaxException e) {
			return "";
		}
	}
	public static boolean isJar() {
		String addr = AssetLoader.class.getResource("AssetLoader.class").getProtocol();
		return addr.equals("jar");
	}
	/**
	 * Gets the location of the `src` directory. If running from a JAR file, unpacks the JAR file first.
	 * @return Location of the `src` directory.
	 */
	public static String getSrcDirectoryLocation() {
		try {
			if (isJar()) {
				if (unpack_dir.exists()) {
					return unpack_dir.getAbsolutePath();
				}
				System.out.println("assetloader: unpacking jar file...");
				// Create extraction directory:
				ProcessBuilder pb = new ProcessBuilder("mkdir", unpack_dir.getAbsolutePath());
				pb.directory(new File("/tmp"));
				pb.start().waitFor();
				// Unpack the jar:
				pb = new ProcessBuilder("jar", "xvf", getRootLocation());
				pb.directory(unpack_dir);
				pb.start().waitFor();
				System.out.println("assetloader: unpacked jar file to " + unpack_dir.getAbsolutePath());
				addShutdownHook();
				return unpack_dir.getAbsolutePath();
			} else {
				return getRootLocation();
			}
		} catch (IOException | InterruptedException e) {
			return "";
		}
	}
	public static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			ProcessBuilder pb = new ProcessBuilder("rm", "-r", unpack_dir.getAbsolutePath());
			pb.directory(new File("/tmp"));
			try {
				pb.start().waitFor();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			System.out.println("assetloader: deleted temporary folder " + unpack_dir.getAbsolutePath());
		}));
	}
	protected static HashMap<String, File> cacheResourceLocations = new HashMap<String, File>();
	public static File getResourceLocation(String name) {
		// Check if the file is already cached:
		if (cacheResourceLocations.containsKey(name)) {
			File f = cacheResourceLocations.get(name);
			return f;
		}
		// If not, find the file:
		String root = getSrcDirectoryLocation() + "/resources";
		String newName = root + "/" + name;
		File f = new File(newName);
		cacheResourceLocations.put(name, f);
		return f;
	}
	public static String getResource(String name) {
		try {
			File f = getResourceLocation(name);
			byte[] bytes = new byte[(int)(f.length())];
			FileInputStream fis = new FileInputStream(f);
			fis.read(bytes);
			fis.close();
			return new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}