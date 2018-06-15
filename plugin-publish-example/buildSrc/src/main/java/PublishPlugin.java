import org.gradle.api.Named;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class PublishPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        PublishingExtension publishing = project.getExtensions().create("publishing", PublishingExtension.class, project.container(Publication.class));

        publishing.publications.all(publication -> {
            project.getTasks().create("publish" + publication.getName());
        });
    }

    public static class PublishingExtension {
        private final NamedDomainObjectContainer<Publication> publications;

        public PublishingExtension(NamedDomainObjectContainer<Publication> publications) {
            this.publications = publications;
        }

        public NamedDomainObjectContainer<Publication> getPublications() {
            return publications;
        }
    }

    public static class Publication implements Named {
        private String name;

        public Publication(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
