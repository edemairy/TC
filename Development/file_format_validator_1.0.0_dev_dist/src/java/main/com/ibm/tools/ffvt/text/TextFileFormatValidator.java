package com.ibm.tools.ffvt.text;

import java.util.regex.*;
import com.ibm.tools.ffvt.*;
import com.ibm.tools.ffvt.text.FieldExtractor;
import java.io.File;
import java.lang.Class;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class is an implementation of FileFormatValidator that can be used for validating text files that contain tabular data. It assumes that each line of a file contains data for a single row and supports three different line types: header, data and footer. Lines of each type can have different formats. This class uses pluggable FieldExtractor instances to extract field values from the text lines, and then it uses pluggable FieldsValidator instance to validate extracted field values. This class uses Apache Commons Logging library to perform logging of errors and debug information.
 * 
 * Thread Safety:
 * This class is immutable and thread safe. It uses thread safe FieldExtractor and FieldsValidator instances.
 * It's assumed that internal logging implementation used by Apache Commons Logging library is thread safe.
 */
public class TextFileFormatValidator implements FileFormatValidator {

    /**
     * The user-defined format name that corresponds to this validator. This value is taken from the configuration is simply used to initialize ValidationResult#expectedFormatName property. Is initialized in the constructor and never changed after that. Cannot be null/empty. Is used in validate().
     */
    private final String formatName;
    /**
     * The mapping from line type to configuration details for this line type. Collection instance is initialized in the constructor and never changed after that. Cannot be null/empty, cannot contain null key or value. Is used in validate(), processLine() and detectLineType(). Has a protected getter to be used by subclasses.
     */
    private final Map<LineType, LineTypeDetails> lineTypes;
    /**
     * The pattern that matches all lines that need to be ignored. Is initialized in the constructor and never changed after that. Is null if no lines should be ignored. Is used in validate().
     */
    private final Pattern ignoredLinePattern;
    /**
     * The Apache Commons logger to be used by this class for logging errors and debug information. Is initialized during construction and never changed after that. Cannot be null. Is used in validate(). Has a protected getter to be used by subclasses.
     */
    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Creates an instance of TextFileFormatValidator.
     * See section 3.2.3 of CS for details about the configuration parameters. Please use value constraints provided in that section to check whether values read from the configuration file are valid.
     * 
     * Parameters:
     * configFilePath - the path of the XML configuration file
     * 
     * Throws:
     * IllegalArgumentException if configFilePath is null/empty
     * FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration file
     *  
     * @throws IllegalArgumentException if configFilePath is null/empty
     * @throws FileFormatValidatorConfigurationException if some error occurred when initializing this class using the provided configuration file
     * @param configFilePath the path of the XML configuration file
     */
    public TextFileFormatValidator(String configFilePath) {
        if ((configFilePath == null) || (configFilePath.isEmpty())) {
            throw new IllegalArgumentException("configFilePath must not be null/empty.");
        }

//           * Implementation Notes:
//     * 1. Get document builder factory:
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            //* 2. Get document builder:
            DocumentBuilder db = dbf.newDocumentBuilder();
            //* 3. Parse the configuration XML file:
            Document document = db.parse(new File(configFilePath));
            //* 4. Get the root element:
            Element rootElement = document.getDocumentElement();
            //* 5. Get format name from the attribute:
            formatName = rootElement.getAttribute("formatName");
            if (rootElement.hasAttribute("ignoredLinePattern")) {
                //*      6.1. Get pattern string for lines to be ignored:
                String ignoredLinePatternStr = rootElement.getAttribute("ignoredLinePattern");
                //*      6.2. Compile the regexp pattern:
                ignoredLinePattern = Pattern.compile(ignoredLinePatternStr);
            } else {
                ignoredLinePattern = null;
            }

            //* 8. Create mapping from line types to configuration:
            lineTypes = new HashMap<LineType, LineTypeDetails>();

            Element lineTypeElement;
            NodeList elements = rootElement.getElementsByTagName("lineType");
            for (int i = 0; i < elements.getLength(); i++) {
                lineTypeElement = (Element) elements.item(i);
                //            Get line type from the attribute:
                String lineTypeStr = lineTypeElement.getAttribute("type");
                //          Convert line type to enum value:
                LineType lineType = LineType.valueOf(lineTypeStr.toUpperCase());
                if (lineTypes.containsKey(lineType)) {
                    throw new FileFormatValidatorConfigurationException("");
                }
                //        Create line type details instance:
                LineTypeDetails lineTypeDetails = new LineTypeDetails();
                if (lineTypeElement.hasAttribute("pattern")) {
                    //Get pattern string from the attribute:
                    String patternStr = lineTypeElement.getAttribute("pattern");
                    //Compile the pattern to be used for identifying lines of this type:
                    Pattern pattern = Pattern.compile(patternStr);
                    //Set pattern to the details:
                    lineTypeDetails.setPattern(pattern);
                    if (lineTypeElement.hasAttribute("num")) {
                        //              9.6.1. Parse attribute as integer:
                        int num = Integer.parseInt(lineTypeElement.getAttribute("num"));
                        //*              9.6.2. Set minimum expected lines number:
                        lineTypeDetails.setMinLinesNum(num);
                        //*              9.6.3. Set maximum expected lines number:
                        lineTypeDetails.setMaxLinesNum(num);
                        //*              9.6.4. Set exact expected lines number:
                        lineTypeDetails.setExactLinesNum(num);
                    } else {//*      9.7. Else
                        if (lineTypeElement.hasAttribute("minNum")) {
                            //*                         9.7.1.1. Set minimum expected lines number:
                            lineTypeDetails.setMinLinesNum(Integer.parseInt(lineTypeElement.getAttribute("minNum")));
                        }
                        if (lineTypeElement.hasAttribute("maxNum")) {
                            lineTypeDetails.setMaxLinesNum(Integer.parseInt(lineTypeElement.getAttribute("maxNum")));
                        }
                        Element extractorElement = (Element) lineTypeElement.getElementsByTagName("extractor");

                        String extractorType = extractorElement.getAttribute("type");

                        if (extractorType.equalsIgnoreCase("csv")) {
                            extractorType = "com.ibm.tools.ffvt.text.extractors.CSVFieldExtractor";
                        } else if (extractorType.equalsIgnoreCase("fixed")) {
                            extractorType = "com.ibm.tools.ffvt.text.extractors.FixedLengthFieldExtractor";
                        }
                        Class<FieldExtractor> extractorClass = (Class<FieldExtractor>) Class.forName(extractorType);

                        Constructor<FieldExtractor> extractorConstructor = extractorClass.getConstructor(Element.class);

                        FieldExtractor fieldExtractor = extractorConstructor.newInstance(extractorElement);
                        lineTypeDetails.setFieldExtractor(fieldExtractor);
                        ArrayList<FieldsValidator> validators = new ArrayList<FieldsValidator>();
                        Element validatorsElement = (Element) lineTypeElement.getElementsByTagName("validators");
                        if (validatorsElement != null) {


                            for (i = 0; i < validatorsElement.getElementsByTagName("validator").getLength(); ++i) {
                                Element validatorElement = (Element) validatorsElement.getElementsByTagName("validator").item(i);
                                String validatorType = validatorElement.getAttribute("type");
                                if (validatorType.equalsIgnoreCase("regex")) {
                                    validatorType = "com.ibm.tools.ffvt.text.validators.RegexFieldValidator";
                                } else if (validatorType.equalsIgnoreCase("fieldType")) {
                                    validatorType = "com.ibm.tools.ffvt.text.validators.FieldTypeValidator";
                                } else if (validatorType.equalsIgnoreCase(
                                        "numberComparison")) {
                                    validatorType = "com.ibm.tools.ffvt.text.validators.NumberComparisonValidator";
                                } else if (validatorType.equalsIgnoreCase(
                                        "dateComparison")) {
                                    validatorType = "com.ibm.tools.ffvt.text.validators.DateComparisonValidator";
                                } else if (validatorType.equalsIgnoreCase(
                                        "twoNumberFieldsComparison")) {
                                    validatorType = "com.ibm.tools.ffvt.text.validators.TwoNumberFieldsComparisonValidator";
                                } else if (validatorType.equalsIgnoreCase(
                                        "twoDateFieldsComparison")) {
                                    validatorType = "com.ibm.tools.ffvt.text.validators.TwoDateFieldsComparisonValidator";
                                }

                                Class<FieldsValidator> validatorClass = (Class<FieldsValidator>) Class.forName(validatorType);
                                Constructor<FieldsValidator> validatorConstructor = validatorClass.getConstructor(Element.class);
                                FieldsValidator fieldsValidator = validatorConstructor.newInstance(validatorElement);
                                validators.add(fieldsValidator);

                            }
                        }
                        lineTypeDetails.setFieldsValidators(validators);
                        this.lineTypes.put(lineType, lineTypeDetails);
                    }
                }
            }
        } catch (Exception e) {
            throw new FileFormatValidatorConfigurationException("A problem occurred when reading the configuration" + e.getMessage());
        }
    }

    /**
     * Validates a file with the specified path.
     *
     * Parameters:
     * filePath - the path of the file to be validated
     *
     * Returns:
     * the validation result (not null)
     *
     * Throws:
     * IllegalArgumentException if filePath is null/empty
     * FileFormatValidationException if some fatal error occurred when performing the validation (exception is not thrown when file has invalid format)
     *
     * Implementation Notes:
     * 1. Create file reader for file to be validated:
     *      fileReader = new FileReader(filePath);
     * 2. Create buffered reader:
     *      reader = new BufferedReader(fileReader);
     * 3. Create validation context instance:
     *      context = new ValidationContext();
     * 4. Create a list for not ignored lines from the file:
     *      lines = new ArrayList<String>();
     * 5. While (true) do: // breaks when end of the file is reached
     *      5.1. Read the next line:
     *              String line = reader.readLine();
     *      5.2. If line == null then break;
     *      5.3. boolean shouldBeIgnored;
     *      5.4. If ignoredLinePattern != null then
     *              5.4.1. Get ignored pattern matcher for this line:
     *                         Matcher matcher = ignoredLinePattern.matcher(line);
     *              5.4.2. Check if this line should be ignored according to the pattern:
     *                         shouldBeIgnored = matcher.matches();
     *      5.5. Else
     *              5.5.1. shouldBeIgnored = false;
     *      5.6. If not shouldBeIgnored then
     *              5.6.1. Add line to the list:
     *                         lines.add(line);
     * 6. Close the file:
     *      reader.close();
     * 7. Get position of the first line of the file counting from the end (0-based):
     *      int posFromEnd = lines.size() - 1;
     * 8. int index = 0; // will hold 0-based index of the currently processed line
     * 9. For each line from lines do:
     *      9.1. Set current line index to the context:
     *              context.setLineIndex(index);
     *      9.2. Set position of the current line from end to the context:
     *              context.setPositionFromEnd(posFromEnd);
     *      9.3. Process the next line:
     *              processLine(line, context);
     *      9.4. posFromEnd--;
     *      9.5. index++;
     * 10. For each (lineType; lineTypeDetails) from lineTypes do:
     *        10.1. Get the minimum expected lines number:
     *                  int minLinesNum = lineTypeDetails.getMinLinesNum();
     *        10.2. Get the maximum expected lines number:
     *                  int maxLinesNum = lineTypeDetails.getMaxLinesNum();
     *        10.3. Get the number of processed lines of this type:
     *                  int processedLinesNum = context.getProcessedLinesNumByType(lineType);
     *        10.4. If minLinesNum != -1 and processedLinesNum < minLinesNum then
     *                  10.4.1. Add error message to the context:
     *                                context.addError("Not enough lines of " + lineType + " type were found (minimum expected number is " + minLinesNum + ")");
     *        10.5. If maxLinesNum != -1 and processedLinesNum > maxLinesNum then
     *                  10.5.1. Add error message to the context:
     *                                context.addError("The number of expected lines of " + lineType + " type was exceeded (maximum expected number is " + maxLinesNum + ")");
     * 11. Create a validation result instance:
     *        result = new ValidationResult();
     * 12. Get errors list from the context:
     *        List<String> errors = context.getErrors();
     * 13. If errors.isEmpty() then
     *        13.1. Set value indicating that validation was successful:
     *                  result.setSuccessful(true);
     * 14. Set errors to the validation result:
     *        result.setErrors(errors);
     * 15. Set current timestamp to the validation result:
     *        result.setTimestamp(new Date());
     * 16. Set expected format name to the validation result:
     *        result.setExpectedFormatName(formatName);
     * 17. Set input file path to the validation result:
     *        result.setFilePath(filePath);
     * 18. Return result.
     * @throws IllegalArgumentException if filePath is null/empty
     * @throws FileFormatValidationException if some fatal error occurred when performing the validation (exception is not thrown when file has invalid format)
     * @param filePath the path of the file to be validated
     * @return the validation result (not null)
     */
    public ValidationResult validate(String filePath) {
        return null;
    }

    /**
     * Processes the next line of the file.
     * 
     * Parameters:
     * line - the line of the file
     * context - the validation context
     * 
     * Throws:
     * IllegalArgumentException if line or context is null
     * 
     * Implementation Notes:
     * 1. Detect the type of the line:
     *      LineType lineType = detectLineType(line, context);
     * 2. If lineType == null then return;
     * 3. String errorPrefix = "Line " + (context.getLineIndex() + 1) + " - ";
     * 4. If lineType == LineType.HEADER and context.getProcessedLinesNum(LineType.DATA) > 0 then
     *      4.1. Add error message to the context:
     *              context.addError(errorPrefix + "header line was found after data line(s)");
     * 5. Else if lineType == LineType.HEADER and context.getProcessedLinesNum(LineType.FOOTER) > 0 then
     *      5.1. Add error message to the context:
     *              context.addError(errorPrefix + "header line was found after the footer");
     * 6. Else if lineType == LineType.DATA and context.getProcessedLinesNum(LineType.FOOTER) > 0 then
     *      6.1. Add error message to the context:
     *              context.addError(errorPrefix + "data line was found after the footer");
     * 7. Increase the processed lines number for this line type:
     *      context.increaseProcessedLinesNum(lineType);
     * 8. Get configuration details for this line type:
     *      LineTypeDetails lineTypeDetails = lineTypes.get(lineType);
     * 9. Get field extractor to be used for this line type:
     *      FieldExtractor fieldExtractor = lineTypeDetails.getFieldExtractor();
     * 10. Extract fields from the line:
     *        List<String> fieldValues = fieldExtractor.extract(line);
     * 11. If exception was thrown on the previous step then
     *        11.1. Add error message to the context:
     *                  context.addError(errorPrefix + "fields cannot be extracted from the line");
     *        11.2. Log and ignore exception.
     * 12. Create a list for fields validation errors:
     *        fieldsValidationErrors = new ArrayList<String>();
     * 13. Get fields validators to be used for this line type:
     *        List<FieldsValidator> fieldsValidators = lineTypeDetails.getFieldsValidators();
     * 14. For each fieldsValidator from fieldsValidators do:
     *        14.1. Validate the extracted fields:
     *                  fieldsValidator.validate(fieldValues, fieldsValidationErrors);
     * 15. For each fieldsValidationError from fieldsValidationErrors do:
     *        15.1. Add error message to the context:
     *                  context.addError(errorPrefix + fieldsValidationError);
     * @throws IllegalArgumentException if line or context is null
     * @param context the validation context
     * @param line the line of the file
     */
    protected void processLine(String line, ValidationContext context) {
    }

    /**
     * Detects the type of the given file line.
     * 
     * Parameters:
     * line - the line which type should be detected
     * context - the validation context
     * 
     * Returns:
     * the detected line type or null if line type cannot be detected
     * 
     * Throws:
     * IllegalArgumentException if line or context is null
     * 
     * Implementation Notes:
     * 1. For each (lineType; lineTypeDetails) from lineTypes do:
     *      1.1. Get pattern to be used for identifying lines of this type:
     *              Pattern pattern = lineTypeDetails.getPattern();
     *      1.2. If pattern != null then
     *              1.2.1. Get matcher for this line:
     *                         Matcher matcher = pattern.matcher(line);
     *              1.2.2. Check if the current line matches the pattern:
     *                         boolean matches = matcher.matches();
     *              1.2.3. If matches then return lineType;
     * 2. Get details for header line type:
     *      LineTypeDetails headerDetails = lineTypes.get(LineType.HEADER);
     * 3. Get details for data line type:
     *      LineTypeDetails dataDetails = lineTypes.get(LineType.DATA);
     * 4. Get details for footer line type:
     *      LineTypeDetails footerDetails = lineTypes.get(LineType.FOOTER);
     * 5. If headerDetails != null and headerDetails.getExactLinesNum() != -1 and context.getLineIndex() < headerDetails.getExactLinesNum() and headerDetails.getPattern() == null then
     *      5.1. Return LineType.HEADER.
     * 6. If footerDetails != null and footerDetails.getExactLinesNum() != -1 and context.getPositionFromEnd() < footerDetails.getExactLinesNum() and footerDetails.getPattern() == null then
     *      6.1. Return LineType.FOOTER.
     * 7. If dataDetails != null and dataDetails.getPattern() == null then
     *      7.1. Return LineType.DATA.
     * 8. Add error message to the context:
     *      context.addError("Line " + (context.getLineIndex() + 1) + " - type of this line cannot be identified");
     * 9. Return null.
     * @throws IllegalArgumentException if line or context is null
     * @param context the validation context
     * @param line the line which type should be detected
     * @return the detected line type or null if line type cannot be detected
     */
    protected LineType detectLineType(String line, ValidationContext context) {
        return null;
    }

    /**
     * Retrieves the mapping from line type to configuration details for this line type.
     * 
     * Returns:
     * the mapping from line type to configuration details for this line type
     * @return the mapping from line type to configuration details for this line type
     */
    protected Map<LineType, LineTypeDetails> getLineTypes() {
        return null;
    }

    /**
     * Retrieves the Apache Commons logger to be used by subclasses for logging errors and debug information.
     * 
     * Returns:
     * the Apache Commons logger to be used by subclasses for logging errors and debug information
     * @return the Apache Commons logger to be used by subclasses for logging errors and debug information
     */
    protected Log getLog() {
        return null;
    }
}
