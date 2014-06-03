/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-4-20       上午11:25:25
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package com.nf.framework.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nf.framework.ImageUtil;
import com.nf.framework.exception.LogUtil;

public class FileUtils {


	public static FileUtils fileHelper = null;

	public static FileUtils getInstance() {

		if (fileHelper == null) {

			fileHelper = new FileUtils();
		}
		return fileHelper;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 *            要创建的文件名
	 * @return 创建得到的文件
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(new SdCardUtil().getSDCardPath()+File.separator+ fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 创建绝对路径文件夹
	 * 
	 * @param fileName
	 *            要创建的文件名
	 * @return 创建得到的文件
	 */
	public File createAbsoluteDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 创建绝对路径文件
	 * 
	 * @param fileName
	 *            要创建的文件名
	 * @return 创建得到的文件
	 */
	public File createFile(String filePath, String fileName) throws IOException {
		String fileStr = null;
		if (!filePath.endsWith(File.separator)) {
			fileStr = filePath + File.separator + fileName;
		} else {
			fileStr = filePath + fileName;
		}
		File file = new File(fileStr);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 *            要创建的目录名
	 * @return 创建得到的目录
	 */
	public File createAbsoluteSDDir(String absoluteDirName) {
		File dir = new File(absoluteDirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 获取图片扩展名
	 * 
	 * @param picUrl
	 * @return
	 */
	public String getPicSty(String picUrl) {

		return picUrl.substring(picUrl.lastIndexOf(".") + 1, picUrl.length());

	}

	/**
	 * 判断包是否存在
	 * 
	 * @param absolutePackagePath
	 *            绝对路径
	 * @return boolean, true表示存在，false表示不存在
	 */
	public boolean isAbsolutePackageExist(String absolutePackagePath) {
		File file = new File(absolutePackagePath);
		return file.exists();
	}
	/**
	 * 读取sdcard文件夹中的图片，并根据屏幕精度生成略缩图
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap buildBitmap(String absolutepicPath, float density) {
		if (absolutepicPath == null) {
			return null;
		}
		Bitmap bitmap = null;
		try {
			File imgFile = new File(absolutepicPath);

			if (imgFile.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(imgFile);
				if (fis.available() != 0) {
					bitmap = ImageUtil.decodeBitmapFromFile(imgFile.toString(),
							density);
				}
			}
		} catch (Exception e) {

			e.getStackTrace();
		}
		return bitmap;
	}

//	/**
//	 * 将一个输入流中的内容写入到SD卡上生成文件
//	 * 
//	 * @param path
//	 *            文件目录
//	 * @param fileName
//	 *            文件名
//	 * @param inputStream
//	 *            字节输入流
//	 * @return 得到的文件
//	 */
//	public File writeToSDCard(String path, String fileName, Bitmap bitmap) {
//		File file = null;
//		OutputStream output = null;
//		try {
//			if (!isPackageExist(path))
//				createSDDir(path);
//			file = createSDFile(path + File.separator + fileName);
//			output = new FileOutputStream(file);
//			byte buffer[] = new byte[4 * 1024];
//			InputStream inputStream = ImageUtil.BitmapTOInputStream(bitmap);
//			bitmap = null;
//			while ((inputStream.read(buffer)) != -1) {
//				output.write(buffer);
//			}
//			output.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				output.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return file;
//	}
//
//	/**
//	 * 将一个输入流中的内容写入到SD卡上生成文件
//	 * 
//	 * @param 觉得路径文件
//	 * @param inputStream
//	 *            字节输入流
//	 * @return 得到的文件
//	 */
//	public File writeToSDCard(String absoluteFilePath, Bitmap bitmap) {
//		File file = null;
//		OutputStream output = null;
//		try {
//			file = new File(absoluteFilePath);
//			file.createNewFile();
//			output = new FileOutputStream(file);
//			byte buffer[] = new byte[4 * 1024];
//			InputStream inputStream = ImageUtil.BitmapTOInputStream(bitmap);
//			bitmap = null;
//			while ((inputStream.read(buffer)) != -1) {
//				output.write(buffer);
//			}
//			output.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				output.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return file;
//	}
//
//	/**
//	 * 将一个输入流中的内容写入到SD卡上生成文件
//	 * 
//	 * @param path
//	 *            文件目录
//	 * @param fileName
//	 *            文件名
//	 * @param inputStream
//	 *            字节输入流
//	 * @return 得到的文件
//	 */
//	public File writeToSDCard(String path, String fileName, String str) {
//		File file = null;
//		try {
//			if (!isPackageExist(path))
//				createSDDir(path);
//			file = createSDFile(path + File.separator + fileName);
//			write(file, str);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return file;
//	}
//
//	/**
//	 * 删除指定路径的文件夹
//	 */
//	public void DeleteDirFolder(String path) {
//
//		DeleteFolder(SDCardPath + path);
//	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			LogUtil.w("FileHelper","FileHelper.deleteFile()________________"+ sPath);
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取Asset文件夹下的文件内容
	 * 
	 * @param mcontext
	 * @param fileName
	 * @return
	 */
	public String getAssetFileContent(Context mcontext, String fileName) {
		InputStream inputStream;
		try {
			inputStream = mcontext.getResources().getAssets().open(fileName);
			return InputStream2String(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * InputStream2String
	 * 
	 * @param inputStream
	 * @return
	 */
	public String InputStream2String(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {

		}
		return outputStream.toString();

	}

	public void XmlByDomj4(Context mcontext, String fileName) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document document = db.parse(mcontext.getResources()
					.getAssets().open(fileName));
			Node root = document.getFirstChild();
			NodeList list1 = root.getChildNodes();
			for (int i = 0; i < list1.getLength(); i++) {
				Node node = list1.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					LogUtil.w(mcontext,node.getNodeName());
					if (node.hasChildNodes()) {
						NodeList list2 = node.getChildNodes();
						for (int a = 0; a < list2.getLength(); a++) {
							Node node2 = list2.item(a);
							if (node2.getNodeType() == Node.ELEMENT_NODE) {
								LogUtil.w(mcontext,node2.getNodeName());
								LogUtil.w(mcontext,node2.getNodeValue());
							}
						}
					} else {
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 解析xml
	 * 
	 * @param mcontext
	 * @param file
	 */
	public Document XmlByDomj4(File file) {
		org.w3c.dom.Document document = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 读取数据
	 * 
	 * @param instr
	 * @param encodeStr
	 * @return
	 * @throws Exception
	 */

	public String read(InputStream instr, String encodeStr) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = instr.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		byte[] data = out.toByteArray();
		out.close();
		return new String(data, encodeStr);
	}

	/**
	 * 读取数据
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws Exception
	 */
	public byte[] readTobyte(String filePath) throws Exception {
		File file = new File(filePath);
		FileInputStream input = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		byte[] data = null;
		int len = -1;
		while ((len = input.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		data = out.toByteArray();
		out.close();
		return data;
	}

	/**
	 * 读取数据
	 * 
	 * @param instr
	 * @param encodeStr
	 * @return
	 * @throws Exception
	 */

	public String read(File file) throws Exception {
		FileInputStream input = new FileInputStream(file);
		return read(input, "utf-8");
	}

	/**
	 * 写入数据
	 * 
	 * @param instr
	 * @return
	 * @throws Exception
	 */

	public void write(File file, String writeData) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(writeData);
		writer.close();
	}

	/**
	 * 将raw中的初始文件保存到指定目录下
	 * 
	 * @param rawId
	 *            raw中文件的id
	 * @param fileSavePath
	 *            转储的文件路径文件名称 final String tempFileName=path+"tempInit.zip";
	 */
	public boolean InitFileToSDCard(Context mcontext, int rawId,
			String fileSavePath) {

		InputStream inputStream = null;
		try {
			inputStream = mcontext.getResources().openRawResource(rawId); // 这里就是Raw文件引用位置
			SaveInputStreamToFile(inputStream, fileSavePath);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		return true;
	}

	/**
	 * 将Assets中的初始文件保存到指定目录下
	 * 
	 * @param assetsFileName
	 *            Assets中文件的名称
	 * @param fileSavePath
	 *            转储的文件路径文件名称
	 */
	public boolean InitAssetsFileToSDCard(Context mcontext,
			String assetsFileName, String fileSavePath) {
		if (assetsFileName == null) {
			return false;
		}
		InputStream inputStream = null;
		try {
			inputStream = mcontext.getResources().getAssets()
					.open(assetsFileName);
			SaveInputStreamToFile(inputStream, fileSavePath);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		return true;
	}

	/***
	 * 将数据流保存到指定文件
	 * 
	 * @param inputStream
	 * @param fileSavePath
	 * @throws Exception
	 */
	public void SaveInputStreamToFile(InputStream inputStream,
			String fileSavePath) throws Exception {

		int len = 4096;//
		int readCount = 0, readSum = 0;
		byte[] buffer = new byte[len];
		FileOutputStream fos = new FileOutputStream(fileSavePath);
		while ((readCount = inputStream.read(buffer)) != -1) {
			readSum += readCount;
			fos.write(buffer, 0, readCount);
		}
		fos.flush();
		fos.close();
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * 复制文件夹
	 * 
	 * @param sourceDir
	 *            不包含该文件夹，只有该文件夹中的全部子文件或文件夹
	 * @param targetDir
	 * @throws IOException
	 */
	public void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + File.separator + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + File.separator + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}

	}

//	/**
//	 * 读取sdcard文件夹中的图片，并生成略缩图
//	 * 
//	 * @return
//	 * @throws FileNotFoundException
//	 */
//	public Bitmap buildBitmap(String packagePath, String picPathName,
//			int reqWidth, int reqHeight) {
//
//		Bitmap bitmap = null;
//		try {
//			File imgFile = new File(SDCardPath + packagePath + "/"
//					+ picPathName);
//
//			if (imgFile.exists()) {
//				FileInputStream fis = null;
//				fis = new FileInputStream(imgFile);
//				if (fis.available() != 0) {
//					bitmap = ImageUtil.decodeBitmapFromFile(imgFile.toString(),
//							reqWidth, reqHeight);
//				}
//			}
//		} catch (Exception e) {
//
//			e.getStackTrace();
//		}
//		return bitmap;
//	}

	/**
	 * 读取sdcard文件夹中的图片，并生成略缩图
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap buildBitmapByAbsolutePath(String absolutepicPath,
			int reqWidth, int reqHeight) {

		Bitmap bitmap = null;
		try {
			File imgFile = new File(absolutepicPath);
			if (imgFile.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(imgFile);
				if (fis.available() != 0) {
					bitmap = ImageUtil.decodeBitmapFromFile(absolutepicPath,
							reqWidth, reqHeight);
				}
			}
		} catch (Exception e) {

			e.getStackTrace();
		}
		return bitmap;
	}

	/**
	 * 读取sdcard文件夹中的图片，并生成略缩图
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap buildBitmapByAbsolutePath(String absolutepicPath) {

		Bitmap bitmap = null;
		try {
			File imgFile = new File(absolutepicPath);
			if (imgFile.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(imgFile);
				if (fis.available() != 0) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = false; // 设置了此属性一定要记得将值设置为false
					bitmap = BitmapFactory.decodeFile(imgFile.toString(),
							options);
				}
			}
		} catch (Exception e) {

			e.getStackTrace();
		}
		return bitmap;
	}

	/**
	 * 遍历指定文件夹
	 * 
	 * @param strPath
	 * @return
	 */
	public List<String> refreshFileList(String strPath) {
		List<String> filelist = new ArrayList<String>();
		File dir = new File(strPath);
		File[] files = dir.listFiles();

		if (files == null)
			return filelist;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				filelist.addAll(refreshFileList(files[i].getAbsolutePath()));
			} else {
				filelist.add(files[i].getAbsolutePath());
			}
		}
		return filelist;
	}

	/**
	 * 遍历指定文件夹 文件名称
	 * 
	 * @param strPath
	 * @return
	 */
	public List<String> queryFileNameList(String strPath) {
		List<String> filelist = new ArrayList<String>();
		File dir = new File(strPath);
		File[] files = dir.listFiles();

		if (files == null)
			return filelist;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				filelist.addAll(refreshFileList(files[i].getName()));
			} else {
				filelist.add(files[i].getName());
			}
		}
		return filelist;
	}

	/**
	 * 
	 * 将文件分成若干个单元 每次按照指定单元读取内容
	 * 
	 * @param sqlFile
	 *            需要按行读取的sql文件
	 * @param filePart
	 *            需要读取的文件部分
	 * @param unit
	 *            每次读取的文件单元
	 * @return
	 */
	public List<String> loadSql(String sqlFile, int filePart, int unit) {
		List<String> sqlList = new ArrayList<String>();
		final int BUFFER_SIZE = 0x300000;// 缓冲区大小为3M
		try {
			File f = new File(sqlFile);
			/**
			 * map(FileChannel.MapMode mode,long position, long size) mode -
			 * 根据是按只读、读取/写入或专用（写入时拷贝）来映射文件，分别为 FileChannel.MapMode 类中所定义的
			 * READ_ONLY、READ_WRITE 或 PRIVATE 之一 position -
			 * 文件中的位置，映射区域从此位置开始；必须为非负数 size - 要映射的区域大小；必须为非负数且不大于
			 * Integer.MAX_VALUE
			 * 
			 * 所以若想读取文件后半部分内容，如例子所写；若想读取文本后1/8内容，需要这样写map(FileChannel.MapMode.
			 * READ_ONLY, f.length()*7/8,f.length()/8)
			 * 
			 * 想读取文件所有内容，需要这样写map(FileChannel.MapMode.READ_ONLY, 0,f.length())
			 */
			MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r")
					.getChannel().map(FileChannel.MapMode.READ_ONLY,
							f.length() / 2, f.length() / 2);
			byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容
			long start = System.currentTimeMillis();
			for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {
				if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {
					for (int i = 0; i < BUFFER_SIZE; i++)
						dst[i] = inputBuffer.get(offset + i);
				} else {
					for (int i = 0; i < inputBuffer.capacity() - offset; i++)
						dst[i] = inputBuffer.get(offset + i);
				}
				int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
						: inputBuffer.capacity() % BUFFER_SIZE;
				sqlList.add(new String(dst, 0, length));// new //
														// String(dst,0,length)这样可以取出缓存保存的字符串，可以对其进行操作
			}
			long end = System.currentTimeMillis();

			LogUtil.i("FileHelper","读取文件文件一半内容花费：" + (end - start) + "毫秒");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlList;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	/* 2013.3.4新增文件复制 文件按行读取等方法 */
	// ///////////////////////////////////////////////////////////////////////////////

	/**
	 * The number of bytes in a kilobyte.
	 */
	public static final long ONE_KB = 1024;

	/**
	 * The number of bytes in a megabyte.
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * The file copy buffer size (10 MB) （原为30MB，为更适合在手机上使用，将其改为10MB，by
	 * Geek_Soledad)
	 */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 10;

	/**
	 * <p>
	 * 将一个目录下的文件全部拷贝到另一个目录里面，并且保留文件日期。
	 * </p>
	 * <p>
	 * 如果目标目录不存在，则被创建。 如果目标目录已经存在，则将会合并两个文件夹的内容，若有冲突则替换掉目标目录中的文件。
	 * </p>
	 * 
	 * @param srcDir
	 *            源目录，不能为null且必须存在。
	 * @param destDir
	 *            目标目录，不能为null。
	 * @throws NullPointerException
	 *             如果源目录或目标目录为null。
	 * @throws IOException
	 *             如果源目录或目标目录无效。
	 * @throws IOException
	 *             如果拷贝中出现IO错误。
	 */
	public static void copyDirectoryToDirectory(File srcDir, File destDir)
			throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (srcDir.exists() && srcDir.isDirectory() == false) {
			throw new IllegalArgumentException("Source '" + destDir
					+ "' is not a directory");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new IllegalArgumentException("Destination '" + destDir
					+ "' is not a directory");
		}
		copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
	}

	/**
	 * <p>
	 * 将目录及其以下子目录拷贝到一个新的位置，并且保留文件日期。
	 * <p>
	 * 如果目标目录不存在，则被创建。 如果目标目录已经存在，则将会合并两个文件夹的内容，若有冲突则替换掉目标目录中的文件。
	 * <p>
	 * 
	 * @param srcDir
	 *            一个存在的源目录，不能为null。
	 * @param destDir
	 *            新的目录，不能为null。
	 * 
	 * @throws NullPointerException
	 *             如果源目录或目标目录为null。
	 * @throws IOException
	 *             如果源目录或目标目录无效。
	 * @throws IOException
	 *             如果拷贝中出现IO错误。
	 */
	public static void copyDirectory(File srcDir, File destDir)
			throws IOException {
		copyDirectory(srcDir, destDir, true);
	}

	/**
	 * 拷贝目录到一个新的位置。
	 * <p>
	 * 该方法将拷贝指定的源目录的所有内容到一个新的目录中。
	 * </p>
	 * <p>
	 * 如果目标目录不存在，则被创建。 如果目标目录已经存在，则将会合并两个文件夹的内容，若有冲突则替换掉目标目录中的文件。
	 * </p>
	 * 
	 * @param srcDir
	 *            一个存在的源目录，不能为null。
	 * @param destDir
	 *            新的目录，不能为null。
	 * 
	 * @throws NullPointerException
	 *             如果源目录或目标目录为null。
	 * @throws IOException
	 *             如果源目录或目标目录无效。
	 * @throws IOException
	 *             如果拷贝中出现IO错误。
	 */
	public static void copyDirectory(File srcDir, File destDir,
			boolean preserveFileDate) throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (srcDir.exists() && srcDir.isDirectory() == false) {
			throw new IllegalArgumentException("Source '" + destDir
					+ "' is not a directory");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new IllegalArgumentException("Destination '" + destDir
					+ "' is not a directory");
		}
		if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
			throw new IOException("Source '" + srcDir + "' and destination '"
					+ destDir + "' are the same");
		}

		// 为满足当目标目录在源目录里面的情况。
		List<String> exclusionList = null;
		if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
			File[] srcFiles = srcDir.listFiles();
			if (srcFiles != null && srcFiles.length > 0) {
				exclusionList = new ArrayList<String>(srcFiles.length);
				for (File srcFile : srcFiles) {
					File copiedFile = new File(destDir, srcFile.getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}

		doCopyDirectory(srcDir, destDir, preserveFileDate, exclusionList);
	}

	/**
	 * Internal copy directory method.
	 * 
	 * @param srcDir
	 *            the validated source directory, must not be <code>null</code>
	 * @param destDir
	 *            the validated destination directory, must not be
	 *            <code>null</code>
	 * @param filter
	 *            the filter to apply, null means copy all directories and files
	 * @param preserveFileDate
	 *            whether to preserve the file date
	 * @param exclusionList
	 *            List of files and directories to exclude from the copy, may be
	 *            null
	 * @throws IOException
	 *             if an error occurs
	 * @since Commons IO 1.1
	 */
	private static void doCopyDirectory(File srcDir, File destDir,
			boolean preserveFileDate, List<String> exclusionList)
			throws IOException {
		// recurse
		File[] srcFiles = srcDir.listFiles();
		if (srcFiles == null) { // null if abstract pathname does not denote a
								// directory, or if an I/O error occurs
			throw new IOException("Failed to list contents of " + srcDir);
		}
		if (destDir.exists()) {
			if (destDir.isDirectory() == false) {
				throw new IOException("Destination '" + destDir
						+ "' exists but is not a directory");
			}
		} else {
			if (!destDir.mkdirs() && !destDir.isDirectory()) {
				throw new IOException("Destination '" + destDir
						+ "' directory cannot be created");
			}
		}
		if (destDir.canWrite() == false) {
			throw new IOException("Destination '" + destDir
					+ "' cannot be written to");
		}
		for (File srcFile : srcFiles) {
			File dstFile = new File(destDir, srcFile.getName());
			if (exclusionList == null
					|| !exclusionList.contains(srcFile.getCanonicalPath())) {
				if (srcFile.isDirectory()) {
					doCopyDirectory(srcFile, dstFile, preserveFileDate,
							exclusionList);
				} else {
					doCopyFile(srcFile, dstFile, preserveFileDate);
				}
			}
		}

		// Do this last, as the above has probably affected directory metadata
		if (preserveFileDate) {
			destDir.setLastModified(srcDir.lastModified());
		}
	}

	/**
	 * Internal copy file method.
	 * 
	 * @param srcFile
	 *            the validated source file, must not be <code>null</code>
	 * @param destFile
	 *            the validated destination file, must not be <code>null</code>
	 * @param preserveFileDate
	 *            whether to preserve the file date
	 * @throws IOException
	 *             if an error occurs
	 */
	private static void doCopyFile(File srcFile, File destFile,
			boolean preserveFileDate) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				count = (size - pos) > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE
						: (size - pos);
				pos += output.transferFrom(input, pos, count);
			}
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(fos);
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(fis);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '"
					+ srcFile + "' to '" + destFile + "'");
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}

	/**
	 * Opens a {@link FileInputStream} for the specified file, providing better
	 * error messages than simply calling <code>new FileInputStream(file)</code>
	 * .
	 * <p>
	 * At the end of the method either the stream will be successfully opened,
	 * or an exception will have been thrown.
	 * <p>
	 * An exception is thrown if the file does not exist. An exception is thrown
	 * if the file object exists but is a directory. An exception is thrown if
	 * the file exists but cannot be read.
	 * 
	 * @param file
	 *            the file to open for input, must not be <code>null</code>
	 * @return a new {@link FileInputStream} for the specified file
	 * @throws FileNotFoundException
	 *             if the file does not exist
	 * @throws IOException
	 *             if the file object is a directory
	 * @throws IOException
	 *             if the file cannot be read
	 * @since Commons IO 1.3
	 */
	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file
					+ "' does not exist");
		}
		return new FileInputStream(file);
	}

	/**
	 * Reads the contents of a file line by line to a List of Strings. The file
	 * is always closed.
	 * 
	 * @param file
	 *            the file to read, must not be <code>null</code>
	 * @param encoding
	 *            the encoding to use, <code>null</code> means platform default
	 * @return the list of Strings representing each line in the file, never
	 *         <code>null</code>
	 * @throws IOException
	 *             in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException
	 *             if the encoding is not supported by the VM
	 * @since Commons IO 1.1
	 */
	public static List<String> readLines(File file, String encoding)
			throws IOException {
		InputStream in = null;
		try {
			in = openInputStream(file);
			return readLines(in, encoding);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * Reads the contents of a file line by line to a List of Strings using the
	 * default encoding for the VM. The file is always closed.
	 * 
	 * @param file
	 *            the file to read, must not be <code>null</code>
	 * @return the list of Strings representing each line in the file, never
	 *         <code>null</code>
	 * @throws IOException
	 *             in case of an I/O error
	 * @since Commons IO 1.3
	 */
	public static List<String> readLines(File file) throws IOException {
		return readLines(file, null);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a list of Strings, one
	 * entry per line, using the default character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from, not null
	 * @return the list of Strings, never null
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static List<String> readLines(InputStream input) throws IOException {
		InputStreamReader reader = new InputStreamReader(input);
		return readLines(reader);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a list of Strings, one
	 * entry per line, using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input
	 *            the <code>InputStream</code> to read from, not null
	 * @param encoding
	 *            the encoding to use, null means platform default
	 * @return the list of Strings, never null
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static List<String> readLines(InputStream input, String encoding)
			throws IOException {
		if (encoding == null) {
			return readLines(input);
		} else {
			InputStreamReader reader = new InputStreamReader(input, encoding);
			return readLines(reader);
		}
	}

	/**
	 * Get the contents of a <code>Reader</code> as a list of Strings, one entry
	 * per line.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * 
	 * @param input
	 *            the <code>Reader</code> to read from, not null
	 * @return the list of Strings, never null
	 * @throws NullPointerException
	 *             if the input is null
	 * @throws IOException
	 *             if an I/O error occurs
	 * @since Commons IO 1.1
	 */
	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		List<String> list = new ArrayList<String>();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}

	// ///////////////////////////////////////////////////////////////////////////
	/** 获取文件夹的大小 **/
	// ///////////////////////////////////////////////////////////////////////////
	/**
	 * 取得文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			LogUtil.e("FileHelper","文件不存在");
		}
		return s;
	}

	/**
	 * 递归 取得文件夹大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		if (flist == null) {
			return size;
		}
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 转换文件大小 单位
	 * 
	 * @param fileS
	 * @return
	 */
	public String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 递归求取目录文件个数
	 * 
	 * @param f
	 * @return
	 */
	public long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

	/**
	 * 修改程序。<br>
	 * 内部递归调用，进行子目录的更名
	 * 
	 * @param path
	 *            路径
	 * @param from
	 *            原始的后缀名，包括那个(.点)
	 * @param to
	 *            改名的后缀，也包括那个(.点)
	 */
	public void BatchReNameFileSuffix(String path, String from, String to) {
		File f = new File(path);
		File[] fs = f.listFiles();
		for (int i = 0; i < fs.length; ++i) {
			File f2 = fs[i];
			if (f2.isDirectory()) {
				BatchReNameFileSuffix(f2.getPath(), from, to);
			} else {
				String name = f2.getName();
				if (name.endsWith(from)) {
					f2.renameTo(new File(f2.getParent() + "/"
							+ name.substring(0, name.indexOf(from)) + to));
				}
			}
		}
	}

	/***
	 * 
	 * @param path
	 *            路径
	 * @param from
	 *            原始的后缀名，包括那个(.点)
	 * @param appendSuffix
	 *            追加的后缀，也包括那个(.点)
	 */
	public void BatchAppendFileSuffix(String path, String from,
			String appendSuffix) {
		File f = new File(path);
		File[] fs = f.listFiles();
		for (int i = 0; i < fs.length; ++i) {
			File f2 = fs[i];
			if (f2.isDirectory()) {
				BatchReNameFileSuffix(f2.getPath(), from, appendSuffix);
			} else {
				String name = f2.getName();
				if (name.endsWith(from)) {
					f2.renameTo(new File(f2.getParent() + File.separator + name
							+ appendSuffix));
				}
			}
		}
	}

	/***
	 * 将还有gif png jpg格式的图片添加后缀名
	 * 
	 * @param path
	 * @param appendSuffix
	 */
	public void BatchAppendImageFileSuffix(String path, String appendSuffix) {
		File f = new File(path);
		File[] fs = f.listFiles();
		for (int i = 0; i < fs.length; ++i) {
			File f2 = fs[i];
			if (f2.isDirectory()) {
				BatchAppendImageFileSuffix(f2.getPath(), appendSuffix);
			} else {
				String name = f2.getName();
				Pattern p = Pattern.compile("[\\s\\S]+.(gif|jpg|png|jpeg)$",
						Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(name);
				if (m.matches()) {
					f2.renameTo(new File(f2.getParent() + File.separator + name
							+ appendSuffix));
				}
			}
		}
	}

}