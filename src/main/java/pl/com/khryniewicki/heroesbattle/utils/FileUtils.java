package pl.com.khryniewicki.heroesbattle.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
    private static Map<String, List<Resource>> resourcesRegistry;

    private FileUtils() {
    }

    public static String loadAsString(InputStream in) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static File pathTransformer(String ending, String inside, String path) throws IOException {
        List<Resource> resources = resourcesFinder(ending, inside);
        File result;

        for (Resource resource : resources) {
            if (path.equals(resource.getFilename())) {
                result = resource.getFile();
                return result;
            }

        }

        return null;
    }

    public static List<Resource> resourcesFinder(String ending, String inside) {
        if (resourcesRegistry == null) {
            resourcesRegistry = new HashMap<>();
        }
        if (!resourcesRegistry.containsKey(ending)) {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = new Resource[0];

            try {
                resources = resolver.getResources("classpath*:/**/" + inside + "/**/*." + ending);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resourcesRegistry.put(ending, Arrays.asList(resources));
        }
        return resourcesRegistry.get(ending);
    }
}
