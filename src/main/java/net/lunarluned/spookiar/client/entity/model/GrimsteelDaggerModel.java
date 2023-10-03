package net.lunarluned.spookiar.client.entity.model;

import net.lunarluned.spookiar.registry.entities.projectiles.GrimsteelDaggerProjectile;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GrimsteelDaggerModel<T extends GrimsteelDaggerProjectile> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart dagger;

    public GrimsteelDaggerModel(ModelPart modelPart) {
        this.root = modelPart;
        this.dagger = root.getChild("Dagger");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("Dagger", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -10.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(7, 7).addBox(-1.0F, -3.0F, -0.6F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(GrimsteelDaggerProjectile grimsteelDaggerProjectile, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.dagger.xRot = (grimsteelDaggerProjectile.lifeTime);
        this.root.setPos(0.0f, -20.0f, 0.0f);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
