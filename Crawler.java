import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    public static void main(String[] args){
        String url = "https://ca.eyebuydirect.com/eyeglasses/frames/pacific-black-m-17061";
        ArrayList matches = new ArrayList();
        ArrayList visited = new ArrayList();
        crawl(1, url, visited, matches);
        System.out.println(matches.get(2));
        for(int i = 0;i<matches.size();i++){
            if(matches.get(i).equals(1)){
                System.out.println(visited.get(i));
            }
        }
    }

    private static void crawl(int level, String url, ArrayList<String> visited, ArrayList<Integer> matches) {
        if(level <= 5) {
            Document doc = request(url, visited, matches);

            if(doc != null){
                for(Element link : doc.select("a[href]")){
                    String next_link = link.absUrl("href");
                    if (visited.contains(next_link) == false && !next_link.contains("tel")&& next_link.contains("eyebuydirect")){
                        crawl(level++, next_link, visited, matches);
                    }else if(!next_link.contains("eyebuydirect")){
                        level = 6;
                    }
                }
            }
        }
    }

    private static Document request(String url, ArrayList<String> v, ArrayList<Integer> m) {
        try{
            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            int matches = 0;

            if(con.response().statusCode() == 200){
                System.out.println("Link: " + url);
                System.out.println(doc.title());
                v.add(url);

                Elements erre = doc.getElementsContainingText("customers");
                if(erre.size() != 0){
                    System.out.println("pog");
                    matches+=1;
                }

                m.add(matches);
                for(int i = 0;i<m.size();i++){
                    System.out.println(m.get(i));
                }

                return doc;
            }
            return null;
        }
        catch(IOException e){
            return null;
        }
    }
}
