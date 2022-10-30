import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class VelocityConverter {
    public static void main(String[] args) throws IOException {
        VelocityContext context = new VelocityContext();

        File file = new File("src/main/resources/ПВ-Израиль_банки.xml");
        XStream xStream = new XStream(new DomDriver());
        // Написать корневой элемент вместо mass
        xStream.alias("mass", Map.class);
        xStream.registerConverter(new MapEntryConverter());
        Map<String,Object> map = (Map<String,Object>) xStream.fromXML(file);

        context.put("data", map);

        StringWriter stringWriter = new StringWriter();
        Reader reader = new FileReader("src/main/resources/sheet1.xml");
        Velocity.evaluate(context, stringWriter, "VelocityService", reader);

        byte[] bytes = stringWriter.toString().getBytes(Charset.forName("windows-1251"));
        String result = Arrays.toString(bytes);

        FileUtils.writeByteArrayToFile(new File("src/main/resources/result.xml"), bytes);

        System.out.println(result);
    }
}

