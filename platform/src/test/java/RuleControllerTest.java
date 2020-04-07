import com.alibaba.fastjson.JSON;
import com.alibaba.test.steed.Application;
import com.alibaba.test.steed.domain.AjaxResult;
import com.alibaba.test.steed.model.RuleWithBLOBs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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

import static org.junit.Assert.*;

/**
 * Created by liyang on 2019/9/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class RuleControllerTest {

    private MockMvc mock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static int ruleId;

    @Before
    public void setUp() throws Exception {
        mock = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSelectAllByPage() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/rule/selectAllByPage")
                .param( "pageNum","1" ).param( "pageSize","1" )
                .accept( MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
    }


    @Test
    public void testRefreshRuleSets() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/rule/refreshRuleSets")
                .accept(MediaType.APPLICATION_JSON);
        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
    }

    @Test
    public void testDeleteRule() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/rule/deleteRule")
                .param( "ruleId", String.valueOf(ruleId) )
                .accept(MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
    }

    @Test
    public void testAddrule() throws Exception{
        RuleWithBLOBs rule = new RuleWithBLOBs(  );
        rule.setrWhen( "when" );
        rule.setrThen( "then" );
        rule.setrVerify( "verify" );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/rule/addRule")
                .content( JSON.toJSONString(rule) ).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        ResultActions perform = mock.perform(request);
        perform.andExpect( MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        int status = mvcResult.getResponse().getStatus();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals("请求错误", 200, status);
        AjaxResult result = JSON.parseObject( response.getContentAsString(), AjaxResult.class );
        ruleId = Integer.valueOf( result.getData().toString());
    }

}