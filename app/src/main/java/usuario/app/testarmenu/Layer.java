package usuario.app.testarmenu;

/**
 * Created by marco on 6/28/16.
 */
public class Layer {
    private String name;
    private String title;
    private boolean active;

    public Layer(String name, String title, boolean active) {
        this.name = name;
        this.title = title;
        this.active = active;
    }
    public Layer(String name, String title) {
        this(name, title, true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", active=" + active +
                '}';
    }
}
