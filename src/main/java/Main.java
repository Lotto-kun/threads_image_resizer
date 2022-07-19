import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageResizer.class);
    private static final Marker EXCEPTIONS_MARKER = MarkerFactory.getMarker("EXCEPTIONS");

    public static void main(String[] args) {
        String srcFolder = "D:/Wallpapers";
        String dstFolder = "D:/WallpapersResized";
        File srcDir = new File(srcFolder);
        File[] files = srcDir.listFiles();
        assert files != null;
        createDir(dstFolder);
        int cores = Runtime.getRuntime().availableProcessors();
        int part = files.length / cores;
        int leftBehind = files.length % cores;

        for (int i = 0; i < cores; i++) {
            int toCopy = part;
            if (i == cores - 1) {
                toCopy += leftBehind;
            }
            if (toCopy == 0) {
                continue;
            }
            File[] partFiles = new File[toCopy];
            System.arraycopy(files, part * i, partFiles, 0, toCopy);
            new ImageResizer(partFiles, dstFolder, System.currentTimeMillis()).start();
        }
    }

    public static void createDir(String destinationDirectory) {
        Path destination = Paths.get(destinationDirectory);
        if (Files.notExists(destination)) {
            try {
                Files.createDirectories(destination);
            } catch (Exception ex) {
                LOGGER.error(EXCEPTIONS_MARKER, "ошибка создания директории назначения", ex);
                ex.printStackTrace();
            }
        }
    }
}
