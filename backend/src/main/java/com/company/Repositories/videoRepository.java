package com.company.Repositories;

import com.company.DTOs.UserWithoutPassword;
import com.company.DTOs.VideoWithUsername;
import com.company.Entities.User;
import com.company.Entities.Video;
import com.company.Services.DTOConverter;
import com.company.utilities.Encrypter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class videoRepository {
    Connection con;

    public ArrayList<VideoWithUsername> getVideosByUsername(String username) {
        ArrayList<VideoWithUsername> allVideosByUser = new ArrayList<VideoWithUsername>();

        try {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty","root","root");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            PreparedStatement pStatement = con.prepareStatement("SELECT users.userId, users.username,  videos.videoId, videos.link, videos.title, videos.description FROM users, videos WHERE users.username = ? AND videos.userId = users.userId");
            pStatement.setString(1, username);
            ResultSet rs = pStatement.executeQuery();

            while(rs.next()) {
                // We must manually specify at which index and which datatypes each column in the result is.
                VideoWithUsername newVideo = new VideoWithUsername(0,0,"Not found", "Not found", "Not found", "Not found");
                newVideo.setUserId(rs.getInt(1));
                newVideo.setUsername(rs.getString(2));
                newVideo.setVideoId(rs.getInt(3));
                newVideo.setLink(rs.getString(4));
                newVideo.setTitle(rs.getString(5));
                newVideo.setDescription(rs.getString(6));
                allVideosByUser.add(newVideo);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }

        return allVideosByUser;
    }

    public ArrayList<VideoWithUsername> getVideosByTitle(String title) {
        ArrayList<VideoWithUsername> allVideos = new ArrayList<VideoWithUsername>();
        ArrayList<Video> videosToConvert = new ArrayList<Video>();
        ArrayList<Integer> userIDs = new ArrayList<Integer>();

        try {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty","root","root");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String baseQuery = ("SELECT videoId, userId, link, title, description FROM videos WHERE title LIKE '%$replaceThis%'").replace("$replaceThis", title);
            PreparedStatement pStatement = con.prepareStatement(baseQuery);
            //pStatement.setString(1, title);
            ResultSet rs = pStatement.executeQuery();

            while(rs.next()) {
                // We must manually specify at which index and which datatypes each column in the result is.
                Video newVideo = new Video(0,0,"Not found", "Not found", "Not found");
                newVideo.setVideoId(rs.getInt(1));
                newVideo.setUserId(rs.getInt(2));
                if(!userIDs.contains(rs.getInt(2))){
                    userIDs.add(rs.getInt(2));
                }
                newVideo.setLink(rs.getString(3));
                newVideo.setTitle(rs.getString(4));
                newVideo.setDescription(rs.getString(5));
                videosToConvert.add(newVideo);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }

        HashMap<Integer, String> mappedUsers = new userRepository().getAllUsernamesById(userIDs);
        System.out.println(mappedUsers.toString());
        DTOConverter dtoConverter = new DTOConverter();
        for(int i = 0; i < videosToConvert.size(); i++){
            allVideos.add(dtoConverter.turnVideoIntoDto(videosToConvert.get(i), mappedUsers));
        }
        return allVideos;
    }

    public ArrayList<VideoWithUsername> getAllVideos(){
        ArrayList<VideoWithUsername> allVideos = new ArrayList<VideoWithUsername>();
        ArrayList<Video> videosToConvert = new ArrayList<Video>();
        ArrayList<Integer> userIDs = new ArrayList<Integer>();
        try {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/kittykitty","root","root");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            PreparedStatement pStatement = con.prepareStatement("SELECT * FROM videos");
            ResultSet rs = pStatement.executeQuery();

            while(rs.next()) {
                // We must manually specify at which index and which datatypes each column in the result is.
                Video newVideo = new Video(0,0,"Not found", "Not found", "Not found");
                newVideo.setVideoId(rs.getInt(1));
                newVideo.setUserId(rs.getInt(2));
                if(!userIDs.contains(rs.getInt(2))){
                    userIDs.add(rs.getInt(2));
                }
                newVideo.setLink(rs.getString(3));
                newVideo.setTitle(rs.getString(4));
                newVideo.setDescription(rs.getString(5));
                videosToConvert.add(newVideo);
            }
            con.close();
        }catch(Exception e){
            System.out.println(e);
        }

        HashMap<Integer, String> mappedUsers = new userRepository().getAllUsernamesById(userIDs);
        System.out.println(mappedUsers.toString());
        DTOConverter dtoConverter = new DTOConverter();
        for(int i = 0; i < videosToConvert.size(); i++){
            allVideos.add(dtoConverter.turnVideoIntoDto(videosToConvert.get(i), mappedUsers));
        }
        return allVideos;
    }

}
