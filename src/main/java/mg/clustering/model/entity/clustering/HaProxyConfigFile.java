package mg.clustering.model.entity.clustering;

import lombok.Getter;
import lombok.Setter;
import mg.clustering.model.core.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HaProxyConfigFile {
    List<HaProxyFrontEnd> haProxyFrontEnds = new ArrayList<>();
    List<HaProxyBackEnd> haProxyBackEnds = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        for (HaProxyFrontEnd haProxyFrontEnd : haProxyFrontEnds) {
            a.append(haProxyFrontEnd.toString()).append("\n");
        }
        for (HaProxyBackEnd haProxyBackEnd : haProxyBackEnds) {
            a.append(haProxyBackEnd.toString()).append("\n");
        }
        return a.toString();
    }

    public static HaProxyConfigFile parseHaProxyConfigFile(String content) {
        HaProxyConfigFile haProxyConfigFile = new HaProxyConfigFile();
        String[] lines = content.split("\n");
        HaProxyFrontEnd haProxyFrontEnd = null;
        HaProxyBackEnd haProxyBackEnd = null;
        String type = "";
        StringBuilder temp = new StringBuilder();
        for (String line : lines) {
            if(line.startsWith("frontend")) {
                type = "frontend";
            } else if(line.startsWith("backend")) {
                type = "backend";
            } else if(line.isBlank()) {
                if(type.equals("frontend")) {
                    haProxyFrontEnd = HaProxyFrontEnd.parseHaProxyFrontEnd(temp.toString());
                    haProxyConfigFile.getHaProxyFrontEnds().add(haProxyFrontEnd);
                } else if(type.equals("backend")) {
                    haProxyBackEnd = HaProxyBackEnd.parseHaProxyBackEnd(temp.toString());
                    haProxyConfigFile.getHaProxyBackEnds().add(haProxyBackEnd);
                }
                temp = new StringBuilder();
                type = "";
            }
            temp.append(line).append("\n");
        }

        if(type.equals("frontend")) {
            haProxyFrontEnd = HaProxyFrontEnd.parseHaProxyFrontEnd(temp.toString());
            haProxyConfigFile.getHaProxyFrontEnds().add(haProxyFrontEnd);
        } else if(type.equals("backend")) {
            haProxyBackEnd = HaProxyBackEnd.parseHaProxyBackEnd(temp.toString());
            haProxyConfigFile.getHaProxyBackEnds().add(haProxyBackEnd);
        }
        return haProxyConfigFile;
    }

    public void save(String path) {
        Utils.writeFile(path, toString());
    }
    
    public void addFrontend(HaProxyFrontEnd haProxyFrontEnd) {
        for (HaProxyFrontEnd haProxyFrontEnd1 : getHaProxyFrontEnds()) {
            if(haProxyFrontEnd1.getName().equals(haProxyFrontEnd.getName()))
                throw new RuntimeException("Frontend " + haProxyFrontEnd.getName() + " already exists");
        }
        haProxyFrontEnds.add(haProxyFrontEnd);
    }

    public void addBackend(HaProxyBackEnd haProxyBackEnd) {
        for (HaProxyBackEnd haProxyBackEnd1 : getHaProxyBackEnds()) {
            if(haProxyBackEnd1.getName().equals(haProxyBackEnd.getName()))
                throw new RuntimeException("Backend " + haProxyBackEnd.getName() + " already exists");
        }
        haProxyBackEnds.add(haProxyBackEnd);
    }
}
