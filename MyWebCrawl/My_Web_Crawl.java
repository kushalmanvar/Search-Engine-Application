package MyWebCrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class My_Web_Crawl {
	//creating a list to store urls
   public static List<URL> crawl(URL start, int limit) {
       List<URL> listofurls = new ArrayList<URL>(limit);
       listofurls.add(start);

       //We make a set of url named urlscopy which is same as the urls for faster execution
      
       Set<URL> urls_copy = new HashSet<URL>(listofurls);
       int i = 0;
       while (listofurls.size() < limit && i < listofurls.size()) {
           URL currentUrl = listofurls.get(i);
           for (URL url : extractLinks(currentUrl)) {
               if (urls_copy.add(url)) {
                   listofurls.add(url);
                   if (listofurls.size() == limit) {
                       break;
                   }
               }
           }
           i++;
       }
       return listofurls;
   }

//Shows how crawler works and dsiplay crawled links.
   public static void main(String[] args) {
       try {
           URL i = new URL("http://www.uwindsor.ca");
           int limit = 150;

           long start = System.currentTimeMillis();
           List<URL> discovered = My_Web_Crawl.crawl(i, limit);
           long finish = System.currentTimeMillis();
           System.out.println("Showing results: ");
           int i1 = 1;
           Iterator<URL> iterator = discovered.iterator();
           while (iterator.hasNext() && i1 <= 150) {
               System.out.println(iterator.next());
               i1++;
           }
       }
       catch (MalformedURLException e) {
           System.err.println("The URL to start crawling with is invalid.");
       }
   }
    
   	//Extract all links contained in web page
    //return set of links in order they occur
    
   private static LinkedHashSet<URL> extractLinks(URL url) {
       LinkedHashSet<URL> links = new LinkedHashSet<URL>();
       Pattern p = Pattern.compile("href=\"((http://|https://|www).*?)\"", Pattern.DOTALL);
       Matcher m = p.matcher(fetchContent(url));

       while (m.find()) {
           String linkStr = normalizeUrlStr(m.group(1));
           try {
               URL link = new URL(linkStr);
               links.add(link);
           }
           catch (MalformedURLException e) {
               System.err.println("Page at " + url + " has a link to invalid URL : " + linkStr + ".");
           }
       }
       return links;
   }

   //used to fetch the content from url and return fetched content
   private static String fetchContent(URL url) {
       StringBuilder stringBuilder = new StringBuilder();
       try {
           BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
           String inputLine;
           while ((inputLine = in.readLine()) != null) {
               stringBuilder.append(inputLine);
           }
           in.close();
       }
       catch (IOException e) {
           System.err.println("An error occured from " + url);
       }
       return stringBuilder.toString();
   }

  //It normalizes the string representation of url so its easier to transform to url object.
   private static String normalizeUrlStr(String urlStr) {
       if (!urlStr.startsWith("http")) {
           urlStr = "http://" + urlStr;
       }
       if (urlStr.endsWith("/")) {
           urlStr = urlStr.substring(0, urlStr.length() - 1);
       }
       if (urlStr.contains("#")) {
           urlStr = urlStr.substring(0, urlStr.indexOf("#"));
       }
       return urlStr;
   }

}
