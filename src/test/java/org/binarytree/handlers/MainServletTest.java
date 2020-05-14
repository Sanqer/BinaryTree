package org.binarytree.handlers;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.binarytree.Main;
import org.binarytree.model.Answer;
import org.binarytree.model.Person;
import org.binarytree.tree.TreeAdapter;
import org.binarytree.tree.TreeAdapterTest;
import org.binarytree.utils.SerializerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class MainServletTest {
    private static Logger log = LoggerFactory.getLogger(TreeAdapterTest.class.getSimpleName());
    private TreeAdapter db;

    @Before
    public void setUp() throws Exception {
        try {
            Main.main(null);
            db = TreeAdapter.getInstance();
            db.delete(-1);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Test
    public void getTest_success() {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8026/person");
        try {
            CloseableHttpResponse resp = client.execute(httpGet);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertArrayEquals(db.get().toArray(), ans.getItems().toArray());
            assertEquals("OK", ans.getStatus());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Test
    public void getTest_badRequest() {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8026/person?inn=bb");
        try {
            CloseableHttpResponse resp = client.execute(httpGet);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertNull(ans.getItems());
            assertEquals("BAD_REQUEST", ans.getStatus());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    @Test
    public void getByInnTest_success() {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8026/person?inn=1");
        try {
            CloseableHttpResponse resp = client.execute(httpGet);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals(db.get(1), ans.getItems().get(0));
            assertEquals("OK", ans.getStatus());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            assertNotNull(null);
        }
    }

    @Test
    public void getByInnTest_notFound() {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://localhost:8026/person?inn=101");
        try {
            CloseableHttpResponse resp = client.execute(httpGet);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals("NOT_FOUND", ans.getStatus());
            assertNull(ans.getItems());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            assertNotNull(null);
        }
    }

    @Test
    public void postAddUpdatedDeleted_success() {
        Person person = new Person();
        person.setInn(-1);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:8026/person");
        try {
            httpPost.setEntity(new StringEntity(SerializerFactory.getSerializer().serialize(person)));
            CloseableHttpResponse resp = client.execute(httpPost);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals("ADDED", ans.getStatus());
            assertEquals(person, ans.getItems().get(0));
            resp.close();

            person.setName("myName");
            httpPost.setEntity(new StringEntity(SerializerFactory.getSerializer().serialize(person)));
            resp = client.execute(httpPost);
            strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals("UPDATED", ans.getStatus());
            assertEquals("myName", ans.getItems().get(0).getName());
            resp.close();

            HttpDelete httpDelete = new HttpDelete("http://localhost:8026/person?inn=-1");
            resp = client.execute(httpDelete);
            strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals("DELETED", ans.getStatus());
            assertNull(ans.getItems());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            assertNotNull(null);
        }
    }

    @Test
    public void postAdd_badRequest() {
        Person person = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:8026/person");
        try {
            httpPost.setEntity(new StringEntity(SerializerFactory.getSerializer().serialize(person)));
            CloseableHttpResponse resp = client.execute(httpPost);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals("BAD_REQUEST", ans.getStatus());
            assertNull(ans.getItems());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            assertNotNull(null);
        }
    }

    @Test
    public void deleteDelete_notFound() {
        Person person = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpDelete httpDelete = new HttpDelete("http://localhost:8026/person?inn=100100101");
        try {
            CloseableHttpResponse resp = client.execute(httpDelete);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals("NOT_FOUND", ans.getStatus());
            assertNull(ans.getItems());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            assertNotNull(null);
        }
    }

    @Test
    public void deleteDelete_badRequest() {
        Person person = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpDelete httpDelete = new HttpDelete("http://localhost:8026/person?inn=badRequest");
        try {
            CloseableHttpResponse resp = client.execute(httpDelete);
            String strResp = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
            Answer ans = SerializerFactory.getSerializer().deserialize(strResp, Answer.class);
            assertEquals("BAD_REQUEST", ans.getStatus());
            assertNull(ans.getItems());
            resp.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            assertNotNull(null);
        }
    }


    @After
    public void tearDown() throws Exception {
        Main.stopServer();
    }
}