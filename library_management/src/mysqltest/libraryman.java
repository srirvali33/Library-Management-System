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

//import net.proteanit.sql.DbUtils;

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
 * Insert new book,Update book and Search details about book. The Search and
 * Update method are implemented in the below program. Search
 * functionality:Search has four fields Title,author,ISBN,DewyDecimal number as
 * text fields and clear,search and update buttons. 'Clear' button clears all
 * the text fields,'Search' button searches from the database for the fields
 * given. Search could one field,two,three or all four fields.The combination of
 * the fields can be any(Eg: ISBN,Title or Title etc). 'Update' button is used
 * to update the books data in the database. The update same as search can be
 * done different ways with existing fields. The user can update Title, author
 * and dewy decimal of a book based on the reference of ISBN number. With the
 * ISBN number the user can update combination of fields like
 * (Title,DewyDecimal) or (Author,Title) at a time, or a single field like
 * Title.
 *
 * Written by SriRavali Baru
 */

/* library management class for search and update book data */
public class libraryman extends JFrame implements ActionListener {

	/*
	 * Declaring the below variables outside so that they could be accessed globally
	 */
	private JPanel contentPane; // content pane where all the J frame elements are inserted
	/* below are few text fields from which the data can be retrieved */
	private JTextField title;
	private JTextField author;
	private JTextField isbn;
	private JTextField dewydecimal;

	/*
	 * Creating different table models in JFrame so that data for search could be
	 * retrieved according to given parameters in textfields
	 */

	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel model1 = new DefaultTableModel();
	DefaultTableModel model2 = new DefaultTableModel();
	DefaultTableModel model3 = new DefaultTableModel();
	DefaultTableModel model4 = new DefaultTableModel();
	DefaultTableModel model5 = new DefaultTableModel();

	/*
	 * Setting column names used to display the tables with column names as of the
	 * original tables. Each table has different column names
	 */

	String[] columnNames = { "ISBN", "DDSN", "AuthorName", "Title" };
	String[] columnNames1 = { "ISBN", "DDSN" };
	String[] columnNames2 = { "Title", "AuthorName" };
	String[] columnNames3 = { "Title", "ISBN", "DDSN" };
	String[] columnNames4 = { "AuthorName", "ISBN", "DDSN", "Title" };
	String[] columnNames5 = { "ISBN", "Title", "Typeofbook", "IsFiction" };

	/* Creating a table object to display books data */
	static JTable table = new JTable();

	/* Setting connection,statement,result set */
	private Connection connect = null;
	private Statement statement = null;
	public ResultSet resultSet = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					libraryman frame = new libraryman();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Action performed generalized one which searches the details about books from
	 * books and other tables such as resources and publications and authors
	 */

	public void actionPerformed(ActionEvent e) {

		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement", "root", "");
			// Setting different table models and its corresponding column names
			model.setColumnIdentifiers(columnNames);
			model1.setColumnIdentifiers(columnNames1);
			model2.setColumnIdentifiers(columnNames2);
			model3.setColumnIdentifiers(columnNames3);
			model4.setColumnIdentifiers(columnNames4);
			model5.setColumnIdentifiers(columnNames5);
			
			// Making the table clear by deleting all rows if present
			while (model.getRowCount() > 0) {

				model.removeRow(0);
			}

			// checking if all the fields isbn, dewydecimal, author,title are entered
			if ((isbn.getText().length() > 0) && (author.getText().length() > 0) && (dewydecimal.getText().length() > 0)
					&& (title.getText().length() > 0)) {

				// Statements allow to issue SQL queries to the database

				PreparedStatement st = connect.prepareStatement(
						"Select * from publications p,books b,Authors a where p.LCCN=b.LCCN and p.ResourceId=a.ResourceId and p.title=? and a.AuthorName=? and b.isbn=? and b.DDSN=?");
				st.setString(1, title.getText());
				st.setString(2, author.getText());
				st.setString(3, isbn.getText());
				st.setString(4, dewydecimal.getText());
				// Executing Query
				resultSet = st.executeQuery();
				// Making the table clear by deleting all rows if present
				while (model.getRowCount() > 0) {

					model.removeRow(0);
				}

				// Creating the table with model type and its corresponding parameters
				table.setModel(model);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				String ISBN = "";
				String DDSN = "";
				String aut = "";
				String Title = "";
				int i = 0;
				// loop runs and fetches all rows from the result obtained
				while (resultSet.next()) {
					ISBN = resultSet.getString("ISBN");
					DDSN = resultSet.getString("DDSN");
					aut = resultSet.getString("AuthorName");
					Title = resultSet.getString("Title");
					model.addRow(new Object[] { ISBN, Title, DDSN, aut });
					i++;
				}

				// if no rows are returned 'No Record Found' message is displayed
				if (i < 1) {
					JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (i == 1) {
					System.out.println(i + " Record Found");
				} else {
					System.out.println(i + " Records Found");
				}
				st.close();

			}

			// search based on title as reference
			else if (title.getText().length() > 0) {
				PreparedStatement statement = null;

				if (author.getText().length() > 0) {

					if (isbn.getText().length() > 0) {

						// If all three title, author, isbn are provided

						statement = connect.prepareStatement(
								"Select distinct * from publications p,authors a,books b where p.Resourceid=a.Resourceid and p.LCCN=b.LCCN and p.title=? and a.AuthorName=? and b.isbn=?");
						statement.setString(1, title.getText());
						statement.setString(2, author.getText());
						statement.setString(3, isbn.getText());
					}

					else if (dewydecimal.getText().length() > 0) {

						// If all three title, author, dewy decimal are provided

						statement = connect.prepareStatement(
								"Select  distinct * from publications p,authors a,books b where p.Resourceid=a.Resourceid and p.LCCN=b.LCCN and p.title=? and a.AuthorName=? and b.DDSN=?");
						statement.setString(1, title.getText());
						statement.setString(2, author.getText());
						statement.setString(3, dewydecimal.getText());
					} else {

						// If title and author only are provided
						statement = connect.prepareStatement(
								"Select  distinct * from publications p,authors a where p.Resourceid=a.Resourceid  and p.title=? and a.AuthorName=?");
						statement.setString(1, title.getText());
						statement.setString(2, author.getText());
					}

					// Executing Query
					resultSet = statement.executeQuery();
					table.setModel(new DefaultTableModel());
					// Making the table clear by deleting all rows if present
					while (model2.getRowCount() > 0) {

						model2.removeRow(0);
					}
					// Creating the table with model type and its corresponding parameters
					table.setModel(model2);
					table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
					table.setFillsViewportHeight(true);
					String ISBN = "";
					String aut = "";
					int i = 0;
					// loop runs and fetches all rows from the result obtained
					while (resultSet.next()) {
						ISBN = resultSet.getString("Title");
						aut = resultSet.getString("AuthorName");
						model2.addRow(new Object[] { ISBN, aut });
						i++;
					}

					// if no rows are returned 'No Record Found' message is displayed
					if (i < 1) {
						JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
					}
					if (i == 1) {
						System.out.println(i + " Record Found");
					} else {
						System.out.println(i + " Records Found");
					}
					statement.close();
					return;

				}

				// Search based on ISBN Number and title

				else if (isbn.getText().length() > 0) {
					
					if (dewydecimal.getText().length() > 0) {

						statement = connect.prepareStatement(
								"Select * from publications p,books b where p.LCCN=b.LCCN  and p.title=? and b.DDSN=?");
						statement.setString(1, title.getText());
						statement.setString(2, dewydecimal.getText());
					}
					else {
					statement = connect
							.prepareStatement("Select * from publications p,books b where p.LCCN=b.LCCN  and b.isbn=?");
					statement.setString(1, isbn.getText());
					}

				}

				// Search based on dewy Number and title
				else if (dewydecimal.getText().length() > 0) {

					statement = connect.prepareStatement(
							"Select * from publications p,books b where p.LCCN=b.LCCN  and p.title=? and b.DDSN=?");
					statement.setString(1, title.getText());
					statement.setString(2, dewydecimal.getText());
				}

				// search on title
				else {
					statement = connect.prepareStatement(
							"Select * from publications p,books b where p.LCCN=b.LCCN  and p.title=?");
					statement.setString(1, title.getText());
				}
				// Excuting Query
				resultSet = statement.executeQuery();
				// Making the table clear by deleting all rows if present
				while (model3.getRowCount() > 0) {

					model3.removeRow(0);
				}
				// Creating the table with model type and its corresponding parameters
				table.setModel(model3);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				String ISBN = "";
				String DDSN = "";
				String Title = "";
				int i = 0;
				// loop runs and fetches all rows from the result obtained
				while (resultSet.next()) {
					ISBN = resultSet.getString("ISBN");
					DDSN = resultSet.getString("DDSN");
					Title = resultSet.getString("Title");
					model3.addRow(new Object[] { Title, ISBN, DDSN });
					i++;
				}
				// if no rows are returned 'No Record Found' message is displayed
				if (i < 1) {
					JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (i == 1) {
					System.out.println(i + " Record Found");
				} else {
					System.out.println(i + " Records Found");
				}
				// table.setModel(DbUtils.resultSetToTableModel(resultSet));
				statement.close();
			}

			// Search based on author as reference
			else if (author.getText().length() > 0) {

				PreparedStatement statement = null;
				if (isbn.getText().length() > 0) {

					// search on author,isbn,dewy decimal as entry
					if (dewydecimal.getText().length() > 0) {
						statement = connect.prepareStatement(
								"Select * from publications p,books b,authors a where  a.Resourceid=p.Resourceid and p.LCCN=b.LCCN and b.isbn=? and a.AuthorName=? and b.DDSN=?");

						statement.setString(1, isbn.getText());
						statement.setString(2, author.getText());
						statement.setString(3, dewydecimal.getText());

					}
					// search on author and isbn
					else {
						statement = connect.prepareStatement(
								"Select * from publications p,books b,authors a where p.LCCN=b.LCCN and p.Resourceid=a.Resourceid and b.isbn=? and a.AuthorName=?");
						statement.setString(1, isbn.getText());
						statement.setString(2, author.getText());
					}

				}

				// search on author and dewydecimal
				else if (dewydecimal.getText().length() > 0) {

					statement = connect.prepareStatement(
							"Select * from publications p,books b,authors a where p.LCCN=b.LCCN  and p.Resourceid=a.Resourceid  and b.DDSN=? and  a.AuthorName=?");
					statement.setString(1, dewydecimal.getText());
					statement.setString(2, author.getText());

				}

				// serach author
				else {
					statement = connect.prepareStatement(
							"Select * from publications p,Authors a,books b where p.Resourceid=a.Resourceid and p.LCCN=b.LCCN and a.Authorname=?");
					statement.setString(1, author.getText());

				}
				// Excuting Query
				resultSet = statement.executeQuery();
				// make rows clear
				while (model4.getRowCount() > 0) {

					model4.removeRow(0);
				}
				// Creating the table with model type and its corresponding parameters
				table.setModel(model4);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				String aut = "";
				String ISBN = "";
				String DDSN = "";
				String tit = "";
				int i = 0;
				// loop runs and fetches all rows from the result obtained
				while (resultSet.next()) {
					aut = resultSet.getString("AuthorName");
					ISBN = resultSet.getString("ISBN");
					DDSN = resultSet.getString("DDSN");
					tit = resultSet.getString("Title");
					model4.addRow(new Object[] { aut, ISBN, DDSN, tit });
					i++;
				}
				// if no rows are returned 'No Record Found' message is displayed
				if (i < 1) {
					JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (i == 1) {
					System.out.println(i + " Record Found");
				} else {
					System.out.println(i + " Records Found");
				}

				statement.close();
			}

			// fetch based on ISBN
			else if (isbn.getText().length() > 0) {
				PreparedStatement statement = connect
						.prepareStatement("Select * from books b,publications p where p.LCCN=b.LCCN  and b.ISBN=?");
				statement.setString(1, isbn.getText());
				// Excuting Query
				resultSet = statement.executeQuery();
				// make rows clear
				while (model5.getRowCount() > 0) {

					model5.removeRow(0);
				}
				// Creating the table with model type and its corresponding parameters
				table.setModel(model5);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				String ISBN = "";
				String titl = "";
				String typebk = "";
				String isfictn = "";
				int i = 0;
				// loop runs and fetches all rows from the result obtained
				while (resultSet.next()) {
					ISBN = resultSet.getString("ISBN");
					titl = resultSet.getString("Title");
					typebk = resultSet.getString("Typeofbook");
					isfictn = resultSet.getString("IsFiction");
					model5.addRow(new Object[] { ISBN, titl, typebk, isfictn });
					i++;
				}
				// if no rows are returned 'No Record Found' message is displayed
				if (i < 1) {
					JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (i == 1) {
					System.out.println(i + " Record Found");
				} else {
					System.out.println(i + " Records Found");
				}
				statement.close();
			}

			// Search on Dewy Decimal Number
			else if (dewydecimal.getText().length() > 0) {
				PreparedStatement statement = connect.prepareStatement("Select * from Books where DDSN=?");
				statement.setString(1, dewydecimal.getText());
				// Excuting Query
				resultSet = statement.executeQuery();
				table.setModel(new DefaultTableModel());
				// Clearing Rows
				while (model1.getRowCount() > 0) {

					model1.removeRow(0);
				}
				// Creating the table with model type and its corresponding parameters
				table.setModel(model1);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				String ISBN = "";
				String DDSN = "";
				int i = 0;
				// loop runs and fetches all rows from the result obtained
				while (resultSet.next()) {
					ISBN = resultSet.getString("ISBN");
					DDSN = resultSet.getString("DDSN");
					model1.addRow(new Object[] { ISBN, DDSN });
					i++;
				}
				// if no rows are returned 'No Record Found' message is displayed
				if (i < 1) {
					JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if (i == 1) {
					System.out.println(i + " Record Found");
				} else {
					System.out.println(i + " Records Found");
				}
				statement.close();
			}

			// If none of the fields are entered the below message is displayed
			else {
				JOptionPane.showMessageDialog(null, "Please Enter Atleast One Field To Search");
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

	// close the resultSet,connections,statement after every method
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * Create the frame.
	 */
	public libraryman() {
		setTitle("Library Book Entry");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 490);
		contentPane = new JPanel();
		contentPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		contentPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("TITLE");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel.setBounds(10, 14, 48, 14);
		contentPane.add(lblNewLabel);

		title = new JTextField();
		title.setBounds(78, 12, 315, 17);
		contentPane.add(title);
		title.setColumns(10);

		JLabel lblAuthors = new JLabel("AUTHOR(S)");
		lblAuthors.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblAuthors.setBounds(10, 45, 68, 14);
		contentPane.add(lblAuthors);

		author = new JTextField();
		author.setBounds(78, 45, 315, 14);
		contentPane.add(author);
		author.setColumns(10);

		JLabel lblIsbn = new JLabel("ISBN");
		lblIsbn.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblIsbn.setBounds(10, 80, 48, 14);
		contentPane.add(lblIsbn);

		isbn = new JTextField();
		isbn.setBounds(81, 70, 315, 20);
		contentPane.add(isbn);
		isbn.setColumns(10);

		JLabel lblDewydecimal = new JLabel("DEWYDECIMAL");
		lblDewydecimal.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblDewydecimal.setBounds(10, 105, 83, 14);
		contentPane.add(lblDewydecimal);

		dewydecimal = new JTextField();
		dewydecimal.setBounds(122, 101, 96, 14);
		contentPane.add(dewydecimal);
		dewydecimal.setColumns(10);

		JButton clearbtn = new JButton("CLEAR");
		clearbtn.setBounds(39, 159, 80, 14);
		contentPane.add(clearbtn);
		/*
		 * Clear methods action handler clears all the fields
		 * 
		 */
		clearbtn.addActionListener(new AbstractAction("CLEAR") {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					title.setText("");
					author.setText("");
					isbn.setText("");
					dewydecimal.setText("");

				}

				catch (Exception ex) {

					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

				}

				finally {
					close();
				}

			}

		});

		JButton btnSearch = new JButton("SEARCH");
		btnSearch.setBounds(212, 159, 108, 14);
		contentPane.add(btnSearch);
		/*
		 * Calls the search method of Action performed
		 */
		btnSearch.addActionListener(this);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 184, 776, 226);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(table);

		table.setColumnSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] { {}, {}, }, new String[] {}));
		table.setCellSelectionEnabled(true);

		JLabel lblLibrary = DefaultComponentFactory.getInstance().createTitle("LIBRARY");
		lblLibrary.setBounds(29, -11, 91, 14);
		contentPane.add(lblLibrary);

		JLabel lblSearch = new JLabel("Search and Update");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblSearch.setBounds(10, 426, 96, 14);
		contentPane.add(lblSearch);

		JButton update = new JButton("UPDATE");
		update.setBounds(370, 155, 108, 14);
		contentPane.add(update);
		/*
		 * Update Function implementation in actionperformed
		 */

		update.addActionListener(new AbstractAction("UPDATE") {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					// Create connections
					Class.forName("com.mysql.jdbc.Driver");
					connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement", "root", "");
					int flag=0;
					// Update based on ISBN as reference
					if ((isbn.getText().length() > 0)) {
						// If all other fields are given for updation
						if ((author.getText().length() > 0) && (dewydecimal.getText().length() > 0)
								&& (title.getText().length() > 0)) {

							// Call for stored procedure is done taking the parameters from GUI
							CallableStatement statementproc = connect
									.prepareCall("{call librarymanagement.Updateisdewautitl(?, ?, ?, ?, ?)}");
							statementproc.setString(1, dewydecimal.getText());
							statementproc.setString(2, title.getText());
							statementproc.setString(3, isbn.getText());
							statementproc.setString(4, author.getText());
							statementproc.execute();
							flag=statementproc.getInt("flag");
							statementproc.close();
							
							
							

						}

						// Update based on Title
						else if ((title.getText().length() > 0)) {

							// Update both author,title
							if ((author.getText().length() > 0)) {

								// Call for stored procedure is done taking the parameters from GUI
								CallableStatement statementproc = connect
										.prepareCall("{call librarymanagement.Updateautitl(?, ?, ?, ?)}");
								statementproc.setString(1, title.getText());
								statementproc.setString(2, isbn.getText());
								statementproc.setString(3, author.getText());
								statementproc.execute();
								flag=statementproc.getInt("flag");
								statementproc.close();

							}

							// Update both author,dewydecimal
							else if ((dewydecimal.getText().length() > 0)) {

								// Call for other stored procedure is done taking the parameters from GUI
								CallableStatement statementproc = connect
										.prepareCall("{call librarymanagement.Updatedewatitl(?, ?, ?, ?)}");
								statementproc.setString(1, dewydecimal.getText());
								statementproc.setString(2, title.getText());
								statementproc.setString(3, isbn.getText());
								statementproc.execute();
								flag=statementproc.getInt("flag");
								statementproc.close();

							}

							// Update only title
							else {

								// Call for other stored procedure is done taking the parameters from GUI
								CallableStatement statementproc = connect
										.prepareCall("{call librarymanagement.Updatetitle(?, ?, ?)}");
								statementproc.setString(1, title.getText());
								statementproc.setString(2, isbn.getText());
								statementproc.execute();
								flag=statementproc.getInt("flag");
								statementproc.close();

							}

						}

						// Update Based on author
						else if ((author.getText().length() > 0)) {

							// update both author and dewydecimal
							if ((dewydecimal.getText().length() > 0)) {

								// Call for other stored procedure is done taking the parameters from GUI
								CallableStatement statementproc = connect
										.prepareCall("{call librarymanagement.Updateautdew(?, ?, ?,?)}");
								statementproc.setString(1, dewydecimal.getText());
								statementproc.setString(2, isbn.getText());
								statementproc.setString(3, author.getText());
								statementproc.execute();
								flag=statementproc.getInt("flag");
								statementproc.close();

							}

							// update only author
							else {

								// Call for other stored procedure is done taking the parameters from GUI
								CallableStatement statementproc = connect
										.prepareCall("{call librarymanagement.Updateauthor(?, ?, ?)}");
								statementproc.setString(1, isbn.getText());
								statementproc.setString(2, author.getText());
								statementproc.execute();
								flag=statementproc.getInt("flag");
								statementproc.close();

							}

						}
						// update dewydecimal only
						else if ((dewydecimal.getText().length() > 0)) {

							// Call for other stored procedure is done taking the parameters from GUI
							CallableStatement statementproc = connect
									.prepareCall("{call librarymanagement.Updatedewy(?, ?, ?)}");
							statementproc.setString(1, isbn.getText());
							statementproc.setString(2, dewydecimal.getText());
							statementproc.execute();
							flag=statementproc.getInt("flag");
							statementproc.close();

						}
						
						if(flag>0) {
							
							JOptionPane.showMessageDialog(null,"Successfully Data Updated");
						}
						
						else {
							
							JOptionPane.showMessageDialog(null,"Data cannot be updated due to wrong inputs");
						}
						
						
						

					}
					// Dispaly message to enter the fields and ISBN for Updation
					else {

						JOptionPane.showMessageDialog(null, "Please enter ISBN and atleast one field for updation");

					}

				}

				// Expection handling with error message displaying
				catch (Exception ex) {

					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

				}

				finally {
					close();
				}

			}

		});
	}
}
