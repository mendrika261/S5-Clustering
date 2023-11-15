package mg.clustering.model.entity.deployment;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
public class HaProxyFrontEnd {
    private String name;
    private String mode;
    private String bind;
    private String default_backend;

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder("\nfrontend " + getName() + "\n");
        Field[] fields = getClass().getDeclaredFields();
        for (Field field: fields) {
            try {
                if(field.get(this) != null)
                    a.append("\t").append(field.getName()).append(" ").append(field.get(this)).append("\n");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Field " + field.getName() + " must be set or not accessible!");
            }
        }
        return a.toString();
    }

    public static HaProxyFrontEnd parseHaProxyFrontEnd(String content) {
        HaProxyFrontEnd haProxyFrontEnd = new HaProxyFrontEnd();
        String[] lines = content.split("\n");
        Field[] fields = haProxyFrontEnd.getClass().getDeclaredFields();
        for (String line : lines) {
            line = line.trim();
            if(line.startsWith("frontend")) {
                haProxyFrontEnd = new HaProxyFrontEnd();
                haProxyFrontEnd.setName(line.substring(line.indexOf("frontend")+8).trim());
            } else {
                for (Field field : fields) {
                    if(line.startsWith(field.getName())) {
                        try {
                            field.set(haProxyFrontEnd, line.substring(line.indexOf(field.getName())+field.getName().length()).trim());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Field " + field.getName() + " must be set or not accessible!");
                        }
                    }
                }
            }
        }
        return haProxyFrontEnd;
    }
}
