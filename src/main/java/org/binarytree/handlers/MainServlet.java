package org.binarytree.handlers;

import org.apache.commons.io.IOUtils;
import org.binarytree.model.Answer;
import org.binarytree.model.Person;
import org.binarytree.tree.DataAdapter;
import org.binarytree.tree.TreeAdapter;
import org.binarytree.utils.Common;
import org.binarytree.utils.SerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class MainServlet extends HttpServlet
{
    private static Logger log = LoggerFactory.getLogger(MainServlet.class.getSimpleName());
    private DataAdapter db = TreeAdapter.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        Answer answer = null;
        resp.setContentType(Common.getHttpFormat());
        String reqStr = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
        Person r = SerializerFactory.getSerializer().Deserialize(reqStr, Person.class);
        if(r == null)
        {
            log.error("BAD_REQUEST in doGet - invalid post body");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = new Answer("BAD_REQUEST", null);
            resp.getWriter().println(SerializerFactory.getSerializer().Serialize(answer));
            return;
        }
        boolean added = db.addOrUpdate(r);
        resp.setStatus(HttpServletResponse.SC_OK);
        String status;
        if (added) {
            status = "ADDED";
        } else {
            status = "UPDATED";
        }
        answer = new Answer(status, Collections.singletonList(db.get(r.getInn())));
        resp.getWriter().println(SerializerFactory.getSerializer().Serialize(answer));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Answer answer = null;
        response.setContentType(Common.getHttpFormat());
        try {
            String innStr = request.getParameter("inn");
            List<Person> persons = null;
            String status = "OK";
            if (innStr == null) {
                persons = db.get();
            } else {
                long inn = Long.parseLong(innStr);
                Person personFound = db.get(inn);
                if (personFound != null) {
                    persons = Collections.singletonList(personFound);
                } else {
                    status = "NOT_FOUND";
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
            answer = new Answer(status, persons);
        } catch (Exception ex) {
            log.error("BAD_REQUEST in doGet - {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = new Answer("BAD_REQUEST", null);
        }
        response.getWriter().println(SerializerFactory.getSerializer().Serialize(answer));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Answer answer = null;
        resp.setContentType(Common.getHttpFormat());
        try {
            long inn = Long.parseLong(req.getParameter("inn"));
            boolean deleted = db.delete(inn);
            String status;
            if (deleted) {
                status = "DELETED";
            } else {
                status = "NOT_FOUND";
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            answer = new Answer(status, null);
        } catch (Exception ex) {
            log.error("BAD_REQUEST in doDelete - {}", ex.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = new Answer("BAD_REQUEST", null);
        }
        resp.getWriter().println(SerializerFactory.getSerializer().Serialize(answer));
    }
}
