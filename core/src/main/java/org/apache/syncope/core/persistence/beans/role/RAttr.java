/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.core.persistence.beans.role;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import org.apache.syncope.core.persistence.beans.AbstractAttributable;
import org.apache.syncope.core.persistence.beans.AbstractAttr;
import org.apache.syncope.core.persistence.beans.AbstractAttrValue;
import org.apache.syncope.core.persistence.beans.AbstractSchema;

@Entity
public class RAttr extends AbstractAttr {

    private static final long serialVersionUID = 2848159565890995780L;
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private SyncopeRole owner;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schema_name")
    private RSchema schema;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true, mappedBy = "attribute")
    @Valid
    private List<RAttrValue> values;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "attribute")
    @Valid
    private RAttrUniqueValue uniqueValue;

    public RAttr() {
        super();
        values = new ArrayList<RAttrValue>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public <T extends AbstractAttributable> T getOwner() {
        return (T) owner;
    }

    @Override
    public <T extends AbstractAttributable> void setOwner(T owner) {
        if (!(owner instanceof SyncopeRole)) {
            throw new ClassCastException("owner is expected to be typed SyncopeRole: " + owner.getClass().getName());
        }
        this.owner = (SyncopeRole) owner;
    }

    @Override
    public <T extends AbstractSchema> T getSchema() {
        return (T) schema;
    }

    @Override
    public <T extends AbstractSchema> void setSchema(T schema) {
        if (!(schema instanceof RSchema)) {
            throw new ClassCastException("schema is expected to be typed RSchema: " + schema.getClass().getName());
        }
        this.schema = (RSchema) schema;
    }

    @Override
    public <T extends AbstractAttrValue> boolean addValue(final T attributeValue) {
        if (!(attributeValue instanceof RAttrValue)) {
            throw new ClassCastException("attributeValue is expected to be typed RAttrValue: " + attributeValue.getClass().getName());
        }
        return values.add((RAttrValue) attributeValue);
    }

    @Override
    public <T extends AbstractAttrValue> boolean removeValue(final T attributeValue) {
        if (!(attributeValue instanceof RAttrValue)) {
            throw new ClassCastException("attributeValue is expected to be typed RAttrValue: " + attributeValue.getClass().getName());
        }
        return values.remove((RAttrValue) attributeValue);
    }

    @Override
    public <T extends AbstractAttrValue> List<T> getValues() {
        return (List<T>) values;
    }

    @Override
    public <T extends AbstractAttrValue> void setValues(final List<T> attributeValues) {

        this.values.clear();
        if (attributeValues != null && !attributeValues.isEmpty()) {
            for (T mav : attributeValues) {
                mav.setAttribute(this);
            }
            this.values.addAll((List<RAttrValue>) attributeValues);
        }
    }

    @Override
    public <T extends AbstractAttrValue> T getUniqueValue() {
        return (T) uniqueValue;
    }

    @Override
    public <T extends AbstractAttrValue> void setUniqueValue(final T uniqueAttributeValue) {
        if (!(uniqueAttributeValue instanceof RAttrUniqueValue)) {
            throw new ClassCastException("uniqueAttributeValue is expected to be typed RAttrUniqueValue: " + uniqueAttributeValue.getClass().getName());
        }
        this.uniqueValue = (RAttrUniqueValue) uniqueAttributeValue;
    }
}
