package com.example.mrzhang.databasedemo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {
	private ClassUtil(){}

	/**
	 * @param class1 当前的class文件
	 * @return 与其同目录的所有class名字
	 */
	public static List<String> getClassesName(Class<?> class1){
		List<String> list = new ArrayList<>();
		URL resource = class1.getResource("");
		File classFile = new File(resource.getFile());
		
		File[] files = classFile.listFiles();
		for (File file : files) {
			System.out.println(file.getAbsolutePath()+"++++++++++++++++++++++");
		}
		
		
		return list;
	}
}
