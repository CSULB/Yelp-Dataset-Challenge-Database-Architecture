package edu.csulb.yelp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import edu.csulb.yelp.Models.AttributesModel;
import edu.csulb.yelp.Models.BusinessModel;
import edu.csulb.yelp.Models.CategoryModel;
import edu.csulb.yelp.Models.ReviewsModel;
import edu.csulb.yelp.database.DatabaseKeys.Attributes;
import edu.csulb.yelp.database.DatabaseKeys.BusinessTable;
import edu.csulb.yelp.database.DatabaseKeys.CategoriesTable;
import edu.csulb.yelp.database.DatabaseKeys.ReviewsTable;
import edu.csulb.yelp.database.DatabaseKeys.UsersTable;

public class hw3 {

	private JFrame frame;
	private JList<String> mainCategoryList, attributesList, subCategoryCheckboxes;
	private JTable filteredBusinesses;
	private DefaultTableModel tModel;
	private String subCategoryCSV, attributesCSV;
	private JComboBox dayOfWeek, startTime, endTime;
	private JButton btnSearch;
	private JLabel lblDayOfThe;
	private JLabel lblOpensAt;
	private JLabel lblClosesAt;
	private String selectedMainCategoriesCSV;
	private JLabel lblMainCategories;
	private JLabel lblSubcategories;
	private JLabel lblAttributes;
	private JLabel lblMatchingBusiness;
	private JTextField txtCity;
	private JTextField txtZipcode;
	private JTextField txtState;
	private JLabel lblFiltersuseAfter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 window = new hw3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public hw3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1332, 665);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel txtpnUseCtrl = new JLabel();
		txtpnUseCtrl.setText(
				"Use Ctrl + Click to select multiple Categories and Subcategories. Click on a row to see the business' reviews");
		GridBagConstraints c1 = new GridBagConstraints();
		c1.insets = new Insets(0, 0, 5, 0);
		c1.gridwidth = 4;
		c1.fill = GridBagConstraints.BOTH;
		c1.gridx = 0;
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridy = 0;
		frame.getContentPane().add(txtpnUseCtrl, c1);

		lblMainCategories = new JLabel("Main Categories");
		lblMainCategories.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblMainCategories = new GridBagConstraints();
		gbc_lblMainCategories.insets = new Insets(0, 0, 5, 5);
		gbc_lblMainCategories.gridx = 0;
		gbc_lblMainCategories.gridy = 1;
		frame.getContentPane().add(lblMainCategories, gbc_lblMainCategories);

		lblSubcategories = new JLabel("Sub-Categories");
		lblSubcategories.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblSubcategories = new GridBagConstraints();
		gbc_lblSubcategories.insets = new Insets(0, 0, 5, 5);
		gbc_lblSubcategories.gridx = 1;
		gbc_lblSubcategories.gridy = 1;
		frame.getContentPane().add(lblSubcategories, gbc_lblSubcategories);

		lblAttributes = new JLabel("Attributes");
		lblAttributes.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblAttributes = new GridBagConstraints();
		gbc_lblAttributes.insets = new Insets(0, 0, 5, 5);
		gbc_lblAttributes.gridx = 2;
		gbc_lblAttributes.gridy = 1;
		frame.getContentPane().add(lblAttributes, gbc_lblAttributes);

		lblMatchingBusiness = new JLabel("Matching Business");
		lblMatchingBusiness.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblMatchingBusiness = new GridBagConstraints();
		gbc_lblMatchingBusiness.insets = new Insets(0, 0, 5, 0);
		gbc_lblMatchingBusiness.gridx = 3;
		gbc_lblMatchingBusiness.gridy = 1;
		frame.getContentPane().add(lblMatchingBusiness, gbc_lblMatchingBusiness);

		mainCategoryList = new JList<String>();
		GridBagConstraints c2 = new GridBagConstraints();
		c2.insets = new Insets(0, 0, 5, 5);
		c2.weightx = 0.2;
		c2.fill = GridBagConstraints.BOTH;
		c2.gridx = 0;
		c2.gridy = 2;
		frame.getContentPane().add(new JScrollPane(mainCategoryList), c2);

		subCategoryCheckboxes = new JList<String>();
		GridBagConstraints c3 = new GridBagConstraints();
		c3.insets = new Insets(0, 0, 5, 5);
		c3.weightx = 0.2;
		c3.fill = GridBagConstraints.BOTH;
		c3.gridx = 1;
		c3.gridy = 2;
		frame.getContentPane().add(new JScrollPane(subCategoryCheckboxes), c3);

		attributesList = new JList<String>();
		GridBagConstraints c4 = new GridBagConstraints();
		c4.insets = new Insets(0, 0, 5, 5);
		c4.weightx = 0.2;
		c4.fill = GridBagConstraints.BOTH;
		c4.gridx = 2;
		c4.gridy = 2;
		frame.getContentPane().add(new JScrollPane(attributesList), c4);

		tModel = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		filteredBusinesses = new JTable();
		filteredBusinesses.setModel(tModel);
		tModel.addColumn("ID");
		tModel.addColumn("Name");
		tModel.addColumn("City");
		tModel.addColumn("State");
		tModel.addColumn("Zip");
		tModel.addColumn("Stars");
		filteredBusinesses.getColumnModel().getColumn(0).setWidth(0);
		filteredBusinesses.getColumnModel().getColumn(0).setMinWidth(0);
		filteredBusinesses.getColumnModel().getColumn(0).setMaxWidth(0);

		GridBagConstraints c5 = new GridBagConstraints();
		c5.insets = new Insets(0, 0, 5, 0);
		c5.weightx = 0.4;
		c5.fill = GridBagConstraints.BOTH;
		c5.gridx = 3;
		c5.gridy = 2;
		frame.getContentPane().add(new JScrollPane(filteredBusinesses), c5);

		String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

		lblDayOfThe = new JLabel("Day Of The Week");
		lblDayOfThe.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblDayOfThe = new GridBagConstraints();
		gbc_lblDayOfThe.insets = new Insets(0, 0, 5, 5);
		gbc_lblDayOfThe.gridx = 0;
		gbc_lblDayOfThe.gridy = 3;
		frame.getContentPane().add(lblDayOfThe, gbc_lblDayOfThe);

		lblOpensAt = new JLabel("Opens At");
		lblOpensAt.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblOpensAt = new GridBagConstraints();
		gbc_lblOpensAt.insets = new Insets(0, 0, 5, 5);
		gbc_lblOpensAt.gridx = 1;
		gbc_lblOpensAt.gridy = 3;
		frame.getContentPane().add(lblOpensAt, gbc_lblOpensAt);

		lblClosesAt = new JLabel("Closes At");
		lblClosesAt.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblClosesAt = new GridBagConstraints();
		gbc_lblClosesAt.insets = new Insets(0, 0, 5, 5);
		gbc_lblClosesAt.gridx = 2;
		gbc_lblClosesAt.gridy = 3;
		frame.getContentPane().add(lblClosesAt, gbc_lblClosesAt);

		dayOfWeek = new JComboBox(days);
		GridBagConstraints c6 = new GridBagConstraints();
		c6.insets = new Insets(0, 0, 5, 5);
		c6.fill = GridBagConstraints.HORIZONTAL;
		c6.gridx = 0;
		c6.gridy = 4;
		frame.getContentPane().add(dayOfWeek, c6);

		String[] time = new String[24];
		for (int i = 0; i < 24; i++) {
			time[i] = String.valueOf(i) + ":00";
		}
		startTime = new JComboBox(time);
		GridBagConstraints c7 = new GridBagConstraints();
		c7.insets = new Insets(0, 0, 5, 5);
		c7.fill = GridBagConstraints.HORIZONTAL;
		c7.gridx = 1;
		c7.gridy = 4;
		frame.getContentPane().add(startTime, c7);

		endTime = new JComboBox(time);
		GridBagConstraints c8 = new GridBagConstraints();
		c8.insets = new Insets(0, 0, 5, 5);
		c8.fill = GridBagConstraints.HORIZONTAL;
		c8.gridx = 2;
		c8.gridy = 4;
		frame.getContentPane().add(endTime, c8);

		// lblFiltersuseAfter = new JLabel("Filters (Use after you get
		// matches)");
		// lblFiltersuseAfter.setFont(new Font("Tahoma", Font.BOLD, 11));
		// GridBagConstraints gbc_lblFiltersuseAfter = new GridBagConstraints();
		// gbc_lblFiltersuseAfter.insets = new Insets(0, 0, 5, 5);
		// gbc_lblFiltersuseAfter.gridx = 0;
		// gbc_lblFiltersuseAfter.gridy = 5;
		// frame.getContentPane().add(lblFiltersuseAfter,
		// gbc_lblFiltersuseAfter);

		btnSearch = new JButton("Search");
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.gridheight = 3;
		gbc_btnSearch.gridx = 3;
		gbc_btnSearch.gridy = 4;
		frame.getContentPane().add(btnSearch, gbc_btnSearch);

		// txtState = new JTextField();
		// txtState.setText("State");
		// GridBagConstraints gbc_txtState = new GridBagConstraints();
		// gbc_txtState.insets = new Insets(0, 0, 0, 5);
		// gbc_txtState.fill = GridBagConstraints.HORIZONTAL;
		// gbc_txtState.gridx = 0;
		// gbc_txtState.gridy = 6;
		// frame.getContentPane().add(txtState, gbc_txtState);
		// txtState.setColumns(1);
		// txtState.getDocument().addDocumentListener(new DocumentListener() {
		//
		// @Override
		// public void changedUpdate(DocumentEvent arg0) {
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent arg0) {
		// }
		//
		// @Override
		// public void removeUpdate(DocumentEvent arg0) {
		// }
		// });
		//
		// txtCity = new JTextField();
		// txtCity.setText("City");
		// GridBagConstraints gbc_txtCity = new GridBagConstraints();
		// gbc_txtCity.insets = new Insets(0, 0, 0, 5);
		// gbc_txtCity.fill = GridBagConstraints.HORIZONTAL;
		// gbc_txtCity.gridx = 1;
		// gbc_txtCity.gridy = 6;
		// frame.getContentPane().add(txtCity, gbc_txtCity);
		// txtCity.setColumns(1);
		// txtCity.getDocument().addDocumentListener(new DocumentListener() {
		//
		// @Override
		// public void changedUpdate(DocumentEvent arg0) {
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent arg0) {
		// }
		//
		// @Override
		// public void removeUpdate(DocumentEvent arg0) {
		// }
		// });
		//
		// txtZipcode = new JTextField();
		// txtZipcode.setText("Zipcode");
		// GridBagConstraints gbc_txtZipcode = new GridBagConstraints();
		// gbc_txtZipcode.insets = new Insets(0, 0, 0, 5);
		// gbc_txtZipcode.fill = GridBagConstraints.HORIZONTAL;
		// gbc_txtZipcode.gridx = 2;
		// gbc_txtZipcode.gridy = 6;
		// frame.getContentPane().add(txtZipcode, gbc_txtZipcode);
		// txtZipcode.setColumns(1);
		// txtZipcode.getDocument().addDocumentListener(new DocumentListener() {
		//
		// @Override
		// public void changedUpdate(DocumentEvent arg0) {
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent arg0) {
		// }
		//
		// @Override
		// public void removeUpdate(DocumentEvent arg0) {
		// }
		// });

		setupMainCategories();
		setupSubCategories();
		setAttributes();
		setupReviewsTable();
		setupJButton();
	}

	private void setupJButton() {
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BusinessModel bm = new BusinessModel();
				if (bm.open()) {
					int day = 1;
					String selectedDay = String.valueOf(dayOfWeek.getSelectedItem());
					switch (selectedDay) {
					case "Monday":
						day = 2;
						break;
					case "Tuesday":
						day = 3;
						break;
					case "Wednesday":
						day = 4;
						break;
					case "Thursday":
						day = 5;
						break;
					case "Friday":
						day = 6;
						break;
					case "Saturday":
						day = 7;
						break;
					default:
						break;
					}
					String start = "'" + String.valueOf(startTime.getSelectedItem()) + "'";
					String end = "'" + String.valueOf(endTime.getSelectedItem()) + "'";

					ResultSet rs = bm.customTimeSelect(day, start, end, attributesCSV);
					try {
						tModel.setRowCount(0);
						while (rs.next()) {
							String[] cityAndZip = rs.getString(BusinessTable.CITY).split("##");
							tModel.addRow(new Object[] { rs.getString(BusinessTable.BUSINESS_ID),
									rs.getString(BusinessTable.NAME), cityAndZip[0], rs.getString(BusinessTable.STATE),
									cityAndZip[1], rs.getString(BusinessTable.STARS) });
						}
						rs.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	private void setupReviewsTable() {
		filteredBusinesses.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evnt) {
				if (evnt.getClickCount() == 1) {
					System.out.println("Row: " + filteredBusinesses.getSelectedRow());
					String businessId = (String) tModel.getValueAt(filteredBusinesses.getSelectedRow(), 0);

					DefaultTableModel model = new DefaultTableModel() {

						@Override
						public boolean isCellEditable(int row, int column) {
							return false;
						}
					};

					JTable reviews = new JTable(model);
					JFrame frame2 = new JFrame();
					frame2.getContentPane().setLayout(new BorderLayout());
					frame2.getContentPane().add(new JScrollPane(reviews));
					frame2.pack();
					frame2.setLocationRelativeTo(null);
					frame2.setVisible(true);
					frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

					model.addColumn("Review Date");
					model.addColumn("Stars");
					model.addColumn("Username");
					model.addColumn("Comment");
					model.addColumn("Useful Votes");

					try {
						ReviewsModel rm = new ReviewsModel();
						if (rm.open()) {
							model.setRowCount(0);
							ResultSet rs = rm.selectBusinessReviews(businessId);
							while (rs.next()) {
								model.addRow(new Object[] { rs.getString(ReviewsTable.POSTED_ON),
										rs.getString(ReviewsTable.STARS), rs.getString(UsersTable.NAME),
										rs.getString(ReviewsTable.TEXT), rs.getString(ReviewsTable.VOTES_USEFUL) });
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void setAttributes() {
		attributesList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {

					// Get all selected values into an array.
					List<String> selectedList = attributesList.getSelectedValuesList();
					tModel.setRowCount(0);
					if (selectedList.size() > 0) {

						// Create a CSV for the IN clause
						String selectedListCSV = "";
						for (String string : selectedList) {
							selectedListCSV += "'" + string + "',";
						}

						// Remove trailing comma.
						attributesCSV = selectedListCSV = selectedListCSV.substring(0, selectedListCSV.length() - 1);

						BusinessModel bm = new BusinessModel();
						if (bm.open()) {
							// Populate all the sub categories.
							ResultSet rs = bm.customSelect(subCategoryCSV, attributesCSV);
							try {
								tModel.setRowCount(0);
								while (rs.next()) {
									String[] cityAndZip = rs.getString(BusinessTable.CITY).split("##");
									tModel.addRow(new Object[] { rs.getString(BusinessTable.BUSINESS_ID),
											rs.getString(BusinessTable.NAME), cityAndZip[0],
											rs.getString(BusinessTable.STATE), cityAndZip[1],
											rs.getString(BusinessTable.STARS) });
								}
								rs.close();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							bm.close();
						}
					} else {
						attributesList.removeAll();
						tModel.setRowCount(0);
					}
				}
			}
		});
	}

	private void setupSubCategories() {
		subCategoryCheckboxes.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {

					// Get all selected values into an
					// array.
					List<String> selectedList = subCategoryCheckboxes.getSelectedValuesList();
					attributesList.setListData(new String[] {});
					tModel.setRowCount(0);
					if (selectedList.size() > 0) {

						// Create a CSV for the IN clause
						String selectedListCSV = "";
						for (String string : selectedList) {
							selectedListCSV += "'" + string + "',";
						}

						// Remove trailing comma.
						subCategoryCSV = selectedListCSV = selectedListCSV.substring(0, selectedListCSV.length() - 1);

						AttributesModel am = new AttributesModel();
						if (am.open()) {
							// Create an array for all the
							// sub
							// categories.
							int rowCount = am.getAttributesCount(selectedListCSV);
							String[] subCheckboxes = new String[rowCount];

							// Populate all the sub categories.
							ResultSet rs = am.getAttributes(selectedListCSV);
							try {
								int i = 0;
								while (rs.next()) {
									subCheckboxes[i] = rs.getString(Attributes.ATT_NAME);
									i++;
								}
								rs.close();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							attributesList.setListData(subCheckboxes);
							am.close();
						}
					} else {
						attributesList.setListData(new String[] {});
						tModel.setRowCount(0);
					}
				}
			}
		});
	}

	private void setupMainCategories() {

		try {
			CategoryModel cm = new CategoryModel();
			if (cm.open()) {
				int rowCount = cm.countAll();
				String[] mainCheckBoxes = new String[rowCount];
				ResultSet rs = cm.getMainCategories();
				int i = 0;
				while (rs.next()) {
					mainCheckBoxes[i] = rs.getString(CategoriesTable.CAT_NAME);
					i++;
				}
				rs.close();
				mainCategoryList.setListData(mainCheckBoxes);
			}

			mainCategoryList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {

						// Get all selected values into an array.
						List<String> selectedList = mainCategoryList.getSelectedValuesList();
						attributesList.setListData(new String[] {});
						tModel.setRowCount(0);
						if (selectedList.size() > 0) {

							// Create a CSV for the IN clause
							selectedMainCategoriesCSV = "";
							for (String string : selectedList) {
								selectedMainCategoriesCSV += "'" + string + "',";
							}

							// Remove trailing comma.
							selectedMainCategoriesCSV = selectedMainCategoriesCSV.substring(0,
									selectedMainCategoriesCSV.length() - 1);

							// Create an array for all the sub categories.
							int rowCount = cm.getSubCategoriesCount(selectedMainCategoriesCSV);
							String[] subCheckboxes = new String[rowCount];

							// Populate all the sub categories.
							ResultSet rs = cm.getSubCategories(selectedMainCategoriesCSV);
							try {
								int i = 0;
								while (rs.next()) {
									subCheckboxes[i] = rs.getString(CategoriesTable.CAT_NAME);
									i++;
								}
								rs.close();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							subCategoryCheckboxes.setListData(subCheckboxes);
						} else {
							attributesList.setListData(new String[] {});
							tModel.setRowCount(0);
						}

					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
