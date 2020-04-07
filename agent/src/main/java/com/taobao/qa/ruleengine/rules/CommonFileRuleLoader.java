package com.taobao.qa.ruleengine.rules;

import com.taobao.qa.ruleengine.utils.CommonConstants;
import com.taobao.qa.ruleengine.utils.ExpressFileReader;
import com.taobao.qa.ruleengine.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonFileRuleLoader implements IRuleLoader {
    private static final Logger logger = LoggerFactory.getLogger(CommonFileRuleLoader.class);

    private String ruleFilePattern;
    private String ruleFileLocation;

    public CommonFileRuleLoader(String ruleFilePattern, String ruleFileLocation) {
        this.ruleFilePattern = ruleFilePattern;
        this.ruleFileLocation = ruleFileLocation;
    }

    private List<String> getRuleFilePaths() {
        String pattern = String.format("glob:**/%s",ruleFilePattern);
        try {
            return ExpressFileReader.getFileListWithGlobPattern(pattern, ruleFileLocation);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getRuleIdFromFileName(String filename) {
        Pattern p = Pattern.compile("\\.*_(\\d+)\\.ql$");
        Matcher m = p.matcher(filename);
        if(m.find()) {
            String resultInStr = m.group(1);
            return Integer.parseInt(resultInStr);
        }
        String errmsg = String.format("Failed to extract rule id from filename \"%s\"",filename);
        logger.error(errmsg);
        throw new IllegalArgumentException(errmsg);
    }

    @Override
    public List<Rule> loadRules(String rule_table) throws IOException {
        List<String> ruleFileList = getRuleFilePaths();
        List<Rule> output = new Vector<Rule>();

        for (String filename : ruleFileList) {
            Map<String, String> ruleExpressions = ExpressFileReader.loadRuleExpressFromFile(filename);

            int id              = getRuleIdFromFileName(filename);
            String name         = ruleExpressions.get("name");
            String whenExpr     = ruleExpressions.get("when");
            String verifyExpr   = ruleExpressions.get("verify");
            String thenExpr     = ruleExpressions.get("then");
            String category     = ruleExpressions.get("category").trim();
            Integer categoryId  = CommonConstants.RULE_CATEGORY.get(category);
            String smoketype    = ruleExpressions.get("smoketype");
            String debugtype    = ruleExpressions.get("debugtype");
            String product      = ruleExpressions.get("product");
            String query        = ruleExpressions.get("query");
            String extrinfo      = ruleExpressions.get("extrinfo");
            int isautogen = 0;
            if(ruleExpressions.containsKey("isautogen")) {
                isautogen = Integer.valueOf(ruleExpressions.get("isautogen"));
            }


            // Sanity Check
            if(null == categoryId) {
                String errmsg = String.format("Unknown category \"%s\"",category);
                logger.error(errmsg);
                throw new IllegalArgumentException(errmsg);
            }

            // 获取 Rule 的 level
            String levelStr = ruleExpressions.get("level");
            int level = Integer.parseInt(levelStr.trim());

            // 获取 Rule 的 Tags
            HashSet<String> tags = null;
            String tagsStr = ruleExpressions.get("type");
            if (null != tagsStr) {
                tagsStr = tagsStr.trim().replace("\n", "");
                tags = StringUtil.csvStringToHashSet(tagsStr);
            }

            // whenExpr and verifyExpr是必须的
            if (name == null || whenExpr == null || verifyExpr == null) {
                String errmsg = String.format("in file \"%s\", whenExpr or verifyExpr is missing.", filename);
                logger.error(errmsg);
                throw new IllegalStateException(errmsg);
            }

            output.add(new Rule(id,name, whenExpr, verifyExpr, thenExpr, tags, category, categoryId, level,false,smoketype,debugtype,product,isautogen));
        }
        return output;
    }
}