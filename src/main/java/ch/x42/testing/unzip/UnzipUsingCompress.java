package ch.x42.testing.unzip;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class UnzipUsingCompress {
    public static void main(String[] args) throws Exception {
        if(args.length != 2) {
            throw new Exception("Usage: " + UnzipUsingCompress.class.getSimpleName() + " <zip file> <output folder>");
        }
        unzip(args[0], args[1]);
    }

    public static void unzip(String filename, String outputFolderName) throws Exception {

        final File outputFolder = new File(outputFolderName);
        final File inputFile = new File(filename);
        System.err.println("Input file: :" + inputFile.getName());
        System.err.println("Output folder: :" + outputFolder.getName());

        try (
            InputStream is = new FileInputStream(inputFile);
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
        ) {
            ZipArchiveEntry entry = null;

            while ((entry = (ZipArchiveEntry) ais.getNextEntry()) != null) {
                File outFile = new File(outputFolder + File.separator + entry.getName());
                if(outFile.isDirectory()) {
                    if(!outFile.exists()) {
                        outFile.mkdirs();
                    }
                } else if(outFile.exists()) {
                    System.err.println("File exists, won't overwrite it: " + entry.getName());
                } else {
                    System.err.println("Extracting: " + entry.getName());
                    FileUtils.copyInputStreamToFile(ais, outFile);
                }
            }
        }
    }
}