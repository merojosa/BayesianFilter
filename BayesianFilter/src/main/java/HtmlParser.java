import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.lang.String;

public class HtmlParser {
    /**
     * Empty constructor of HtmlParser
     */
    public HtmlParser() {
    }

    /**
     * Parse the html to plain text
     * @param html
     * @return parsedHtml
     */
    public String parseHtmlToPlainText(String html)
    {
        String parsedHtml = "";
        Document document = Jsoup.parse(html);
        Elements elements = document.getAllElements();
        for(Element element : elements)
        {
            parsedHtml = parsedHtml.concat(element.ownText());
            parsedHtml = parsedHtml.concat(" ");
        }
        return parsedHtml;
    }



}
