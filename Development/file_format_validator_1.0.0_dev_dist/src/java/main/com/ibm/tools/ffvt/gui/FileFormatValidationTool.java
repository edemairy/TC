package com.ibm.tools.ffvt.gui;

/**
 * This is main class of desktop GUI application that performs file format validation. This class simply reads configuration Properties file and delegates execution to FileFormatValidatorFrame. This class uses Apache Commons Logging library to perform logging of errors and debug information.
 * 
 * Thread Safety:
 * This class is immutable and thread safe. It's safe to use multiple instances of this application when different output file paths are used to avoid file access conflicts.
 */
public class FileFormatValidationTool {
    /**
     * The Apache Commons logger to be used by this class for logging errors and debug information. Is initialized during class loading and never changed after that. Cannot be null. Is used in main().
     */
    private static final Log LOG = LogFactory.getLog(FileFormatValidationTool.class);

    /**
     * Empty private constructor.
     * 
     * Implementation Notes:
     * Do nothing.
     */
    private FileFormatValidationTool() {
    }

    /**
     * This is the main method of File Format Validation Tool desktop application.
     * This method MUST NOT throw any exception. Instead it must print out the detailed error explanation to the standard error output and terminate.
     * 
     * Parameters:
     * args - the command line arguments
     * 
     * Implementation Notes:
     * 1. String configFileName = "FileFormatValidationTool.properties";
     * 2. If args.length > 1 or (args.length == 0 and not new File(configFileName).isFile()) then
     *      2.1. System.out.println("Usage: java -jar FileFormatValidationTool.jar [<config_file_name>]");
     *      2.2. System.out.println("              where <config_file_name> is the path of the properties configuration file for the utility (default is FileFormatValidationTool.properties)");
     *      2.3. System.exit(1);
     * 3. If args.length == 1 then
     *      3.1. configFileName = args[0];
     * 4. Create Properties instance for configuration:
     *      properties = new Properties();
     * 5. Create file input stream for configuration file:
     *      inputStream = new FileInputStream(configFileName);
     * 6. Load configuration properties:
     *      properties.load(inputStream);
     * 7. Create the main window of GUI application:
     *      frame = new FileFormatValidatorFrame(properties);
     * 8. Activate the window:
     *      frame.setVisible(true);
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
}

