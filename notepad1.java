import java.util.Scanner;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class extends JFrame to inherit all methods for active use.
 * This class implements the interface ActionListener to handle user actions
 */
interface note{
    void initializeMenus();
    void saveFile();
    void initializeKeyBindings() ;
    void openFile() ;
    void exitFile(boolean clear) ;
}

 abstract class notepad2 extends JFrame implements ActionListener,note{
     private         JMenuBar      menuBar;
    private         JTextArea     txtArea;
    private final   JMenu[]       menu = new JMenu[4];
    private final   JMenuItem[]   menuItem = new JMenuItem[7];
    private         JMenu         fontMenu;
    private final   JMenuItem[]   fontItem = new JMenuItem[20];
    //  Declaring all swing components for access throughout multiple methods
   
    /**
     * This constructor initializes all components, sets the workspace
     * preferences and connects everything to the JFrame superclass
     */
    public notepad2() {

        //  Assigns a title to the notepad
        super("The Notepad");

        //  Initializes the text area for user input
        txtArea = new JTextArea(24, 54);

        //  Sets formatting to break line at whole words, and adding
        //  some border to the JTextArea.
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // If content exceeds size of viewport, make scrolling through the
        // content possible
        JScrollPane scrollPane = new JScrollPane(txtArea);

        //  initializes all menus, menuItems and corresponding key bindings
        initializeMenus();
        initializeKeyBindings();

        //  adding menus and JTextArea to the JFrame
        add(menuBar);
        add(scrollPane, BorderLayout.SOUTH);

        //  Presenting the JFrame, and stating the programs close operation
        setVisible(true);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * This method constructs and adds names and actionListener's to
     * JMenu's and JMenuItem's, which are then added to the JMenuBar.
     */
    public void initializeMenus() {

        //  Declaring the menu bar
        menuBar = new JMenuBar();

        //  Assigns names to the main menu and adding them to
        //  actionListener's and the menu bar
        String[] menuNames = {"File", "Edit", "Format", "Help"};

        for (int i = 0; i < menu.length; i++) {
            menu[i] = new JMenu(menuNames[i]);
            menu[i].addActionListener(this);
            menuBar.add(menu[i]);
        }

        //  Assigns names to the menu items and adding them to
        //  actionListener's and the main menu
        String[] itemString = {"Open", "Save", "Exit", "Text Wrap",
                "No Text Wrap", "Clear", "About Simple Notepad"};

        for (int i = 0; i < menuItem.length; i++) {
            menuItem[i] = new JMenuItem(itemString[i]);
            menuItem[i].addActionListener(this);

            if (i < 3)      { menu[0].add(menuItem[i]);} // Adding to 'File'
            else if (i < 5) { menu[1].add(menuItem[i]);} // Adding to 'Edit'
            else if (i < 6) { menu[2].add(menuItem[i]);} // Adding to 'Format'
            else            { menu[3].add(menuItem[i]);} // Adding to 'Help'
        }

        // Initializing the menu for fonts and adding
        // menu items with font sizes
        fontMenu = new JMenu("Font");

        for (int i = 0; i < fontItem.length; i++) {
            fontItem[i] = new JMenuItem(String.valueOf((i + 1) * 2));
            fontItem[i].addActionListener(this);
            fontMenu.add(fontItem[i]);
        }

        //  Adding the 'Font' menu to the 'Edit' menu
        menu[1].add(fontMenu);
    }

    /**
     * This method assigns a mnemonic character for each menu and menuItem.
     * If the program is viewed on a Mac, alt(option) key must be pressed
     * to view the mnemonic icon.
     */
    public void initializeKeyBindings() {

        //  The assignment had mnemonic icons on different indexes in its
        //  presentation. To deliver exactly what the assignment specified,
        //  and to keep the code readable, i wrote each mnemonic statement
        //  separately.
        menu[0].setMnemonic(KeyEvent.VK_F);     //  File
        menu[1].setMnemonic(KeyEvent.VK_E);     //  Edit
        menu[2].setMnemonic(KeyEvent.VK_O);     //  Format
        menu[3].setMnemonic(KeyEvent.VK_H);     //  Help

        
        menuItem[0].setMnemonic(KeyEvent.VK_O); //  Open
        menuItem[1].setMnemonic(KeyEvent.VK_S); //  Save
        menuItem[2].setMnemonic(KeyEvent.VK_E); //  Exit
        menuItem[3].setMnemonic(KeyEvent.VK_W); //  Text Wrap
        menuItem[4].setMnemonic(KeyEvent.VK_N); //  No Text Wrap
        menuItem[5].setMnemonic(KeyEvent.VK_C); //  Clear
        menuItem[6].setMnemonic(KeyEvent.VK_A); //  About

        fontMenu.setMnemonic(KeyEvent.VK_F);    //  Font
    }

    /**
     * This method prompts user to enter name of file that will be attempted
     * to be opened
     *
     * @exception FileNotFoundException On input error.
     */
    public void openFile() {

        //  Prompts user for the name of the file to read
        String fileOutput = "";
        String fileName = JOptionPane.showInputDialog(txtArea, "Enter location here!");

        //  If the user has typed anything, attempt to open file
        if (fileName != null) {
            try {

                //  Read everything from file until the end-anchor is found: \Z
                Scanner content = new Scanner(new File(fileName));
                content.useDelimiter("\\Z");

                while (content.hasNext()) {
                    fileOutput += content.next();
                }

                //  Store content read from file in text area and close dialog
                //  box.
                txtArea.setText(fileOutput);
                content.close();

                //  If file was not found, print the exception that was thrown
            } catch (FileNotFoundException fnfe) {
                System.out.println("This error was thrown: " + fnfe);
            }
        }
    }

    /**
     * This method  prompts user to enter name of file that will be attempted
     * to be stored
     *
     * @exception IOException On output error.
     */
    public void saveFile() {

        //  Prompts user for the name of the file to be stored
        String fileName = JOptionPane.showInputDialog(txtArea, "Enter location here!");

        //  If user has typed in anything, attempt to save file
        if (fileName != null) {

            //  Copy content from the text area into the file being created
            try {
                PrintWriter content = new PrintWriter(new File(fileName));
                content.write(txtArea.getText());
                content.close();

                //  If there was an error, print the exception
            } catch (IOException ioe) {
                System.out.println("This error was thrown: " + ioe);
            }
        }
    }

    /**
     *  This method prompts user with three options
     *      - Save text area to file through method saveFile()
     *      - Exit the program without storing file
     *      - Go back to superclass JFrame
     *
     * @param clear handles what to do after a user has selected an option.
     *              If clear is true, do not exit program after statement.
     *              If clear is false, exit program after statement.
     */
    public void exitFile(boolean clear) {

        //  Prompts user with options to store file, not to store file or
        int options = JOptionPane.showConfirmDialog(txtArea, "Are you sure you want to exit?");

        //  If user acted on the YES option
        if (options == JOptionPane.YES_OPTION) {
            saveFile();

            // And first acted on the 'Clear' menuItem
            if (clear) {
                txtArea.setText("");

            // And first acted on the 'Exit' menuItem
            } else {
                System.exit(0);
            }
        }

        //  If user acted on the NO option
        else if (options == JOptionPane.NO_OPTION) {

            // And first acted on the 'Clear' menuItem
            if (clear) {
                txtArea.setText("");

            // And first acted on the 'Exit' menuItem
            } else {
                System.exit(0);
            }
        }
    }

    public void clearFile(boolean clear) {



            // And first acted on the 'Clear' menuItem
        if (clear) {
            txtArea.setText("");

        // And first acted on the 'Exit' menuItem
        } else {
            System.exit(0);
        }
    }
        


    /**
     * This method handles user actions on the menu's
     *
     * @param e handles actionListeners
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        //  Stores a variable with the source of the action event for
        //  readable event handling
        Object source = e.getSource();

        //  Names of menuItems that the user can act on is a comment to its
        //  preceding code
        //  Open:
        if (source == menuItem[0]) {
            openFile();
        }

        //  Save:
        if (source == menuItem[1]) {
            saveFile();
        }

        //  Exit:
        if (source == menuItem[2]) {

            //  Exit's the file directly if JTextArea is empty
            if (txtArea.getText().equals("")){
                System.exit(0);
            }
            else {
                exitFile(false);
            }
        }

        //  Text Wrap:
        if (source == menuItem[3]) {
            txtArea.setLineWrap(true);
            txtArea.setWrapStyleWord(true);
        }

        //  No Text Wrap:
        if (source == menuItem[4]) {
            txtArea.setLineWrap(false);
            txtArea.setWrapStyleWord(false);
        }

        //  Font:
        //  Assigns new font size to match the users size preference entered
        for (int i = 0; i < fontItem.length; i++) {
            if (source == fontItem[i]) {
                txtArea.setFont(new Font("times new roman", Font.PLAIN,((i + 1) * 2)));
            }
        }

        //  Clear:
        //  If there is content in JTextArea, prompt user to save content
        if (source == menuItem[5] && !txtArea.getText().equals("")) {
                clearFile(true);
        }

        //  About:
        if (source == menuItem[6]) {
            JOptionPane.showMessageDialog(txtArea,"This application was\nmade for a Java Project" +"\n\nby Nihar, Shreyas, Siddhant, Ritvik");
        }
    }

    /**
     * This method is main, it declares and initializes a new Object of
     * the class notepad
     */
   
   }


class notepad1 extends notepad2 implements note,ActionListener{
     public static void main(String[] args) {
        notepad1 st = new notepad1();

}
}