package net.lunarluned.spookiar.client.entity.model;

import net.lunarluned.spookiar.client.entity.animations.GhostAnimations;
import net.lunarluned.spookiar.client.entity.animations.PrimeSpiritAnimations;
import net.lunarluned.spookiar.registry.entities.living_entities.prime_spirit.PrimeSpirit;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PrimeSpiritModel<T extends PrimeSpirit> extends HierarchicalModel<T> {
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart root;

	public PrimeSpiritModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("Body");
		this.head = body.getChild("Head");
	}

	public static LayerDefinition getLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();


		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, -19.0F, 0.0F));

		PartDefinition body_n_r1 = Body.addOrReplaceChild("body_n_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -20.0599F, -4.0863F, 14.0F, 20.0F, 7.0F, new CubeDeformation(-0.2F))
				.texOffs(0, 27).addBox(-7.0F, -20.0599F, -4.0863F, 14.0F, 20.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, 0.0F));

		PartDefinition head_r1 = Head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(86, 0).addBox(-5.0F, -14.0F, -5.0F, 10.0F, 13.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(86, 22).addBox(-5.0F, -14.0F, -5.0F, 10.0F, 13.0F, 9.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition Cloak = Body.addOrReplaceChild("Cloak", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, 2.0F));

		PartDefinition cloak_r_r1 = Cloak.addOrReplaceChild("cloak_r_r1", CubeListBuilder.create().texOffs(0, 54).addBox(-16.5F, -1.5673F, -4.225F, 14.0F, 11.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, -0.1571F));

		PartDefinition cloak_l_r1 = Cloak.addOrReplaceChild("cloak_l_r1", CubeListBuilder.create().texOffs(0, 54).addBox(2.6F, -1.4673F, -4.125F, 14.0F, 11.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.1309F));

		PartDefinition cloak_m_r1 = Cloak.addOrReplaceChild("cloak_m_r1", CubeListBuilder.create().texOffs(0, 54).addBox(-7.0F, 0.4327F, -4.125F, 14.0F, 11.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Trail = Body.addOrReplaceChild("Trail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition trail_n_r1 = Trail.addOrReplaceChild("trail_n_r1", CubeListBuilder.create().texOffs(42, 18).addBox(-7.0F, -3.75F, -2.75F, 14.0F, 10.0F, 8.0F, new CubeDeformation(-0.2F))
				.texOffs(42, 0).addBox(-7.0F, -3.75F, -2.75F, 14.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition Trail2 = Trail.addOrReplaceChild("Trail2", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 3.0F));

		PartDefinition trail_r1 = Trail2.addOrReplaceChild("trail_r1", CubeListBuilder.create().texOffs(44, 56).addBox(-5.0F, 4.5255F, -9.859F, 10.0F, 12.0F, 7.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.6144F, 0.0F, 0.0F));

		PartDefinition trail_r2 = Trail2.addOrReplaceChild("trail_r2", CubeListBuilder.create().texOffs(42, 36).addBox(-6.0F, -1.4745F, -3.859F, 12.0F, 12.0F, 8.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.829F, 0.0F, 0.0F));

		PartDefinition Arm_Left = Body.addOrReplaceChild("Arm_Left", CubeListBuilder.create().texOffs(0, 75).addBox(-0.4F, -1.0F, -2.0F, 7.0F, 30.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(28, 75).addBox(-0.4F, -1.0F, -2.0F, 7.0F, 30.0F, 7.0F, new CubeDeformation(0.3F)), PartPose.offset(8.0F, -18.0F, 0.0F));

		PartDefinition Arm_Right = Body.addOrReplaceChild("Arm_Right", CubeListBuilder.create().texOffs(82, 81).addBox(-6.3F, -1.0F, -2.0F, 7.0F, 30.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(82, 44).addBox(-6.3F, -1.0F, -2.0F, 7.0F, 30.0F, 7.0F, new CubeDeformation(0.3F)), PartPose.offset(-8.0F, -18.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T primeSpirit, float angle, float distance, float animationProgress, float yaw, float pitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.root.setPos(0.0f, 23.0f, 0.0f);
		this.head.yRot = yaw * 0.017453292F;
		this.head.xRot = pitch * 0.017453292F;
		float k = Math.min((float) primeSpirit.getDeltaMovement().lengthSqr() * 150.0f, 8.0f);
		this.animate(primeSpirit.bigPunchAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_BIG_PUNCH, animationProgress);
		this.animate(primeSpirit.normal_attackAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_NORMAL_ATTACK, animationProgress);
		this.animate(primeSpirit.walkAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_WALK, animationProgress, k);
		this.animate(primeSpirit.weakenAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_WEAKEN, animationProgress);
		this.animate(primeSpirit.summonAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_SUMMON, animationProgress);
		this.animate(primeSpirit.deathAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_DEATH, animationProgress);
		this.animate(primeSpirit.runAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_RUN, animationProgress);
		this.animate(primeSpirit.deathPoseAnimationState, PrimeSpiritAnimations.PRIME_SPIRIT_DEATH_POSE, animationProgress);
	}
}
