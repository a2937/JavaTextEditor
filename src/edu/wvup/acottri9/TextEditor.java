package edu.wvup.acottri9;

import javax.swing.*; // for the main JFrame design
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*; // for the GUI stuff
import java.awt.event.*; // for the event handling
import java.util.Map;
import java.util.Scanner; // for reading from a file
import java.io.*; // for writing to a file




public class TextEditor extends JFrame implements ActionListener, KeyListener  {

	private boolean inDarkTheme = false;
    private Highlighter.HighlightPainter paint = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
    private Highlighter highlighter;
    private JLabel labelLineCount;
	private JTextArea textArea = new JTextArea("",0, 0);
	private MenuBar ourMenuBar = new MenuBar(); // first, create a MenuBar item
	private Menu file = new Menu(); // our File menu
    private Menu statistics = new Menu(); // our Stats menu
	private Menu style = new Menu(); // our Style menu

	// what's going in File? let's see...
	private MenuItem openFile = new MenuItem();  // an open option
	private MenuItem saveFile = new MenuItem(); // a save option
	private MenuItem close = new MenuItem(); // and a close option!

	//in style
	private MenuItem darkTheme = new MenuItem(); // black on white instead of white on black
	private MenuItem fontName = new MenuItem(); // The name of the font
	private MenuItem fontSize = new MenuItem(); // The size of the font

    //in statistics
    private MenuItem vowelCount = new MenuItem(); // Displays a print out of the number of occurrences of each vowel.
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				TextEditor frame = new TextEditor();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TextEditor() {
		this.setSize(500, 300); // set the initial size of the window
		this.setTitle("Java Notepad Tutorial"); // set the title of the window
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // set the default close operation (exit when it gets closed)

        JScrollPane scroll = new JScrollPane (textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		this.textArea.setFont(new Font("Century Gothic", Font.BOLD, 12)); // set a default font for the TextArea
        this.textArea.addKeyListener(this);

        highlighter = textArea.getHighlighter();
		// this is why we didn't have to worry about the size of the TextArea!
		this.getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
		this.getContentPane().add(scroll);
		
		labelLineCount = new JLabel("");
		
		getContentPane().add(labelLineCount, BorderLayout.SOUTH);

		// add our menu bar into the GUI
		this.setMenuBar(this.ourMenuBar);
		this.ourMenuBar.add(this.file); // we'll configure this later
		this.ourMenuBar.add(this.style);
		this.ourMenuBar.add(this.statistics);
		// first off, the design of the menuBar itself. Pretty simple, all we need to do
		// is add a couple of menus, which will be populated later on
		this.file.setLabel("File");
		this.style.setLabel("Style");
		this.statistics.setLabel("Statistics");
		// now it's time to work with the menu. I'm only going to add a basic File menu
		// but you could add more!
		
		// now we can start working on the content of the menu~ this gets a little repetitive,
		// so please bare with me!

		this.darkTheme.setLabel("Invert color palette");
		this.darkTheme.addActionListener(this);
		this.style.add(this.darkTheme);

		// time for the repetitive stuff. let's add the "Open" option
		this.openFile.setLabel("Open"); // set the label of the menu item
		this.openFile.addActionListener(this); // add an action listener (so we know when it's been clicked
		this.openFile.setShortcut(new MenuShortcut(KeyEvent.VK_O, false)); // set a keyboard shortcut
		this.file.add(this.openFile); // add it to the "File" menu
		
		// and the save...
		this.saveFile.setLabel("Save");
		this.saveFile.addActionListener(this);
		this.saveFile.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		this.file.add(this.saveFile);
		
		// and finally, the close option
		this.close.setLabel("Close");
		// along with our "CTRL+F4" shortcut to close the window, we also have
		// the default closer, as stated at the beginning of this tutorial.
		// this means that we actually have TWO shortcuts to close:
		// 1) the default close operation (example, Alt+F4 on Windows)
		// 2) CTRL+F4, which we are about to define now: (this one will appear in the label)
		this.close.setShortcut(new MenuShortcut(KeyEvent.VK_F4, false));
		this.close.addActionListener(this);
		this.file.add(this.close);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// if the source of the event was our "close" option
		if (e.getSource() == this.close)
        {
            this.dispose(); // dispose all resources and close the application
        }
        else if(e.getSource() == this.vowelCount)
        {
            StringBuilder builder = new StringBuilder();
            builder.append("Vowel Count: \n");
            Map<Character, Integer> vowels = LineCounter.countVowels(textArea.getText());
            builder.append("a : ").append(vowels.get('a'));
            builder.append("e : ").append(vowels.get('e'));
            builder.append("i : ").append(vowels.get('i'));
            builder.append("o : ").append(vowels.get('o'));
            builder.append("u : ").append(vowels.get('u'));
            JOptionPane.showMessageDialog(getParent(), builder.toString() );
        }
        else if(e.getSource() == this.darkTheme)
        {
            if(!inDarkTheme)
            {
                textArea.setBackground(Color.black);
                textArea.setForeground(Color.white);

                /*
                 * labelLineCount.setBackground(Color.black);
                 * labelLineCount.setForeground(Color.white);
                 * labelLineCount.updateUI();
                 */
                inDarkTheme = true;
            }
            else
            {
                textArea.setBackground(Color.white);
                textArea.setForeground(Color.black);



                labelLineCount.setBackground(Color.white);
                labelLineCount.setForeground(Color.black);
                labelLineCount.updateUI();


                inDarkTheme = false;
            }
        }
		
		// if the source was the "open" option
		else if (e.getSource() == this.openFile) {
			JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to browse files to open)
			int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
			// NOTE: because we are OPENing a file, we call showOpenDialog~
			// if the user clicked OK, we have "APPROVE_OPTION"
			// so we want to open the file
			if (option == JFileChooser.APPROVE_OPTION) 
			{
                this.textArea.setText(""); // clear the TextArea before applying the file contents
                try (Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath()))) {
                    // create a scanner to read the file (getSelectedFile().getPath() will get the path to the file)
                    while (scan.hasNext()) {
                        // while there's still something to read
                        this.textArea.append(scan.nextLine() + "\n"); // append the line to the TextArea

                    }
                    labelLineCount.setText(LineCounter.CountLines(textArea.getText()) + " Lines " + LineCounter.CountCharacters(textArea.getText()) + " characters");
                    if (textArea.getText().contains("class")) {
                        int start = textArea.getText().indexOf("class");

                        int end = start + "class".length();
                        try
                        {
                            highlighter.addHighlight(start, end, paint);
                        } catch (BadLocationException ex) {
                            System.err.println(ex.getLocalizedMessage());
                        }
                    }

                } catch (Exception ex) { // catch any exceptions, and...
                    // ...write to the debug console
                    System.out.println(ex.getMessage());
                }
			}
		}
		
		// and lastly, if the source of the event was the "save" option
		else if (e.getSource() == this.saveFile) 
		{
			JFileChooser save = new JFileChooser(); // again, open a file chooser
			int option = save.showSaveDialog(this); // similar to the open file, only this time we call
			// showSaveDialog instead of showOpenDialog
			// if the user clicked OK (and not cancel)
			if (option == JFileChooser.APPROVE_OPTION)
			{
				try(BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath())))
				{
					// create a buffered writer to write to a file
					out.write(this.textArea.getText()); // write the contents of the TextArea to the file
				  // close the file stream
				} catch (Exception ex) { // again, catch any exceptions and...
					// ...write to the debug console
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	/**
	 * Invoked when a key has been typed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key typed event.
	 *
	 * @param e
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
	    if(textArea.getText().contains("class"))
        {
            int start = textArea.getText().indexOf("class");
            int end = start + "class".length();
            try
            {
                highlighter.addHighlight(start,end,paint);
            }
            catch (BadLocationException ex)
            {
                System.err.println(ex.getLocalizedMessage());
            }
        }

        labelLineCount.setText(LineCounter.CountLines(textArea.getText()) + " Lines " + LineCounter.CountCharacters(textArea.getText()) + " characters");


	}

	/**
	 * Invoked when a key has been pressed.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key pressed event.
	 *
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{

	}

	/**
	 * Invoked when a key has been released.
	 * See the class description for {@link KeyEvent} for a definition of
	 * a key released event.
	 *
	 * @param e
	 */
	@Override
	public void keyReleased(KeyEvent e) {

	}
}
