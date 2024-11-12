package nz.co.tmsandbox.utilities;

import nz.co.tmsandbox.engine.IAccessor;
import nz.co.tmsandbox.engine.TestDataAccessor;
import nz.co.tmsandbox.webinteractivities.DebugMessageLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class IOOperations {
    /**
     * Deletes filename
     *
     * @param fileName filename
     */
    public void deleteFile(String fileName) {
        String fileWithPath = IAccessor.testDataAccessor.getBuTestDataFilesPath() + File.separator + fileName;
        if (Paths.get(fileWithPath).toFile().exists()) {
            try {
                Files.delete(Paths.get(fileWithPath));
                DebugMessageLogger.debugMessageLogger.logInformation("File deleted " + fileWithPath);
            } catch (IOException e) {
                DebugMessageLogger.debugMessageLogger.logException("Exception while deleting the file " + fileWithPath, e);
            }
        }
    }

    /**
     * Deletes folders
     *
     * @param delFolderName folder name
     */
    public void deleteFolder(String delFolderName) {
        Path path = Paths.get(delFolderName);
        try {
            if (new File(delFolderName).exists())
                Files.walk(path).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            DebugMessageLogger.debugMessageLogger.logException("Exception while deleting the folder " + delFolderName, e);
        }
    }


    /**
     * Gets the file size
     *
     * @param fileName filename
     * @return returns the file size
     */
    public int getFileSize(String fileName) {
        int fileSize = 0;
        try {
            fileSize = (int) Files.size(Paths.get(fileName));
            DebugMessageLogger.debugMessageLogger.logInformation("Retrieving file size, file " + fileName + " size " + fileSize);
        } catch (IOException e) {
            DebugMessageLogger.debugMessageLogger.logException("Exception in retrieving file size, file " + fileName + " size " + fileSize, e);
        }
        return fileSize;
    }

}
