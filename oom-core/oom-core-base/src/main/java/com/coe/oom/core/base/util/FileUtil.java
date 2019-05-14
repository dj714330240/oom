package com.coe.oom.core.base.util;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * FileUtil. Simple file operation class.
 * 
 * @author BeanSoft
 */
public class FileUtil {
	/**
	 * The buffer.
	 */
	protected static byte buf[] = new byte[1024];

	/**
	 * 创建路径
	 * 
	 * @param path
	 * @return
	 */
	public static boolean mkdirs(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean delete(String path) {
		try {
			File f = new File(path);
			f.deleteOnExit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Read content from local file. FIXME How to judge UTF-8 and GBK, the
	 * correct code should be: FileReader fr = new FileReader(new
	 * InputStreamReader(fileName, "ENCODING")); Might let the user select the
	 * encoding would be a better idea. While reading UTF-8 files, the content
	 * is bad when saved out.
	 * 
	 * @param fileName
	 *            - local file name to read
	 * @return
	 * @throws Exception
	 */
	public static String readFileAsString(String fileName) throws Exception {
		String content = new String(readFileBinary(fileName));

		return content;
	}

	/**
	 * 读取文件并返回为给定字符集的字符串.
	 * 
	 * @param fileName
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String readFileAsString(String fileName, String encoding) throws Exception {
		String content = new String(readFileBinary(fileName), encoding);

		return content;
	}

	/**
	 * 读取文件并返回为给定字符集的字符串.
	 * 
	 * @param fileName
	 * @param encoding
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String readFileAsString(InputStream in) throws IOException {
		String content = new String(readFileBinary(in));
		return content;
	}

	/**
	 * Read content from local file to binary byte array.
	 * 
	 * @param fileName
	 *            - local file name to read
	 * @return
	 * @throws Exception
	 */
	public static byte[] readFileBinary(String fileName) throws Exception {
		FileInputStream fin = new FileInputStream(fileName);

		return readFileBinary(fin);
	}

	/**
	 * 从输入流读取数据为二进制字节数组.
	 * 
	 * @param streamIn
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileBinary(InputStream streamIn) throws IOException {
		BufferedInputStream in = new BufferedInputStream(streamIn);

		ByteArrayOutputStream out = new ByteArrayOutputStream(10240);

		int len;
		while ((len = in.read(buf)) >= 0)
			out.write(buf, 0, len);
		in.close();

		return out.toByteArray();
	}

	/**
	 * Write string content to local file.
	 * 
	 * @param fileName
	 *            - local file name will write to
	 * @param content
	 *            String text
	 * @return true if success
	 * @throws IOException
	 */
	public static boolean writeFileString(String fileName, String content) throws IOException {
		FileWriter fout = new FileWriter(fileName);
		fout.write(content);
		fout.close();
		return true;
	}

	/**
	 * Write string content to local file using given character encoding.
	 * 
	 * @param fileName
	 *            - local file name will write to
	 * @param content
	 *            String text
	 * @param encoding
	 *            the encoding
	 * @return true if success
	 * @throws IOException
	 */
	public static boolean writeFileString(String fileName, String content, String encoding) throws IOException {
		OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(fileName), encoding);

		fout.write(content);
		fout.close();
		return true;
	}

	/**
	 * Write binary byte array to local file.
	 * 
	 * @param fileName
	 *            - local file name will write to
	 * @param content
	 *            binary byte array
	 * @return true if success
	 * @throws IOException
	 */
	public static boolean writeFileBinary(String fileName, byte[] content) throws IOException {
		if (content == null || StringUtil.isNull(fileName)) {
			return false;
		}
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(fileName);
			fout.write(content);
		} catch (IOException e) {
			throw e;
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

	/**
	 * 检查文件名是否合法.文件名字不能包含字符\/:*?"<>|
	 * 
	 * @param fileName文件名
	 *            ,不包含路径
	 * @return boolean is valid file name
	 */
	public static boolean isValidFileName(String fileName) {
		boolean isValid = true;
		String errChar = "\\/:*?\"<>|"; //
		if (fileName == null || fileName.length() == 0) {
			isValid = false;
		} else {
			for (int i = 0; i < errChar.length(); i++) {
				if (fileName.indexOf(errChar.charAt(i)) != -1) {
					isValid = false;
					break;
				}
			}
		}
		return isValid;
	}

	/**
	 * 把非法文件名转换为合法文件名.
	 * 
	 * @param fileName
	 * @return
	 */
	public static String replaceInvalidFileChars(String fileName) {
		StringBuffer out = new StringBuffer();

		for (int i = 0; i < fileName.length(); i++) {
			char ch = fileName.charAt(i);
			// Replace invlid chars: \\/:*?\"<>|
			switch (ch) {
			case '\\':
			case '/':
			case ':':
			case '*':
			case '?':
			case '\"':
			case '<':
			case '>':
			case '|':
				out.append('_');
				break;
			default:
				out.append(ch);
			}
		}

		return out.toString();
	}

	/**
	 * Convert a given file name to a URL(URI) string.
	 * 
	 * @param fileName
	 *            - the file to parse
	 * @return - URL string
	 */
	public static String filePathToURL(String fileName) {
		String fileUrl = new File(fileName).toURI().toString();
		return fileUrl;
	}

	/**
	 * Write string content to local file.
	 * 
	 * @param fileName
	 *            - local file name will write to
	 * @param content
	 *            String text
	 * @return true if success
	 * @throws IOException
	 */
	public static boolean appendFileString(String fileName, String content) throws IOException {
		OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8");
		fout.write(content);
		fout.close();
		return true;
	}

	 

	/**
	 * 解压
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void unZip(String pathAndName, String savePathAndName) throws Exception {

		// TODO Auto-generated method stub
		try {
			ZipInputStream Zin = new ZipInputStream(new FileInputStream(pathAndName));// 输入源zip路径
			BufferedInputStream Bin = new BufferedInputStream(Zin);

			File Fout = null;
			ZipEntry entry;
			try {
				while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
					Fout = new File(savePathAndName, entry.getName());
					if (!Fout.exists()) {
						(new File(Fout.getParent())).mkdirs();
					}
					FileOutputStream out = new FileOutputStream(Fout);
					BufferedOutputStream Bout = new BufferedOutputStream(out);
					int b;
					while ((b = Bin.read()) != -1) {
						Bout.write(b);
					}
					Bout.close();
					out.close();
				}
				Bin.close();
				Zin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取某个文件夹下的所有文件
	 */
	public static List<String> readfile(List<String> pathlist, String filepath) {

		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				pathlist.add(file.getPath());

			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						pathlist.add(readfile.getPath());
					} else if (readfile.isDirectory()) {
						readfile(pathlist, filepath + "\\" + filelist[i]);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return pathlist;
	}

	/**
	 * 根据文件路径和文件名获取文件名
	 * 
	 * @param filePathAndName
	 * @return
	 */
	public static String getFileName(String filePathAndName) {
		int a = filePathAndName.lastIndexOf("\\");
		int b = filePathAndName.lastIndexOf("/");
		String fileName = filePathAndName.substring((a > b ? a : b) + 1, filePathAndName.length());
		return fileName;
	}

	public static boolean copy(String fileFrom, String fileTo) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(fileFrom);
			out = new FileOutputStream(fileTo);
			byte[] bt = new byte[1024];
			int count;
			while ((count = in.read(bt)) > 0) {
				out.write(bt, 0, count);
			}
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 递归获取全部文件
	 * 
	 * @param strPath
	 * @return
	 */
	public static List<File> getFileList(String strPath) {
		List<File> filelist = new ArrayList<File>();
		File dir = new File(strPath);
		File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
		if (files == null) {
			return filelist;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				// 判断是文件夹,递归
				filelist.addAll(getFileList(files[i].getAbsolutePath()));
			} else {
				filelist.add(files[i]);
			}
		}
		return filelist;
	}

	 

	/**
	 * Description: 将base64编码内容转换为Pdf
	 * 
	 * @param base64编码内容，文件的存储路径（含文件名）
	 * @Author fuyuwei Create Date: 2015年7月30日 上午9:40:23
	 */
	public static void base64StringToPdf(String base64Content, String filePath) {
		BASE64Decoder decoder = new BASE64Decoder();
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			byte[] bytes = decoder.decodeBuffer(base64Content);// base64编码内容转换为字节数组
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
			bis = new BufferedInputStream(byteInputStream);
			File file = new File(filePath);
			File path = file.getParentFile();
			if (!path.exists()) {
				path.mkdirs();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);

			byte[] buffer = new byte[1024];
			int length = bis.read(buffer);
			while (length != -1) {
				bos.write(buffer, 0, length);
				length = bis.read(buffer);
			}
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e2) {
			}

		}
	}
}