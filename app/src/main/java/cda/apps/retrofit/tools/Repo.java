package cda.apps.retrofit.tools;

import com.google.gson.annotations.SerializedName;

public class Repo {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    //@SerializedName("full_name")
    public String full_name;

    //@SerializedName("html_url")
    public String html_url;


//    public Repo(int id, String name) {
//        this.id = id;
//        this.name = name;
//    }


    @Override
    public String toString() {
        return "[" + id + "] " + name +  " | " + full_name + " | " + html_url;
    }
}
