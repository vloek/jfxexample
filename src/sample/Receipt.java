package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;


public class Receipt {
    public String imgUrl;
    public String url;
    public String content;
    public String title;


    public Receipt(Element element) {
        this.imgUrl  = element.select(".b-carousel__block").first()
                .select("img").first().attr("src");

        this.url     = element.select("h3.b-recipe-widget__name").first()
                .select("a").first().attr("href");

        this.title   = element.select("h3.b-recipe-widget__name a").text();
        this.content = element.select("div.b-widget-ingredients-list").html();
    }


    public String toString() {
        return "<h1>" + this.title + "</h1>"
                + "<img align='center' style='width: 70%; " +
                    "padding: 20px; border: 1px solid #eee; " +
                    "margin:0 auto; display: block;' src="
                + this.imgUrl + "><br><br>"
                + this.content + "<br><hr></br>";
    }

    public Receipt(String loc) throws IOException {
        Document doc = Jsoup.connect(loc).get();
        this.title = doc.select("h1.s-recipe-name").html();
        this.content = doc.select("ol.instructions").html();
        this.imgUrl  = doc.select("#s-photoListContainer img").first().attr("src");
    }
}
