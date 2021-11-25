package Wiki;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Worm
{
	public static void main(String[] args)
	{
//		Wiki("xml");
//		Wiki("HTTP");
//		Wiki("L4D");
//		Wiki("hk");
//		Wiki("calc");
	Wiki("ci");
	}

	private static String Parse(String rawStr)
	{// extract rawStr
		String raw = rawStr;
		if (raw.contains("<"))
		{
			raw = raw.substring(raw.indexOf(">") + 1, raw.lastIndexOf("<"));
		}
		System.out.println(raw);
		return raw;
	}

	private static boolean CheckOk(String item, String Possi)
	{
		System.out.println(item + "||" + Possi);
		System.out.println(Step2.Heu.H1(item, Possi) || Step2.Heu.H2(item, Possi) || Step2.Heu.H3(item, Possi));

		return (Step2.Heu.H1(item, Possi) || Step2.Heu.H2(item, Possi) || Step2.Heu.H3(item, Possi));
//		return false;
	}

	public static HashSet<String> Wiki(String item)
	{
		if (item.length() == 1)
			return null;
		HashSet<String> res = new HashSet<String>();

		Document doc = null;
		try
		{
			doc = Jsoup.parse(new URL("https://en.wikipedia.org/" + item), 3000);
		} catch (IOException e)
		{
			try
			{
				doc = Jsoup.parse(new URL("https://en.wikipedia.org/w/index.php?search=" + item), 3000);
			} catch (IOException e1)
			{
				System.out.println("无法连接至此站点，请更换维基百科镜像地址重试！");
				return null;
			}
		}

		Element redir = doc.getElementById("firstHeading");
		{
			if (CheckOk(item, redir.text()))
				res.add(redir.text());
		}
		// 0. L4D
		if (redir.getElementsByTag("i").size() != 0)
		{
//			System.out.println("重定向页面的标题" + redir.getElementsByTag("i").first().text());
			if (CheckOk(item, redir.getElementsByTag("i").first().text()))
				res.add(redir.getElementsByTag("i").first().text());
		}
//		System.out.println("////////////////////////////////////");
		// cov19
		Element ele2 = doc.getElementsByClass("searchdidyoumean").first();
		if (ele2 != null)
		{
//			System.out.println(ele2.getElementsByTag("a").first().text());
			if (CheckOk(item, ele2.getElementsByTag("a").first().text()))
				res.add(ele2.getElementsByTag("a").first().text());
//			System.out.println("////////////////////////////////////");
		}
//		for (String string : res)
//		{
//			System.out.println(string);
//		}

//			if (res.size() >= 1)
//				return res;
		// 1. xml \ html
		Elements possi1 = doc.select("b");
		int num = 0;
		for (Element element : possi1)
		{
			if (num++ >= 5)
				break;
//			System.out.println(element.toString());
			String str = Parse(element.toString());
			if (!str.contains("<") && CheckOk(item, str) && !item.equals(str))
			{
//				System.out.println(str);
				res.add(str);
			}
		}
//		System.out.println("////////////////////////////////////");
		// 2. cat dog
		if (res.size() != 0)
		{
			Element content = doc.getElementById("mw-content-text");
			Elements Ps = content.getElementsByTag("p");
			num = 0;
			for (Element element : Ps)
			{
				if (num++ < 3)
					System.out.println(element.text());
			}
		}
		System.out.println("////////////////////////////////////");
//		 cn mj
		Element ele1 = doc.getElementById("mw-content-text");
		Elements elems = ele1.select("li");
		for (Element element : elems)
		{
			if (element.getElementsByTag("a").size() != 0)
				System.out.println(element.getElementsByTag("a").first().text());
		}
		System.out.println("////////////////////////////////////");

		return res;

	}



}
