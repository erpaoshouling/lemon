package com.mossle.party.domain;

// Generated by Hibernate Tools
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PartyStruct .
 * 
 * @author Lingo
 */
@Entity
@Table(name = "PARTY_STRUCT")
public class PartyStruct implements java.io.Serializable {
    private static final long serialVersionUID = 0L;

    /** null,null,null. */
    private PartyStructId id;

    /** null. */
    private PartyEntity parentEntity;

    /** null. */
    private PartyEntity childEntity;

    /** null. */
    private PartyStructType partyStructType;

    /** null. */
    private Integer priority;

    public PartyStruct() {
    }

    public PartyStruct(PartyStructId id, PartyEntity parentEntity,
            PartyEntity childEntity, PartyStructType partyStructType) {
        this.id = id;
        this.parentEntity = parentEntity;
        this.childEntity = childEntity;
        this.partyStructType = partyStructType;
    }

    public PartyStruct(PartyStructId id, PartyEntity parentEntity,
            PartyEntity childEntity, PartyStructType partyStructType,
            Integer priority) {
        this.id = id;
        this.parentEntity = parentEntity;
        this.childEntity = childEntity;
        this.partyStructType = partyStructType;
        this.priority = priority;
    }

    /** @return null,null,null. */
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "structTypeId", column = @Column(name = "STRUCT_TYPE_ID", nullable = false)),
            @AttributeOverride(name = "parentEntityId", column = @Column(name = "PARENT_ENTITY_ID", nullable = false)),
            @AttributeOverride(name = "childEntityId", column = @Column(name = "CHILD_ENTITY_ID", nullable = false)) })
    public PartyStructId getId() {
        return this.id;
    }

    /**
     * @param id
     *            null,null,null.
     */
    public void setId(PartyStructId id) {
        this.id = id;
    }

    /** @return null. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ENTITY_ID", nullable = false, insertable = false, updatable = false)
    public PartyEntity getParentEntity() {
        return this.parentEntity;
    }

    /**
     * @param parentEntity
     *            null.
     */
    public void setParentEntity(PartyEntity parentEntity) {
        this.parentEntity = parentEntity;
    }

    /** @return null. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHILD_ENTITY_ID", nullable = false, insertable = false, updatable = false)
    public PartyEntity getChildEntity() {
        return this.childEntity;
    }

    /**
     * @param childEntity
     *            null.
     */
    public void setChildEntity(PartyEntity childEntity) {
        this.childEntity = childEntity;
    }

    /** @return null. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STRUCT_TYPE_ID", nullable = false, insertable = false, updatable = false)
    public PartyStructType getPartyStructType() {
        return this.partyStructType;
    }

    /**
     * @param partyStructType
     *            null.
     */
    public void setPartyStructType(PartyStructType partyStructType) {
        this.partyStructType = partyStructType;
    }

    /** @return null. */
    @Column(name = "PRIORITY")
    public Integer getPriority() {
        return this.priority;
    }

    /**
     * @param priority
     *            null.
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
