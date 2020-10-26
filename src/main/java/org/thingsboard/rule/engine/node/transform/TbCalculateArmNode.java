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
import org.thingsboard.rule.engine.node.entity.*;
import org.thingsboard.server.common.data.plugin.ComponentType;
import org.thingsboard.server.common.msg.TbMsg;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
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
    private Device arm;
    private Device angle;
    private Device range;
    private Device armHead;
    private Device load;

    @Override
    public void init(TbContext ctx, TbNodeConfiguration configuration) throws TbNodeException {
        this.config = TbNodeUtils.convert(configuration, TbCalculateSumNodeConfiguration.class);
        arm = config.getArm();
        angle = config.getAngle();
        range = config.getRange();
        armHead = config.getArmHead();
        load = config.getLoad();
        inputKey = config.getInputKey();
        outputKey = config.getOutputKey();

    }

    @Override
    public void onMsg(TbContext ctx, TbMsg msg) throws ExecutionException, InterruptedException, TbNodeException {
        double sum = 0;
        boolean hasRecords = false;
        TbHtoD tbHtoD = new TbHtoD();
        try {
            JsonNode jsonNode = mapper.readTree(msg.getData());
            ObjectNode objectNode = (ObjectNode)jsonNode;
            Iterator<String> iterator = jsonNode.fieldNames();
            log.info("--------------转换节点----------------");
            while (iterator.hasNext()) {
                String field = iterator.next();
                if (field.startsWith(inputKey) ) {
                    hasRecords = true;
                    //拿到can_data的value
                    String value = jsonNode.get(field).asText();//00000000a000ac00
                    //得到臂长的十进制数字
                    String trans = tbHtoD.trans(arm.getStart(), arm.getLength(), value, 16);
                    //臂长长度 * 缩放系数 + 偏移量
                    arm.setValue( new BigDecimal(trans).multiply(arm.getScalingFactor()).add(arm.getOffset()));
                    angle.setValue(new BigDecimal(tbHtoD.trans(angle.getStart(), angle.getLength(), value, 16)).multiply(angle.getScalingFactor()).add(angle.getOffset()));
                    range.setValue(new BigDecimal(tbHtoD.trans(range.getStart(), range.getLength(), value, 16)).multiply(range.getScalingFactor()).add(range.getOffset()));
                    armHead.setValue(new BigDecimal(tbHtoD.trans(armHead.getStart(), armHead.getLength(), value, 16)).multiply(armHead.getScalingFactor()).add(armHead.getOffset()));
                    load.setValue(new BigDecimal(tbHtoD.trans(load.getStart(), load.getLength(), value, 16)).multiply(load.getScalingFactor()).add(load.getOffset()));
                    objectNode.remove("Can_data");
                    objectNode.put("arm", arm.getValue());
                    objectNode.put("angle",angle.getValue());
                    objectNode.put("range",range.getValue());
                    objectNode.put("armHead",armHead.getValue());
                    objectNode.put("load",load.getValue());
                    log.info(objectNode.toString());
                }
            }
            if (hasRecords) {
                TbMsg newMsg = TbMsg.transformMsg(msg, msg.getType(), msg.getOriginator(), msg.getMetaData(), mapper.writeValueAsString(objectNode));
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
