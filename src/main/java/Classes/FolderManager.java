package Classes;

import Classes.DB.Config;
import Classes.Entities.Interfaces.ThreeConsumer;
import Classes.Logic.Logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by SM David on 8/16/18.
 */
public class FolderManager {
    private ThreeConsumer<Path, Path,String> operationListener;
    private Consumer<Double> operationProgress;
    private Consumer<String> operationFile;

    public List<Path> getFiles(String directory, boolean recursive) throws IOException {
        return getFiles(directory, recursive, null);
    }

    public List<Path> getFiles(String directory, boolean recursive, String regex) throws IOException {
        final List<Path> files = new ArrayList<>();
        Pattern pattern = (regex == null) ? null : Pattern.compile(regex);
        //walk
        try (Stream<Path> paths = (recursive) ? Files.walk(Paths.get(directory)) : Files.list(Paths.get(directory))) {
            paths
                    .filter((f) -> {
                        if(!Files.isRegularFile(f))
                            return false;
                        Matcher matcher = (pattern != null) ? pattern.matcher(f.getFileName().toString()) : null;
                        return (matcher == null || matcher.find());
                    })
                    .forEach((f)->{files.add(f);});
        }
        return files;
    }

    public List<Path> getFolders(String directory, boolean recursive, String regex) throws IOException {
        final List<Path> files = new ArrayList<>();
        Pattern pattern = (regex == null) ? null : Pattern.compile(regex);
        //walk
        try (Stream<Path> paths = (recursive) ? Files.walk(Paths.get(directory)) : Files.list(Paths.get(directory))) {
            paths
                    .filter((f) -> {
                        if(!Files.isDirectory(f) || !Files.isReadable(f) || Objects.equals(f.getFileName().toString(), ".") ||  Objects.equals(f.getFileName().toString(), ".."))
                            return false;
                        Matcher matcher = (pattern != null) ? pattern.matcher(f.getFileName().toString()) : null;
                        return (matcher == null || matcher.find());
                    })
                    .forEach((f)->{files.add(f);});
        }
        return files;
    }

    public void copyFiles(List<Path> paths, String destPath, boolean replace) throws IOException {
        copyFiles(paths, destPath, null, replace);
    }

    public void copyFiles(List<Path> paths, String destPath, String regex, boolean replace) throws IOException {

        if (destPath.substring(0, destPath.length() - 1) != File.separator)
            destPath += File.separator;

        int i = 0;
        Double progress = 0D;
        Pattern pattern = (regex == null) ? null : Pattern.compile(regex);
        for (Path f : paths) {
            i++;
            progress = (((double) i / (double) paths.size()) * 100);
            try {
                updateStatus(f, copyFile(f, destPath, pattern,replace), "OK",progress );
            } catch (Exception e) {
                updateStatus(f,null,e.getMessage(),progress);
            }
        }
    }

    public void copyFiles(List<Path> paths,String dest, Map<String, String> destPathMap, String regex, boolean replace) throws IOException {


        int i = 0;
        Double progress = 0D;
        Pattern pattern = (regex == null) ? null : Pattern.compile(regex);
        for (Path f : paths) {
            String destPath = dest + File.separator;
            if (pattern != null) {
                Matcher matcher = pattern.matcher(f.getFileName().toString());
                String cat = "undefined";
                if (matcher.find()) {
                    cat= destPathMap.get(matcher.group());
                }

                destPath += cat + File.separator;
            }
            i++;
            progress = (((double) i / (double) paths.size()) * 100);
            try {
                updateStatus(f, copyFile(f, destPath, pattern,replace), "OK",progress );
            } catch (Exception e) {
                updateStatus(f,null,e.getMessage(),progress);
            }
        }
    }

    private Path copyFile(Path f, String finalDest, Pattern pattern,boolean replace) throws Exception {
        try {
            if(f == null)
                throw new Exception("Invalid source");

            if(!Files.isReadable(f))
                throw new Exception("Folder " + f.toString() + " is not readable");



            Config config = Logic.getInstance().getConfig();
            if (finalDest == null)
                throw new Exception("Invalid destination");

            if (finalDest.substring(0, finalDest.length() - 1) != File.separator)
                finalDest += File.separator;

            updateFileStatus(f.toString());

            if (pattern != null) {
                Matcher matcher;
                if (
                        !(matcher = pattern.matcher((config.getFileSearch()?f.getFileName().toString():f.getParent().getFileName().toString()))).find() &&
                        !(matcher = pattern.matcher((config.getFileSearch()?f.getFileName().toString():f.getParent().getParent().getFileName().toString()))).find()
                        ) {

                    if(!config.getSaveNoMatch())
                        throw new Exception("Ignored - Invalid " + (config.getFileSearch()?"file":"folder"));
                    finalDest += config.getNoMatchFolder() + File.separator;

                }else
                    finalDest += matcher.group(1) + File.separator;
            }
            if (!Files.exists(Paths.get(finalDest)))
                Files.createDirectories(Paths.get(finalDest));

            if(!Files.isWritable(Paths.get(finalDest)))
                throw new Exception("Folder " + finalDest + " is not writable");

            Path finalPath = Paths.get(finalDest + f.getFileName());


            //ADD _1 in case of existent file
            if (!replace){
                int i=1;
                while (Files.exists(finalPath)) {
                    String file = f.getFileName().toString();
                    int lastDot = file.lastIndexOf('.');
                    file = file.substring(0, lastDot) + "_" +(i++) +file.substring(lastDot);
                    finalPath = Paths.get(finalDest + file);
                }
            }

            //TODO: CHECK if it works
            if(config.getFileSearch() && config.getParentCheck() &&  pattern != null && Files.exists(finalPath))
            {
                Matcher matcher = pattern.matcher(f.getParent().getFileName().toString());
                if (matcher.find()) {
                    finalPath = Paths.get(finalPath.getParent().getParent().toString()+ File.separator + matcher.group(1) + File.separator + finalPath.getFileName() );
                }
            }




            finalPath = Files.copy(f, finalPath, (replace ? StandardCopyOption.REPLACE_EXISTING : StandardCopyOption.COPY_ATTRIBUTES));
            try {
                Files.delete(f);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception(finalPath.toString() + "  - Delete failed - " + e.getMessage() );
            }
            return finalPath;

        }catch (FileAlreadyExistsException e) {
            throw new FileAlreadyExistsException("File already exist");
        }catch(IOException e) {
            e.printStackTrace();
            throw new Exception("Coping file error: " + e.getMessage());
        }
    }

    public void setOperationListener(ThreeConsumer<Path, Path,String> operationListener) {
        this.operationListener = operationListener;
    }

    public void setOperationProgress(Consumer<Double> operationProgress) {
        this.operationProgress = operationProgress;
    }

    public void setOperationFile(Consumer<String> operationFile) {
        this.operationFile = operationFile;
    }

    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    private void updateStatus(Path src, Path dst,String status, Double progress) {
        if (operationListener != null)
            operationListener.accept(src, dst,status);

        if (this.operationProgress != null)
            this.operationProgress.accept(progress);
    }

    private void updateFileStatus(String file) {
        if (this.operationFile != null)
            this.operationFile.accept(file);
    }

}
