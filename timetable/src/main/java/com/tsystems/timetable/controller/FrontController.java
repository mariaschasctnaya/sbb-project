package com.tsystems.timetable.controller;

import com.tsystems.timetable.dispatcher.Dispatcher;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
public class FrontController extends HttpServlet {
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) {
        Dispatcher dispatcher = new Dispatcher();
        try {
            dispatcher.dispatch(request, response);
        } catch (ServletException | IOException e) {
            log.error(e,e);
        }
    }
}
