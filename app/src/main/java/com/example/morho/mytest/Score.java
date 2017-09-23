package com.example.morho.mytest;

import android.app.Activity;
import android.content.res.AssetManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Morho on 7/29/2017.
 */

public class Score {
    public List<HashMap<String, String>> getdata(Activity ac) throws IOException {
        Document scoredoc = Jsoup.parse(ac.getAssets().open("score.html"), "GB2312", "");

        List<HashMap<String, String>> list = new ArrayList<>();


        Element info_table = scoredoc.getElementById("tbXsxx");
        Elements tr = info_table.getElementsByTag("tr");

//        ScoreEntity se = new ScoreEntity();
//        se.setScore_date(tr.get(0).child(0).text());
//        se.setId(tr.get(1).child(0).text().substring(3));
//        se.setName(tr.get(1).child(1).text().substring(3));
//        se.setAcademy(tr.get(1).child(2).text().substring(3));
//        se.setClass_name(tr.get(2).child(1).text().substring(4));
//        System.out.println(se.toString());

        Element score_table = scoredoc.getElementById("DataGrid1");
        Elements score = score_table.getElementsByTag("tr");
        score.remove(0);
//        score_info sc = new score_info();

        for (Element t : score) {
            HashMap<String, String> data = new HashMap<>();
            data.put("sc_name", t.child(3).text());
            data.put("sc_sc", t.child(11).text());
//            sc.setId(t.child(2).text());
//            sc.setName(t.child(3).text());
//            sc.setCredit(t.child(6).text());
//            sc.setAtt_home(t.child(7).text());
//            sc.setMid_score(t.child(8).text());
//            sc.setFinal_score(t.child(9).text());
//            sc.setLab_score(t.child(10).text());
//            sc.setReal_score(t.child(11).text());
//            System.out.println(sc.toString());
            list.add(data);
        }
        return list;
    }
}
