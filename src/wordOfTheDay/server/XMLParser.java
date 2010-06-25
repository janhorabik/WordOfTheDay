package wordOfTheDay.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wordOfTheDay.server.service.GetTodaysWordServiceImpl;

public class XMLParser {
	private static final Logger log = Logger
			.getLogger(GetTodaysWordServiceImpl.class.getName());

	public static String initiateWords(ByteArrayInputStream stream, String email) {
		try {
			log.info("initiate words started");
			Document document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(stream);
			NodeList words = document.getElementsByTagName("word");
			for (int i = 0; i < words.getLength(); i++) {
				Node node = words.item(i);
				NodeList children = node.getChildNodes();
				String name = new String();
				String explanation = new String();
				List<String> usage = new LinkedList<String>();
				for (int j = 0; j < children.getLength(); j++) {
					Node child = children.item(j);
					log.info(child.toString());
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						if (child.getNodeName().equals("name"))
							name = child.getTextContent().trim();
						if (child.getNodeName().equals("explanationEnglish"))
							explanation = child.getTextContent().trim();
						if (child.getNodeName().equals("example"))
							usage.add(child.getTextContent().trim());
					}
				}
				try {
					log.info("saving: " + name + explanation + usage.size());
					if (name != null && explanation != null
							&& (!name.equals("")) && (!explanation.equals(""))) {
						log.info("making persistent");
						PersistenceManager pm = PMF.get()
								.getPersistenceManager();
						pm
								.makePersistent(new PersistentWord14(name,
										explanation, usage,
										PMF.getYoungestAvailableDate(email),
										email));
					}
				} finally {
				}
			}
			return "Words have been initiated";
		} catch (Exception e) {
			return "An error occured while parsing the file: " + e.toString();
		}
	}

	public static String export(String email) {
		List<PersistentWord14> words = PMF.getAllWords(email);
		String ret = new String();
		ret += "<?xml version='1.0' encoding='UTF-8'?><words>";
		for (PersistentWord14 word : words) {
			ret += "<word><name>" + word.getName()
					+ "</name><explanationEnglish>" + word.getExplanation()
					+ "</explanationEnglish>";
			for (String usage : word.getUsage()) {
				ret += "<example>" + usage + "</example>";
			}
			ret += "</word>";
		}
		ret += "</words>";
		return ret;
	}
}
