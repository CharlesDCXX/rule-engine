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
package org.thingsboard.rule.engine.node.transform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.thingsboard.rule.engine.api.TbRelationTypes.SUCCESS;

@Slf4j
@RuleNode(
        type = ComponentType.TRANSFORMATION,
        name = "calculate arm--------------------------",
        configClazz = TbCalculateSumNodeConfiguration.class,
        nodeDescription = "Calculates Sum of the telemetry data, which fields begin with the specified prefix. ",
        nodeDetails = "If fields in Message payload start with the <code>Input Key</code>, the Sum of these fields is added to the new Message payload.",
        uiResources = {"static/rulenode/custom-nodes-config.js"}
        //, configDirective = "tbTransformationNodeSumConfig"
)
public class TbCalculateArmNode implements TbNode {

    private static final ObjectMapper mapper = new ObjectMapper();

    private TbCalculateSumNodeConfiguration config;
    private String inputKey;
    private String outputKey;
    private double armL;
    private int armStart;
    private int armLength;
    private double armScalingFactor;
    private double armOffset;
    private int armType;

    @Override
    public void init(TbContext ctx, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, TbCalculateSumNodeConfiguration.class);
        inputKey = config.getInputKey();
        outputKey = config.getOutputKey();
        armL = config.getArmL();
        armStart = config.getArmStart();
        armLength = config.getArmLength();
        armScalingFactor = config.getArmScalingFactor();
        armOffset = config.getArmOffset();
    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) throws ExecutionException, InterruptedException, TbNodeException {
        double sum = 0;
        boolean hasRecords = false;
        TbHtoD tbHtoD = new TbHtoD();
        try {
            JsonNode jsonNode = mapper.readTree(msg.getData());
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String field = iterator.next();
                if (field.startsWith(inputKey) ) {
                    hasRecords = true;
                    //拿到can_data的value
                    String value = jsonNode.get(field).asText();//00000000a000ac00
                    //得到臂长的十进制数字
                    String trans = tbHtoD.trans(armStart, armLength, value, 16);
                    int tranValue = Integer.valueOf(trans);
                    //臂长长度 * 缩放系数 + 偏移量
                    armL = tranValue * armScalingFactor + armOffset;
                    log.info(armL+"");
                    ObjectNode objectNode = (ObjectNode) jsonNode;
                    log.info(objectNode.toString());
                }
            }
            if (hasRecords) {
                TbMsg newMsg = TbMsg.transformMsg(msg, msg.getType(), msg.getOriginator(), msg.getMetaData(), mapper.writeValueAsString(mapper.createObjectNode().put(outputKey, sum)));
                ctx.tellNext(newMsg, SUCCESS);
            } else {
                ctx.tellFailure(msg, new Exception("Message doesn't contains the key: " + inputKey));
            }
        } catch (IOException e) {
            ctx.tellFailure(msg, e);
        }
    }



    @Override
    public void destroy() {

    }
}
