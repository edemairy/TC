package com.ibm.tools.ffvt.gui;

/**
 * This class is an implementation of ActionListener that is used for performing an action when choose file button is pressed.
 * 
 * Thread Safety:
 * This class is immutable, but not thread safe since it uses not thread safe JTextField instance. This class should be accessed from the GUI thread.
 */
private class ChooseFileButtonListener implements ActionListener {
    /**
     * The text field to be updated with chosen file path. Is initialized in the constructor and never changed after that. Cannot be null. Is used in actionPerformed().
     */
    private final JTextField textField;

    /**
     * Creates an instance of ChooseFileButtonListener.
     * 
     * Parameters:
     * textField - the text field to be updated with chosen file path
     * 
     * Throws:
     * IllegalArgumentException if textField is null
     * 
     * Implementation Notes:
     * this.textField = textField;
     * @throws IllegalArgumentException if textField is null
     * @param textField the text field to be updated with chosen file path
     */
    public synchronized ChooseFileButtonListener(JTextField textField) {
    }

    /**
     * This method is executed when a button for choosing file path is pressed.
     * 
     * Parameters:
     * event - the action event
     * 
     * Implementation Notes:
     * 1. JFileChooser fileChooser = new JFileChooser();
     * 2. fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
     * 3. int returnValue = fileChooser.showOpenDialog(null);
     * 4. If returnValue == JFileChooser.APPROVE_OPTION then
     *      4.1. File file = fileChooser.getSelectedFile();
     *      4.2. String filePath = file.getAbsolutePath();
     *      4.3. textField.setText(filePath);
     * @param event the action event
     */
    public synchronized void actionPerformed(ActionEvent event) {
    }
}

