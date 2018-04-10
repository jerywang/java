package drools;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;

/**
 * RuleBaseFacatory.java 八月 11,2017 wangguoxing@baidu.com
 */
public class RuleBaseFacatory {
    private static RuleBase ruleBase;

    public static RuleBase getRuleBase(){
        return null != ruleBase ? ruleBase : RuleBaseFactory.newRuleBase();
    }
}