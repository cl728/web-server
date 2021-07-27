package cn.sxt.server.core;

import java.util.HashSet;
import java.util.Set;

/**
 * servlet-mapping
 */
public class Mapping {
    private String name;
    private Set<String> patterns = new HashSet<>();

    public Mapping() {

    }

    public Mapping(String name, Set<String> patterns) {
        this.name = name;
        this.patterns = patterns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<String> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "name='" + name + '\'' +
                ", patterns=" + patterns +
                '}';
    }

    public void addPattern(String pattern) {
        patterns.add( pattern );
    }
}
