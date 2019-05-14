package com.coe.oom.core.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class XmlUtil {
	private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	/**
	 * xml 转对象
	 * 
	 * @param xml
	 * @param c
	 * @return
	 */
	public static <T> Object toObject(String xml, Class<T> c) {
		if (!xml.trim().startsWith("<?xml")) {
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + xml;
		}
		ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller um = context.createUnmarshaller();
			return um.unmarshal(inputStream);
		} catch (JAXBException e) {
			logger.error("xml :" + xml + "  转对象 异常:" + e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * xml 转对象
	 * 
	 * @param xml
	 * @param c
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static <T> Object toObject2(String xml, Class<T> c) throws UnsupportedEncodingException {
		if (xml.startsWith("\"") && xml.endsWith("\"")) {
			xml = xml.substring(1, xml.length() - 1);
		}
		if (!xml.trim().startsWith("<?xml")) {
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + xml;
		}
		ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller um = context.createUnmarshaller();
			return um.unmarshal(inputStream);
		} catch (JAXBException e) {
			logger.error("xml :" + xml + "  转对象 异常:" + e.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 对象转xml
	 * 
	 * @param c
	 * @param object
	 * @return
	 */
	public static <T> String toXml(T object) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller m = context.createMarshaller();
			m.marshal(object, output);
			return output.toString();
		} catch (JAXBException e) {
			logger.error("对象转xml 异常:" + EeceptionUtil.getMessage(e));
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 对象转xml
	 * 
	 * @param c
	 * @param object
	 * @return
	 */
	public static <T> String toXml2(T object) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller m = context.createMarshaller();
			m.marshal(object, output);
			try {
				return output.toString("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (JAXBException e) {
			logger.error("对象转xml 异常:" + e.getMessage());
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
