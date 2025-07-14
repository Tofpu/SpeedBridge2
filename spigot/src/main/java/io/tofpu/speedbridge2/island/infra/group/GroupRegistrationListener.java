package io.tofpu.speedbridge2.island.infra.group;

import io.github.revxrsal.eventbus.SubscribeEvent;
import io.tofpu.speedbridge2.group.domain.event.GroupRegistrationEvent;
import io.tofpu.speedbridge2.group.service.GroupService;
import io.tofpu.speedbridge2.island.service.IslandService;
import io.tofpu.speedbridge2.schematic.infra.SchematicHandler;
import org.jooq.meta.derby.sys.Sys;

public class GroupRegistrationListener {
    private final IslandService islandService;
    private final GroupService groupService;
    private final SchematicHandler schematicHandler;

    public GroupRegistrationListener(IslandService islandService, GroupService groupService, SchematicHandler schematicHandler) {
        this.islandService = islandService;
        this.groupService = groupService;
        this.schematicHandler = schematicHandler;
    }

    @SubscribeEvent
    public void onGroupRegistrationChange(GroupRegistrationEvent ignoredEvent) {
        System.out.println("onGroupRegistrationChange called: " + ignoredEvent);
        System.out.println("Re-resolving all islands now...");
        int changes = islandService.reResolveAllIslands(groupService, schematicHandler);
        System.out.println("Finished resolving islands. Total changes = " + changes);
    }
}
