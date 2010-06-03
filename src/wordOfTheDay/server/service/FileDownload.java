package wordOfTheDay.server.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wordOfTheDay.server.XMLParser;

public class FileDownload extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String xml = XMLParser.export((String) req.getSession().getAttribute("email"));
		ServletOutputStream op = res.getOutputStream();
		res.setContentType("text/xml");
		res.setContentLength(xml.length());
		res
				.setHeader("Content-Disposition",
						"attachment; filename='words.xml'");
		op.print(xml);
		op.flush();
		op.close();
	}
}