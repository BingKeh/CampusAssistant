package com.example.morho.mytest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Morho on 7/27/2017.
 */

public class NetTool{

    public static String stu_name;
    public static String stu_id;
    public static String host = "http://jwjx.njit.edu.cn";
    public static String VIEWSTATE;

    public static final OkHttpClient httpclient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore =
                        new HashMap<String, List<Cookie>>();
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                    for(Cookie cookie:cookies) {
                        System.out.println("cookie name: " + cookie.name());
                        System.out.println("cookie domain: " + cookie.domain());
                        System.out.println("cookie path: " + cookie.path());
                    }
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    if (cookies == null) {
                        System.out.println("No Cookie!");
                    } else {
                        ;
                    }
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            }).build();

    public NetTool() {
        super();
        System.out.println("NetTool started!");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("NetTool stopped!");
        super.finalize();
    }

    public HashMap<String, String> dologinjwc(String url) {
                return null;
    }

    public Bitmap getCode(String url) throws IOException {
        Request request = new Request.Builder()
                .url("http://jwjx.njit.edu.cn/Default2.aspx")
                .build();
        Response response = httpclient.newCall(request).execute();


        request = new Request.Builder()
                .url(url)
                .build();
        response = httpclient.newCall(request).execute();
        byte[] imgbuf = response.body().bytes();
        if (imgbuf != null) {
            return BitmapFactory.decodeByteArray(imgbuf, 0, imgbuf.length);
        }
        return null;
    }

    public String[] do_login_jwc(String... params) throws IOException {

        String url = params[0];
        String usr = params[1];
        String psd = params[2];
        String code = params[3];
        RequestBody formBody = new FormBody.Builder()
                .add("__VIEWSTATE", "dDwtNTE2MjI4MTQ7Oz7j2BjEQ4cDEffr+K8yeXHBPnpEJg==")
                .add("txtUserName", usr)
                .add("TextBox1", "")
                .add("TextBox2", psd)
                .add("txtSecretCode", code)
                .add("RadioButtonList1", URLEncoder.encode("学生", "gbk"))
                .add("Button1", "")
                .add("lbLanguage", "")
                .add("hidPdrs", "")
                .add("hidsc", "")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = httpclient.newCall(request).execute();
        String source = response.body().string();
        System.out.println(source);
        Document doc = Jsoup.parse(source);
        Element name = doc.getElementById("xhxm");

        if(name != null) {
            System.out.println(VIEWSTATE);
            stu_name = name.text().substring(0, name.text().lastIndexOf("同"));
            stu_id = usr;
            String[] Ret = new String[2];
            Ret[0] = "SUCCEED";
            Ret[1] = stu_name;
            return Ret;
        } else {
            String[] Ret = new String[2];
            Ret[0] = "FAILED";
            Ret[1] = "Code Failed";
            return Ret;
        }
    }

    public List<HashMap<String, String>> get_sc_info(String... params) throws IOException {
        String url_name = URLEncoder.encode(stu_name, "GBK");
        String url = host + "/xscjcx_dq.aspx?xh=" + stu_id + "&xm=" + url_name + "&gnmkdm=N121605";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Origin", host)
                .addHeader("Referer", url)
                .build();
        Response response = httpclient.newCall(request).execute();
        String sc_source = response.body().string();
        Document doc = Jsoup.parse(sc_source);
        Element form = doc.getElementById("Form1");
        VIEWSTATE =  form.select("input[name=__VIEWSTATE]").val();
        System.out.println(VIEWSTATE);
        RequestBody formBody = new FormBody.Builder()
                .add("__EVENTTARGET", "ddlxq")
                .add("__EVENTARGUMENT", "")
                .add("__VIEWSTATE", VIEWSTATE)
                .add("ddlxn", URLEncoder.encode("全部", "GBK"))
                .add("ddlxq", URLEncoder.encode("全部", "GBK"))
                .add("btnCx", URLEncoder.encode("查询", "GBK"))
                .build();
        request = new Request.Builder()
                .url(url)
                .addHeader("Origin", host)
                .addHeader("Referer", url)
                .post(formBody)
                .build();
        response = httpclient.newCall(request).execute();
        return get_sc_list(response.body().string());
    }

    public List<Course> get_cs_info(String... params) throws IOException {
        String year = params[0];
        String term = null;
        try {
            term = params[1];
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        String xnd = "";
        switch (year) {
            case "1": {
                xnd = "2014-2015";
                break;
            }
            case "2": {
                xnd = "2015-2016";
                break;
            }
            case "3": {
                xnd = "2016-2017";
                break;
            }
            case "4": {
                xnd = "2017-2018";
                break;
            }
            default: {
                xnd = "2017-2018";
                term = "1";
            }
        }
        System.out.println("The Year is " + xnd + " the term is " + term);
        String url_name = URLEncoder.encode(stu_name, "GBK");
        String url = host + "/xskbcx.aspx?xh=" + stu_id + "&xm=" + url_name + "&gnmkdm=N121603";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Origin", host)
                .addHeader("Referer", url)
                .build();
        Response response = httpclient.newCall(request).execute();
        String body = response.body().string();
        Document doc  = Jsoup.parse(body);
        Element form = doc.getElementById("xskb_form");
        Element name = doc.getElementById("Table1");
        String VIEWSTATE = form.select("input[name=__VIEWSTATE]").val();
        System.out.println("The VIEWSTATE is " + VIEWSTATE);
        System.out.println(name.toString());

        // get course at year and term
//        FormBody formBody = new FormBody.Builder()
//                .add("__EVENTTARGET", "xqd")
//                .add("__EVENTARGUMENT", "")
//                .add("__VIEWSTATE", VIEWSTATE)
//                .add("xnd", xnd)
//                .add("xqd", term)
//                .build();
//
//        request = new Request.Builder()
//                .url(url)
//                .addHeader("Origin", host)
//                .addHeader("Referer", url)
//                .post(formBody)
//                .build();
//        response = httpclient.newCall(request).execute();
        return get_cs_list(body);
    }

    private List<Course> get_cs_list(String source) {
        Document doc = Jsoup.parse(source);
        Element name = doc.getElementById("Table1");
        Elements kcb = name.getElementsByTag("tr");
        for (Element t : kcb) {
            for (Element t_child : t.children()) {
                if ("早晨上午下午晚上".contains(t_child.text())) {
                    System.out.println(t_child.text());
                    t_child.remove();
                }
            }
        }

        List<Course> list = new ArrayList<Course>();
        System.out.println("************************");
        List<String[]> course_list = new ArrayList<String[]>();
        for (Element t : kcb) {
            for (Element t_child : t.children()) {
                if (t_child.text().contains("周")) {
                    String[] array = t_child.text().split(" ");
                    List<String> str_list = new ArrayList<String>(Arrays.asList(array));
                    int rows = array.length / 5;
                    for (int i = 0; i < rows; i++) {
                        String[] array_item = new String[5];
                        str_list.subList(i * 5 + 0, i * 5 + 5).toArray(array_item);
                        String cname = array_item[0];
                        String ctype = array_item[1];
                        String ctime = array_item[2];
                        String cteacher = array_item[3];
                        String clocation = array_item[4];
                        course_list.add(array_item);
                        Course c = new Course(cname, ctype, ctime, cteacher, clocation);
                        list.add(c);
                    }
                }
            }
        }

        return list;
    }

    private List<HashMap<String, String>> get_sc_list(String source) {
        Document doc = Jsoup.parse(source);
        List<HashMap<String, String>> list = new ArrayList<>();
        Element score_table = doc.getElementById("DataGrid1");

        if (score_table == null) {
            return null;
        }
        Element info_table = doc.getElementById("tbXsxx");
        Elements tr = info_table.getElementsByTag("tr");
        HashMap<String, String> data_info = new HashMap<>();
        // data.put("sc_data", tr.get(0).child(0).text());
        data_info.put("STU_ID", tr.get(1).child(0).text().substring(3));
        data_info.put("STU_NAME", tr.get(1).child(1).text().substring(3));
        data_info.put("STU_ACADEMY", tr.get(1).child(2).text().substring(3));
        data_info.put("STU_CLASS", tr.get(2).child(1).text().substring(4));
        System.out.println(data_info);
        list.add(data_info);

        Elements score = score_table.getElementsByTag("tr");

        score.remove(0);
        for (Element t : score) {
            HashMap<String, String> data = new HashMap<>();
            data.put("years", t.child(0).text());
            data.put("term", t.child(1).text());
            data.put("cid", t.child(2).text());
            data.put("cname", t.child(3).text());
            data.put("credits", t.child(6).text());
            data.put("normal_score", t.child(7).text());
            data.put("end_score", t.child(9).text());
            data.put("score", t.child(11).text());
            System.out.println("The Credits is " + t.child(6).text());
            list.add(data);
        }
        return list;
    }

    public boolean do_login(String... params) throws IOException {
        String usr = params[0];
        String pwd_md5 = params[1];
        String action = "DO_LOGIN";

        FormBody form  = new FormBody.Builder()
                .add("action", action)
                .add("usr", usr)
                .add("psd", pwd_md5)
                .build();
        Request request = new Request.Builder()
                .url("http://115.159.216.38/app/do_action")
                .post(form)
                .build();
        Response response = httpclient.newCall(request).execute();
        String json = response.body().string();
        System.out.println(json);
        System.out.println("The usr is " + usr + " pwd_md5 is " + pwd_md5);
        HashMap<String, String> data = new HashMap<>();
        data = new Gson().fromJson(json, data.getClass());
        String ret = data.get("RESULT");
        switch (ret) {
            case "SUCCEED":
                return true;
            case "FAILED":
                return false;
            case "":
                return false;

        }
        return false;
    }

    public boolean do_valid(String... params) throws IOException {
        String usr = params[0];
        FormBody formBody = new FormBody.Builder()
                .add("action", "DO_VALID")
                .add("usr", usr)
                .build();
        Request request = new Request.Builder()
                .url("http://115.159.216.38/app/do_action")
                .post(formBody)
                .build();
        Response response = httpclient.newCall(request).execute();
        String json = response.body().string();
        System.out.println(json);
        HashMap<String, String> data = new HashMap<>();
        data = new Gson().fromJson(json, data.getClass());
        switch (data.get("RESULT")) {
            case "SUCCEED":
                return true;
            case "FAILED":
                return false;
        }
        return false;
    }

    public boolean do_register(String... params) throws IOException {
        String usr = params[0];
        String pwd = params[1];
        FormBody formBody = new FormBody.Builder()
                .add("action", "DO_REGISTER")
                .add("usr", usr)
                .add("psd", pwd)
                .build();
        Request request = new Request.Builder()
                .url("http://115.159.216.38/app/do_action")
                .post(formBody)
                .build();
        Response response = httpclient.newCall(request).execute();
        String json = response.body().string();
        System.out.println(json);
        HashMap<String, String> data = new HashMap<>();
        data = new Gson().fromJson(json, data.getClass());
        switch (data.get("RESULT")) {
            case "SUCCEED":
                return true;
            case "FAILED":
                return false;
        }
        return false;
    }

    public boolean do_upload_info(HashMap<String, String> data) throws IOException {
        String usr_id = data.get("USR_ID");
        String stu_id = data.get("STU_ID");
        String stu_name = data.get("STU_NAME");
        String json = new Gson().toJson(data);
        String json_url = URLEncoder.encode(json, "UTF-8");
        System.out.println(json_url);
        FormBody formBody = new FormBody.Builder()
                .add("action", "DO_UPLOAD_INFO")
                .add("json", json_url)
                .build();
        Request request = new Request.Builder()
                .url("http://115.159.216.38/app/do_action")
                .post(formBody)
                .build();
        Response response = httpclient.newCall(request).execute();
        String source = response.body().string();
        System.out.println(source);
        HashMap<String, String> ret = new HashMap<>();
        ret = new Gson().fromJson(source, ret.getClass());
        String result = ret.get("RESULT");
        if (result.equals("SUCCEED")) {
            return true;
        }
        return false;
    }

    public boolean do_upload_sug(String... params) throws IOException {
        String usr = params[0];
        String suggestion = URLEncoder.encode(params[1], "UTF-8");
        System.out.println("The name is " + usr);
        FormBody formBody = new FormBody.Builder()
                .add("action", "DO_UPLOAD_SUGGEST")
                .add("usr", usr)
                .add("suggest", suggestion)
                .build();
        Request request = new Request.Builder()
                .url("http://115.159.216.38/app/do_action")
                .post(formBody)
                .build();
        Response response = httpclient.newCall(request).execute();
        String source = response.body().string();
        System.out.println(source);
        HashMap<String, String> ret = new HashMap<>();
        ret = new Gson().fromJson(source, ret.getClass());
        String result = ret.get("RESULT");
        if (result.equals("SUCCEED")) {
            return true;
        }
        return false;

    }




}
