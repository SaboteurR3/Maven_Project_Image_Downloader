package ge.tsu.project;

import ge.tsu.project.downloader.ImageDownloader;

import java.io.IOException;
import java.nio.file.Paths;

public class Launcher {
    public static void main(String[] args) throws IOException {

        ImageDownloader pngDownloader = new ImageDownloader(
                "http://freeuni.edu.ge/ge",
                Paths.get("C:/Users/Saboteur/IdeaProjects/Maven_Project_1/downloadedImages"),
                "png");
         pngDownloader.download();

        ImageDownloader jpegDownloader = new ImageDownloader(
                "http://freeuni.edu.ge/ge",
                Paths.get("C:/Users/Saboteur/IdeaProjects/Maven_Project_1/downloadedImages"),
                "jpeg");
         jpegDownloader.download();
    }
}
