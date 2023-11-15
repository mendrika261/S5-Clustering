package mg.clustering.model.entity.deployment;

import lombok.Getter;
import lombok.Setter;

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
}
