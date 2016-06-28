/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package test.demo;

/**
 * Created by wangguoxing on 15-10-28.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class NewsManager {

    /**
     * @param args
     */
    public static void main(String[] args) {

        List newss = getNewsList();

        for (int i = 0; i < newss.size(); i++) {
            News news = (News) newss.get(i);

            System.out.println("hits:" + news.getHits());

        }

    }

    public static List getNewsList() {

        List<News> list = new ArrayList();

        News news1 = new News();
        news1.setHits(1);
        list.add(news1);

        News news2 = new News();
        news2.setHits(7);
        list.add(news2);

        News news3 = new News();
        news3.setHits(3);
        list.add(news3);

        News news4 = new News();
        news4.setHits(5);
        list.add(news4);

        // 按点击数倒序
        Collections.sort(list, new Comparator<News>() {
            public int compare(News arg0, News arg1) {
                int hits0 = arg0.getHits();
                int hits1 = arg1.getHits();
                if (hits1 > hits0) {
                    return 1;
                } else if (hits1 == hits0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return list;
    }

}

class News {
    private int hits;

    public void setHits(int i) {
        this.hits = i;
    }

    public int getHits() {
        return hits;
    }
}