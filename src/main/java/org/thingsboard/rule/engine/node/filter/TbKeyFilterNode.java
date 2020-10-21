/**
 * Copyright © 2018 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.rule.engine.node.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.rule.engine.api.RuleNode;
import org.thingsboard.rule.engine.api.TbContext;
import org.thingsboard.rule.engine.api.TbNode;
import org.thingsboard.rule.engine.api.TbNodeConfiguration;
import org.thingsboard.rule.engine.api.TbNodeException;
import org.thingsboard.rule.engine.api.util.TbNodeUtils;
import org.thingsboard.server.common.data.plugin.ComponentType;
import org.thingsboard.server.common.msg.TbMsg;

import java.io.IOException;
import java.util.Map;


@Slf4j
@RuleNode(
        type = ComponentType.FILTER,
        name = "check key---------------------",
        relationTypes = {"True", "False","193","194", "187"},
        configClazz = TbKeyFilterNodeConfiguration.class,
        nodeDescription = "Checks the existence of the selected key in the message payload.",
        nodeDetails = "If the selected key  exists - send Message via <b>True</b> chain, otherwise <b>False</b> chain is used.",
        uiResources = {"static/rulenode/custom-nodes-config.js"}
        //, configDirective = "tbFilterNodeCheckKeyConfig"
)
public class TbKeyFilterNode implements TbNode {

    private static final ObjectMapper mapper = new ObjectMapper();

    private TbKeyFilterNodeConfiguration config;
    private String key;


    @Override
    public void init(TbContext tbContext, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, TbKeyFilterNodeConfiguration.class);
        key = config.getKey();
    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) {
        try {
            log.info("d-----------------------------------------------");
            log.info(ctx.toString());
            log.info(mapper.readTree(msg.getData()).asText());
            log.info(msg.getData());//{"hello":"1111"}
            log.info(mapper.readTree(msg.getData()).has(key)+"");//false
            Map<String, Object> tmpMap= null;
            try {
                //将上个节点的数据转换成map
                tmpMap = mapper.readValue(msg.getData(), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("mapper.readValue()没有转换成功");
            }
            log.info(tmpMap.get("cobid")+"");
//            ctx.tellNext(msg, mapper.readTree(msg.getData()).has(key) ? "True" : "False");
            //从map中查询cobid类型
            String filter = tmpMap.get("cobid")+"";
            if (filter!=null){
                ctx.tellNext(msg, filter );
            }else {
                ctx.tellFailure(msg,new Exception("无此类型"));
            }
        } catch (IOException e) {
            ctx.tellFailure(msg, e);
        }
    }

    @Override
    public void destroy() {
    }
}
