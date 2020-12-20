package gr.alexc.idelearn.ui.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

	public static void unzipToDirectory(ZipInputStream zipInputStream, Path path) throws IOException {

		ZipEntry entry = null;
		try {
			entry = zipInputStream.getNextEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (entry != null) {
			if (!entry.isDirectory()) {
				newFile(path, entry.getName(), zipInputStream);
			} else {
				newDir(path, entry.getName());
			}
			entry = zipInputStream.getNextEntry();
		}

		zipInputStream.closeEntry();
		zipInputStream.close();
	}

	private static void newDir(Path destinationDir, String name) {

		File file = new File(destinationDir.toFile(), name);
		file.mkdirs();

	}

	private static void newFile(Path destinationDir, String name, ZipInputStream zipInputStream) {
		byte[] buffer = new byte[1024];
		try {
			File file = new File(destinationDir.toFile(), name);
			FileOutputStream fos = new FileOutputStream(file);
			int len;
			while ((len = zipInputStream.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
