package ai.openfabric.api.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
public class Datable implements Serializable {

    @ApiModelProperty(hidden = true)
    public Date createdAt;

    @ApiModelProperty(hidden = true)
    public Date updatedAt;

    @ApiModelProperty(hidden = true)
    public Date deletedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }

}
