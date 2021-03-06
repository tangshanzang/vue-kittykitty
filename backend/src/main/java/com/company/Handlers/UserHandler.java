package com.company.Handlers;

import com.company.DTOs.UserWithoutPassword;
import com.company.Repositories.userRepository;
import com.company.utilities.Validator;
import express.Express;

public class UserHandler {

    private final Express app;
    private final userRepository userRepository;
    private final Validator loginValidator;

    public UserHandler(Express app, userRepository userRepository) {
        this.app = app;
        this.userRepository = userRepository;
        this.loginValidator = new Validator();
        initUserHandler();
    }


    private void initUserHandler() {

        // login user
        app.post("/api/login", (req, res) -> {
            Object user = loginValidator.loginValidation((String) req.body().get("providedUserName"), (String) req.body().get("providedPassword"), userRepository);

            // set server session storage if login successful
            if (user != null) {
                req.session("current-user", user);
            }

            res.json(user);
        });


        app.post("/api/updateUser", (req, res) -> {
           userRepository.updateUserInfo(Integer.parseInt(req.body().get("userId").toString()), req.body().get("username").toString(), req.body().get("password").toString(), req.body().get("description").toString(),  req.body().get("profileURL").toString());
        });


        // register user
        app.post("/api/register", (req, res) -> {
            UserWithoutPassword user = userRepository.registerNewUser((String) req.body().get("providedUserName"), (String) req.body().get("providedPassword"), (String) req.body().get("providedDescription"), (String) req.body().get("providedProfileURL"), 0, 0);

            // set server session storage if login successful
            if (user != null) {
                req.session("current-user", user);
            }

            res.json(user);
        });

        // who am i? get logged in user
        app.get("/api/whoami", (req, res) -> {
            // return user saved in session
            res.json(req.session("current-user"));
        });

        app.get("/api/logout", (req, res) -> {
            // remove user from session
            req.session("current-user", null);
        });

        app.get("/rest/getUserByUsername", (req, res) -> {
            res.json(userRepository.getUserByUsername(req.query("providedUsername")));
        });

        app.get("/rest/getMatchedUserList", (req, res) -> {
            res.json(userRepository.getMatchedUserList(req.query("keyword")));
        });
    }
}

