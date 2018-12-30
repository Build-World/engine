package com.buildworld.game.blocks.properties;

import com.shawnclake.morgencore.core.component.services.ListService;

import java.util.ArrayList;

public class BlockPropertyService extends ListService<IBlockProperty> {

    private ArrayList<IBlockProperty> mandatory = new ArrayList<>();

    public ArrayList<IBlockProperty> getMandatory() {
        return mandatory;
    }

    @Override
    public void add(IBlockProperty item) {
        super.add(item);
        if(!item.isOptional())
        {
            this.getMandatory().add(item);

        }

    }
}
