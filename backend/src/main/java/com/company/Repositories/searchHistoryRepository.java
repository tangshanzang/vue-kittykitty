package com.company.Repositories;

import com.company.Entities.SearchHistory;
import com.company.Entities.Video;

import java.sql.*;
import java.util.ArrayList;


public class searchHistoryRepository {
    Connection con, con2;

    public void registerSearchHistory(SearchHistory newSearchHistory) throws SQLException {

        ArrayList<SearchHistory> searchHistories = findHistoryListByUserId(newSearchHistory.getUserId());

        if (searchHistories.size() >= 6) {
            removeExceededHistoryByUserId(newSearchHistory.getUserId(), searchHistories.get(4).getHistoryId());
        }

        try {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty", "root", "root");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            PreparedStatement registerSearchHistory = con.prepareStatement("INSERT INTO searchHistories (userId, keyWord, time) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            registerSearchHistory.setInt(1, newSearchHistory.getUserId());
            registerSearchHistory.setString(2, newSearchHistory.getKeyWord());
            registerSearchHistory.setString(3, newSearchHistory.getTime());
            registerSearchHistory.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        con.close();
    }

    public ArrayList<SearchHistory> findHistoryListByUserId(Integer userId) throws SQLException {
        ArrayList<SearchHistory> historyListOfUserDescendingByTime = new ArrayList<SearchHistory>();
        try {
            try {
                con2 = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty", "root", "root");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Statement searchHistoryList = con2.createStatement();
            String query = "Select * from searchhistories Where userId = " + userId + " ORDER BY historyId DESC LIMIT 6;";
            ResultSet result = searchHistoryList.executeQuery(query);

            while (result.next()) {
                SearchHistory searchHistory = new SearchHistory(result.getInt("userId"), result.getString("keyWord"), result.getString("time"), result.getInt("historyId"));
                historyListOfUserDescendingByTime.add(searchHistory);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        con2.close();
        return historyListOfUserDescendingByTime;
    }


    public void removeExceededHistoryByUserId(Integer userId, Integer historyId) throws SQLException {
        try {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty", "root", "root");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            PreparedStatement deleteSearchHistory = con.prepareStatement("Delete from searchhistories Where userId = ? AND historyId < ?", Statement.RETURN_GENERATED_KEYS);
            deleteSearchHistory.setInt(1, userId);
            deleteSearchHistory.setInt(2, historyId);
            deleteSearchHistory.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        con.close();
    }

    public void clearHistories(Integer userId) throws SQLException {
        try {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty", "root", "root");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            PreparedStatement deleteSearchHistory = con.prepareStatement("Delete from searchhistories Where userId = ?", Statement.RETURN_GENERATED_KEYS);
            deleteSearchHistory.setInt(1, userId);
            deleteSearchHistory.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        con.close();
    }

    public ArrayList<String> getTrendingSearch() throws SQLException {
        ArrayList<String> trendingHistoryList = new ArrayList<>();
        try {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty", "root", "root");
            } catch (SQLException e) {
                e.printStackTrace();
            }


            Statement trendingSearchList = con.createStatement();
            String query = "SELECT keyword, COUNT(historyId) FROM searchhistories GROUP BY keyword ORDER BY COUNT(historyId) DESC LIMIT 10;";
            ResultSet result = trendingSearchList.executeQuery(query);

            while (result.next()) {
                trendingHistoryList.add(result.getString("keyword"));
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        con.close();
        return trendingHistoryList;
    }

    public ArrayList<Video> getMatchedVideoList(String keyword) throws SQLException {
        ArrayList<Video> matchedVideoList = new ArrayList<>();
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/kittykitty", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Statement trendingSearchList = con.createStatement();
        String query = "SELECT * FROM videos WHERE LOWER(title) LIKE " + "'%" + keyword + "%'";
        ResultSet result = trendingSearchList.executeQuery(query);

        while (result.next()) {
            Video video =
                    new Video(result.getInt("videoId"),
                            result.getInt("userId"),
                            result.getTimestamp("uploadDate").getTime(),
                            result.getString("videoUrl"),
                            result.getString("title"),
                            result.getString("description"),
                            result.getInt("views"),
                            result.getString("postedByUsername"),
                            String.valueOf(result.getInt("likes")),
                            String.valueOf(result.getInt("dislikes")),
                            String.valueOf(result.getInt("stars")));

            matchedVideoList.add(video);
        }

        con.close();
        return matchedVideoList;
    }
}
