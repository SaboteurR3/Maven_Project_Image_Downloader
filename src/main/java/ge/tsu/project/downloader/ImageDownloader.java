package ge.tsu.project.downloader;

import org.apache.tika.Tika;
import org.apache.tika.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ImageDownloader {
    protected static final Tika TIKA = new Tika();
    protected String url;
    protected final Path imageDownloadFolder;
    protected String mimeType;

    public ImageDownloader(String url, Path imageDownloadFolder, String mimeType) {
        this.url = url;
        this.imageDownloadFolder = imageDownloadFolder;
        this.mimeType = mimeType;
    }
    public void download() throws IOException {
        Set<String> downloadedImages = new HashSet<>();
        // grab html page
        Document doc = Jsoup.connect(url).get();
        System.out.println("Found page with title: " +  doc.title());
        // find image tags
        Elements images = doc.select("img[src]");
        for (var image : images) {
            String imageUrl = image.attr("abs:src");
            if(!StringUtils.isBlank(imageUrl))
                downloadedImages.add(imageUrl);
        }
        // download bytes of those images
        String detect;
        for(var image : downloadedImages) {
            URL url = new URL(image);
            try {
                detect = TIKA.detect(url);
            }
            catch (IOException exc) {
                System.err.println("Error: " + exc.getMessage());
                continue;
            }

            System.out.println("Detected mimetype: " + detect);
            // give bytes to Tika
            if(detect.equals("image/" + mimeType)) {
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                byte [] savedBytes = urlConnection.getInputStream().readAllBytes();
                Path imageDownloadPath = imageDownloadFolder.resolve(UUID.randomUUID().toString().concat("." + mimeType));
                try(OutputStream outputStream = Files.newOutputStream(imageDownloadPath)) {
                    outputStream.write(savedBytes);
                }
            }
        }
    }
}
