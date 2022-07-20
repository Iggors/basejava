package ru.basejava.web;

import ru.basejava.Config;
import ru.basejava.model.Resume;
import ru.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage = Config.get().getStorage();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<style>");
        writer.println("table, th, td {");
        writer.println("border: 1px solid black; border-style: dotted");
        writer.println("}");
        writer.println("</style>");
        writer.println("<table>");
        writer.println("<tr>");
        writer.println("<td style=\"font-size: 15pt; font-family: monospace; font-weight: 600\">UUID</td>");
        writer.println("<td style=\"font-size: 15pt; font-family: monospace; font-weight: 600\">FULL_NAME</td>");
        writer.println("</tr>");

        List<Resume> resumes = storage.getAllSorted();

        for (Resume resume : resumes) {
            writer.println("<tr>");
            writer.println("<td>" + resume.getUuid() + "</td>");
            writer.println("<td>" + resume.getFullName() + "</td>");
            writer.println("</tr>");
        }

        writer.println("</table>");
        writer.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
