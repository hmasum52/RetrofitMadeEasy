package com.hmasum18.retrofitmadeeasy.jsonplaceholder;

public class Topic {
   /* {
        "userId": 1,
            "id": 1,
            "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
    }*/
    int userId;
    int id;
    String title;
    String body;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Topic{" +  '\n' +
                "userId:: " + userId +  '\n' +
                "id:: " + id +  '\n' +
                "title:: " + title + '\n' +
                "body:: " + body + '\n' +
                '}';
    }
}
