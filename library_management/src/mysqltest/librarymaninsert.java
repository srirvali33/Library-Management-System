package mysqltest;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

/******************************
 * Library Book Entry This Program tracks Library book entries. It has 3 modes
 * Insert new book,Update book and Search details about book. Insertion of books
 * is implemented below. Clear button clears all text fields. Save button is
 * used for inserting data. The GUI has the fields which are essential for
 * inserting a new book in DB. The mandatory fields for insertion are shown by
 * '*' in the J label and non mandatory field is 'Dewy decimal number'. The user
 * enters all the fields for Non fiction book and leaves Dewy decimal number for
 * fiction book. The publisher drop down has list of publishers from DB and user
 * can select one for insertion and clicks on 'SAVE'. Then if all the data is
 * consistent the new book is entered in database otherwise relevant error
 * messages are shown to user.
 * 
 * Written by SriRavali Baru
 */
/* library management class for inserting book data */
public class librarymaninsert extends JFrame implements ActionListener {

	/*
	 * Declaring the below variables outside so that they could be accessed globally
	 */
	/* below are few text fields from which the data can be retrieved */
	private JPanel contentPane;
	private JTextField title;
	private JTextField author;
	private JTextField isbn;
	private JTextField dewydecimal;
	private final JTable table = new JTable();

	/* Declaring connection and statement variables which are used for queries */
	private Connection connect1 = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement1 = null;
	PreparedStatement preparedStatement2 = null;
	PreparedStatement preparedStatement3 = null;
	PreparedStatement preparedStatement4 = null;
	public ResultSet resultSet = null;
	private JTextField borrowtype;
	private JTextField lccn;
	private JTextField pubdate;
	private JTextField editfie;
	private JTextField noofpages;
	private JTextField typebook;
	private JTextField isfiction;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					librarymaninsert frame = new librarymaninsert();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Action performed generalized one which clears the details about books
	 */

	public void actionPerformed(ActionEvent e) {

		try {
			title.setText("");
			author.setText("");
			isbn.setText("");
			dewydecimal.setText("");
			borrowtype.setText("");
			lccn.setText("");
			pubdate.setText("");
			editfie.setText("");
			noofpages.setText("");
			isfiction.setText("");
			typebook.setText("");

		}

		// Exception handling with appropriate message to user is shown as 'Error
		// Message'
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}

		finally {
			close();
		}

	}

	// close the resultSet,statement,connections
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect1 != null) {
				connect1.close();
			}
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * Create the frame.
	 */
	public librarymaninsert() {
		try {
			// Load driver
			Class.forName("com.mysql.jdbc.Driver");
			connect1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement", "root", "");
		} catch (ClassNotFoundException e1) {
			// Exception handling with appropriate message to user is shown as 'Error
			// Message'
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e1) {

			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		setTitle("Library Book Entry");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 825, 483);
		contentPane = new JPanel();
		contentPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		contentPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("TITLE *");
		lblNewLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblNewLabel.setBounds(10, 14, 48, 14);
		contentPane.add(lblNewLabel);

		title = new JTextField();
		title.setBounds(78, 12, 150, 17);
		contentPane.add(title);
		title.setColumns(10);

		JComboBox publishers = new JComboBox();
		publishers.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		publishers.setToolTipText("Publishers");
		publishers.setEditable(true);
		publishers.setBounds(88, 126, 157, 18);
		contentPane.add(publishers);

		try {

			// This will load the MySQL driver, each DB has its own driver

			statement = connect1.createStatement();
			// Execute the above query
			resultSet = statement.executeQuery("Select distinct Publisher from publications");
			// Get all publishers and load in the frontend
			while (resultSet.next()) {

				publishers.addItem(resultSet.getString("Publisher"));

			}
			statement.close();
			connect1.close();
		}

		// Exception handling with appropriate message to user is shown as 'Error
		// Message'
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}

		finally {
			close();
		}

		JLabel lblAuthors = new JLabel("AUTHOR(S) *");
		lblAuthors.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblAuthors.setBounds(10, 45, 68, 14);
		contentPane.add(lblAuthors);

		author = new JTextField();
		author.setBounds(78, 45, 150, 14);
		contentPane.add(author);
		author.setColumns(10);

		JLabel lblIsbn = new JLabel("ISBN *");
		lblIsbn.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblIsbn.setBounds(10, 80, 48, 14);
		contentPane.add(lblIsbn);

		isbn = new JTextField();
		isbn.setBounds(81, 70, 96, 20);
		contentPane.add(isbn);
		isbn.setColumns(10);

		JLabel lblDewydecimal = new JLabel("DEWYDECIMAL");
		lblDewydecimal.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblDewydecimal.setBounds(10, 105, 83, 14);
		contentPane.add(lblDewydecimal);

		dewydecimal = new JTextField();
		dewydecimal.setBounds(122, 101, 96, 14);
		contentPane.add(dewydecimal);
		dewydecimal.setColumns(10);

		JLabel lblPublisher = new JLabel("PUBLISHER *");
		lblPublisher.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblPublisher.setBounds(10, 130, 68, 14);
		contentPane.add(lblPublisher);

		JButton savebtn = new JButton("SAVE");
		savebtn.setBounds(150, 159, 68, 14);
		contentPane.add(savebtn);
		// Performing the save button functionality of insertion
		savebtn.addActionListener(new AbstractAction("SAVE") {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					// Get all values from frontend into below variables
					int titvar = title.getText().length();
					int boorwvar = borrowtype.getText().length();
					int typebvar = typebook.getText().length();
					int authvar = author.getText().length();
					int lccvar = lccn.getText().length();
					int ficvar = isfiction.getText().length();
					int isbnvar = isbn.getText().length();
					int pubdatevar = pubdate.getText().length();
					int dewvar = dewydecimal.getText().length();
					int editf = editfie.getText().length();
					int pagvar = noofpages.getText().length();

					// This will load the MySQL driver, each DB has its own driver
					Class.forName("com.mysql.jdbc.Driver");
					Connection connect1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement",
							"root", "");
					// Statements allow to issue SQL queries to the database
					// Check all the values are entered for insertion
					if ((titvar > 0) && (boorwvar > 0) && (typebvar > 0) && (authvar > 0) && (lccvar > 0)
							&& (ficvar > 0) && (isbnvar > 0) && (pubdatevar > 0) && (editf > 0) && (pagvar > 0)) {
						statement = connect1.prepareStatement("SELECT MAX(ResourceId) FROM resources");

						ResultSet rs = statement.executeQuery("SELECT MAX(ResourceId) FROM resources");
						String reid = null;
						int resid = 0;
						// Get the maximum resourceid to insert the newitem by adding 1 to it
						while (rs.next()) {

							reid = rs.getString("MAX(ResourceId)");
							resid = Integer.parseInt(reid) + 1;
						}
						statement.close();
						// Insert into resources table
						preparedStatement1 = connect1.prepareStatement("INSERT INTO  resources VALUES ( ?,?,?,?)");
						// Insert into publications table
						preparedStatement2 = connect1
								.prepareStatement("INSERT INTO  PUBLICATIONS VALUES ( ?,?,?,?,?,?,?,?)");
						// Insert into Books table
						preparedStatement3 = connect1.prepareStatement("INSERT INTO  BOOKS VALUES ( ?,?,?,?,?)");
						// Insert into Authors table
						preparedStatement4 = connect1.prepareStatement("INSERT INTO  authors VALUES ( ?,?)");
						preparedStatement1.setInt(1, resid);
						preparedStatement1.setString(2, "Books");
						preparedStatement1.setString(3, title.getText());
						preparedStatement1.setString(4, borrowtype.getText());
						preparedStatement1.executeUpdate();
						preparedStatement1.close();
						preparedStatement2.setString(1, lccn.getText());
						preparedStatement2.setString(2, title.getText());
						preparedStatement2.setString(3, "1");
						preparedStatement2.setString(4, publishers.getSelectedItem().toString());
						preparedStatement2.setString(5, pubdate.getText());
						preparedStatement2.setString(6, editfie.getText());
						preparedStatement2.setString(7, noofpages.getText());
						preparedStatement2.setInt(8, resid);
						preparedStatement2.executeUpdate();
						preparedStatement2.close();
						preparedStatement3.setString(1, isbn.getText());
						preparedStatement3.setString(2, typebook.getText());
						preparedStatement3.setString(3, lccn.getText());
						preparedStatement3.setString(4, isfiction.getText());
						preparedStatement3.setString(5, dewydecimal.getText());
						preparedStatement3.executeUpdate();
						preparedStatement3.close();
						preparedStatement4.setInt(1, resid);
						String autstr = author.getText();
						// If 2 or more authors are given then split and add the author one by one
						String[] arrOfStr = autstr.split(";", 2);
						for (String a : arrOfStr) {
							preparedStatement4.setString(2, a);
							preparedStatement4.executeUpdate();
						}
						preparedStatement4.close();
						JOptionPane.showMessageDialog(null,"Successfully Data Inserted");
					}

					// Else display ,message to enter all mandatory fields
					else {

						JOptionPane.showMessageDialog(null, "Please Enter all the Required Fields");
					}
				}

				// Exception handling with appropriate message to user is shown as 'Error
				// Message'
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

				}

				finally {
					close();
				}

			}

		});

		JButton clearbtn = new JButton("CLEAR");
		clearbtn.setBounds(301, 159, 80, 14);
		contentPane.add(clearbtn);
		// Action performed to clear data for new insertion
		clearbtn.addActionListener(this);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 184, 713, 185);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(table);

		table.setColumnSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] { {}, {}, }, new String[] {}));
		table.setCellSelectionEnabled(true);

		JLabel lblLibrary = DefaultComponentFactory.getInstance().createTitle("LIBRARY");
		lblLibrary.setBounds(29, -11, 91, 14);
		contentPane.add(lblLibrary);

		JLabel lblBorro = new JLabel("BORROWTYPE *");
		lblBorro.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblBorro.setBounds(252, 14, 68, 14);
		contentPane.add(lblBorro);

		borrowtype = new JTextField();
		borrowtype.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		borrowtype.setBounds(318, 11, 96, 20);
		contentPane.add(borrowtype);
		borrowtype.setColumns(10);

		JLabel lblLccn = new JLabel("LCCN *");
		lblLccn.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblLccn.setBounds(252, 45, 35, 14);
		contentPane.add(lblLccn);

		lccn = new JTextField();
		lccn.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		lccn.setColumns(10);
		lccn.setBounds(285, 42, 96, 20);
		contentPane.add(lccn);

		JLabel lblPublicationDate = new JLabel("PUBLICATION DATE *");
		lblPublicationDate.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblPublicationDate.setBounds(187, 70, 91, 14);
		contentPane.add(lblPublicationDate);

		pubdate = new JTextField();
		pubdate.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		pubdate.setColumns(10);
		pubdate.setBounds(285, 70, 96, 20);
		contentPane.add(pubdate);

		Label edition = new Label("EDITION *");
		edition.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		edition.setBounds(235, 101, 52, 22);
		contentPane.add(edition);

		editfie = new JTextField();
		editfie.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		editfie.setColumns(10);
		editfie.setBounds(295, 102, 73, 20);
		contentPane.add(editfie);

		Label label = new Label("NO OF PAGES *");
		label.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		label.setBounds(459, 122, 83, 22);
		contentPane.add(label);

		noofpages = new JTextField();
		noofpages.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		noofpages.setColumns(10);
		noofpages.setBounds(548, 125, 58, 20);
		contentPane.add(noofpages);

		Label label_1 = new Label("TYPE OF BOOK *");
		label_1.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		label_1.setBounds(443, 14, 103, 22);
		contentPane.add(label_1);

		typebook = new JTextField();
		typebook.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		typebook.setColumns(10);
		typebook.setBounds(551, 11, 116, 20);
		contentPane.add(typebook);

		Label label_2 = new Label("IS FICTION *");
		label_2.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		label_2.setBounds(426, 45, 62, 22);
		contentPane.add(label_2);

		isfiction = new JTextField();
		isfiction.setFont(new Font("Arial Narrow", Font.PLAIN, 11));
		isfiction.setColumns(10);
		isfiction.setBounds(494, 42, 68, 20);
		contentPane.add(isfiction);

		JLabel lblInsert = new JLabel("Insert and Save");
		lblInsert.setBounds(29, 408, 125, 14);
		contentPane.add(lblInsert);
		
		JLabel lblSelectOrType = new JLabel("SELECT OR TYPE FROM DROPDOWN");
		lblSelectOrType.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		lblSelectOrType.setBounds(255, 128, 178, 16);
		contentPane.add(lblSelectOrType);

	}
}
