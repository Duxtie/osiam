/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2013-2016 tarent solutions GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.osiam.resources.converter;

import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.MemberRef;
import org.osiam.storage.dao.ResourceDao;
import org.osiam.storage.entities.GroupEntity;
import org.osiam.storage.entities.ResourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GroupConverter implements Converter<Group, GroupEntity> {

    private final ResourceDao resourceDao;
    private final MetaConverter metaConverter;

    @Autowired
    public GroupConverter(ResourceDao resourceDao, MetaConverter metaConverter) {
        this.resourceDao = resourceDao;
        this.metaConverter = metaConverter;
    }

    @Override
    public GroupEntity fromScim(Group group) {
        if (group == null) {
            return null;
        }

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setDisplayName(group.getDisplayName());
        groupEntity.setExternalId(group.getExternalId());

        for (MemberRef member : group.getMembers()) {
            addMember(member, groupEntity);
        }

        return groupEntity;
    }

    private void addMember(MemberRef member, GroupEntity groupEntity) {
        String uuid = member.getValue();

        ResourceEntity resource = resourceDao.getById(uuid, ResourceEntity.class);

        groupEntity.addMember(resource);
    }

    @Override
    public Group toScim(GroupEntity group) {
        if (group == null) {
            return null;
        }

        Group.Builder groupBuilder = new Group.Builder(group.getDisplayName())
                .setId(group.getId().toString())
                .setMeta(metaConverter.toScim(group.getMeta()))
                .setExternalId(group.getExternalId());

        Set<MemberRef> members = new HashSet<>();
        for (ResourceEntity member : group.getMembers()) {
            MemberRef memberRef = new MemberRef.Builder()
                    .setValue(member.getId().toString())
                    .setReference(member.getMeta().getLocation())
                    .setDisplay(member.getDisplayName() != null ? member.getDisplayName() : null)
                    .setType(member.getType())
                    .build();

            members.add(memberRef);
        }
        groupBuilder.setMembers(members);

        return groupBuilder.build();
    }
}
