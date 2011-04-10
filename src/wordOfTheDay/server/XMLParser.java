package wordOfTheDay.server;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import wordOfTheDay.server.service.NoteServiceImpl;

public class XMLParser {
	private static final Logger log = Logger
			.getLogger(NoteServiceImpl.class.getName());

	public static String initiateWords(ByteArrayInputStream stream, String email) {
		try {
			log.info("initiate words started");
			LinkedList<PersistentWord25> wordsToSave = new LinkedList<PersistentWord25>();
			int youngestDate = PMF.getYoungestAvailableDate(email);
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
							name = itrim(child.getTextContent().trim());
						if (child.getNodeName().equals("explanationEnglish"))
							explanation = itrim(child.getTextContent().trim());
						if (child.getNodeName().equals("example"))
							usage.add(itrim(child.getTextContent().trim()));
					}
				}
				try {
					log.info("saving: " + name + explanation + usage.size());
					if (name != null && explanation != null
							&& (!name.equals("")) && (!explanation.equals(""))) {
						wordsToSave.add(new PersistentWord25(name, explanation,
								usage, youngestDate, email,
								new LinkedList<String>()));
						youngestDate = Date.getNextDay(youngestDate);
					}
				} finally {
				}
			}
			PersistenceManager pm = PMF.get().getPersistenceManager();
			log.info("saving " + wordsToSave.size() + " words");
			pm.makePersistentAll(wordsToSave);
			return "Words have been initiated";
		} catch (Exception e) {
			return "An error occured while parsing the file: " + e.toString();
		}
	}

	public static String export(String email) {
		List<PersistentWord25> words = PMF.getAllWords(email);
		String ret = new String();
		ret += "<?xml version='1.0' encoding='UTF-8'?><words>";
		for (PersistentWord25 word : words) {
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

	public static String itrim(String source) {
		return source.replaceAll("\\b\\s{2,}\\b", " ");
	}

}
