package fr.supercomete.datamanager.FileManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class Fileutils {
	public static void createFile(File file) throws IOException{
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}
	public static void save(File file,String text) {
		final FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.write(text);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public static String readFileFromResources(Class<?> claz, String fileName) throws IOException {
        ClassLoader classLoader = claz.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        }
    }
	public static String loadContent(File file) {
		if(file.exists()) {
			try {
				final BufferedReader reader = new BufferedReader(new FileReader(file));
				final StringBuilder text = new StringBuilder();
				String line;
				while((line = reader.readLine())!=null) {
					text.append(line);
				}
				reader.close();
				return text.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return"";
	}
}
