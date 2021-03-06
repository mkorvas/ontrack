package net.nemerosa.ontrack.model.structure;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;
import net.nemerosa.ontrack.model.buildfilter.BuildDiff;
import net.nemerosa.ontrack.model.form.Form;

@Data
@AllArgsConstructor
public class Build implements ProjectEntity {

    private final ID id;
    private final String name;
    private final String description;
    @Wither
    private final Signature signature;
    @JsonView({Build.class, BuildView.class, PromotionRun.class, ValidationRun.class, BuildDiff.class})
    private final Branch branch;

    public static Build of(Branch branch, NameDescription nameDescription, Signature signature) {
        return new Build(
                ID.NONE,
                nameDescription.getName(),
                nameDescription.getDescription(),
                signature,
                branch
        );
    }

    @Override
    public Project getProject() {
        return getBranch().getProject();
    }

    @Override
    public ProjectEntityType getProjectEntityType() {
        return ProjectEntityType.BUILD;
    }

    @Override
    public String getEntityDisplayName() {
        return String.format("Build %s/%s/%s", branch.getProject().getName(), branch.getName(), name);
    }

    public Build withId(ID id) {
        return new Build(id, name, description, signature, branch);
    }

    public static Form form() {
        return Form.nameAndDescription();
    }

    public Form asForm() {
        return form()
                .fill("name", name)
                .fill("description", description);
    }

    public Build update(NameDescription nameDescription) {
        return new Build(
                id,
                nameDescription.getName(),
                nameDescription.getDescription(),
                signature,
                branch
        );
    }
}
