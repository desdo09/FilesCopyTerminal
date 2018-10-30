package UI;

import Classes.DB.Config;
import Classes.Entities.Category;
import Classes.Entities.Product;
import Classes.Logic.Logic;
import Classes.Logic.VerticalReport;
import Classes.Utils.Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static Scanner in = new Scanner(System.in);
    private final String[] directories = new String[]{"Barcode", "Vertical", "noBarcode"};
    private final String[] filesTypes = new String[]{"All", "BarCode", "BarCode Image file","BarCode Video file","Image files","Video files"};
    private final Map<String, String> sources = new HashMap<String, String>();
    private final Map<String, String> destination = new HashMap<String, String>();
    private final Map<String, Boolean> replace = new HashMap<String, Boolean>();
    private boolean useApi = false;
    private Logic logic = Logic.getInstance();
    private int selecedFileType = -1;
    private boolean DEBUG = false;

    public static void main(String[] args) {

        (new Main()).start();

    }

    public void start() {
        String option = "";
        do {
            logic.setOperationProgress((progress)->{
                System.out.println("LOG - Progress: " + progress + "%");
            });
            logic.setOperationListener((s,d,st)->{
                if(d != null)
                    System.out.println("LOG - Status: " + st + " for copy from " + s + " to " + d);
                else
                    System.out.println("LOG - Status: " + st + " for copy from " + s);
            });
            try {

                System.out.println("Select option:");
                System.out.println("\t <1>Set " + directories[0] + " source");
                System.out.println("\t <2>Set " + directories[0] + " destination");
                System.out.println("\t <3>Set " + directories[0] + " replace");
                System.out.println("\t <4>Set use api");

                System.out.println("\t <5>Set " + directories[1] + " source");
                System.out.println("\t <6>Set " + directories[1] + " destination");
                System.out.println("\t <7>Set " + directories[1] + " replace");

                System.out.println("\t <8>Set " + directories[2] + " source");
                System.out.println("\t <9>Set " + directories[2] + " destination");
                System.out.println("\t <10>Set " + directories[2] + " replace");


                System.out.println("\t <11>Set file type");

                System.out.println("\t <12>Copy all");
                System.out.println("\t <13>Copy " + directories[0]);
                System.out.println("\t <14>Copy " + directories[1]);
                System.out.println("\t <15>Copy " + directories[2]);

                System.out.println("\t <16>Print settings");


                option = in.nextLine();
                doAction(Integer.parseInt(option));
            } catch (NumberFormatException e) {
                if(DEBUG) e.printStackTrace();
                System.out.println("Error: Invalid option");
            } catch (Exception e) {
                if(DEBUG) e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }
        } while (!Objects.equals(option, "0"));
    }

    public void doAction(int option) throws Exception {
        switch (option) {
            case 1:
                System.out.println("Folder path");
                setSource(in.nextLine(), 0);
                break;
            case 2:
                System.out.println("Folder path");
                setDestination(in.nextLine(), 0);
                break;
            case 3:
                setReplace(0);
                break;
            case 4:
                useApi = !useApi;
                break;
            case 5:
                System.out.println("Folder path");
                setSource(in.nextLine(), 1);
                break;
            case 6:
                System.out.println("Folder path");
                setDestination(in.nextLine(), 1);
                break;
            case 7:
                setReplace(1);
                break;
            case 8:
                System.out.println("Folder path");
                setSource(in.nextLine(), 2);
                break;
            case 9:
                setDestination(in.nextLine(), 2);
                break;
            case 10:
                setReplace(2);
                break;
            case 11:
                setSelecedFileType();
                break;
            case 12:
                copyAll(true,true,true);
                break;
            case 13:
                copyAll(true,false,false);
                break;
            case 14:
                copyAll(false,true,false);
                break;
            case 15:
                copyAll(false,false,true);
                break;
            case 16:
                System.out.println("*************************SETTINGS**********************");
                printAll();
                System.out.println("*******************************************************");
                break;
            case 17:
                System.out.println("Debug is " +(DEBUG = !DEBUG));
                break;
            default:
                throw new Exception("Invalid option");
        }
    }

    private void setSource(String path, int type) throws Exception {
        if (!Files.exists(Paths.get(path)))
            throw new Exception("Invalid source path: " + path);

        String last = sources.get(directories[type]);
        sources.put(directories[type], path);
        if (type != 0)
            return;

        String temp = sources.get(directories[1]);

        if (temp == null || temp.isEmpty() || (last == null && temp.equals(last))) {
            sources.put(directories[1], path);
        }
    }

    private void setDestination(String path, int type) {

        String last = destination.get(directories[type]);
        destination.put(directories[type], path);
        if (type != 0)
            return;

        String temp = destination.get(directories[1]);

        if (temp == null || temp.isEmpty() || (last == null && temp.equals(last))) {
            destination.put(directories[1], path);
        }

        temp = sources.get(directories[2]);

        if (temp == null || temp.isEmpty() || (last == null && temp.equals(last))) {
            sources.put(directories[2], path + File.separator + logic.getConfig().getNoMatchFolder());
        }

        temp = destination.get(directories[2]);

        if (temp == null || temp.isEmpty() || (last == null && temp.equals(last))) {
            destination.put(directories[2], path);
        }

    }

    private void setReplace(int type) {
        Boolean temp = replace.get(directories[type]);
        if (temp == null)
            replace.put(directories[type], true);
        else
            replace.put(directories[type], !temp);
    }

    private void setSelecedFileType() throws Exception {
        System.out.println("Select file type:");
        for (int i = 0; i < filesTypes.length; i++) {
            System.out.print("<" + (i + 1) + "> " + filesTypes[i] + "  ");
        }
        System.out.println();
        int tempType = Integer.parseInt(in.nextLine());
        if (tempType <= 0 || tempType > filesTypes.length)
            throw new Exception("Invalid option");

        selecedFileType = tempType-1;
    }


    private void copyAll(boolean barcode, boolean vertical, boolean noBarcode) throws Exception {

        if(selecedFileType == -1)
            throw new Exception("File type not set");

        if (barcode) {
            System.out.println("*************************Coping Barcode**********************");
            if (useApi && logic.getConfig().getAPiUrl().isEmpty())
                throw new Exception("API is not set");

            if( sources.get(directories[0]) == null)
                throw new Exception(directories[0] + " source is not set");

            if( destination.get(directories[0]) == null)
                throw new Exception(directories[0] + " destination is not set");

            replace.putIfAbsent(directories[0], false);

            //BarCode
            copy(
                    sources.get(directories[0]),
                    destination.get(directories[0]),
                    useApi,
                    replace.get(directories[0]),
                    false
            );
            System.out.println("*************************END COPY**********************");
        }
        if (vertical) {

            System.out.println("*************************Coping Vertical**********************");

            if( sources.get(directories[1]) == null)
                throw new Exception(directories[1] + " source is not set");

            if( destination.get(directories[1]) == null)
                throw new Exception(directories[1] + " destination is not set");

            replace.putIfAbsent(directories[1], false);

            //Vertical
            List<Path> verticalDirs = logic.getFolders(sources.get(directories[1]), false, "(?i)vertical");
            for (Path v : verticalDirs) {
                copy(
                        v.toString(),
                        destination.get(directories[1]),
                        false,
                        replace.get(directories[1]),
                        true
                );
            }

            System.out.println("*************************END COPY**********************");
        }

        if (noBarcode && Files.exists(Paths.get(sources.get(directories[2])))) {

            System.out.println("*************************Coping NO Barcode**********************");
            if( sources.get(directories[2]) == null)
                throw new Exception(directories[2] + " source is not set");

            if( destination.get(directories[2]) == null)
                throw new Exception(directories[2] + " destination is not set");

            replace.putIfAbsent(directories[2], false);
            //NoBarcode
            copy(
                    sources.get(directories[2]),
                    destination.get(directories[2]),
                    false,
                    replace.get(directories[2]),
                    true
            );
            System.out.println("*************************END COPY**********************");
        }

        VerticalReport report = new VerticalReport(sources.get(directories[0]));
        report.toExcel();
        report.toJson();

        System.out.println("Progress completed");


    }


    private boolean copy(String source, String destination, boolean api, boolean replace, boolean searchInFile) throws Exception {
        logic.getConfig().setFileSearch(searchInFile);
        System.out.println("coping from " + source + " to " + destination + " replace " + replace);

        if (source == null || source.isEmpty())
            throw new Exception("Invalid source folder");

        if (destination == null || destination.isEmpty())
            throw new Exception("Invalid destination folder");

        if (destination.contains(source))
            throw new Exception("Invalid destination folder\nDestination folder cannot be inside of source folder");

        if (selecedFileType == 0)
            throw new Exception("Please select file type");


        //Reset status text
        System.out.println("Getting barcode files");

        // Get files in path
        List<Path> paths = logic.getFiles(source, true, getFilesRegex());
        if (paths.isEmpty()) {
            System.out.println("No files found in " + source);
            return true;
        }
        //Update status text
        System.out.println("total files: " + paths.size());

        // In case that api is not required, just copy files
        if (!api) {
            logic.copyFiles(paths, destination, getQueryRegex(), replace);
            return true;
        }

        // In case that api is required
        //Hash map to save product barcode -> product category
        HashMap<String, String> dst = new HashMap<>();
        Map<Long, Category> allCategories = logic.getCategoriesManager().getAllCategoriesMap();
        for (Product p : logic.getProductsManager().getAllProducts()) {
            Category c = allCategories.get(p.getCategoryId());
            if (c == null) {
                dst.put(p.getBarcode(), p.getCategory());
                continue;
            }

            dst.put(p.getBarcode(), c.getFinalCategory());
        }


        // Copy files by using api
        logic.copyFiles(paths, destination, dst, getQueryRegex(), replace);

        return true;


    }


    private String getFilesRegex() {
        try {
            Config config = logic.getConfig();
            switch (selecedFileType) {
                case 0:
                    return null;
                case 1:
                    return config.getBarReg();
                case 2:
                    return "((" + config.getBarReg() + ")(.?)+(\\.(?i)(" + Utils.typeToRegex(config.getImgTypes()) + "))$)";
                case 3:
                    return "((" + config.getBarReg() + ")(.?)+(\\.(?i)(" + Utils.typeToRegex(config.getVideosTypes()) + "))$)";
                case 4:
                    return "([^\\s]+(\\.(?i)(" + Utils.typeToRegex(config.getImgTypes()) + "))$)";
                case 5:
                    return "([^\\s]+(\\.(?i)(" + Utils.typeToRegex(config.getVideosTypes()) + "))$)";
            }
        } catch (Exception e) {
            if(DEBUG) e.printStackTrace();
            e.printStackTrace();
        }
        return null;
    }

    private String getQueryRegex() {

        if (selecedFileType > 0 && selecedFileType < 6)
            return Logic.getInstance().getConfig().getBarReg();

        return null;
    }

    private void printAll() {
        for (String s : directories) {
            System.out.println(s + " source: " + sources.get(s));
            System.out.println(s + " destination: " + destination.get(s));
            Boolean temp = replace.get(s);
            System.out.println(s + " replace: " + (temp != null && temp));

            System.out.println();
        }

        System.out.println("Images files type: " + logic.getConfig().getImgTypes());
        System.out.println("Videos files type: " + logic.getConfig().getVideosTypes());
        System.out.println("Barcode regex: " + logic.getConfig().getBarReg());

        System.out.println();
        System.out.println("File Type: " + (selecedFileType == -1 ?"NONE" : filesTypes[selecedFileType]));
        System.out.println("Using API: " + useApi);

    }

}
