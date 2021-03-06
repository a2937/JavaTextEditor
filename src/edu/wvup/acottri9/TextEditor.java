package edu.wvup.acottri9;

import javax.swing.*; // for the main JFrame design
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*; // for the GUI stuff
import java.awt.event.*; // for the event handling
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; // for reading from a file
import java.io.*; // for writing to a file
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class TextEditor extends JFrame implements ActionListener, KeyListener  {

	private int tabCount; 
	
	private boolean inDarkTheme = false;
	private boolean useWordWrap = true;
    private transient  Highlighter.HighlightPainter paint = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
    private transient Highlighter highlighter;
    private JLabel labelLineCount;
	private JTextArea textArea = new JTextArea("",0, 0);
	private transient MenuBar ourMenuBar = new MenuBar(); // first, create a MenuBar item
	private transient Menu file = new Menu(); // our File menu
    private transient Menu tools = new Menu(); // our Tools menu
	private transient Menu style = new Menu(); // our Style menu
	private transient Menu language = new Menu(); // a new language menu for syntax highlighting
    private transient Menu help = new Menu(); // a standard help menu


	// what's going in File? let's see...
	private MenuItem openFile = new MenuItem();  // an open option
	private MenuItem saveFile = new MenuItem(); // a save option
	private MenuItem close = new MenuItem(); // and a close option!

	private Map<String,String> fileContents = new HashMap<String,String>(); 
	
	//in style
	private MenuItem darkTheme = new MenuItem(); // black on white instead of white on black
	private MenuItem fontName = new MenuItem(); // The name of the font
	private MenuItem fontSize = new MenuItem(); // The size of the font
	private MenuItem wordWrapOption = new MenuItem(); // Whether or not words should go to the next line

    //in tools
    private MenuItem vowelCount = new MenuItem(); // Displays a print out of the number of occurrences of each vowel.
    private MenuItem performRegix = new MenuItem(); // Creates a display to run Regular expressions against the text in the file
	private MenuItem findAndReplace = new MenuItem(); // The most important feature of a text editor; a find and replace option
    private MenuItem encrypt = new MenuItem(); // This button will take the text and apply a caesar cypher to it
    private MenuItem decrypt = new MenuItem(); // This button will take the text and decrypt it with a caesar cypher

    //In the help menu
    private MenuItem aboutApp = new MenuItem(); // Displays information build info
    private MenuItem docs = new MenuItem(); //In case users need information on keybindings/shortcuts and that stuff

	private MenuItem javaLang = new MenuItem(); // A button for syntax highlighting java
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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
		this.setTitle("Custom Notepad"); // set the title of the window
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // set the default close operation (exit when it gets closed)

        JScrollPane scrollArea = new JScrollPane (textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		this.textArea.setFont(new Font("Century Gothic", Font.BOLD, 12)); // set a default font for the TextArea
        this.textArea.setLineWrap(useWordWrap);
        this.textArea.setWrapStyleWord(useWordWrap);
        this.textArea.addKeyListener(this);

        highlighter = textArea.getHighlighter();
		// this is why we didn't have to worry about the size of the TextArea!
		this.getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
		
		getContentPane().add(tabbedPane, BorderLayout.NORTH);
		
		this.getContentPane().add(scrollArea);
	     
		this.tabbedPane.addChangeListener(new ChangeListener() 
		{

			@Override
			public void stateChanged(ChangeEvent e) 
			{
				  if (e.getSource() instanceof JTabbedPane)
				  {
                      JTabbedPane pane = (JTabbedPane) e.getSource();
                      if(pane.getSelectedIndex() != -1)
                      {
                    	  textArea.setText(fileContents.get(pane.getTitleAt(pane.getSelectedIndex())));
                    	  labelLineCount.setText(LineCounter.CountLines(textArea.getText()) + " Lines " + LineCounter.CountCharacters(textArea.getText()) + " characters");
                      }
                      else
                      {
                    	  textArea.setText("");
                      }
				  }
				
			}
		
		});
		
		
		labelLineCount = new JLabel("");
		
		getContentPane().add(labelLineCount, BorderLayout.SOUTH);

		// add our menu bar into the GUI
		this.setMenuBar(this.ourMenuBar);
		this.ourMenuBar.add(this.file); // we'll configure this later
		this.ourMenuBar.add(this.style);
		this.ourMenuBar.add(this.tools);
        this.ourMenuBar.add(this.language);
		this.ourMenuBar.add(this.help);
		// first off, the design of the menuBar itself. Pretty simple, all we need to do
		// is add a couple of menus, which will be populated later on
		this.file.setLabel("File");
		this.style.setLabel("Style");
		this.tools.setLabel("Tools");
		this.help.setLabel("Help");
		this.language.setLabel("Language");


		//Font style
		this.fontName.setLabel("Select different font");
		this.fontName.addActionListener(this);
		this.style.add(this.fontName);

		//Font size
		this.fontSize.setLabel("Change Font Size");
		this.fontSize.addActionListener(this);
		this.style.add(this.fontSize);

		//Dark theme
		this.darkTheme.setLabel("Invert color palette");
		this.darkTheme.addActionListener(this);
		this.style.add(this.darkTheme);

		//encrypt
        this.encrypt.setLabel("Encrypt text");
        this.encrypt.addActionListener(this);
        this.tools.add(encrypt);

        //decrypt
        this.decrypt.setLabel("Decrypt text");
        this.decrypt.addActionListener(this);
        this.tools.add(decrypt);

		//Word wrap
		this.wordWrapOption.setLabel("Use word wrapping");
		this.wordWrapOption.addActionListener(this);
		this.style.add(this.wordWrapOption);




        //Documentation
        this.docs.setLabel("Tips and tricks");
        this.docs.addActionListener(this);
        this.help.add(this.docs);

        //Java lang option
        this.javaLang.setLabel("Java");
        this.javaLang.addActionListener(this);
        this.language.add(this.javaLang);

        //About information
        this.aboutApp.setLabel("About");
        this.aboutApp.addActionListener(this);
        this.help.add(this.aboutApp);


        //Find and replace
		this.findAndReplace.setLabel("Find and replace");
		this.findAndReplace.addActionListener(this);
		this.tools.add(this.findAndReplace);

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

		//Vowel count menu item
        this.vowelCount.setLabel("Count vowels.");
        this.vowelCount.addActionListener(this);
		this.tools.add(vowelCount);

		//Regix menu item
        this.performRegix.setLabel("Run Regular Expression");
        this.performRegix.addActionListener(this);
		this.tools.add(performRegix);
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
            builder.append("a : ").append(vowels.get('a')).append("\n");
            builder.append("e : ").append(vowels.get('e')).append("\n");
            builder.append("i : ").append(vowels.get('i')).append("\n");
            builder.append("o : ").append(vowels.get('o')).append("\n");
            builder.append("u : ").append(vowels.get('u'));
            JOptionPane.showMessageDialog(this, builder.toString() );
        }
        else if(e.getSource() == this.javaLang)
		{
            Highlighter h = textArea.getHighlighter();
            h.removeAllHighlights();


		}
        else if(e.getSource() == this.encrypt)
        {
            try
            {
                int shift = Integer.parseInt(JOptionPane.showInputDialog(this,"What is the number you want associated with the encryption?"));
                String encryptedText = CypherTools.encryptText(textArea.getText(),shift);
                textArea.setText(encryptedText);
                JOptionPane.showMessageDialog(this,"In order to decrypt this text,  you'll need the number associated with it. \n" +
                                                     "Your number was " + shift + " make a note of it.");
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(this,"Cannot read number. Aborting.","Error",JOptionPane.ERROR_MESSAGE);
            }

        }

        else if(e.getSource() == this.decrypt)
        {
            try
            {
                int shift = Integer.parseInt(JOptionPane.showInputDialog(this,"What is the number associated with the encryption?"));
                String normalText = CypherTools.decryptText(textArea.getText(),shift);
                textArea.setText(normalText);
            }
            catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(this,"Cannot read number. Aborting.","Error",JOptionPane.ERROR_MESSAGE);
            }

        }
        else if(e.getSource() == this.performRegix)
        {
            String expression = JOptionPane.showInputDialog(this,"Insert a regular expression.");
            try
            {
                if(expression != null)
                {
                    Pattern pattern = Pattern.compile(expression);
                    Matcher matcher = pattern.matcher(textArea.getText());

                    if(matcher.find())
                    {

                        /*
                        for(int i = 0; i  <matcher.groupCount() - 1; i++)
                        {
                            builder.append(matcher.group(i)).append(",");
                        }
                        builder.append(matcher.group(matcher.groupCount()));
                        */
                        JOptionPane.showMessageDialog(this, "Matches found: " + matcher.groupCount());
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "No matches.");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Error: expression empty");
                }

            }
            catch(PatternSyntaxException exception)
            {
                JOptionPane.showMessageDialog(this, "Error on Regex : \n" + exception.getMessage() );
            }
        }
        else if(e.getSource() == this.findAndReplace)
		{
			JTextField toFind = new JTextField();
			JTextField replacement = new JTextField();
			Object[] message = {
					"To find:", toFind,
					"Replace with:", replacement
			};

			int option = JOptionPane.showConfirmDialog(this, message, "Find and Replace", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION)
			{
				String theWordToFind = toFind.getText();
				String theWordToReplaceWith = replacement.getText();
				String ourContents = textArea.getText();
				textArea.setText(ourContents.replaceAll(theWordToFind,theWordToReplaceWith));

			}
		}
		else if(e.getSource() == this.wordWrapOption)
        {
            useWordWrap = !useWordWrap;
            this.textArea.setLineWrap(useWordWrap);
            this.textArea.setWrapStyleWord(useWordWrap);

        }
        else if(e.getSource() == this.darkTheme)
        {
            if(!inDarkTheme)
            {
                textArea.setBackground(Color.black);
                textArea.setForeground(Color.white);

                inDarkTheme = true;
            }
            else
            {
                textArea.setBackground(Color.white);
                textArea.setForeground(Color.black);
                inDarkTheme = false;
            }
        }
        else if(e.getSource() == this.fontSize)
		{
			String fontSizeString = JOptionPane.showInputDialog(this,"Pick a new font size. Current size: " + textArea.getFont().getSize());
			try
			{
				float properFontSize = Float.parseFloat(fontSizeString);
				textArea.setFont(textArea.getFont().deriveFont(properFontSize));
			}
			catch(NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(this, "Error on Font Size setting: \n" + ex.getMessage() );
			}
		}
		else if(e.getSource() == this.aboutApp)
		{
			JOptionPane.showMessageDialog(this, "This is a custom notepad app developed in my free time.","About app", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == this.docs)
		{
			JOptionPane.showMessageDialog(this, "Coming soon.","Tips and tricks", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == this.fontName)
        {
                GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Font[] fonts = graphicsEnvironment.getAllFonts();
                String[] fontNames = new String[fonts.length];
                for(int i = 0; i < fonts.length; i++)
                {
                    fontNames[i] = fonts[i].getName();
                }
                //JComboBox<Font> fontBox = new JComboBox<>(fonts);
                 JFrame frame = new JFrame();
                 frame.setAlwaysOnTop(true);
                 String selectedFontName =  (String)JOptionPane.showInputDialog(frame, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null, fontNames, textArea.getFont().getName());
                 Font selectedFont = new Font(selectedFontName, Font.PLAIN, textArea.getFont().getSize());
                // if(selectedFont != null)
                // {
                     textArea.setFont(selectedFont);
                // }
                //fontBox.setVisible(true);
               // panel.add(fontBox);
                //panel.setVisible(true);
                //fontBox.setPopupVisible(true);
        }
		
		// if the source was the "open" option
		else if (e.getSource() == this.openFile) 
		{
			JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to browse files to open)
			int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
			// NOTE: because we are OPENing a file, we call showOpenDialog~
			// if the user clicked OK, we have "APPROVE_OPTION"
			// so we want to open the file
			if (option == JFileChooser.APPROVE_OPTION) 
			{
                this.textArea.setText(""); // clear the TextArea before applying the file contents
                this.tabbedPane.addTab(open.getSelectedFile().getName(), new JLabel());
                this.tabbedPane.setTabComponentAt(tabCount, new ButtonTabComponent(this.tabbedPane));
                this.tabbedPane.setSelectedIndex(tabCount);
                 this.tabCount++; 
               
                try (Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath())))
				{
                    // create a scanner to read the file (getSelectedFile().getPath() will get the path to the file)
                    while (scan.hasNext()) {
                        // while there's still something to read
                        this.textArea.append(scan.nextLine() + "\n"); // append the line to the TextArea
              
                    }
              
                    fileContents.put(open.getSelectedFile().getName(), textArea.getText()); 
                    labelLineCount.setText(LineCounter.CountLines(textArea.getText()) + " Lines " + LineCounter.CountCharacters(textArea.getText()) + " characters");
                   
                   // 
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
					this.fileContents.put(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),textArea.getText()); 
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
		labelLineCount.setText(LineCounter.CountLines(textArea.getText()) + " Lines " + LineCounter.CountCharacters(textArea.getText()) + " characters");
		/*
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


		*/

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
