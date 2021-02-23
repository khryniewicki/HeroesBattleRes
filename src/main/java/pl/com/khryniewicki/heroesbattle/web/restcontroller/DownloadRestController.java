package pl.com.khryniewicki.heroesbattle.web.restcontroller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.com.khryniewicki.heroesbattle.utils.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;

@RestController
@CrossOrigin({"http://localhost:4200", "https://heroes-battle.khryniewicki.pl"})
public class DownloadRestController {

    @GetMapping(value = "/api/download/{fileName:.+}", produces = "multipart/form-data")
    public void downloader(HttpServletResponse response, @PathVariable("fileName") String fileName) {
        System.out.println(fileName);
        try {
            String[] split = fileName.split("\\.");
            System.out.println(fileName);
            File file = FileUtils.pathTransformer(split[1], "downloads", fileName);

            assert file != null;
            if (file.exists()) {

                URLConnection connection = file.toURI().toURL().openConnection();
                String mimeType = connection.getContentType();
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentLength((int) file.length());

                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[100000000];
                int b;

                while ((b = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, b);
                }

                fis.close();
                os.close();
            } else {
                System.out.println("Requested " + fileName + " file not found!!");
            }
        } catch (IOException e) {
            System.out.println("Error:- " + e.getMessage());
        }
    }
}
