package com.example.mylap.utils;

import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.webkit.WebSettings;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Format {
    public static String formatText(String text, boolean haveImage) {
        Document document = Jsoup.parse(text);
        if(haveImage) {
            Elements imgElements = document.select("img");
            for (Element imgElement : imgElements) {
                String imgSrc = imgElement.attr("src");
                String imgAlt = imgElement.attr("alt");

                imgElement.after("<br><img src='" + imgSrc + "' alt='" + imgAlt + "'/><br>");
                imgElement.remove();
            }

            return document.toString();
        } else {
            String plainText = document.text();
            return plainText;
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            CharSequence formattedText = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
//            return formattedText.toString();
//        } else {
//            CharSequence formattedText = Html.fromHtml(text);
//            return formattedText.toString();
//        }
    }

    public static int[] convertTimeMiliseconds(long miliSeconds) {
        int[] result = new int[3];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliSeconds);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0, nên cộng thêm 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        result[0] = day;
        result[1] = month;
        result[2] = year;

        return result;
    }
}
