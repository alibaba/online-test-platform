import com.alibaba.test.steed.Application;
import com.alibaba.test.steed.controller.TaskController;
import com.alibaba.test.steed.service.ITaskService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
/**
 * Created by liyang on 2019/8/28.
 */
public class TaskControllerTest {

    private MockMvc mock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testError() throws Exception {
        mock.perform( MockMvcRequestBuilders.get("/api/task/selectResult").accept( MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSelectResult() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/task/selectResult")
                .param( "taskId","1" ).param( "caseIds","1,2,3" )
                .accept(MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());

        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
    }

    @Test
    public void testSelectAllByPage() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/task/selectAllByPage")
                .param( "pageNum","1" ).param( "pageSize","1" )
                .accept(MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
    }

    @Test
    public void testGetTask() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/task/getTask")
                .param( "id","1" )
                .accept(MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
    }

    @Test
    public void testRunTest() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/task/runTest")
                .param( "host","abc.abc" ).param( "comment","test comment" )
                .accept(MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isBadRequest());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 400, status);
    }

    @Test
    public void testDeleteTask() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/task/deleteTask")
                .param( "id","1" )
                .accept(MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
    }


}
