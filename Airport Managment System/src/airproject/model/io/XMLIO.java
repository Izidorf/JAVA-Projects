package airproject.model.io;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLIO {

	private static final String XML_PATH = System.getenv("TEMP") + "\\airproject\\xml\\";
	private static boolean dirExists = false;

	public static <T> void writeXml(Class<T> className, T obj) {
		writeXml(className, obj, new File(getDefaultFileName(className.getName())));
	}

	public static <T> void writeXml(Class<T> className, T obj, File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(className);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(obj, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static <T> T readXml(Class<T> className) {
		return readXml(className, new File(getDefaultFileName(className.getName())));
	}

	@SuppressWarnings("unchecked")
	public static <T> T readXml(Class<T> className, File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(className);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDefaultFileName(String className) {
		if (!dirExists) {
			new File(XML_PATH).mkdirs();
			dirExists = true;
		}
		return XML_PATH + className.substring(className.lastIndexOf(".") + 1) + ".xml";
	}

}
