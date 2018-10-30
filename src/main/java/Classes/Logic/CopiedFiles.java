package Classes.Logic;

import java.nio.file.Path;

/**
 * Created by SM David on 8/19/18.
 */
public class    CopiedFiles {
    private Path source;
    private String destination;

    public CopiedFiles(Path source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        if(source == null)
            return "";
        return source.toString();
    }

    public String getDestination() {
        if(destination == null)
            return "";

        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
