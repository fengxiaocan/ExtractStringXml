package com.evil.extract;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.evil.extract.FileChoose.ChooseCallback;

public class BaseUi {

	private JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BaseUi window = new BaseUi();
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
	public BaseUi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(Constants.POSITION_X, Constants.POSITION_Y, Constants.WIDTH, Constants.HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("\u9009\u62E9android \u9879\u76EE\u4E0B\u7684res -> values ->strings.xml");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileChoose.select(new ChooseCallback() {
					
					@Override
					public int setFileSelectionMode() {
						return JFileChooser.FILES_ONLY;
					}
					
					@Override
					public Map<String, String[]> setExtensionFilter() {
						Map<String, String[]> map = new HashMap();
						map.put("xml", new String[]{"xml"});
						return map;
					}
					
					@Override
					public String setChooseTitle() {
						return "请选择string.xml文件";
					}
					
					@Override
					public void chooseResult(File file) {
						new Thread(new Runnable() {
							public void run() {
								try {
									BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
									File outFile = new File(".");
									BufferedWriter bwXml = new BufferedWriter(new FileWriter(new File(outFile, "string.xml")));
									String line;
									while ((line = br.readLine()) != null) {
										if (line.contains("name=")) {
											int endIndex = line.indexOf("\">");
											int beginIndex1 = endIndex + 2;
											int endIndex1 = line.indexOf("</string>");
											String value = line.substring(beginIndex1, endIndex1);
											bwXml.write(value);
											bwXml.newLine();
										}
									}
									br.close();
									bwXml.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
								JOptionPane.showMessageDialog(null, "success", "提取成功!", JOptionPane.INFORMATION_MESSAGE); 
							}
						}).start();
					}
					
					@Override
					public Component attachParent() {
						return frame;
					}
				});
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(82)
					.addComponent(btnNewButton)
					.addContainerGap(14, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(32)
					.addComponent(btnNewButton)
					.addContainerGap(206, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
