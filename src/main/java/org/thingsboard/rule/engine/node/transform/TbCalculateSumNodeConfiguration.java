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

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;
import org.thingsboard.rule.engine.node.entity.*;

import java.math.BigDecimal;


@Data
public class TbCalculateSumNodeConfiguration implements NodeConfiguration<TbCalculateSumNodeConfiguration> {

    private String inputKey;
    private String outputKey;
    private Device arm;
    private Device angle;
    private Device range;
    private Device armHead;
    private Device load;


    @Override
    public TbCalculateSumNodeConfiguration defaultConfiguration() {
        TbCalculateSumNodeConfiguration configuration = new TbCalculateSumNodeConfiguration();
        configuration.setInputKey("Can_data");
        configuration.setOutputKey("TemperatureSum");
        configuration.setArm(new Device(BigDecimal.ZERO,0,16,BigDecimal.valueOf(0.01),BigDecimal.ZERO,0));
        configuration.setAngle(new Device(BigDecimal.ZERO,16, 16, BigDecimal.valueOf(0.01), BigDecimal.ZERO, 0));
        configuration.setRange(new Device(BigDecimal.ZERO,32, 16, BigDecimal.valueOf(0.01), BigDecimal.ZERO, 0));
        configuration.setArmHead(new Device(BigDecimal.ZERO,48, 16, BigDecimal.valueOf(0.01), BigDecimal.ZERO, 0));
        configuration.setLoad(new Device(BigDecimal.ZERO,0, 16, BigDecimal.valueOf(0.1), BigDecimal.ZERO, 0));
        return configuration;
    }

}


