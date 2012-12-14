/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.guvnor.projecteditor.backend.server.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kie.guvnor.projecteditor.model.ClockTypeOption;
import org.kie.guvnor.projecteditor.model.KSessionModel;
import org.kie.guvnor.projecteditor.model.ListenerModel;
import org.kie.guvnor.projecteditor.model.WorkItemHandlerModel;

public class KSessionConverter
        extends AbstractXStreamConverter {

    public KSessionConverter() {
        super(KSessionModel.class);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        KSessionModel kSession = (KSessionModel) value;
        writer.addAttribute("name", kSession.getName());
        writer.addAttribute("type", kSession.getType());
        writer.addAttribute("default", Boolean.toString(kSession.isDefault()));
        if (kSession.getClockType() != null) {
            writer.addAttribute("clockType", kSession.getClockType().getClockTypeAsString());
        }
        if (kSession.getScope() != null) {
            writer.addAttribute("scope", kSession.getScope().toString());
        }
        for (ListenerModel listener : kSession.getListenerModels()) {
            writeObject(writer, context, listener.getKind().toString(), listener);
        }
        for (WorkItemHandlerModel wih : kSession.getWorkItemHandelerModels()) {
            writeObject(writer, context, "workItemHandler", wih);
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        KSessionModel kSession = new KSessionModel();
        kSession.setName(reader.getAttribute("name"));
        String type = reader.getAttribute("type");
        if (type == null) {
            kSession.setType("stateful");
        } else {
            kSession.setType(type);
        }

        String clockType = reader.getAttribute("clockType");
        if (clockType != null) {
            kSession.setClockType(ClockTypeOption.get(clockType));
        }
        return kSession;
    }
}
