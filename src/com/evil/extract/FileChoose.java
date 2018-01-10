package com.evil.extract;

import java.awt.Component;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FileChoose {
	public static void select(ChooseCallback callback) {
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView(); // 注意了，这里重要的一句
		Map<String, String[]> filters = callback.setExtensionFilter();
		if (filters != null) {
			Set<String> set = filters.keySet();
			for (String key : set) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter(key, filters.get(key));
				fileChooser.addChoosableFileFilter(filter);
			}
		}

		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle(callback.setChooseTitle());
		fileChooser.setApproveButtonText("确定");

		fileChooser.setFileSelectionMode(callback.setFileSelectionMode());

		int result = fileChooser.showOpenDialog(callback.attachParent());

		if (JFileChooser.APPROVE_OPTION == result) {
			callback.chooseResult(fileChooser.getSelectedFile());
		}
	}

	public interface ChooseCallback {
		/**
		 * 设置过滤器
		 * 
		 * @return {"text"{"txt,xml,java"},"java"{"java"}}
		 */
		Map<String, String[]> setExtensionFilter();

		/**
		 * 设置标题
		 * 
		 * @return
		 */
		String setChooseTitle();

		/**
		 * @return 依附的窗口
		 */
		Component attachParent();

		/**
		 * 结果回调
		 * 
		 * @param file
		 */
		void chooseResult(File file);

		/**
		 * 设置选择模式
		 * 
		 * @return JFileChooser.FILES_AND_DIRECTORIES 等
		 */
		int setFileSelectionMode();
	}
}
