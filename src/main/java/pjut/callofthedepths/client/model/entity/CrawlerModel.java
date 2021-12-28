package pjut.callofthedepths.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import pjut.callofthedepths.common.entity.Crawler;

public class CrawlerModel extends HierarchicalModel<Crawler> {
    private ModelPart root;

    public CrawlerModel() {
        this.root = createBodyLayer().bakeRoot();
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 1, 1);
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(Crawler p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_) {

    }
}
