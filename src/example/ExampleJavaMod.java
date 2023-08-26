package example;

import arc.util.Timer;
import mindustry.Vars;
import mindustry.core.GameState.State;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.mod.Mod;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItemDynamic;
import mindustry.world.consumers.ConsumeItemFilter;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.consumers.ConsumeLiquidFilter;
import mindustry.world.consumers.Consumers;

public class ExampleJavaMod extends Mod
{
    public ExampleJavaMod() {
        Timer.schedule(() -> {
            Vars.state.rules.reactorExplosions = false;
            
            if (!Vars.state.is(State.playing)) {
                
                return;
                
            }

            Groups.build.each((build) -> {
                load(build);
            });


        }, 0.0f, 0.1f);
    }

    private static void load(Building build) {
        Block block = build.block;
        Consumers con = block.consumes;
        Consume[] consumes = con.all();
        for (Consume conn : consumes) {
            if (conn instanceof ConsumeItems) {
                ItemStack[] itStacks = ((ConsumeItems)conn).items;
                for (ItemStack itStack : itStacks) {
                    build.items.set(itStack.item, block.itemCapacity);
                }
            }
            if (conn instanceof ConsumeLiquid) {
                Liquid liquid = ((ConsumeLiquid)conn).liquid;
                build.liquids.add(liquid, block.liquidCapacity - build.liquids.get(liquid));
            }
            if (conn instanceof ConsumeItemFilter) {
                for (Item it : Vars.content.items()) {
                    if (((ConsumeItemFilter)conn).filter.get(it)) {
                        build.items.set(it, build.block.itemCapacity);
                    }
                }
            }
            if (conn instanceof ConsumeLiquidFilter) {
                for (Liquid liq : Vars.content.liquids()) {
                    if (((ConsumeLiquidFilter)conn).filter.get(liq)) {
                        build.liquids.add(liq, block.liquidCapacity - build.liquids.get(liq));
                    }
                }
            }

        }
    }
}
